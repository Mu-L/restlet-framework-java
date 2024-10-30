/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.ext.osgi;

import org.restlet.routing.Filter;

/**
 * This is an OSGi service interface for registering Restlet filters with a
 * router or a resource. Users are expected to register an instance as an OSGi
 * service. It is recommended that you extend the {@link BaseFilterProvider}
 * implementation. You may provide your own implementation of
 * {@link FilterProvider} if you need complete control.
 * 
 * @author Bryan Hunt
 * @deprecated Will be removed in next major release.
 */
@Deprecated
public interface FilterProvider extends RestletProvider {
    /**
     * 
     * @return the filter instance
     */
    Filter getFilter();
}
