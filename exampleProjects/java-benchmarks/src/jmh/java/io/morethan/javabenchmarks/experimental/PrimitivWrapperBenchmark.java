package io.morethan.javabenchmarks.experimental;

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
import org.openjdk.jmh.infra.Blackhole;

import com.google.common.collect.ImmutableList;

/**
 * Benchmarking different approaches access primitive values (flexible schema).
 */
@Fork(value = 2)
@Warmup(iterations = 10)
@Measurement(iterations = 5)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class PrimitivWrapperBenchmark {

    private ValueBase _valueBase = new ValueBase(ImmutableList.<PrimitivHolder>builder()
            .add(new PrimitivStringHolder())
            .add(new PrimitivLongHolder())
            .add(new PrimitivBooleanHolder())
            .add(new PrimitivStringHolder())
            .add(new PrimitivLongHolder())
            .add(new PrimitivBooleanHolder())
            .build());

    private long _currentRow = 0;

    @Benchmark
    public void cast1(Blackhole blackhole) {
        blackhole.consume(_valueBase.getString_Cast1(0, _currentRow));
        blackhole.consume(_valueBase.getLong_Cast1(1, _currentRow));
        blackhole.consume(_valueBase.getBoolean_Cast1(2, _currentRow));
        blackhole.consume(_valueBase.getString_Cast1(3, _currentRow));
        blackhole.consume(_valueBase.getLong_Cast1(4, _currentRow));
        blackhole.consume(_valueBase.getBoolean_Cast1(5, _currentRow));
        _currentRow++;
    }

    @Benchmark
    public void cast2(Blackhole blackhole) {
        blackhole.consume(_valueBase.getString_Cast2(0, _currentRow));
        blackhole.consume(_valueBase.getLong_Cast2(1, _currentRow));
        blackhole.consume(_valueBase.getBoolean_Cast2(2, _currentRow));
        blackhole.consume(_valueBase.getString_Cast2(3, _currentRow));
        blackhole.consume(_valueBase.getLong_Cast2(4, _currentRow));
        blackhole.consume(_valueBase.getBoolean_Cast2(5, _currentRow));
        _currentRow++;
    }

    @Benchmark
    public void cast3(Blackhole blackhole) {
        blackhole.consume(_valueBase.getString_Cast3(0, _currentRow));
        blackhole.consume(_valueBase.getLong_Cast3(1, _currentRow));
        blackhole.consume(_valueBase.getBoolean_Cast3(2, _currentRow));
        blackhole.consume(_valueBase.getString_Cast3(3, _currentRow));
        blackhole.consume(_valueBase.getLong_Cast3(4, _currentRow));
        blackhole.consume(_valueBase.getBoolean_Cast3(5, _currentRow));
        _currentRow++;
    }

    @Benchmark
    public void extractor(Blackhole blackhole) {
        blackhole.consume(_valueBase.getString_Extract(0, _currentRow));
        blackhole.consume(_valueBase.getLong_Extract(1, _currentRow));
        blackhole.consume(_valueBase.getBoolean_Extract(2, _currentRow));
        blackhole.consume(_valueBase.getString_Extract(3, _currentRow));
        blackhole.consume(_valueBase.getLong_Extract(4, _currentRow));
        blackhole.consume(_valueBase.getBoolean_Extract(5, _currentRow));
        _currentRow++;
    }

    private static class ValueBase {

        private final List<PrimitivHolder> _primitivHolders;
        private final PrimitivExtractor[] _primitivExtractors;

        public ValueBase(List<PrimitivHolder> primitivHolders) {
            _primitivHolders = primitivHolders;
            _primitivExtractors = new PrimitivExtractor[primitivHolders.size()];
            for (int i = 0; i < _primitivExtractors.length; i++) {
                _primitivExtractors[i] = PrimitivExtractor.create(primitivHolders.get(i), i);
            }
        }

        public String getString_Cast1(int columnIndex, long row) {
            PrimitivHolder primitivHolder = _primitivHolders.get(columnIndex);
            if (primitivHolder instanceof PrimitivStringHolder) {
                return ((PrimitivStringHolder) primitivHolder).getString(row);
            }
            throw new UnsupportedOperationException(columnIndex + ": " + primitivHolder.getClass().getSimpleName());
        }

        public long getLong_Cast1(int columnIndex, long row) {
            PrimitivHolder primitivHolder = _primitivHolders.get(columnIndex);
            if (primitivHolder instanceof PrimitivLongHolder) {
                return ((PrimitivLongHolder) primitivHolder).getLong(row);
            }
            throw new UnsupportedOperationException(columnIndex + ": " + primitivHolder.getClass().getSimpleName());
        }

        public boolean getBoolean_Cast1(int columnIndex, long row) {
            PrimitivHolder primitivHolder = _primitivHolders.get(columnIndex);
            if (primitivHolder instanceof PrimitivBooleanHolder) {
                return ((PrimitivBooleanHolder) primitivHolder).getBoolean(row);
            }
            throw new UnsupportedOperationException(columnIndex + ": " + primitivHolder.getClass().getSimpleName());
        }

        public String getString_Cast2(int columnIndex, long row) {
            PrimitivHolder primitivHolder = _primitivHolders.get(columnIndex);
            if (PrimitivStringHolder.class.isInstance(primitivHolder)) {
                return PrimitivStringHolder.class.cast(primitivHolder).getString(row);
            }
            throw new UnsupportedOperationException(columnIndex + ": " + primitivHolder.getClass().getSimpleName());
        }

        public long getLong_Cast2(int columnIndex, long row) {
            PrimitivHolder primitivHolder = _primitivHolders.get(columnIndex);
            if (PrimitivLongHolder.class.isInstance(primitivHolder)) {
                return PrimitivLongHolder.class.cast(primitivHolder).getLong(row);
            }
            throw new UnsupportedOperationException(columnIndex + ": " + primitivHolder.getClass().getSimpleName());
        }

        public boolean getBoolean_Cast2(int columnIndex, long row) {
            PrimitivHolder primitivHolder = _primitivHolders.get(columnIndex);
            if (PrimitivBooleanHolder.class.isInstance(primitivHolder)) {
                return PrimitivBooleanHolder.class.cast(primitivHolder).getBoolean(row);
            }
            throw new UnsupportedOperationException(columnIndex + ": " + primitivHolder.getClass().getSimpleName());
        }

        public String getString_Cast3(int columnIndex, long row) {
            PrimitivHolder primitivHolder = _primitivHolders.get(columnIndex);
            try {
                return PrimitivStringHolder.class.cast(primitivHolder).getString(row);
            } catch (ClassCastException e) {
                throw new UnsupportedOperationException(columnIndex + ": " + primitivHolder.getClass().getSimpleName());
            }
        }

        public long getLong_Cast3(int columnIndex, long row) {
            PrimitivHolder primitivHolder = _primitivHolders.get(columnIndex);
            try {
                return PrimitivLongHolder.class.cast(primitivHolder).getLong(row);
            } catch (ClassCastException e) {
                throw new UnsupportedOperationException(columnIndex + ": " + primitivHolder.getClass().getSimpleName());
            }
        }

        public boolean getBoolean_Cast3(int columnIndex, long row) {
            PrimitivHolder primitivHolder = _primitivHolders.get(columnIndex);
            try {
                return PrimitivBooleanHolder.class.cast(primitivHolder).getBoolean(row);
            } catch (ClassCastException e) {
                throw new UnsupportedOperationException(columnIndex + ": " + primitivHolder.getClass().getSimpleName());
            }
        }

        public String getString_Extract(int columnIndex, long row) {
            return _primitivExtractors[columnIndex].getString(row);
        }

        public long getLong_Extract(int columnIndex, long row) {
            return _primitivExtractors[columnIndex].getLong(row);
        }

        public boolean getBoolean_Extract(int columnIndex, long row) {
            return _primitivExtractors[columnIndex].getBoolean(row);
        }

    }

    private static interface PrimitivHolder {
        // marker interface
    }

    private static class PrimitivStringHolder implements PrimitivHolder {
        public String getString(long row) {
            return "string" + row;
        }
    }

    private static class PrimitivLongHolder implements PrimitivHolder {
        public long getLong(long row) {
            return row;
        }
    }

    private static class PrimitivBooleanHolder implements PrimitivHolder {
        public boolean getBoolean(long row) {
            return row % 2 == 0;
        }
    }

    private static class PrimitivExtractor {

        private final PrimitivHolder _primitivHolder;
        private final int _columnIndex;

        public PrimitivExtractor(PrimitivHolder primitivHolder, int columnIndex) {
            _primitivHolder = primitivHolder;
            _columnIndex = columnIndex;
        }

        public String getString(long row) {
            throw new UnsupportedOperationException(_columnIndex + ": " + _primitivHolder.getClass().getSimpleName());
        }

        public long getLong(long row) {
            throw new UnsupportedOperationException(_columnIndex + ": " + _primitivHolder.getClass().getSimpleName());
        }

        public boolean getBoolean(long row) {
            throw new UnsupportedOperationException(_columnIndex + ": " + _primitivHolder.getClass().getSimpleName());
        }

        public static PrimitivExtractor create(PrimitivHolder primitivHolder, int columnIndex) {
            if (primitivHolder instanceof PrimitivStringHolder) {
                return new PrimitivStringExtractor((PrimitivStringHolder) primitivHolder, columnIndex);
            } else if (primitivHolder instanceof PrimitivLongHolder) {
                return new PrimitivLongExtractor((PrimitivLongHolder) primitivHolder, columnIndex);
            } else if (primitivHolder instanceof PrimitivBooleanHolder) {
                return new PrimitivBooleanExtractor((PrimitivBooleanHolder) primitivHolder, columnIndex);
            }
            throw new UnsupportedOperationException(columnIndex + ": " + primitivHolder.getClass().getSimpleName());
        }
    }

    private static class PrimitivStringExtractor extends PrimitivExtractor {

        private PrimitivStringHolder _primitivHolder;

        public PrimitivStringExtractor(PrimitivStringHolder primitivHolder, int columnIndex) {
            super(primitivHolder, columnIndex);
            _primitivHolder = primitivHolder;
        }

        @Override
        public String getString(long row) {
            return _primitivHolder.getString(row);
        }

    }

    private static class PrimitivLongExtractor extends PrimitivExtractor {

        private PrimitivLongHolder _primitivHolder;

        public PrimitivLongExtractor(PrimitivLongHolder primitivHolder, int columnIndex) {
            super(primitivHolder, columnIndex);
            _primitivHolder = primitivHolder;
        }

        @Override
        public long getLong(long row) {
            return _primitivHolder.getLong(row);
        }
    }

    private static class PrimitivBooleanExtractor extends PrimitivExtractor {

        private PrimitivBooleanHolder _primitivHolder;

        public PrimitivBooleanExtractor(PrimitivBooleanHolder primitivHolder, int columnIndex) {
            super(primitivHolder, columnIndex);
            _primitivHolder = primitivHolder;
        }

        @Override
        public boolean getBoolean(long row) {
            return _primitivHolder.getBoolean(row);
        }
    }
}
