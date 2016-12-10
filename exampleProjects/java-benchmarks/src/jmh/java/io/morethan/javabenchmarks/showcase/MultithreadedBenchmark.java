package io.morethan.javabenchmarks.showcase;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.GroupThreads;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

/**
 * Benchmarks using multithreaded JMH facilities.
 */
@Fork(value = 1)
@Warmup(iterations = 0)
@Measurement(iterations = 5)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Group)
public class MultithreadedBenchmark {

    private static final int MAX_VALUE = 1_000_000;
    AtomicLong _counter = new AtomicLong();

    @Group("withThreads1")
    @GroupThreads(1)
    @Benchmark
    public void withThreads1() throws Exception {
        if (_counter.get() < MAX_VALUE) {
            _counter.incrementAndGet();
        }
    }

    @Group("withThreads2")
    @GroupThreads(2)
    @Benchmark
    public void withThreads2() throws Exception {
        if (_counter.get() < MAX_VALUE) {
            _counter.incrementAndGet();
        }
    }

    @Group("withThreads4")
    @GroupThreads(4)
    @Benchmark
    public void withThreads4() throws Exception {
        if (_counter.get() < MAX_VALUE) {
            _counter.incrementAndGet();
        }

    }

}
