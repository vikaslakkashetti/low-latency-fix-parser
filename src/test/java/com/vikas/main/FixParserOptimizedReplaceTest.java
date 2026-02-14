package com.vikas.main;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.vikas.main.TestUtility.soh;

class FixParserOptimizedReplaceTest {


    @Test
    void shouldParseReplace() {

        FixParserOptimized parser = new FixParserOptimized();

        byte[] message = TestUtility.buildMessage(
                "8=FIX.4.4" + soh() +
                        "9=140" + soh() +
                        "35=G" + soh() +
                        "49=SENDER" + soh() +
                        "56=TARGET" + soh() +
                        "34=4" + soh() +
                        "52=20260212-12:30:00.000" + soh() +
                        "11=REPL1" + soh() +
                        "41=ORDER123" + soh() +
                        "55=AAPL" + soh() +
                        "54=1" + soh() +
                        "44=150.50" + soh() +
                        "38=100" + soh()
        );

        assertEquals(FixError.NO_ERROR, parser.parse(message, message.length));
        assertEquals('G', parser.getMsgType());

        ReplaceOrderView view = parser.getReplaceOrderView();

        assertEquals("REPL1", view.clOrdId().toString());
        assertEquals("ORDER123", view.origClOrdId().toString());
        assertEquals("AAPL", view.symbol().toString());
        assertEquals('1', view.side());
        assertEquals(150.50, view.price(), 0.0001);
        assertEquals(100, view.orderQty());
    }

}