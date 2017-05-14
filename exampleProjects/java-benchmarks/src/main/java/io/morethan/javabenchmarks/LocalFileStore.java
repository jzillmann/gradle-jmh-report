package io.morethan.javabenchmarks;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

public class LocalFileStore implements FileStore {

    public static final LocalFileStore INSTANCE = new LocalFileStore();

    @Override
    public boolean exists(String path) {
        return Files.exists(Paths.get(path));
    }

    @Override
    public OutputStream create(String pathString) throws IOException {
        Path path = Paths.get(pathString);
        Files.createDirectories(path.getParent());
        return Files.newOutputStream(path, StandardOpenOption.CREATE);
    }

    @Override
    public ReadableByteChannel open(String pathString) throws IOException {
        return Files.newByteChannel(Paths.get(pathString), EnumSet.of(StandardOpenOption.READ));
    }

    @Override
    public InputStream openAsStream(String pathString) throws IOException {
        return Files.newInputStream(Paths.get(pathString), StandardOpenOption.READ);
    }

    @Override
    public long length(String pathString) throws IOException {
        return Files.size(Paths.get(pathString));
    }

}
