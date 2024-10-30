/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.restlet.engine.io.IoUtils;
import org.restlet.representation.Representation;

/**
 * Selection listener notifying new content as an {@link InputStream}.
 * 
 * @author Jerome Louvel
 * @deprecated NIO will be removed in next major release.
 */
@Deprecated
public abstract class ByteReadingListener extends ReadingListener {

	/**
	 * Default constructor. Uses a byte buffer of {@link IoUtils#BUFFER_SIZE}
	 * length.
	 * 
	 * @param source The source representation.
	 * @throws IOException
	 */
	public ByteReadingListener(Representation source) throws IOException {
		super(source);
	}

	/**
	 * Constructor. Uses a byte buffer of a given size.
	 * 
	 * @param source     The source byte channel.
	 * @param bufferSize The byte buffer to use.
	 * @throws IOException
	 */
	public ByteReadingListener(Representation source, int bufferSize) throws IOException {
		super(source, bufferSize);
	}

	/**
	 * Callback invoked when new content is available.
	 * 
	 * @param byteBuffer The byte buffer filled with the new content (correctly
	 *                   flip).
	 */
	protected final void onContent(ByteBuffer byteBuffer) {
		onContent(new ByteArrayInputStream(byteBuffer.array(), byteBuffer.arrayOffset(), byteBuffer.remaining()));
	}

	/**
	 * Callback invoked when new content is available.
	 * 
	 * @param inputStream The input stream allowing to retrieve the new content.
	 */
	protected abstract void onContent(InputStream inputStream);

}
