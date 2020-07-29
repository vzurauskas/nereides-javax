package com.vzurauskas.nereides.javax;

import java.io.IOException;
import java.io.InputStream;

public final class AutoResetInputStream extends InputStream {

    private final InputStream origin;

    public AutoResetInputStream(InputStream origin) {
        super();
        origin.mark(1 << 24);
        this.origin = origin;
    }

    @Override
    public void close() throws IOException {
        super.close();
        origin.reset();
    }

    @Override
    public int read() throws IOException {
        return origin.read();
    }
}
