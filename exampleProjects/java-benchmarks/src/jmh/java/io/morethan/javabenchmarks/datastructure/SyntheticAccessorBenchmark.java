package io.morethan.javabenchmarks.datastructure;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

/**
 * You know the eclipse warning 'Access to enclosing ... is emulated by a synthetic accessor method" ? This benchmark
 * tries to measure the effect on performance to it.
 * 
 */
@Fork(value = 2)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class SyntheticAccessorBenchmark {

    private ClassWithPrivateMethod _classWithPrivateMethod = new ClassWithPrivateMethod();
    private ClassWithProtectedMethod _classWithProtectedMethod = new ClassWithProtectedMethod();

    @Benchmark
    @SuppressWarnings("synthetic-access")
    public int emulate() {
        return _classWithPrivateMethod.nextInt();
    }

    @Benchmark
    public int dontEmulate() {
        return _classWithProtectedMethod.nextInt();
    }

    protected static class ClassWithPrivateMethod {

        private Random _random = new Random(23);

        private int nextInt() {
            return _random.nextInt();
        }
    }

    protected static class ClassWithProtectedMethod {

        private Random _random = new Random(23);

        protected int nextInt() {
            return _random.nextInt();
        }
    }

}
