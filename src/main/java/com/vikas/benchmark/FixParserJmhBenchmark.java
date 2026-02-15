package com.vikas.benchmark;
/**
 * JMH microbenchmark measuring average time per FIX message parse.
 * Used to obtain stable ns/op measurements under:
 * - Multiple forks
 * - JVM warmup
 * - GC profiling
 *
 * Baseline result:
 * Benchmark                                                  Mode  Cnt    Score    Error   Units
 * FixParserJmhBenchmark.parseFullMessage                     avgt   10  584.813 ± 66.736   ns/op
 * FixParserJmhBenchmark.parseFullMessage:gc.alloc.rate       avgt   10    0.006 ±  0.001  MB/sec
 * FixParserJmhBenchmark.parseFullMessage:gc.alloc.rate.norm  avgt   10    0.003 ±  0.001    B/op
 * FixParserJmhBenchmark.parseFullMessage:gc.count            avgt   10      ≈ 0           counts
 */

import com.vikas.main.FixError;
import com.vikas.main.FixParserOptimized;
import com.vikas.main.HeaderView;
import com.vikas.main.NewOrderView;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
@Fork(2)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
public class FixParserJmhBenchmark {

    private static final byte SOH = 1;

    private FixParserOptimized parser;
    private byte[] message;

    @Setup
    public void setup() {
        parser = new FixParserOptimized();
        message = buildMessage(
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
    }

    @Benchmark
    public void parseFullMessage(Blackhole bh) {

        FixError err = parser.parse(message, message.length);
        bh.consume(err);

        HeaderView header = parser.getHeaderView();
        bh.consume(header.msgSeqNum());
        bh.consume(header.bodyLength());

        if (parser.getMsgType() == 'D') {
            NewOrderView v = parser.getNewOrderView();
            bh.consume(v.price());
            bh.consume(v.side());
        }
    }

    private byte[] buildMessage(String body) {
        byte[] bytes = body.getBytes(StandardCharsets.US_ASCII);

        int sum = 0;
        for (byte b : bytes) {
            sum += (b & 0xFF);
        }

        int checksum = sum % 256;

        String full = body + "10=" + String.format("%03d", checksum) + soh();
        return full.getBytes(StandardCharsets.US_ASCII);
    }

    private String soh() {
        return String.valueOf((char) SOH);
    }
}