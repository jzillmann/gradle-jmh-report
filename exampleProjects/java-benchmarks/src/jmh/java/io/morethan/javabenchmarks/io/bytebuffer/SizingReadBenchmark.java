package io.morethan.javabenchmarks.io.bytebuffer;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.ByteBuffer;
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

import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

import io.morethan.javabenchmarks.FileStore;
import io.morethan.javabenchmarks.HdfsFileStore;
import io.morethan.javabenchmarks.LocalFileStore;
import io.morethan.javabenchmarks.TestFile;

/**
 * Effect of buffer size on read performance.
 * 
 * Conclusions:
 * <ul>
 * <li>Direct is faster then Heap</li>
 * <li>65.536 seems to be the sweet spot</li>
 * <li>Small sizes like 23 are unbearable slow</li>
 * <li>Big sizes above 1 mio are not too bad, but are usually slower then regular sizes</li>
 * </ul>
 */
@Fork(value = 2)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@BenchmarkMode(Mode.AverageTime)
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public abstract class SizingReadBenchmark {

    private static final int DEFAULT_LOW_LEVEL_CONSUME_SIZE = 4096;

    private static final TestFile _testFile = TestFile.ONE_GB_RANDOM_BYTES;

    @Setup
    public void setUp() throws IOException {
        _testFile.init(createFileStore());
    }

    protected abstract FileStore createFileStore();

    // Direct Buffers
    @Benchmark
    public String direct_4x1024() throws IOException {
        return readFile(ByteBuffer.allocateDirect(4 * 1024), DEFAULT_LOW_LEVEL_CONSUME_SIZE);
    }

    @Benchmark
    public String direct_16x1024() throws IOException {
        return readFile(ByteBuffer.allocateDirect(16 * 1024), DEFAULT_LOW_LEVEL_CONSUME_SIZE);
    }

    @Benchmark
    public String direct_32x1024() throws IOException {
        return readFile(ByteBuffer.allocateDirect(32 * 1024), DEFAULT_LOW_LEVEL_CONSUME_SIZE);
    }

    @Benchmark
    public String direct_64x1024() throws IOException {
        return readFile(ByteBuffer.allocateDirect(64 * 1024), DEFAULT_LOW_LEVEL_CONSUME_SIZE);
    }

    @Benchmark
    public String direct_256x1024() throws IOException {
        return readFile(ByteBuffer.allocateDirect(256 * 1024), DEFAULT_LOW_LEVEL_CONSUME_SIZE);
    }

    // Heap Buffers
    @Benchmark
    public String heap_4x1024() throws IOException {
        return readFile(ByteBuffer.allocate(4 * 1024), DEFAULT_LOW_LEVEL_CONSUME_SIZE);
    }

    @Benchmark
    public String heap_64x1024() throws IOException {
        return readFile(ByteBuffer.allocate(64 * 1024), DEFAULT_LOW_LEVEL_CONSUME_SIZE);
    }

    // Real World Parquet Column Chunk
    @Benchmark
    public String direct_1_928_092() throws IOException {
        return readFile(ByteBuffer.allocateDirect(1928092), DEFAULT_LOW_LEVEL_CONSUME_SIZE);
    }

    @Benchmark
    public String heap_1_928_092() throws IOException {
        return readFile(ByteBuffer.allocate(1928092), DEFAULT_LOW_LEVEL_CONSUME_SIZE);
    }

    // Smashing Slow
    // @Benchmark
    // public String direct_23() throws IOException {
    // return readFile(ByteBuffer.allocateDirect(23), 23);
    // }
    // @Benchmark
    // public String heap_23() throws IOException {
    // return readFile(ByteBuffer.allocate(23), 23);
    // }

    private String readFile(ByteBuffer byteBuffer, int consumeBytesChunk) throws IOException {
        Hasher hasher = Hashing.crc32().newHasher();
        byte[] bytes = new byte[consumeBytesChunk];
        try (ReadableByteChannel channel = _testFile.open();) {
            while (channel.read(byteBuffer) > 0) {
                byteBuffer.flip();
                while (byteBuffer.hasRemaining()) {
                    int length = Math.min(bytes.length, byteBuffer.remaining());
                    byteBuffer.get(bytes, 0, length);
                    hasher.putBytes(bytes, 0, length);
                }
                byteBuffer.rewind();
            }
        }
        String hash = hasher.hash().toString();
        assertThat(hash).isEqualTo("d3a214f6");
        return hash;
    }

    public static class SizingFileReadBenchmark extends SizingReadBenchmark {

        @Override
        protected FileStore createFileStore() {
            return LocalFileStore.INSTANCE;
        }

    }

    public static class SizingHdfsReadBenchmark extends SizingReadBenchmark {

        @Override
        protected FileStore createFileStore() {
            return HdfsFileStore.create();
        }

    }
}
