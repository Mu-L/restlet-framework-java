/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.engine.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

/**
 * Readable byte channel wrapping an input stream.
 * 
 * @author Jerome Louvel
 * @deprecated NIO will be removed in next major release.
 */
@Deprecated
public class InputStreamChannel implements ReadableByteChannel, BlockableChannel {

	/** The underlying input stream. */
	private final InputStream inputStream;

	/** Indicates if the channel is blocking. */
	private final boolean blocking;

	/** Optional byte array buffer. */
	private volatile byte buffer[] = new byte[0];

	/** Indicates if the underlying stream is still open. */
	private volatile boolean open;

	/**
	 * Constructor.
	 * 
	 * @param inputStream
	 * @throws IOException
	 */
	public InputStreamChannel(InputStream inputStream) throws IOException {
		this.inputStream = inputStream;
		this.open = true;
		this.blocking = (inputStream.available() <= 0);
	}

	/**
	 * Closes the underlying input stream.
	 */
	public void close() throws IOException {
		getInputStream().close();
		this.open = false;
	}

	/**
	 * Returns the underlying input stream.
	 * 
	 * @return The underlying input stream.
	 */
	protected InputStream getInputStream() {
		return inputStream;
	}

	/**
	 * True if the underlying input stream is able to indicate available bytes
	 * upfront.
	 * 
	 * @return True if the channel is blocking.
	 */
	public boolean isBlocking() {
		return this.blocking;
	}

	/**
	 * Indicates if the channel and its underlying stream are open.
	 * 
	 * @return True if the channel and its underlying stream are open.
	 */
	public boolean isOpen() {
		return this.open;
	}

	/**
	 * Reads bytes from the underlying stream to the target buffer.
	 * 
	 * @param target The target byte buffer.
	 * @return The number of bytes read.
	 */
	public int read(ByteBuffer target) throws IOException {
		int readLength = 0;

		if (isBlocking()) {
			// Potentially blocking read
			readLength = IoUtils.BUFFER_SIZE;
		} else {
			int available = getInputStream().available();

			if (available > 0) {
				// Attempt to read only the available byte to prevent blocking
				readLength = Math.min(available, target.remaining());
			} else {
				// Attempt to read as many bytes as possible even if blocking
				// occurs
				readLength = target.remaining();
			}
		}

		// Create or reuse a specific byte array as buffer
		return read(target, readLength);
	}

	/**
	 * Reads a given number of bytes into a target byte buffer.
	 * 
	 * @param target     The target byte buffer.
	 * @param readLength The maximum number of bytes to read.
	 * @return The number of bytes effectively read or -1 if end reached.
	 * @throws IOException
	 */
	private int read(ByteBuffer target, int readLength) throws IOException {
		int result = 0;

		if (target.hasArray()) {
			// Use directly the underlying byte array
			byte[] byteArray = target.array();

			result = getInputStream().read(byteArray, target.position(), Math.min(readLength, target.remaining()));

			if (result > 0) {
				target.position(target.position() + result);
			}
		} else {
			if (this.buffer.length < IoUtils.BUFFER_SIZE) {
				this.buffer = new byte[IoUtils.BUFFER_SIZE];
			}

			result = getInputStream().read(this.buffer, 0,
					Math.min(Math.min(readLength, IoUtils.BUFFER_SIZE), target.remaining()));

			if (result > 0) {
				target.put(buffer, 0, result);
			}
		}

		return result;
	}

}
