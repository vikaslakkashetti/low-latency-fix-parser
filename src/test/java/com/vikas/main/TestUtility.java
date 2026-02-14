package com.vikas.main;

import java.nio.charset.StandardCharsets;

public class TestUtility {
    private static final byte SOH = 1;
    public static byte[] buildMessage(String body) {
        byte[] bytes = body.getBytes(StandardCharsets.US_ASCII);
        int sum = 0;
        for (byte b : bytes) sum += (b & 0xFF);
        int checksum = sum % 256;
        String full = body + "10=" + String.format("%03d", checksum) + soh();
        return full.getBytes(StandardCharsets.US_ASCII);
    }
    public static String soh() {
        return String.valueOf((char) SOH);
    }
}
