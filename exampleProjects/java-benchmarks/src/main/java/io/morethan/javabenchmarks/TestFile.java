package io.morethan.javabenchmarks;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.ReadableByteChannel;

import com.google.common.base.Stopwatch;
import com.jakewharton.byteunits.BinaryByteUnit;

/**
 * Wrapper around {@link File} for use in benchmarks. A created file will be cached in the build folder of the project.
 */
public class TestFile {

    public static TestFile ONE_GB = new TestFile("oneGB-randomBytes", FileFiller.randomBytes(BinaryByteUnit.GIBIBYTES.toBytes(1)));

    private final String _path;
    private final FileFiller _filler;

    public TestFile(String name, FileFiller filler) {
        _path = "build/files/" + name + ".file";
        _filler = filler;
    }

    public void create(FileStore fileSource) throws IOException {
        if (fileSource.exists(_path)) {
            System.out.println("Using existing file " + _path + " with size of " + BinaryByteUnit.format(fileSource.length(_path)));
        } else {
            Stopwatch stopwatch = Stopwatch.createStarted();
            try (OutputStream out = new BufferedOutputStream(fileSource.create(_path))) {
                _filler.fill(out);
            } catch (IOException x) {
                throw new RuntimeException(x);
            }
            System.out.println("Wrote file " + _path + " with size of " + BinaryByteUnit.format(fileSource.length(_path)) + " in " + stopwatch);
        }
    }

    public ReadableByteChannel open(FileStore fileStore) throws IOException {
        return fileStore.open(_path);
    }

}
