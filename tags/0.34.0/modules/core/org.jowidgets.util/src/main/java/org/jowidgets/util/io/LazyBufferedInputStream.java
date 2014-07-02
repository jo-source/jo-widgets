/*
 * Copyright (c) 2012, grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 *   names of its contributors may be used to endorse or promote products
 *   derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL jo-widgets.org BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */

package org.jowidgets.util.io;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class provides an buffered input stream that creates its buffer lazily and destroys the buffer when
 * the stream get closed.
 * 
 * This class is not threadsafe
 */
public class LazyBufferedInputStream extends InputStream {

	private final InputStream original;
	private final int bufferSize;

	private BufferedInputStream bufferedInputStream;
	private boolean closed;

	public LazyBufferedInputStream(final InputStream original, final int bufferSize) {
		this.original = original;
		this.bufferSize = bufferSize;
	}

	@Override
	public int read() throws IOException {
		return getInputStream().read();
	}

	@Override
	public int read(final byte[] b) throws IOException {
		return getInputStream().read(b);
	}

	@Override
	public int read(final byte[] b, final int off, final int len) throws IOException {
		return getInputStream().read(b, off, len);
	}

	@Override
	public long skip(final long n) throws IOException {
		return getInputStream().skip(n);
	}

	@Override
	public int available() throws IOException {
		return getInputStream().available();
	}

	@Override
	public void close() throws IOException {
		closed = true;
		if (bufferedInputStream != null) {
			bufferedInputStream.close();
			bufferedInputStream = null;
		}
	}

	@Override
	public synchronized void mark(final int readlimit) {
		try {
			getInputStream().mark(readlimit);
		}
		catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public synchronized void reset() throws IOException {
		getInputStream().reset();
	}

	@Override
	public boolean markSupported() {
		return original.markSupported();
	}

	private InputStream getInputStream() throws IOException {
		if (closed) {
			throw new IOException("Stream was closed");
		}
		if (bufferedInputStream == null) {
			bufferedInputStream = new BufferedInputStream(original, bufferSize);
		}
		return bufferedInputStream;
	}

}
