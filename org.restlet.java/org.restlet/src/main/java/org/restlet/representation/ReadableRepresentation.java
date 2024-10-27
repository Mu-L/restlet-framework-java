/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.representation;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import org.restlet.data.MediaType;
import org.restlet.engine.io.IoUtils;

/**
 * Transient representation based on a readable NIO byte channel.
 * 
 * @author Jerome Louvel
 */
public class ReadableRepresentation extends ChannelRepresentation {

	/** The representation's input stream. */
	private volatile ReadableByteChannel channel;

	/**
	 * Constructor.
	 * 
	 * @param readableChannel The representation's channel.
	 * @param mediaType       The representation's media type.
	 */
	public ReadableRepresentation(ReadableByteChannel readableChannel, MediaType mediaType) {
		this(readableChannel, mediaType, UNKNOWN_SIZE);
	}

	/**
	 * Constructor.
	 * 
	 * @param channel      The representation's channel.
	 * @param mediaType    The representation's media type.
	 * @param expectedSize The expected stream size.
	 */
	public ReadableRepresentation(ReadableByteChannel channel, MediaType mediaType, long expectedSize) {
		super(mediaType);
		setSize(expectedSize);
		this.channel = channel;
		setAvailable(channel != null);
		setTransient(true);
	}

	@Override
	public ReadableByteChannel getChannel() throws IOException {
		ReadableByteChannel result = this.channel;
		setAvailable(false);
		return result;
	}

	/**
	 * Sets the readable channel.
	 * 
	 * @param channel The readable channel.
	 */
	public void setChannel(ReadableByteChannel channel) {
		this.channel = channel;
	}

	@Override
	public void write(WritableByteChannel writableChannel) throws IOException {
		IoUtils.copy(getChannel(), writableChannel);
	}

}
