/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.ext.osgi;

/**
 * This is an OSGi service interface for registering Restlet directories with a
 * router. Users are expected to register an instance as an OSGi service. It is
 * recommended that you use the {@link BaseDirectoryProvider} implementation.
 * You may extend it if necessary, or for complete control, provide your own
 * implementation of {@link DirectoryProvider}.
 * 
 * @author Bryan Hunt
 */
public interface DirectoryProvider extends RestletProvider {
    /**
     * 
     * @return the fully qualified path of the directory
     */
    String getPath();
}
