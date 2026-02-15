package com.vikas.main;

/**
 * Allocation-free Cancel Order view using tag-indexed storage.
 */
public final class CancelOrderView {

    private byte[] buffer;

    private final int[] offsets = new int[FixTag.MAX_TAG_SUPPORTED];
    private final int[] lengths = new int[FixTag.MAX_TAG_SUPPORTED];

    private final AsciiStringView stringView = new AsciiStringView();

    public void wrap(byte[] buffer) {
        this.buffer = buffer;
    }

    public CharSequence clOrdId() {
        stringView.wrap(buffer,
                offsets[FixTag.ClOrdID.tag()],
                lengths[FixTag.ClOrdID.tag()]);
        return stringView;
    }

    public CharSequence origClOrdId() {
        stringView.wrap(buffer,
                offsets[FixTag.OrigClOrdID.tag()],
                lengths[FixTag.OrigClOrdID.tag()]);
        return stringView;
    }

    public CharSequence symbol() {
        stringView.wrap(buffer,
                offsets[FixTag.Symbol.tag()],
                lengths[FixTag.Symbol.tag()]);
        return stringView;
    }

    public int side() {
        return buffer[offsets[FixTag.Side.tag()]];
    }

    void setTag(int tag, int offset, int length) {
        offsets[tag] = offset;
        lengths[tag] = length;
    }

    boolean hasTag(int tag) {
        return lengths[tag] > 0;
    }

    void reset() {
        java.util.Arrays.fill(offsets, 0);
        java.util.Arrays.fill(lengths, 0);
    }
}
