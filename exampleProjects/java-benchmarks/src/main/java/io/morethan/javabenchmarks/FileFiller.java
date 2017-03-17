package io.morethan.javabenchmarks;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

public abstract class FileFiller {

    public abstract void fill(OutputStream outputStream) throws IOException;

    public static FileFiller randomBytes(final long size) {
        return new FileFiller() {
            @Override
            public void fill(OutputStream outputStream) throws IOException {
                Random random = new Random(23);
                byte[] data = new byte[4096];
                for (int i = 0; i < size / data.length; i++) {
                    random.nextBytes(data);
                    outputStream.write(data);
                }
            }
        };
    }

    public static FileFiller ascendingLong(final long howMany) {
        return new FileFiller() {
            @Override
            public void fill(OutputStream outputStream) throws IOException {
                try (DataOutputStream dataOutputStream = new DataOutputStream(outputStream);) {
                    for (int i = 0; i < howMany; i++) {
                        dataOutputStream.writeLong(i);
                    }
                }
            }
        };
    }
}
