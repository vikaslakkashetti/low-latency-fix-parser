package com.vikas.main;

/**
 * Optimized FIX message parser optimized for low-latency.
 *
 * Points to note:
 *  - Zero heap allocations during parsing (views reuse underlying buffer)
 *  - Strict validation of first three FIX header fields (8, 9, 35)
 *  - Separation of common header and message-specific body views
 *  - Parsing is single-pass for field extraction
 *  - Checksum verification is currently implemented as a second pass.
 *  - @Todo : Optimize checksum calculations for single pass *
 *  - All string fields are exposed via AsciiStringView (no String allocation)
 */
public final class FixParserOptimized {

    /** FIX field delimiter (ASCII SOH = 0x01). */
    private static final byte SOH = 1;

    /** Parsed message type (tag 35). */
    private byte msgType;

    // Common header view shared by all message types
    private final HeaderView headerView = new HeaderView();

    // Message-specific body views
    private final NewOrderView newOrderView = new NewOrderView();
    private final CancelOrderView cancelOrderView = new CancelOrderView();
    private final ReplaceOrderView replaceOrderView = new ReplaceOrderView();

    public byte getMsgType() {
        return msgType;
    }

    public HeaderView getHeaderView() {
        return headerView;
    }

    public NewOrderView getNewOrderView() {
        return newOrderView;
    }

    public CancelOrderView getCancelOrderView() {
        return cancelOrderView;
    }

    public ReplaceOrderView getReplaceOrderView() {
        return replaceOrderView;
    }

    /**
     * Parses a FIX message from the provided byte buffer.
     *
     * Assumptions:
     *  - Buffer contains exactly one complete FIX message
     *  - Tag 10 (checksum) is the final field
     *
     * Validation performed:
     *  - Strict ordering of first three header fields (8, 9, 35)
     *  - Basic tag and field format validation
     *  - Checksum validation (mod 256)
     *
     * @return FixError indicating parse success or specific validation failure
     */
    public FixError parse(byte[] buffer, int length) {

        msgType = 0;

        // Wrap underlying buffer in reusable views
        headerView.wrap(buffer);
        newOrderView.wrap(buffer);
        cancelOrderView.wrap(buffer);
        replaceOrderView.wrap(buffer);

        int tag = 0;
        int valueStart = -1;
        int fieldCount = 0;

        int checksumEndIndex = -1;
        int expectedChecksum = -1;
        int tagStartIndex = -1;

        // Single pass field parsing
        for (int i = 0; i < length; i++) {

            byte b = buffer[i];

            if (b == '=') {
                valueStart = i + 1;

            } else if (b == SOH) {

                if (valueStart == -1)
                    return FixError.MALFORMED_FIELD;

                int valueEnd = i;
                int len = valueEnd - valueStart;

                // Enforce strict FIX header ordering for first 3 fields
                if (fieldCount == 0 && tag != 8)
                    return FixError.INVALID_HEADER_ORDER;
                if (fieldCount == 1 && tag != 9)
                    return FixError.INVALID_HEADER_ORDER;
                if (fieldCount == 2 && tag != 35)
                    return FixError.INVALID_HEADER_ORDER;

                // Checksum field terminates parsing
                if (tag == 10) {
                    expectedChecksum = parseInt(buffer, valueStart, valueEnd);
                    checksumEndIndex = tagStartIndex;
                    break;
                }

                switch (tag) {
                    // parse the header fields.
                    case 9:
                        headerView.setBodyLength(parseInt(buffer, valueStart, valueEnd));
                        break;
                    case 34:
                        headerView.setMsgSeqNum(parseInt(buffer, valueStart, valueEnd));
                        break;
                    case 49:
                        headerView.setSender(valueStart, len);
                        break;
                    case 52:
                        headerView.setSendingTime(valueStart, len);
                        break;
                    case 56:
                        headerView.setTarget(valueStart, len);
                        break;
                    case 35:
                        msgType = buffer[valueStart];
                        break;

                    // ===== Body fields =====
                    case 55:
                        if (msgType == 'D') newOrderView.setSymbol(valueStart, len);
                        else if (msgType == 'F') cancelOrderView.setSymbol(valueStart, len);
                        else if (msgType == 'G') replaceOrderView.setSymbol(valueStart, len);
                        break;
                    case 54:
                        if (msgType == 'D') newOrderView.setSide(buffer[valueStart]);
                        else if (msgType == 'F') cancelOrderView.setSide(buffer[valueStart]);
                        else if (msgType == 'G') replaceOrderView.setSide(buffer[valueStart]);
                        break;
                    case 11:
                        if (msgType == 'D') newOrderView.setClOrdId(valueStart, len);
                        else if (msgType == 'F') cancelOrderView.setClOrdId(valueStart, len);
                        else if (msgType == 'G') replaceOrderView.setClOrdId(valueStart, len);
                        break;
                    case 41:
                        if (msgType == 'F') cancelOrderView.setOrigClOrdId(valueStart, len);
                        else if (msgType == 'G') replaceOrderView.setOrigClOrdId(valueStart, len);
                        break;
                    case 1:
                        newOrderView.setAccount(valueStart, len);
                        break;
                    case 21:
                        newOrderView.setHandlInst(buffer[valueStart]);
                        break;
                    case 44:
                        if (msgType == 'D')
                            newOrderView.setPrice(parseDouble(buffer, valueStart, valueEnd));
                        else if (msgType == 'G')
                            replaceOrderView.setPrice(parseDouble(buffer, valueStart, valueEnd));
                        break;
                    case 38:
                        if (msgType == 'G')
                            replaceOrderView.setOrderQty(parseInt(buffer, valueStart, valueEnd));
                        break;
                    case 47:
                        newOrderView.setRule80A(valueStart, len);
                        break;
                    case 59:
                        newOrderView.setTimeInForce(buffer[valueStart]);
                        break;
                    case 60:
                        newOrderView.setTransactTime(valueStart, len);
                        break;
                    case 100:
                        newOrderView.setExDestination(parseInt(buffer, valueStart, valueEnd));
                        break;
                }

                fieldCount++;
                tag = 0;
                valueStart = -1;

            } else if (valueStart == -1) {

                // Parsing numeric tag
                if (b < '0' || b > '9')
                    return FixError.MALFORMED_TAG;

                if (tag == 0)
                    tagStartIndex = i;

                tag = tag * 10 + (b - '0');
            }
        }

        // Second-pass checksum validation
        int checksum = 0;
        for (int i = 0; i < checksumEndIndex; i++)
            checksum += (buffer[i] & 0xFF);

        if ((checksum % 256) != expectedChecksum)
            return FixError.INVALID_CHECKSUM;

        return FixError.NO_ERROR;
    }

    /**
     * Lightweight ASCII integer parsing.
     * Assumes only positive numeric values.
     */
    private static int parseInt(byte[] buffer, int start, int end) {
        int val = 0;
        for (int i = start; i < end; i++)
            val = val * 10 + (buffer[i] - '0');
        return val;
    }

    /**
     * Lightweight ASCII decimal parsing.
     * Avoids any allocation and usage of bigdecimal .
     * Assumes well-formed numeric input - for now.
     * @Todo : to handle not well-formed input. return a Double.NAN and then eventually FixError.INVALID_NUMERIC
     */
    private static double parseDouble(byte[] buffer, int start, int end) {
        double value = 0;
        double divisor = 1;
        boolean fraction = false;

        for (int i = start; i < end; i++) {
            byte b = buffer[i];
            if (b == '.') {
                fraction = true;
            } else {
                value = value * 10 + (b - '0');
                if (fraction)
                    divisor *= 10;
            }
        }
        return fraction ? value / divisor : value;
    }
}