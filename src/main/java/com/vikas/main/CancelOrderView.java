package com.vikas.main;

/**
 * This class represents a view to the Cancel Order message.
 * Basically allocation free new order body view
 */
public final class CancelOrderView {

    private byte[] buffer;

    private int clOrdIdOffset;
    private int clOrdIdLength;

    private int origClOrdIdOffset;
    private int origClOrdIdLength;

    private int symbolOffset;
    private int symbolLength;

    private int side;

    private final AsciiStringView stringView = new AsciiStringView();

    public void wrap(byte[] buffer) {
        this.buffer = buffer;
    }

    public CharSequence clOrdId() {
        stringView.wrap(buffer, clOrdIdOffset, clOrdIdLength);
        return stringView;
    }

    public CharSequence origClOrdId() {
        stringView.wrap(buffer, origClOrdIdOffset, origClOrdIdLength);
        return stringView;
    }

    public CharSequence symbol() {
        stringView.wrap(buffer, symbolOffset, symbolLength);
        return stringView;
    }

    public int side() {
        return side;
    }

    void setClOrdId(int offset, int length) {
        clOrdIdOffset = offset;
        clOrdIdLength = length;
    }

    void setOrigClOrdId(int offset, int length) {
        origClOrdIdOffset = offset;
        origClOrdIdLength = length;
    }

    void setSymbol(int offset, int length) {
        symbolOffset = offset;
        symbolLength = length;
    }

    void setSide(int value) {
        side = value;
    }

    boolean hasClOrdId() {
        return clOrdIdLength > 0;
    }

    boolean hasOrigClOrdId() {
        return origClOrdIdLength > 0;
    }

    void reset() {
        side = 0;

        clOrdIdOffset = 0;
        clOrdIdLength = 0;

        origClOrdIdOffset = 0;
        origClOrdIdLength = 0;

        symbolOffset = 0;
        symbolLength = 0;
    }


}