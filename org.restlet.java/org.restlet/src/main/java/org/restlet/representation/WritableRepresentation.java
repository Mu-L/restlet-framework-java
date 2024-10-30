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
 * Representation based on a writable NIO byte channel. This class is a good
 * basis to write your own representations, especially for the dynamic and large
 * ones.<br>
 * <br>
 * For this you just need to create a subclass and override the abstract
 * Representation.write(WritableByteChannel) method. This method will later be
 * called back by the connectors when the actual representation's content is
 * needed.
 * 
 * @author Jerome Louvel
 * @deprecated NIO will be removed in next major release.
 */
@Deprecated
public abstract class WritableRepresentation extends ChannelRepresentation {
	/**
	 * Constructor.
	 * 
	 * @param mediaType The representation's media type.
	 */
	public WritableRepresentation(MediaType mediaType) {
		super(mediaType);
	}

	@Override
	public ReadableByteChannel getChannel() throws IOException {
		return IoUtils.getChannel(this);
	}

	@Override
	public abstract void write(WritableByteChannel writableChannel) throws IOException;

}
