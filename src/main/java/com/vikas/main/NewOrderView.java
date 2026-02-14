package com.vikas.main;

/**
 * This class represents a view to the New Order message.
 * Basically allocation free new order body view
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

    private int exDestinationOffset;
    private int exDestinationLength;


    private int orderQty;


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

    public CharSequence exDestination() {
        stringView.wrap(buffer, exDestinationOffset, exDestinationLength);
        return stringView;
    }

    public int orderQty() {
        return orderQty;
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

    void setExDestination(int offset, int length) {
        exDestinationOffset = offset;
        exDestinationLength = length;
    }

    void setOrderQty(int value) {
        orderQty = value;
    }


    void setPrice(double value) {
        price = value;
    }

    boolean hasClOrdId() {
        return clOrdIdLength > 0;
    }

    boolean hasSymbol() {
        return symbolLength > 0;
    }

    boolean hasOrderQty() {
        return orderQty > 0;
    }

    boolean hasPrice() {
        return price != 0.0;
    }

    boolean hasSide() {
        return side != 0;
    }

    boolean hasExDestination() {
        return exDestinationLength > 0;
    }

    void reset() {
        orderQty = 0;
        price = 0.0;
        side = 0;
        handlInst = 0;
        timeInForce = 0;


        accountOffset = 0;
        accountLength = 0;

        clOrdIdOffset = 0;
        clOrdIdLength = 0;

        symbolOffset = 0;
        symbolLength = 0;

        rule80AOffset = 0;
        rule80ALength = 0;

        transactTimeOffset = 0;
        transactTimeLength = 0;

        exDestinationOffset = 0;
        exDestinationLength = 0;
    }


}