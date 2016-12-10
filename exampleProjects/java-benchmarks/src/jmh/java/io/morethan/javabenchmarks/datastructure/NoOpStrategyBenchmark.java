package io.morethan.javabenchmarks.datastructure;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;

import com.google.common.base.Optional;

/**
 * Benchmarking two approaches to an optional interface/behavior. Variant a) have a no-operation implementation, variant
 * b) handle a null instance, variant c) have a optional.
 */
@Fork(value = 2)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class NoOpStrategyBenchmark {

    private final Filterable _noOpFilterable = Filterable.NO_OP_FILTERABLE;
    private final Filterable _nullFilterable = null;
    private final Optional<Filterable> _optionalFilterable = Optional.absent();
    private final Filterable _dropEvenFilterable = new DropEvenValues();

    private long _currentValue;

    @TearDown
    public void tearDown() {
        assert _currentValue > 0 : String.format("Expected _currentValue be greater then 0 but was %s", _currentValue);
    }

    @Benchmark
    public boolean nullCheck() {
        final boolean dropValue;
        if (_nullFilterable == null) {
            dropValue = false;
        } else {
            dropValue = _nullFilterable.dropValue(_currentValue);
        }
        _currentValue++;
        return dropValue;
    }

    @Benchmark
    public boolean optional() {
        final boolean dropValue;
        if (_optionalFilterable.isPresent()) {
            dropValue = _nullFilterable.dropValue(_currentValue);
        } else {
            dropValue = false;
        }
        _currentValue++;
        return dropValue;
    }

    @Benchmark
    public boolean noOpImplementation() {
        return _noOpFilterable.dropValue(_currentValue++);
    }

    @Benchmark
    public boolean someImplementation() {
        return _dropEvenFilterable.dropValue(_currentValue++);
    }

    private static interface Filterable {

        boolean dropValue(long i);

        static Filterable NO_OP_FILTERABLE = new Filterable() {
            @Override
            public boolean dropValue(long i) {
                return false;
            }
        };
    }

    private static class DropEvenValues implements Filterable {

        @Override
        public boolean dropValue(long i) {
            return i % 2 == 0;
        }

    }

}
