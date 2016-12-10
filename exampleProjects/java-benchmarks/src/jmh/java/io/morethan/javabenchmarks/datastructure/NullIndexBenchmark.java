package io.morethan.javabenchmarks.datastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.BitSet;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

/**
 * Benchmarking different approaches to maintain an index of null values.
 */
@Fork(value = 2)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class NullIndexBenchmark {

    private static final int MAX = 100_000_000;
    private static final int EVERY_NULL_ELEMENT = 100;
    private int[] _nullsIndices = new int[(MAX / EVERY_NULL_ELEMENT)];
    private boolean[] _nullsFlags = new boolean[MAX];
    private BitSet _nullsBitSet = new BitSet(MAX);

    @Setup
    public void setup() {
        int current = 0;
        for (int i = 0; i < MAX; i++) {
            if (i % EVERY_NULL_ELEMENT == 0) {
                _nullsIndices[current] = i;
                _nullsFlags[i] = true;
                _nullsBitSet.set(i);
                current++;
            }
        }
    }

    @Benchmark
    public int arrayWithNullIndices() {
        int nullCount = 0;
        boolean _nullsDepleted = false;
        for (int i = 0; i < MAX; i++) {
            if ((!_nullsDepleted) && _nullsIndices[nullCount] == i) {
                nullCount++;
                if (nullCount == _nullsIndices.length) {
                    _nullsDepleted = true;
                }
            }
        }
        assertThat(nullCount).isEqualTo(1000000);
        return nullCount;
    }

    @Benchmark
    public long arrayWithBooleans() {
        int nullCount = 0;
        for (int i = 0; i < MAX; i++) {
            if (_nullsFlags[i]) {
                nullCount++;
            }
        }

        assertThat(nullCount).isEqualTo(1000000);
        return nullCount;
    }

    @Benchmark
    public long bitSet() {
        int nullCount = 0;
        for (int i = 0; i < MAX; i++) {
            if (_nullsBitSet.get(i)) {
                nullCount++;
            }
        }

        assertThat(nullCount).isEqualTo(1000000);
        return nullCount;
    }

}
