package jmh.java;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

@Warmup(iterations = 3, time = 2)
@Measurement(iterations = 1, time = 30)
@Fork(1)
@OutputTimeUnit(TimeUnit.MINUTES)
@State(Scope.Benchmark)
public class SomeClassBench {
    @Setup
    public void setup()  throws IOException {
        // Read some file to setup the benchmark
//        String filenamesCSV = System.getenv("SOME_FILENAMES");
        // Read the file and set the fixture data

    }

    @Benchmark
    @OperationsPerInvocation(20_000)
    @BenchmarkMode(Mode.Throughput)
    public void someBenchmarkTest() throws InterruptedException {
        System.out.println("Benchmarking...");
        Thread.sleep(1000);
//        for (Map<String, Object> a : seedAttributed) {
//            byte[] ret = testClass.somemethod(dest);
//            bytes.sample(ret.length);
//            bh.consume(ret);
//        }
    }
}
