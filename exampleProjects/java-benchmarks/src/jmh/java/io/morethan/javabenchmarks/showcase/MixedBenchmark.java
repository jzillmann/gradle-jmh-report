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
 * Benchmark with different {@link BenchmarkMode}s to show that its possible.
 */
@Fork(value = 1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class MixedBenchmark {

    @Benchmark
    @Warmup(iterations = 1)
    @BenchmarkMode(Mode.AverageTime)
    @Measurement(iterations = 3)
    public void avg() throws InterruptedException {
        Thread.sleep(50);
    }

    @Benchmark
    @Warmup(iterations = 1)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
    public void throughput() throws InterruptedException {
        Thread.sleep(50);
    }

    @Benchmark
    @Warmup(iterations = 0)
    @BenchmarkMode(Mode.SingleShotTime)
    public void singleShot() throws InterruptedException {
        Thread.sleep(50);
    }

}
