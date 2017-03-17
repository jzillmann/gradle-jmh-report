package io.morethan.javabenchmarks.io.bytebuffer;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.LongBuffer;
import java.nio.channels.ReadableByteChannel;
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

import com.jakewharton.byteunits.BinaryByteUnit;
import com.yahoo.memory.NativeMemory;

import io.morethan.javabenchmarks.LocalFileStore;
import io.morethan.javabenchmarks.TestFile;

@Fork(value = 2)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@BenchmarkMode(Mode.AverageTime)
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class ReadBenchmark {

    private static final TestFile _testFile = TestFile.ONE_GB_LONGS;

    @Setup
    public void setUp() throws IOException {
        _testFile.init(LocalFileStore.INSTANCE);
    }

    public static void main(String[] args) throws IOException {
        ReadBenchmark benchmark = new ReadBenchmark();
        benchmark.setUp();
        benchmark.readWithMemoryArray();
    }

    @Benchmark
    public void read() throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(64 * 1024);
        long readValueCount = 0;
        try (ReadableByteChannel byteChannel = _testFile.open();) {
            while (byteChannel.read(byteBuffer) > 0) {
                byteBuffer.flip();
                while (byteBuffer.hasRemaining()) {
                    long longValue = byteBuffer.getLong();
                    assertThat(longValue).isEqualTo(readValueCount);
                    readValueCount++;
                }
                byteBuffer.rewind();
            }
        }
        assertThat(readValueCount).isEqualTo(BinaryByteUnit.GIBIBYTES.toBytes(1) / 8);
    }

    @Benchmark
    public void readAsLongBuffer() throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(64 * 1024);
        LongBuffer longBuffer = byteBuffer.asLongBuffer();
        long readValueCount = 0;
        try (ReadableByteChannel byteChannel = _testFile.open();) {
            while (byteChannel.read(byteBuffer) > 0) {
                byteBuffer.flip();
                while (longBuffer.hasRemaining()) {
                    long longValue = longBuffer.get();
                    assertThat(longValue).isEqualTo(readValueCount);
                    readValueCount++;
                }
                longBuffer.rewind();
            }
        }
        assertThat(readValueCount).isEqualTo(BinaryByteUnit.GIBIBYTES.toBytes(1) / 8);
    }

    @Benchmark
    public void readWithMemory() throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(64 * 1024).order(ByteOrder.LITTLE_ENDIAN);
        NativeMemory memory = new NativeMemory(byteBuffer);
        long readValueCount = 0;
        try (ReadableByteChannel byteChannel = _testFile.open();) {
            while (byteChannel.read(byteBuffer) > 0) {
                byteBuffer.flip();
                int numberOfLongs = byteBuffer.remaining() / 8;
                for (int i = 0; i < numberOfLongs; i++) {
                    long longValue = memory.getLong(i * 8);
                    longValue = Long.reverseBytes(longValue);
                    assertThat(longValue).isEqualTo(readValueCount);
                    readValueCount++;
                }
                byteBuffer.rewind();
            }
        }
        assertThat(readValueCount).isEqualTo(BinaryByteUnit.GIBIBYTES.toBytes(1) / 8);
    }

    @Benchmark
    public void readWithMemoryArray() throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(64 * 1024).order(ByteOrder.LITTLE_ENDIAN);
        NativeMemory memory = new NativeMemory(byteBuffer);
        long[] longValues = new long[byteBuffer.capacity() / 8];
        long readValueCount = 0;
        try (ReadableByteChannel byteChannel = _testFile.open();) {
            while (byteChannel.read(byteBuffer) > 0) {
                byteBuffer.flip();
                int numberOfLongs = byteBuffer.remaining() / 8;
                memory.getLongArray(0, longValues, 0, numberOfLongs);
                for (int i = 0; i < numberOfLongs; i++) {
                    long longValue = Long.reverseBytes(longValues[i]);
                    assertThat(longValue).isEqualTo(readValueCount);
                    readValueCount++;
                }
                byteBuffer.rewind();
            }
        }
        assertThat(readValueCount).isEqualTo(BinaryByteUnit.GIBIBYTES.toBytes(1) / 8);
    }

}
