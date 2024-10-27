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

import org.restlet.util.SelectionRegistration;

/**
 * Callback interface when a NIO selection occurs on the selectable object.
 * 
 * @author Jerome Louvel
 */
public interface WakeupListener {

	/**
	 * Callback method invoked when the selection registration wants to wake up the
	 * NIO selector.
	 * 
	 * @param selectionRegistration The selected registration.
	 */
	void onWokeup(SelectionRegistration selectionRegistration) throws IOException;

}
