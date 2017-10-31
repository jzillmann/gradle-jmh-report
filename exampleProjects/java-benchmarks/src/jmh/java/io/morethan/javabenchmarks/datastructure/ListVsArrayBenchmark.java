package io.morethan.javabenchmarks.datastructure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * Accessing all members of a list vs all members of an array.
 */
@Fork(value = 2)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class ListVsArrayBenchmark {

    private List<Integer> _list = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
    private Integer[] _array = new Integer[] { 1, 2, 3, 4 };

    @Benchmark
    public int list_forEach() {
        int sum = 0;
        for (Integer number : _list) {
            sum += number;
        }
        return sum;
    }

    @Benchmark
    public int list_index() {
        int sum = 0;
        for (int i = 0; i < _list.size(); i++) {
            sum += _list.get(i);
        }
        return sum;
    }

    @Benchmark
    public int array() {
        int sum = 0;
        for (int i = 0; i < _array.length; i++) {
            sum += _array[i];
        }
        return sum;
    }

}
