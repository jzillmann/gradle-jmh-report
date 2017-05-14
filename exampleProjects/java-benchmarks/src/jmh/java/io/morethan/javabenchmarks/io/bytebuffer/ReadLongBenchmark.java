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
public class ReadLongBenchmark {

    private ByteBuffer _byteBuffer = ByteBuffer.allocate(64 * 1024).order(ByteOrder.LITTLE_ENDIAN);

    @Setup
    public void setUp() {
        for (int i = 0; i < _byteBuffer.capacity() / 8; i++) {
            _byteBuffer.putLong(i);
        }
        _byteBuffer.flip();
    }

    @Benchmark
    public void getLong() {
        for (int i = 0; i < _byteBuffer.capacity() / 8; i++) {
            long value = _byteBuffer.getLong();
            assertThat(value).isEqualTo(i);
        }
        _byteBuffer.rewind();
    }

    @Benchmark
    public void getLong_reverse() {
        _byteBuffer.order(ByteOrder.BIG_ENDIAN);
        for (int i = 0; i < _byteBuffer.capacity() / 8; i++) {
            long value = Long.reverseBytes(_byteBuffer.getLong());
            assertThat(value).isEqualTo(i);
        }
        _byteBuffer.rewind();
    }

    @Benchmark
    public void getLongWithIndex() {
        for (int i = 0; i < _byteBuffer.capacity() / 8; i++) {
            long value = _byteBuffer.getLong(i * 8);
            assertThat(value).isEqualTo(i);
        }
        _byteBuffer.rewind();
    }

    @Benchmark
    public void longBuffer() {
        LongBuffer longBuffer = _byteBuffer.asLongBuffer();
        for (int i = 0; i < _byteBuffer.capacity() / 8; i++) {
            long value = longBuffer.get();
            assertThat(value).isEqualTo(i);
        }
        _byteBuffer.rewind();
    }

    @Benchmark
    public void oneBytesArray() {
        byte[] bytes = new byte[_byteBuffer.remaining()];
        _byteBuffer.get(bytes);
        for (int i = 0; i < bytes.length / 8; i++) {
            int offset = i * 8;
            long value = (((long) bytes[offset + 7] << 56) +
                    ((long) (bytes[offset + 6] & 255) << 48) +
                    ((long) (bytes[offset + 5] & 255) << 40) +
                    ((long) (bytes[offset + 4] & 255) << 32) +
                    ((long) (bytes[offset + 3] & 255) << 24) +
                    ((bytes[offset + 2] & 255) << 16) +
                    ((bytes[offset + 1] & 255) << 8) +
                    ((bytes[offset + 0] & 255) << 0));
            assertThat(value).isEqualTo(i);
        }
        _byteBuffer.rewind();
    }

    @Benchmark
    public void chunkedBytesArray() {
        byte[] longChunk = new byte[8];
        for (int i = 0; i < _byteBuffer.capacity() / 8; i++) {
            _byteBuffer.get(longChunk);
            long value = (((long) longChunk[7] << 56) +
                    ((long) (longChunk[6] & 255) << 48) +
                    ((long) (longChunk[5] & 255) << 40) +
                    ((long) (longChunk[4] & 255) << 32) +
                    ((long) (longChunk[3] & 255) << 24) +
                    ((longChunk[2] & 255) << 16) +
                    ((longChunk[1] & 255) << 8) +
                    ((longChunk[0] & 255) << 0));
            assertThat(value).isEqualTo(i);
        }
        _byteBuffer.rewind();
    }

    @Benchmark
    public void singleBytes() {
        int offset = 0;
        for (int i = 0; i < _byteBuffer.capacity() / 8; i++) {
            long value = (((long) _byteBuffer.get(offset + 7) << 56) +
                    ((long) (_byteBuffer.get(offset + 6) & 255) << 48) +
                    ((long) (_byteBuffer.get(offset + 5) & 255) << 40) +
                    ((long) (_byteBuffer.get(offset + 4) & 255) << 32) +
                    ((long) (_byteBuffer.get(offset + 3) & 255) << 24) +
                    ((_byteBuffer.get(offset + 2) & 255) << 16) +
                    ((_byteBuffer.get(offset + 1) & 255) << 8) +
                    ((_byteBuffer.get(offset + 0) & 255) << 0));
            offset += 8;
            assertThat(value).isEqualTo(i);
        }
        _byteBuffer.rewind();
    }

}
