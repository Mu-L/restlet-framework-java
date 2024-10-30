/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.engine.io;

import java.nio.channels.Channel;

/**
 * NIO channel that can indicate if it is blocking or non blocking.
 * 
 * @author Jerome Louvel
 * @deprecated NIO will be removed in next major release.
 */
@Deprecated
public interface BlockableChannel extends Channel {

	/**
	 * Indicates if the channel is likely to block upon IO operations.
	 * 
	 * @return True if the channel is likely to block upon IO operations.
	 */
	boolean isBlocking();

}
