package io.morethan.javabenchmarks.showcase;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;

/**
 * Benchmark which executes very fast to test the pipeline.
 */
@Fork(value = 1)
@Warmup(iterations = 0)
@Measurement(iterations = 1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class QuickBenchmark {

    @Benchmark
    public void sleep50Milliseconds() throws InterruptedException {
        Thread.sleep(50);
    }

    @Benchmark
    public void sleep100Milliseconds() throws InterruptedException {
        Thread.sleep(100);
    }

}
