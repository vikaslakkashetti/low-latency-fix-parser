package com.vikas.main;

/**
 * This class represents a view to the Replace Order message.
 * Basically allocation free new order body view
 */
public final class ReplaceOrderView {

    private byte[] buffer;

    // Required fields for Replace (G)

    private int clOrdIdOffset;
    private int clOrdIdLength;

    private int origClOrdIdOffset;
    private int origClOrdIdLength;

    private int symbolOffset;
    private int symbolLength;

    private int side;
    private double price;
    private int orderQty;

    private final AsciiStringView stringView = new AsciiStringView();

    public void wrap(byte[] buffer) {
        this.buffer = buffer;
    }

    // ================= STRING GETTERS =================

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

    // ================= PRIMITIVE GETTERS =================

    public int side() {
        return side;
    }

    public double price() {
        return price;
    }

    public int orderQty() {
        return orderQty;
    }

    // ================= SETTERS (parser only) =================

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

    void setPrice(double value) {
        price = value;
    }

    void setOrderQty(int value) {
        orderQty = value;
    }

    boolean hasClOrdId() { return clOrdIdLength > 0; }

    boolean hasOrigClOrdId() { return origClOrdIdLength > 0;  }

    boolean hasOrderQty() {
        return orderQty > 0;
    }

    boolean hasPrice() {
        return price != 0.0;
    }


    void reset() {
        orderQty = 0;
        price = 0.0;
        side = 0;

        clOrdIdOffset = 0;
        clOrdIdLength = 0;

        origClOrdIdOffset = 0;
        origClOrdIdLength = 0;

        symbolOffset = 0;
        symbolLength = 0;
    }


}