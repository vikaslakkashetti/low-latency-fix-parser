package com.vikas.main;

/**
 * Common FIX 4.4 Tags.
 *
 * This enum includes the most frequently used tags across:
 * - Session layer
 * - Order entry
 * - Execution reports
 * - Market data
 *
 * Not a complete FIX 4.4 universe (which contains 900+ tags),
 * but covers the majority used in trading systems.
 */
public enum FixTag {

    // ===== Standard Header =====
    BeginString(8),
    BodyLength(9),
    MsgType(35),
    MsgSeqNum(34),
    SenderCompID(49),
    TargetCompID(56),
    SendingTime(52),
    CheckSum(10),

    // ===== Session Layer =====
    HeartBtInt(108),
    TestReqID(112),
    ResetSeqNumFlag(141),
    Username(553),
    Password(554),
    EncryptMethod(98),

    // ===== Order Entry =====
    Account(1),
    ClOrdID(11),
    OrigClOrdID(41),
    HandlInst(21),
    Symbol(55),
    Side(54),
    OrderQty(38),
    Price(44),
    OrdType(40),
    TimeInForce(59),
    TransactTime(60),
    Rule80A(47),
    ExDestination(100),

    // ===== Execution Report =====
    OrderID(37),
    ExecID(17),
    ExecType(150),
    OrdStatus(39),
    LeavesQty(151),
    CumQty(14),
    AvgPx(6),
    LastQty(32),
    LastPx(31),
    Text(58),

    // ===== Instrument =====
    SecurityID(48),
    SecurityIDSource(22),
    SecurityType(167),
    MaturityMonthYear(200),
    StrikePrice(202),
    CFICode(461),
    SecurityExchange(207),

    // ===== Market Data =====
    MDReqID(262),
    SubscriptionRequestType(263),
    MarketDepth(264),
    NoMDEntries(268),
    MDEntryType(269),
    MDEntryPx(270),
    MDEntrySize(271),
    MDEntryDate(272),
    MDEntryTime(273),

    // ===== Allocation / Post Trade =====
    AllocID(70),
    NoAllocs(78),
    AllocAccount(79),
    AllocQty(80),

    // ===== Risk / Advanced =====
    ManualOrderIndicator(1028),
    CustOrderCapacity(582),
    OrderCapacity(528);



    private final int tag;
    public static final int MAX_TAG_SUPPORTED = 1028;

    FixTag(int tag) {
        this.tag = tag;
    }

    public int tag() {
        return tag;
    }

    // Optional fast lookup table
    private static final java.util.Map<Integer, FixTag> LOOKUP = new java.util.HashMap<>();

    static {
        for (FixTag t : values()) {
            LOOKUP.put(t.tag, t);
        }
    }

    public static FixTag fromTag(int tag) {
        return LOOKUP.get(tag);
    }
}
