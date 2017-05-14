package io.morethan.javabenchmarks;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class HdfsFileStore implements FileStore {

    private final FileSystem _fileSystem;

    private HdfsFileStore(FileSystem fileSystem) {
        _fileSystem = fileSystem;
    }

    public static HdfsFileStore create() {
        try {
            Configuration configuration = new Configuration();
            configuration.set("fs.defaultFS", "hdfs://localhost:9000");
            return new HdfsFileStore(FileSystem.get(configuration));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean exists(String pathString) throws IOException {
        return _fileSystem.exists(new Path(pathString));
    }

    @Override
    public OutputStream create(String pathString) throws IOException {
        return _fileSystem.create(new Path(pathString));
    }

    @Override
    public ReadableByteChannel open(String pathString) throws IOException {
        final FSDataInputStream inputStream = _fileSystem.open(new Path(pathString));
        return new ReadableByteChannel() {

            @Override
            public boolean isOpen() {
                return true;
            }

            @Override
            public void close() throws IOException {
                inputStream.close();
            }

            @Override
            public int read(ByteBuffer dst) throws IOException {
                return inputStream.read(dst);
            }
        };
    }

    @Override
    public InputStream openAsStream(String pathString) throws IOException {
        return _fileSystem.open(new Path(pathString));
    }

    @Override
    public long length(String pathString) throws IOException {
        return _fileSystem.getFileStatus(new Path(pathString)).getLen();
    }

}
