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

import org.restlet.util.SelectionRegistration;

/**
 * NIO channel that is based on a selectable channel.
 * 
 * @author Jerome Louvel
 */
public interface SelectionChannel extends Channel, BlockableChannel {

	/**
	 * Returns the NIO registration.
	 * 
	 * @return The NIO registration.
	 */
	SelectionRegistration getRegistration();

}
