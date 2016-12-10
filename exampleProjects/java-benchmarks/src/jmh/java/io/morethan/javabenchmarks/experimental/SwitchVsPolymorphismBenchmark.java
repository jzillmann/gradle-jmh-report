package io.morethan.javabenchmarks.experimental;

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
 * Benchmarking different ways coupling a primitive reader with a primitive consumer.
 */
@Fork(value = 2)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class SwitchVsPolymorphismBenchmark {

    IntConsumer _intConsumer = new IntConsumer();
    PolymorphicIntReader _polymorphicReader = new PolymorphicIntReader();
    SwitchIntReader _switchReader = new SwitchIntReader();
    CoupledIntReader _coupledReader = new CoupledIntReader(_intConsumer);

    @Benchmark
    public int polymorphic() {
        _polymorphicReader.read(_intConsumer);
        return _intConsumer.getCurrent();
    }

    @Benchmark
    public int switchStyle() {
        _switchReader.read(_intConsumer);
        return _intConsumer.getCurrent();
    }

    @Benchmark
    public int coupled() {
        _coupledReader.read();
        return _intConsumer.getCurrent();
    }

    public static abstract class PolymorphicReader {

        public void read(Consumer consumer) {
            throw new UnsupportedOperationException("Does not support consumer class " + consumer.getClass());
        }

        public void read(IntConsumer consumer) {
            throw new UnsupportedOperationException();
        }

        public void read(StringConsumer consumer) {
            throw new UnsupportedOperationException();
        }

    }

    public static class PolymorphicIntReader extends PolymorphicReader {

        private int _current;

        @Override
        public void read(IntConsumer consumer) {
            consumer.consume(_current++);
        }

    }

    public static enum Type {
        INT, STRING;
    }

    public static abstract class SwitchReader {

        public final void read(Consumer consumer) {
            switch (consumer.getType()) {
            case INT:
                readInt((IntConsumer) consumer);
                break;
            case STRING:
                readString((StringConsumer) consumer);
                break;
            default:
                throw new UnsupportedOperationException("Does not support consumer of type " + consumer.getType());
            }
        }

        protected void readInt(IntConsumer consumer) {
            throw new UnsupportedOperationException();
        }

        protected void readString(StringConsumer consumer) {
            throw new UnsupportedOperationException();
        }

    }

    public static class SwitchIntReader extends SwitchReader {

        private int _current;

        @Override
        public void readInt(IntConsumer consumer) {
            consumer.consume(_current++);
        }

    }

    public static abstract class CoupledReader {

        public abstract void read();

    }

    public static class CoupledIntReader extends CoupledReader {

        private int _current;
        private final IntConsumer _consumer;

        public CoupledIntReader(IntConsumer consumer) {
            _consumer = consumer;
        }

        @Override
        public void read() {
            _consumer.consume(_current++);

        }

    }

    public static interface Consumer {
        Type getType();
    }

    public static class IntConsumer implements Consumer {

        private int _current;

        public int getCurrent() {
            return _current;
        }

        @Override
        public Type getType() {
            return Type.INT;
        }

        void consume(int value) {
            _current = value;
        }
    }

    public static class StringConsumer implements Consumer {

        private String _current;

        public String getCurrent() {
            return _current;
        }

        @Override
        public Type getType() {
            return Type.STRING;
        }

        void consume(String value) {
            _current = value;
        }
    }
}
