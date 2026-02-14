package com.vikas.main;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class AsciiStringViewTest {

    @Test
    void shouldCompareAsciiCorrectly() {
        AsciiStringView v = new AsciiStringView();
        v.wrap("ASCI".getBytes(StandardCharsets.US_ASCII), 0, 4);

        assertTrue(v.equalsAscii("ASCI"));
        assertFalse(v.equalsAscii("MSFT"));
    }

    @Test
    void shouldRespectEqualsAndHashCodeContract() {

        byte[] buf1 = "ASCI".getBytes(StandardCharsets.US_ASCII);
        byte[] buf2 = "ASCI".getBytes(StandardCharsets.US_ASCII);
        byte[] buf3 = "MSFT".getBytes(StandardCharsets.US_ASCII);

        AsciiStringView v1 = new AsciiStringView();
        AsciiStringView v2 = new AsciiStringView();
        AsciiStringView v3 = new AsciiStringView();

        v1.wrap(buf1, 0, 4);
        v2.wrap(buf2, 0, 4);
        v3.wrap(buf3, 0, 4);

        // equality
        assertEquals(v1, v2);
        assertNotEquals(v1, v3);

        // hashCode consistency
        assertEquals(v1.hashCode(), v2.hashCode());

        // reflexive
        assertEquals(v1, v1);

        // null safety
        assertNotEquals(v1, null);
    }


    @Test
    void shouldCompareDifferentOffsetsSameDataCorrectly() {

        byte[] buf = "XXASCIYY".getBytes(StandardCharsets.US_ASCII);
        byte[] buf2 = "VVASCIVV".getBytes(StandardCharsets.US_ASCII);

        AsciiStringView v1 = new AsciiStringView();
        AsciiStringView v2 = new AsciiStringView();

        v1.wrap(buf, 2, 4); // ASCI
        v2.wrap(buf2, 2, 4); // ASCI

        assertEquals(v1, v2);
    }


}
