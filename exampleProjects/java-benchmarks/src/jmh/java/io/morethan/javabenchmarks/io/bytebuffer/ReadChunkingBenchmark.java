package io.morethan.javabenchmarks.io.bytebuffer;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
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
import com.google.common.primitives.Ints;
import com.jakewharton.byteunits.BinaryByteUnit;

import io.morethan.javabenchmarks.LocalFileStore;
import io.morethan.javabenchmarks.TestFile;

/**
 * You wanne read the next x bytes (and then process them), but x is big. Do you create one big buffer or multiple chunk
 * buffers ?
 */
@Fork(value = 2)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@BenchmarkMode(Mode.AverageTime)
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class ReadChunkingBenchmark {

    private static final TestFile _testFile = TestFile.ONE_GB_RANDOM_BYTES;
    private static final int _chunkSize = Ints.checkedCast(BinaryByteUnit.MEBIBYTES.toBytes(10));

    private ByteBuffer _chunkBuffer = ByteBuffer.allocateDirect(_chunkSize);
    private List<ByteBuffer> _standardSizedBuffers = new ArrayList<>();

    @Setup
    public void setUp() throws IOException {
        _testFile.init(LocalFileStore.INSTANCE);
        for (int i = 0; i < _chunkSize / 64 / 1024; i++) {
            _standardSizedBuffers.add(ByteBuffer.allocateDirect(64 * 1024));
        }
    }

    @Benchmark
    public void readWholeChunk() throws IOException {
        long consumedBytes = 0;
        byte[] bytes = new byte[4096];
        Hasher hasher = Hashing.crc32().newHasher();
        try (ReadableByteChannel byteChannel = _testFile.open();) {
            while (byteChannel.read(_chunkBuffer) > 0) {
                _chunkBuffer.flip();
                while (_chunkBuffer.hasRemaining()) {
                    int length = Math.min(bytes.length, _chunkBuffer.remaining());
                    _chunkBuffer.get(bytes, 0, length);
                    hasher.putBytes(bytes, 0, length);
                    consumedBytes += length;
                }
                _chunkBuffer.rewind();
            }
        }
        assertThat(consumedBytes).isEqualTo(BinaryByteUnit.GIBIBYTES.toBytes(1));
        assertThat(hasher.hash().toString()).isEqualTo("d3a214f6");
    }

    @Benchmark
    public void readChunkedChunk() throws IOException {
        long consumedBytes = 0;
        byte[] bytes = new byte[4096];
        Hasher hasher = Hashing.crc32().newHasher();

        try (ReadableByteChannel byteChannel = _testFile.open();) {
            int lastReadBytes = 0;
            do {
                // read the whole chunk in chunks
                for (ByteBuffer standardBuffer : _standardSizedBuffers) {
                    lastReadBytes = byteChannel.read(standardBuffer);
                    if (lastReadBytes < standardBuffer.capacity()) {
                        break;
                    }
                }
                // consumer all chunks of the chunk
                for (ByteBuffer standardBuffer : _standardSizedBuffers) {
                    if (standardBuffer.position() > 0) {
                        standardBuffer.flip();
                        while (standardBuffer.hasRemaining()) {
                            int length = Math.min(bytes.length, standardBuffer.remaining());
                            consumedBytes += length;
                            standardBuffer.get(bytes, 0, length);
                            hasher.putBytes(bytes, 0, length);
                        }
                        standardBuffer.rewind();
                    }
                }
            } while (lastReadBytes > 0);
        }
        assertThat(consumedBytes).isEqualTo(BinaryByteUnit.GIBIBYTES.toBytes(1));
        assertThat(hasher.hash().toString()).isEqualTo("d3a214f6");
    }
}
