package com.vikas.main;

/**
 * This class represents the FIX message header.
 * Store parsed values of common header fields
 * Provides lightweight accessors without allocation
 * Reusable across multiple FIX message types
 * String fields are exposed via single reused AsciiStringView to avoid allocation *
 * FIX header fields handled (for now)
 * - 9   (BodyLength)
 * - 34  (MsgSeqNum)
 * - 49  (SenderCompID)
 * - 52  (SendingTime)
 * - 56  (TargetCompID)
 * Fields can be added as required
 */
public final class HeaderView {

    /**
     * Underlying FIX message buffer.
     */
    private byte[] buffer;

    // Primitive header fields
    private int bodyLength;
    private int msgSeqNum;

    // Offsets into buffer for the various fields
    private int senderCompOffset;
    private int senderCompLength;

    private int targetCompOffset;
    private int targetCompLength;

    private int sendingTimeOffset;
    private int sendingTimeLength;

    /**
     * Reused string view as explained earlier to avoid per-call allocation and reduce memory footprint.
     */
    private final AsciiStringView stringView = new AsciiStringView();

    /**
     * Wraps the underlying FIX message buffer.
     * No data is copied.
     */
    public void wrap(byte[] buffer) {
        this.buffer = buffer;
    }

    public int bodyLength() {
        return bodyLength;
    }

    public int msgSeqNum() {
        return msgSeqNum;
    }

    /**
     * Returns SenderCompID as a zero-allocation CharSequence.
     */
    public CharSequence senderCompID() {
        stringView.wrap(buffer, senderCompOffset, senderCompLength);
        return stringView;
    }

    /**
     * Returns TargetCompID as a zero-allocation CharSequence.
     */
    public CharSequence targetCompID() {
        stringView.wrap(buffer, targetCompOffset, targetCompLength);
        return stringView;
    }

    /**
     * Returns SendingTime as a zero-allocation CharSequence.
     */
    public CharSequence sendingTime() {
        stringView.wrap(buffer, sendingTimeOffset, sendingTimeLength);
        return stringView;
    }


    void setBodyLength(int value) {
        bodyLength = value;
    }

    void setMsgSeqNum(int value) {
        msgSeqNum = value;
    }

    void setSender(int offset, int length) {
        senderCompOffset = offset;
        senderCompLength = length;
    }

    void setTarget(int offset, int length) {
        targetCompOffset = offset;
        targetCompLength = length;
    }

    void setSendingTime(int offset, int length) {
        sendingTimeOffset = offset;
        sendingTimeLength = length;
    }

    void reset() {
        bodyLength = 0;
        msgSeqNum = 0;

        senderCompOffset = 0;
        senderCompLength = 0;

        targetCompOffset = 0;
        targetCompLength = 0;

        sendingTimeOffset = 0;
        sendingTimeLength = 0;
    }

}