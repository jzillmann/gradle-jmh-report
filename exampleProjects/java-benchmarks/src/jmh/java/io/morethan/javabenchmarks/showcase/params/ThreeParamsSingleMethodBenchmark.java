package io.morethan.javabenchmarks.showcase.params;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

/**
 * Benchmark showing the use of JMH's '@Param' annotation.
 */
@Fork(1)
@Warmup(iterations = 2)
@Measurement(iterations = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
public class ThreeParamsSingleMethodBenchmark {

    @Param({ "10", "20" })
    public int a_milis;
    @Param({ "100", "500", "1000" })
    public int b_micros;
    @Param({ "1000", "3000" })
    public int c_nanos;

    @Benchmark
    public void sleep() throws InterruptedException {
        long time = a_milis;
        time += TimeUnit.MICROSECONDS.toMillis(b_micros);
        time += TimeUnit.NANOSECONDS.toMillis(c_nanos);
        Thread.sleep(time);
    }

}
