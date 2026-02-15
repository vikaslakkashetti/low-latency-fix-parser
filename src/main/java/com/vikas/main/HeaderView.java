package com.vikas.main;

import java.util.Arrays;

/**
 * FIX Header view.
 *
 * - Zero allocation
 * - Offset/length based
 * - Lazy numeric parsing with caching
 * - Reusable across messages
 *
 * Supported header tags:
 * 9  - BodyLength
 * 34 - MsgSeqNum
 * 49 - SenderCompID
 * 52 - SendingTime
 * 56 - TargetCompID
 */
public final class HeaderView {

    private byte[] buffer;

    // Offset/length storage for all tags
    private final int[] offsets = new int[FixTag.MAX_TAG_SUPPORTED];
    private final int[] lengths = new int[FixTag.MAX_TAG_SUPPORTED];

    // Lazy numeric cache
    private int cachedBodyLength;
    private boolean bodyLengthParsed;

    private int cachedMsgSeqNum;
    private boolean msgSeqNumParsed;

    private final AsciiStringView stringView = new AsciiStringView();

    public void wrap(byte[] buffer) {
        this.buffer = buffer;
    }

    // =========================
    // Lazy numeric getters
    // =========================

    public int bodyLength() {
        if (!bodyLengthParsed) {
            int tag = FixTag.BodyLength.tag();
            cachedBodyLength = parseInt(offsets[tag], lengths[tag]);
            bodyLengthParsed = true;
        }
        return cachedBodyLength;
    }

    public int msgSeqNum() {
        if (!msgSeqNumParsed) {
            int tag = FixTag.MsgSeqNum.tag();
            cachedMsgSeqNum = parseInt(offsets[tag], lengths[tag]);
            msgSeqNumParsed = true;
        }
        return cachedMsgSeqNum;
    }

    // =========================
    // String getters (zero allocation)
    // =========================

    public CharSequence senderCompID() {
        int tag = FixTag.SenderCompID.tag();
        stringView.wrap(buffer, offsets[tag], lengths[tag]);
        return stringView;
    }

    public CharSequence targetCompID() {
        int tag = FixTag.TargetCompID.tag();
        stringView.wrap(buffer, offsets[tag], lengths[tag]);
        return stringView;
    }

    public CharSequence sendingTime() {
        int tag = FixTag.SendingTime.tag();
        stringView.wrap(buffer, offsets[tag], lengths[tag]);
        return stringView;
    }

    // =========================
    // Parser entry point
    // =========================

    void setTag(int tag, int offset, int length) {
        offsets[tag] = offset;
        lengths[tag] = length;

        // Reset lazy cache if relevant tag updated
        if (tag == FixTag.BodyLength.tag()) {
            bodyLengthParsed = false;
        } else if (tag == FixTag.MsgSeqNum.tag()) {
            msgSeqNumParsed = false;
        }
    }

    boolean hasTag(int tag) {
        return lengths[tag] > 0;
    }

    // =========================
    // Reset for reuse
    // =========================

    void reset() {
        Arrays.fill(offsets, 0);
        Arrays.fill(lengths, 0);

        bodyLengthParsed = false;
        msgSeqNumParsed = false;

        cachedBodyLength = 0;
        cachedMsgSeqNum = 0;
    }

    // =========================
    // Internal lightweight ASCII int parser
    // =========================

    private int parseInt(int offset, int length) {
        int val = 0;
        int end = offset + length;

        for (int i = offset; i < end; i++)
            val = val * 10 + (buffer[i] - '0');

        return val;
    }
}
