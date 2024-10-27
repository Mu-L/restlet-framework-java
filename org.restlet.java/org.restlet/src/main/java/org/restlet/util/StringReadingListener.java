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
import java.io.Reader;

import org.restlet.engine.io.IoUtils;
import org.restlet.representation.Representation;

/**
 * Selection listener notifying new content as a string.
 * 
 * @author Jerome Louvel
 */
public abstract class StringReadingListener extends CharacterReadingListener {

	/**
	 * Default constructor. Uses a byte buffer of {@link IoUtils#BUFFER_SIZE}
	 * length.
	 * 
	 * @param source The source representation.
	 * @throws IOException
	 */
	public StringReadingListener(Representation source) throws IOException {
		super(source);
	}

	/**
	 * Constructor. Uses a byte buffer of a given size.
	 * 
	 * @param source     The source representation.
	 * @param bufferSize The byte buffer to use.
	 * @throws IOException
	 */
	public StringReadingListener(Representation source, int bufferSize) throws IOException {
		super(source, bufferSize);
	}

	@Override
	protected final void onContent(Reader reader) {
		try {
			int r = reader.read();
			StringBuilder sb = new StringBuilder();

			while (r != -1) {
				sb.append((char) r);
				r = reader.read();
			}

			String s = sb.toString();
			onContent(s);
		} catch (IOException ioe) {
			onError(ioe);
		}
	}

	/**
	 * Callback invoked when new content is available.
	 * 
	 * @param content The new content.
	 */
	protected abstract void onContent(String content);

}
