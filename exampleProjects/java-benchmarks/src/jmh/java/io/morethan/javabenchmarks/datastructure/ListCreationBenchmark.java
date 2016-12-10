package io.morethan.javabenchmarks.datastructure;

import java.util.ArrayList;
import java.util.List;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import com.google.common.collect.ImmutableList;

@Fork(value = 2)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@BenchmarkMode(Mode.Throughput)
@State(Scope.Benchmark)
public class ListCreationBenchmark {

    private static final int ELEMENT_COUNT = 16;
    private List<String> _list = new ArrayList<>(ELEMENT_COUNT);

    @Benchmark
    public List<String> arrayList() {
        List<String> arrayList = new ArrayList<>();
        for (int i = 0; i < ELEMENT_COUNT; i++) {
            arrayList.add(i + "");
        }
        return arrayList;
    }

    @Benchmark
    public List<String> arrayList_preSized() {
        List<String> arrayList = new ArrayList<>(ELEMENT_COUNT);
        for (int i = 0; i < ELEMENT_COUNT; i++) {
            arrayList.add(i + "");
        }
        return arrayList;
    }

    @Benchmark
    public List<String> arrayList_preSized_reUsed() {
        _list.clear();
        for (int i = 0; i < ELEMENT_COUNT; i++) {
            _list.add(i + "");
        }
        return _list;
    }

    @Benchmark
    public List<String> immutableList() {
        ImmutableList.Builder<String> builder = ImmutableList.builder();
        for (int i = 0; i < ELEMENT_COUNT; i++) {
            builder.add(i + "");
        }
        return builder.build();
    }
}
