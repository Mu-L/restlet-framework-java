/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.client.engine.connector;

import org.restlet.client.engine.Helper;

/**
 * Protocol helper.
 * 
 * @author Thierry Boileau
 * 
 */
public abstract class ProtocolHelper extends Helper {

    /**
     * Constructor.
     */
    public ProtocolHelper() {
        super();
        registerMethods();
    }

    /**
     * Register all supported methods. The implementation relies on the
     * {@link org.restlet.client.data.Method#register(org.restlet.client.data.Method)} method.
     */
    public abstract void registerMethods();

}
