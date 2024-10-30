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
 * This is an OSGi service interface for registering Restlet resources with an
 * application. Users are expected to register an instance as an OSGi service.
 * It is recommended that you extend the {@link BaseResourceProvider}
 * implementation. You may provide your own implementation of
 * {@link ResourceProvider} if you need complete control. Resources are
 * registered with an application according to the application alias. If an
 * application is not found that corresponds to the specified alias, the
 * resource will be cached until the application is registered. If your
 * resources are not being registered, check there is not a typo in the alias in
 * both the resource provider and application provider.
 * 
 * It is recommended that you use or extend {@link ResourceBuilder}
 * 
 * @author Bryan Hunt
 * @author Wolfgang Werner
 * @deprecated Will be removed in next major release.
 */
@Deprecated
public interface ResourceProvider extends RestletProvider {
    /**
     * 
     * @return the matching mode to be used for template routes. Defaults to
     *         Template.MODE_EQUALS.
     */
    int getMatchingMode();

    /**
     * 
     * @return the paths to the resource relative to the application alias. The
     *         paths must start with '/'.
     */
    String[] getPaths();
}
