package com.vikas.main;

/**
 * Allocation-free view of ReplaceRequest (35=G).
 * - Stores offsets and lengths per FIX tag
 * - No primitive fields stored directly
 * - Numeric fields parsed lazily and cached
 * - Zero allocations during parsing
 */
public final class ReplaceOrderView {

    private byte[] buffer;

    private final int[] offsets = new int[FixTag.MAX_TAG_SUPPORTED];
    private final int[] lengths = new int[FixTag.MAX_TAG_SUPPORTED];

    // ---- Lazy primitive cache ----
    private boolean priceParsed;
    private double priceCache;

    private boolean qtyParsed;
    private int qtyCache;

    private final AsciiStringView stringView = new AsciiStringView();

    public void wrap(byte[] buffer) {
        this.buffer = buffer;
    }

    /* ===========================
       String / CharSequence fields
       =========================== */

    public CharSequence clOrdId() {
        return wrapView(FixTag.ClOrdID.tag());
    }

    public CharSequence origClOrdId() {
        return wrapView(FixTag.OrigClOrdID.tag());
    }

    public CharSequence symbol() {
        return wrapView(FixTag.Symbol.tag());
    }

    /* ===========================
       Primitive fields (lazy)
       =========================== */

    public int side() {
        return buffer[offsets[FixTag.Side.tag()]];
    }

    public int orderQty() {
        if (!qtyParsed) {
            qtyCache = parseInt(offsets[FixTag.OrderQty.tag()],
                    lengths[FixTag.OrderQty.tag()]);
            qtyParsed = true;
        }
        return qtyCache;
    }

    public double price() {
        if (!priceParsed) {
            priceCache = parseDouble(offsets[FixTag.Price.tag()],
                    lengths[FixTag.Price.tag()]);
            priceParsed = true;
        }
        return priceCache;
    }

    /* ===========================
       Setters (called by parser)
       =========================== */

    void setTag(int tag, int offset, int length) {
        offsets[tag] = offset;
        lengths[tag] = length;

        if (tag == FixTag.Price.tag()) {
            priceParsed = false;
        }
        if (tag == FixTag.OrderQty.tag()) {
            qtyParsed = false;
        }
    }

    /* ===========================
       Presence checks
       =========================== */

    boolean hasClOrdId() {
        return lengths[FixTag.ClOrdID.tag()] > 0;
    }

    boolean hasOrigClOrdId() {
        return lengths[FixTag.OrigClOrdID.tag()] > 0;
    }

    boolean hasOrderQty() {
        return lengths[FixTag.OrderQty.tag()] > 0;
    }

    boolean hasPrice() {
        return lengths[FixTag.Price.tag()] > 0;
    }

    /* ===========================
       Reset for reuse
       =========================== */

    void reset() {
        for (int i = 0; i < offsets.length; i++) {
            offsets[i] = 0;
            lengths[i] = 0;
        }

        priceParsed = false;
        qtyParsed = false;
        priceCache = 0.0;
        qtyCache = 0;
    }

    /* ===========================
       Internal helpers
       =========================== */

    private CharSequence wrapView(int tag) {
        stringView.wrap(buffer, offsets[tag], lengths[tag]);
        return stringView;
    }

    private int parseInt(int offset, int length) {
        int val = 0;
        int end = offset + length;
        for (int i = offset; i < end; i++)
            val = val * 10 + (buffer[i] - '0');
        return val;
    }

    private double parseDouble(int offset, int length) {
        double value = 0;
        double divisor = 1;
        boolean fraction = false;

        int end = offset + length;

        for (int i = offset; i < end; i++) {
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
