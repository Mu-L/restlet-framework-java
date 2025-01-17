/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.Level;

import org.restlet.Context;
import org.restlet.engine.io.IoUtils;
import org.restlet.representation.Representation;

/**
 * Selection listener notifying new content is read into a {@link ByteBuffer}.
 * 
 * @author Jerome Louvel
 * @deprecated NIO will be removed in next major release.
 */
@Deprecated
public abstract class ReadingListener implements SelectionListener {

	/** The internal byte buffer. */
	private final ByteBuffer byteBuffer;

	/** The byte channel to read from when selected. */
	private final ReadableByteChannel byteChannel;

	/**
	 * Constructor. Uses a byte buffer of a given size.
	 * 
	 * @param byteChannel The source byte channel.
	 * @param byteBuffer  The byte buffer to use.
	 * @throws IOException
	 */
	public ReadingListener(ReadableByteChannel byteChannel, ByteBuffer byteBuffer) throws IOException {
		this.byteBuffer = byteBuffer;
		this.byteChannel = byteChannel;
	}

	/**
	 * Constructor. Uses a byte buffer of a given size.
	 * 
	 * @param byteChannel The source byte channel.
	 * @param bufferSize  The size of the byte buffer to use.
	 * @throws IOException
	 */
	public ReadingListener(ReadableByteChannel byteChannel, int bufferSize) throws IOException {
		this(byteChannel, ByteBuffer.allocate(bufferSize));
	}

	/**
	 * Default constructor. Uses a byte buffer of {@link IoUtils#BUFFER_SIZE}
	 * length.
	 * 
	 * @param source The source representation.
	 * @throws IOException
	 */
	public ReadingListener(Representation source) throws IOException {
		this(source, IoUtils.BUFFER_SIZE);
	}

	/**
	 * Constructor. Uses a byte buffer of a given size.
	 * 
	 * @param source     The source byte channel.
	 * @param bufferSize The size of the byte buffer to use.
	 * @throws IOException
	 */
	public ReadingListener(Representation source, int bufferSize) throws IOException {
		this(source.getChannel(), bufferSize);
	}

	/**
	 * Callback invoked when new content is available.
	 * 
	 * @param byteBuffer The byte buffer filled with the new content (correctly
	 *                   flip).
	 */
	protected abstract void onContent(ByteBuffer byteBuffer);

	/**
	 * Callback invoked when the end of the representation has been reached. By
	 * default, it does nothing.
	 */
	protected void onEnd() {
	}

	/**
	 * Callback invoked when an IO exception occurs. By default, it logs the
	 * exception at the {@link Level#WARNING} level.
	 * 
	 * @param ioe The exception caught.
	 */
	protected void onError(IOException ioe) {
		Context.getCurrentLogger().log(Level.WARNING, "", ioe);
	}

	/**
	 * Callback invoked when new content is available. It reads the available bytes
	 * from the source channel into an internal buffer then calls
	 * {@link #onContent(ByteBuffer)} method or the {@link #onEnd()} method or the
	 * {@link #onError(IOException)} method.
	 */
	public final void onSelected(SelectionRegistration selectionRegistration) throws IOException {
		try {
			synchronized (this.byteBuffer) {
				this.byteBuffer.clear();
				int result = this.byteChannel.read(this.byteBuffer);

				if (result > 0) {
					this.byteBuffer.flip();
					onContent(this.byteBuffer);
				} else if (result == -1) {
					onEnd();
				} else {
					Context.getCurrentLogger().fine("NIO selection detected with no content available");
				}
			}
		} catch (IOException ioe) {
			onError(ioe);
		}
	}
}
