package io.morethan.javabenchmarks;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.ReadableByteChannel;

/**
 * A simple file system abstraction.
 */
public interface FileStore {

    boolean exists(String pathString) throws IOException;

    OutputStream create(String pathString) throws IOException;

    ReadableByteChannel open(String pathString) throws IOException;

    InputStream openAsStream(String pathString) throws IOException;

    long length(String pathString) throws IOException;

}