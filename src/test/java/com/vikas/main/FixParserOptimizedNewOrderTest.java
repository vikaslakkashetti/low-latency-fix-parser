package com.vikas.main;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FixParserOptimizedNewOrderTest {

    private static final byte SOH = 1;

    @Test
    void shouldParseNewOrder() {

        FixParserOptimized parser = new FixParserOptimized();

        byte[] message = buildMessage(
                "8=FIX.4.4" + soh() +
                        "9=176" + soh() +
                        "35=D" + soh() +
                        "34=2" + soh() +
                        "49=SENDER" + soh() +
                        "52=20260212-12:30:00.000" + soh() +
                        "56=TARGET" + soh() +
                        "1=ACC123" + soh() +
                        "11=ORDER123" + soh() +
                        "21=1" + soh() +
                        "44=123.45" + soh() +
                        "47=A" + soh() +
                        "54=1" + soh() +
                        "55=AAPL" + soh() +
                        "59=0" + soh() +
                        "60=20260212-12:30:01.000" + soh() +
                        "100=12" + soh()
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
    }

    private byte[] buildMessage(String body) {
        byte[] bytes = body.getBytes(StandardCharsets.US_ASCII);
        int sum = 0;
        for (byte b : bytes) sum += (b & 0xFF);
        int checksum = sum % 256;
        String full = body + "10=" + String.format("%03d", checksum) + soh();
        return full.getBytes(StandardCharsets.US_ASCII);
    }

    private String soh() {
        return String.valueOf((char) SOH);
    }
}