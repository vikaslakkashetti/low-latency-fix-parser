package com.vikas.main;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FixParserOptimizedCancelTest {

    private static final byte SOH = 1;

    @Test
    void shouldParseCancel() {

        FixParserOptimized parser = new FixParserOptimized();

        byte[] message = buildMessage(
                "8=FIX.4.4" + soh() +
                        "9=120" + soh() +
                        "35=F" + soh() +
                        "34=3" + soh() +
                        "49=SENDER" + soh() +
                        "52=20260212-12:30:00.000" + soh() +
                        "56=TARGET" + soh() +
                        "11=CXL1" + soh() +
                        "41=ORDER123" + soh() +
                        "55=AAPL" + soh() +
                        "54=1" + soh()
        );

        assertEquals(FixError.NO_ERROR, parser.parse(message, message.length));
        assertEquals('F', parser.getMsgType());

        CancelOrderView view = parser.getCancelOrderView();

        assertEquals("CXL1", view.clOrdId().toString());
        assertEquals("ORDER123", view.origClOrdId().toString());
        assertEquals("AAPL", view.symbol().toString());
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