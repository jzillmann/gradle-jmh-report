package io.morethan.javabenchmarks.io.bytebuffer;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

@Fork(value = 2)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@BenchmarkMode(Mode.Throughput)
@State(Scope.Benchmark)
public class ReadDoubleBenchmark {

    private ByteBuffer _byteBuffer = ByteBuffer.allocate(1024).order(ByteOrder.LITTLE_ENDIAN);

    @Setup
    public void setUp() {
        for (int i = 0; i < _byteBuffer.capacity() / 8; i++) {
            _byteBuffer.putDouble(i);
        }
        _byteBuffer.flip();
    }

    @Benchmark
    public void getDouble() {
        for (int i = 0; i < _byteBuffer.capacity() / 8; i++) {
            double value = _byteBuffer.getDouble();
            assertThat(value).isEqualTo(i);
        }
        _byteBuffer.rewind();
    }

    @Benchmark
    public void getLongToDouble() {
        for (int i = 0; i < _byteBuffer.capacity() / 8; i++) {
            double value = Double.longBitsToDouble(_byteBuffer.getLong());
            assertThat(value).isEqualTo(i);
        }
        _byteBuffer.rewind();
    }
}
