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
 * This is an OSGi service interface for registering Restlet filters with an
 * application. Users are expected to register an instance as an OSGi service.
 * It is recommended that you use the {@link BaseRouterProvider} implementation.
 * You may extend it if necessary, or for complete control, provide your own
 * implementation of {@link RouterProvider}.
 * 
 * @author Bryan Hunt
 * @deprecated Will be removed in next major release.
 */
@Deprecated
public interface RouterProvider extends RestletProvider {
}
