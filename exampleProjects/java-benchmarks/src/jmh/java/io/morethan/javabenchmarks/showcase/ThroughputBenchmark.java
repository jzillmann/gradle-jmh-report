package io.morethan.javabenchmarks.showcase;

import java.util.Random;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

/**
 * Benchmarks tracking throughput.
 */
@State(Scope.Benchmark)
@Fork(value = 2)
@Warmup(iterations = 1)
@Measurement(iterations = 10)
@BenchmarkMode(Mode.Throughput)
public class ThroughputBenchmark {

    private Random _random = new Random();

    @Benchmark
    public void sleep50Milliseconds() throws InterruptedException {
        Thread.sleep(50);
    }

    @Benchmark
    public void sleep100Milliseconds() throws InterruptedException {
        Thread.sleep(100);
    }

    @Benchmark
    public void sleep100MillisecondsRandom() throws InterruptedException {
        Thread.sleep(50 + _random.nextInt(50));
    }
}
