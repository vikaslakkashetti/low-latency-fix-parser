package com.vikas.main;

import org.junit.jupiter.api.Test;
import static com.vikas.main.TestUtility.soh;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FixParserOptimizedNewOrderTest {


    @Test
    void shouldParseNewOrder() {

        FixParserOptimized parser = new FixParserOptimized();

        byte[] message = TestUtility.buildMessage(
                "8=FIX.4.4" + soh() +
                        "9=176" + soh() +
                        "35=D" + soh() +
                        "49=SENDER" + soh() +
                        "56=TARGET" + soh() +
                        "34=2" + soh() +
                        "52=20260212-12:30:00.000" + soh() +
                        "1=ACC123" + soh() +
                        "11=ORDER123" + soh() +
                        "21=1" + soh() +
                        "44=123.45" + soh() +
                        "47=A" + soh() +
                        "54=1" + soh() +
                        "38=1000" + soh() +
                        "55=AAPL" + soh() +
                        "59=0" + soh() +
                        "60=20260212-12:30:01.000" + soh() +
                        "100=XMAS" + soh()
        );

        assertEquals(FixError.NO_ERROR, parser.parse(message, message.length));

        assertEquals('D', parser.getMsgType());

        HeaderView header = parser.getHeaderView();
        assertEquals(176, header.bodyLength());
        assertEquals(2, header.msgSeqNum());
        assertEquals("SENDER", header.senderCompID().toString());
        assertEquals("TARGET", header.targetCompID().toString());

        NewOrderView view = parser.getNewOrderView();
        assertEquals("ACC123", view.account().toString());
        assertEquals("ORDER123", view.clOrdId().toString());
        assertEquals("AAPL", view.symbol().toString());
        assertEquals(123.45, view.price(), 0.0001);
        assertEquals('1', view.side());
        assertEquals("XMAS", view.exDestination().toString());
    }

    @Test
    void shouldFailWhenNewOrderMissingClOrdId() {

        byte[] message = TestUtility.buildMessage(
                "8=FIX.4.4" + soh() +
                        "9=100" + soh() +
                        "35=D" + soh() +
                        "49=SENDER" + soh() +
                        "56=TARGET" + soh() +
                        "34=1" + soh() +
                        "52=20260212-12:30:00.000" + soh() +
                        "55=AAPL" + soh() +
                        "54=1" + soh()
                // Missing 11=ClOrdID
        );
        FixParserOptimized parser = new FixParserOptimized();
        FixError error = parser.parse(message, message.length);
        assertEquals(FixError.MISSING_CLORDID, error);
    }

    @Test
    void shouldFailWhenNewOrderMissingOrderQty() {

        byte[] message = TestUtility.buildMessage(
                "8=FIX.4.4" + soh() +
                        "9=100" + soh() +
                        "35=D" + soh() +
                        "49=SENDER" + soh() +
                        "56=TARGET" + soh() +
                        "34=1" + soh() +
                        "52=20260212-12:30:00.000" + soh() +
                        "55=AAPL" + soh() +
                        "54=1" + soh() +
                        "11=ClOrdID" + soh()
        );
        FixParserOptimized parser = new FixParserOptimized();
        FixError error = parser.parse(message, message.length);
        assertEquals(FixError.MISSING_ORDER_QTY, error);
    }



}