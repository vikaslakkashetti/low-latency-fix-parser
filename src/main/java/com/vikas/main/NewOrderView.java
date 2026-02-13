package com.vikas.main;

/**
 * This class represents a view to the New Order message.
 * Basically allocation free new order body view
 *
 */
public final class NewOrderView {

    private byte[] buffer;
    private int accountOffset;
    private int accountLength;

    private int clOrdIdOffset;
    private int clOrdIdLength;

    private int handlInst;

    private double price;

    private int rule80AOffset;
    private int rule80ALength;

    private int side;

    private int symbolOffset;
    private int symbolLength;

    private int timeInForce;

    private int transactTimeOffset;
    private int transactTimeLength;

    private int exDestination;

    private final AsciiStringView stringView = new AsciiStringView();

    public void wrap(byte[] buffer) {
        this.buffer = buffer;
    }

    public CharSequence account() {
        stringView.wrap(buffer, accountOffset, accountLength);
        return stringView;
    }

    public CharSequence clOrdId() {
        stringView.wrap(buffer, clOrdIdOffset, clOrdIdLength);
        return stringView;
    }

    public CharSequence symbol() {
        stringView.wrap(buffer, symbolOffset, symbolLength);
        return stringView;
    }

    public CharSequence transactTime() {
        stringView.wrap(buffer, transactTimeOffset, transactTimeLength);
        return stringView;
    }

    public CharSequence rule80A() {
        stringView.wrap(buffer, rule80AOffset, rule80ALength);
        return stringView;
    }

    public int handlInst() {
        return handlInst;
    }

    public double price() {
        return price;
    }

    public int side() {
        return side;
    }

    public int timeInForce() {
        return timeInForce;
    }

    public int exDestination() {
        return exDestination;
    }

    void setAccount(int offset, int length) {
        accountOffset = offset;
        accountLength = length;
    }

    void setClOrdId(int offset, int length) {
        clOrdIdOffset = offset;
        clOrdIdLength = length;
    }

    void setSymbol(int offset, int length) {
        symbolOffset = offset;
        symbolLength = length;
    }

    void setTransactTime(int offset, int length) {
        transactTimeOffset = offset;
        transactTimeLength = length;
    }

    void setRule80A(int offset, int length) {
        rule80AOffset = offset;
        rule80ALength = length;
    }

    void setHandlInst(int value) {
        handlInst = value;
    }

    void setSide(int value) {
        side = value;
    }

    void setTimeInForce(int value) {
        timeInForce = value;
    }

    void setExDestination(int value) {
        exDestination = value;
    }

    void setPrice(double value) {
        price = value;
    }
}