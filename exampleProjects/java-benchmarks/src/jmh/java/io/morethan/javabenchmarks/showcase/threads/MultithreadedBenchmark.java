package io.morethan.javabenchmarks.showcase.threads;

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

    private AtomicLong _counter = new AtomicLong();

    @Group("with1Thread")
    @GroupThreads(1)
    @Benchmark
    public void increment1() throws Exception {
        _counter.incrementAndGet();
    }

    @Group("with1Thread")
    @GroupThreads(1)
    @Benchmark
    public long read1() throws Exception {
        return _counter.get();
    }

    @Group("with2Threads")
    @GroupThreads(2)
    @Benchmark
    public void increment2() throws Exception {
        _counter.incrementAndGet();
    }

    @Group("with2Threads")
    @GroupThreads(2)
    @Benchmark
    public long read2() throws Exception {
        return _counter.get();
    }

    @Group("with4Threads")
    @GroupThreads(4)
    @Benchmark
    public void increment4() throws Exception {
        _counter.incrementAndGet();
    }

    @Group("with4Threads")
    @GroupThreads(4)
    @Benchmark
    public long read4() throws Exception {
        return _counter.get();
    }

}
