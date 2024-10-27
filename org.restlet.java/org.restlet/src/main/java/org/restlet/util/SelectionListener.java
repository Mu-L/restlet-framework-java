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

/**
 * Callback interface when a NIO selection occurs on the selectable object.
 * 
 * @author Jerome Louvel
 */
public interface SelectionListener {

	/**
	 * Callback method invoked when the connection has been selected for IO
	 * operations it registered interest in.
	 * 
	 * @param selectionRegistration The selected registration.
	 */
	void onSelected(SelectionRegistration selectionRegistration) throws IOException;

}
