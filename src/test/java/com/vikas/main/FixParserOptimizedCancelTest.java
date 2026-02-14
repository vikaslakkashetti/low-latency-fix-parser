package com.vikas.main;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.vikas.main.TestUtility.soh;

class FixParserOptimizedCancelTest {


    @Test
    void shouldParseCancel() {

        FixParserOptimized parser = new FixParserOptimized();

        byte[] message = TestUtility.buildMessage(
                "8=FIX.4.4" + soh() +
                        "9=120" + soh() +
                        "35=F" + soh() +
                        "49=SENDER" + soh() +
                        "56=TARGET" + soh() +
                        "34=3" + soh() +
                        "52=20260212-12:30:00.000" + soh() +
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

    @Test
    void shouldFailWhenCancelMissingOrigClOrdId() {

        byte[] message = TestUtility.buildMessage(
                "8=FIX.4.4" + soh() +
                        "9=100" + soh() +
                        "35=F" + soh() +
                        "49=SENDER" + soh() +
                        "56=TARGET" + soh() +
                        "34=1" + soh() +
                        "52=20260212-12:30:00.000" + soh() +
                        "11=CANCEL1" + soh() +
                        "55=AAPL" + soh() +
                        "54=1" + soh()
                // 41=OrigClOrdID
        );

        FixParserOptimized parser = new FixParserOptimized();
        FixError error = parser.parse(message, message.length);

        assertEquals(FixError.MISSING_ORIG_CLORDID, error);
    }

}