/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.ext.osgi;

import org.restlet.Context;
import org.restlet.Restlet;

/**
 * This is a common interface for several of the providers. Users are not
 * expected to implement this interface, but instead implement one of the
 * specialized provider interfaces.
 * 
 * @author Bryan Hunt
 * @deprecated Will be removed in next major release.
 */
@Deprecated
public interface RestletProvider {
    /**
     * 
     * @param context
     *            the Restlet application context
     * @return the node to be used as the inbound root of the handling chain
     */
    Restlet getInboundRoot(Context context);
}
