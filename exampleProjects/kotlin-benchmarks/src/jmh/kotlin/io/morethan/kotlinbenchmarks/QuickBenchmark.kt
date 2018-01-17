package io.morethan.kotlinbenchmarks

import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Fork
import org.openjdk.jmh.annotations.Measurement
import org.openjdk.jmh.annotations.Warmup
import org.openjdk.jmh.annotations.OutputTimeUnit
import java.util.concurrent.TimeUnit

@Fork(value = 1)
@Warmup(iterations = 0)
@Measurement(iterations = 1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
open public class QuickBenchmark {
    
    
    @Benchmark
    public fun sleep50Milliseconds() {
        Thread.sleep(50);
    }

    @Benchmark
    public fun sleep100Milliseconds() {
        Thread.sleep(100);
    }

}