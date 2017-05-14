package io.morethan.javabenchmarks;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.ReadableByteChannel;

import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.jakewharton.byteunits.BinaryByteUnit;

/**
 * Wrapper around {@link File} for use in benchmarks. A created file will be cached in the build folder of the project.
 */
public class TestFile {

    public static TestFile ONE_GB_RANDOM_BYTES = TestFile.create("oneGB-randomBytes", FileFiller.randomBytes(BinaryByteUnit.GIBIBYTES.toBytes(1)));
    public static TestFile ONE_GB_LONGS = TestFile.create("oneGB-ascendingLongs", FileFiller.ascendingLong(BinaryByteUnit.GIBIBYTES.toBytes(1) / 8));

    private final String _path;
    private final FileFiller _filler;
    private FileStore _fileStore;

    private TestFile(String name, FileFiller filler) {
        _path = "build/files/" + name + ".file";
        _filler = filler;
    }

    public static TestFile create(String name, FileFiller fileFiller) {
        return new TestFile(name, fileFiller);
    }

    public void init(FileStore fileStore) throws IOException {
        _fileStore = fileStore;
        if (fileStore.exists(_path)) {
            System.out.println("Using existing file " + _path + " with size of " + BinaryByteUnit.format(fileStore.length(_path)));
        } else {
            Stopwatch stopwatch = Stopwatch.createStarted();
            try (OutputStream out = new BufferedOutputStream(fileStore.create(_path))) {
                _filler.fill(out);
            } catch (IOException x) {
                throw new RuntimeException(x);
            }
            System.out.println("Wrote file " + _path + " with size of " + BinaryByteUnit.format(fileStore.length(_path)) + " in " + stopwatch);
        }
    }

    public ReadableByteChannel open() throws IOException {
        Preconditions.checkState(_fileStore != null, "Call init() first!");
        return _fileStore.open(_path);
    }

    public InputStream openAsStream() throws IOException {
        Preconditions.checkState(_fileStore != null, "Call init() first!");
        return _fileStore.openAsStream(_path);
    }

}
