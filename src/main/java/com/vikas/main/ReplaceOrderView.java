package com.vikas.main;

/**
 * This class represents a view to the Replace Order message.
 * Basically allocation free new order body view
 *
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
}