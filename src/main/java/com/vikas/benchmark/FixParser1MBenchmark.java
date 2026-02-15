package com.vikas.benchmark;

import com.vikas.main.FixParserOptimized;

import java.nio.charset.StandardCharsets;

public class FixParser1MBenchmark {

    private static final byte SOH = 1;

    public static void main(String[] args) {

        FixParserOptimized parser = new FixParserOptimized();
        //Build the fix message
        byte[] message = buildMessage(
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
                        "55=AAPL" + soh() +
                        "59=0" + soh() +
                        "60=20260212-12:30:01.000" + soh() +
                        "100=12" + soh()
        );

        final int iterations = 1_000_000;

        // do a warmup
        for (int i = 0; i < 100_000; i++) {
            parser.parse(message, message.length);
        }

        long start = System.nanoTime();
        //Run the 1 million iterations
        for (int i = 0; i < iterations; i++) {
            parser.parse(message, message.length);
        }

        long end = System.nanoTime();
        //Calculate the output results
        long totalNs = end - start;
        double avgNs = (double) totalNs / iterations;
        double throughput = 1_000_000_000.0 / avgNs;
        //Print the results
        System.out.println("Parsed 1,000,000 messages");
        System.out.println("Total time (ms): " + totalNs / 1_000_000.0);
        System.out.println("Average ns/message: " + avgNs);
        System.out.println("Throughput (msg/sec): " + throughput);
    }

    private static byte[] buildMessage(String body) {
        byte[] bytes = body.getBytes(StandardCharsets.US_ASCII);

        int sum = 0;
        for (byte b : bytes) {
            sum += (b & 0xFF);
        }

        int checksum = sum % 256;

        String full = body + "10=" + String.format("%03d", checksum) + soh();
        return full.getBytes(StandardCharsets.US_ASCII);
    }

    private static String soh() {
        return String.valueOf((char) SOH);
    }
}