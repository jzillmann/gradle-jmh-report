package io.morethan.javabenchmarks.io.bytebuffer;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.LongBuffer;

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
public class ByteSkippingBenchmark {

    private ByteBuffer _byteBuffer = ByteBuffer.allocate(64 * 1024).order(ByteOrder.LITTLE_ENDIAN);

    @Setup
    public void setUp() {
        for (int i = 0; i < _byteBuffer.capacity() / 8; i++) {
            _byteBuffer.putLong(i);
        }
        _byteBuffer.flip();
    }

    private boolean shouldSkip(int i) {
        int singleDigitNumber = i % 10;
        return singleDigitNumber == 3 || singleDigitNumber == 4 || singleDigitNumber == 5 || singleDigitNumber == 8;
    }

    @Benchmark
    public void skip() {
        for (int i = 0; i < _byteBuffer.capacity() / 8; i++) {
            if (shouldSkip(i)) {
                _byteBuffer.position(_byteBuffer.position() + 8);
            } else {
                long value = _byteBuffer.getLong();
                assertThat(value).isEqualTo(i);
            }
        }
        _byteBuffer.rewind();
    }

    @Benchmark
    public void skip_CachedPosition() {
        int skips = 0;
        for (int i = 0; i < _byteBuffer.capacity() / 8; i++) {
            if (shouldSkip(i)) {
                skips++;
            } else {
                if (skips > 0) {
                    _byteBuffer.position(_byteBuffer.position() + skips * 8);
                    skips = 0;
                }
                long value = _byteBuffer.getLong();
                assertThat(value).isEqualTo(i);
            }
        }
        _byteBuffer.rewind();
    }

    @Benchmark
    public void skip_useIndexMethod() {
        for (int i = 0; i < _byteBuffer.capacity() / 8; i++) {
            if (!shouldSkip(i)) {
                long value = _byteBuffer.getLong(i * 8);
                assertThat(value).isEqualTo(i);
            }
        }
        _byteBuffer.rewind();
    }

    @Benchmark
    public void skip_LongBuffer() {
        LongBuffer longBuffer = _byteBuffer.asLongBuffer();
        for (int i = 0; i < _byteBuffer.capacity() / 8; i++) {
            if (shouldSkip(i)) {
                longBuffer.position(longBuffer.position() + 1);
            } else {
                long value = longBuffer.get();
                assertThat(value).isEqualTo(i);
            }
        }
        _byteBuffer.rewind();
    }

    @Benchmark
    public void skip_LongBufferWithIndexMethod() {
        LongBuffer longBuffer = _byteBuffer.asLongBuffer();
        for (int i = 0; i < _byteBuffer.capacity() / 8; i++) {
            if (!shouldSkip(i)) {
                long value = longBuffer.get(i);
                assertThat(value).isEqualTo(i);
            }
        }
        _byteBuffer.rewind();
    }

}
