package io.morethan.javabenchmarks.showcase.threads;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

/**
 * Benchmarks using multithreaded JMH facilities.
 */
@Fork(value = 1)
@Warmup(iterations = 0)
@Measurement(iterations = 5)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class TheMoreThreadsTheSlowerBenchmark {

    private WorkPool _workPool = new WorkPool();

    @Benchmark
    @Threads(1)
    public void with001Threads() throws Exception {
        _workPool.doWork();
    }

    @Benchmark
    @Threads(5)
    public void with005Threads() throws Exception {
        _workPool.doWork();
    }

    @Benchmark
    @Threads(25)
    public void with025Threads() throws Exception {
        _workPool.doWork();
    }

    @Benchmark
    @Threads(50)
    public void with050Threads() throws Exception {
        _workPool.doWork();
    }

    public static class WorkPool {

        private AtomicInteger _threadsDoingWork = new AtomicInteger();

        public void doWork() throws InterruptedException {
            int threadsDoingWork = _threadsDoingWork.getAndIncrement();
            try {
                Thread.sleep(50 + threadsDoingWork * 10);
            } finally {
                _threadsDoingWork.decrementAndGet();
            }

        }
    }

}
