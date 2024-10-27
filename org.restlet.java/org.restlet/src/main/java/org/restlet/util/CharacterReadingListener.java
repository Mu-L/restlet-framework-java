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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.restlet.data.CharacterSet;
import org.restlet.engine.io.IoUtils;
import org.restlet.representation.Representation;

/**
 * Selection listener notifying new content as a {@link Reader}. It relies on
 * the representation's character set for proper character decoding.
 * 
 * @author Jerome Louvel
 */
public abstract class CharacterReadingListener extends ByteReadingListener {

	/** The character set of the associated representation. */
	private final CharacterSet characterSet;

	/**
	 * Default constructor. Uses a byte buffer of {@link IoUtils#BUFFER_SIZE}
	 * length.
	 * 
	 * @param source The source representation.
	 * @throws IOException
	 */
	public CharacterReadingListener(Representation source) throws IOException {
		this(source, IoUtils.BUFFER_SIZE);
	}

	/**
	 * Constructor. Uses a byte buffer of a given size.
	 * 
	 * @param source     The source representation.
	 * @param bufferSize The byte buffer to use.
	 * @throws IOException
	 */
	public CharacterReadingListener(Representation source, int bufferSize) throws IOException {
		super(source, bufferSize);
		this.characterSet = source.getCharacterSet();
	}

	@Override
	protected final void onContent(InputStream inputStream) {
		InputStreamReader isr = new InputStreamReader(inputStream, this.characterSet.toCharset());
		onContent(isr);
	}

	/**
	 * Callback invoked when new content is available.
	 * 
	 * @param reader The reader allowing to retrieve the new content.
	 */
	protected abstract void onContent(Reader reader);

}
