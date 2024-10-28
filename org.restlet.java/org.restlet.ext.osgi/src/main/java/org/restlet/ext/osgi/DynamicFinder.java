/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.ext.osgi;

import org.osgi.framework.Bundle;
import org.osgi.service.log.LogService;
import org.restlet.resource.Finder;
import org.restlet.resource.ServerResource;

/**
 * This class allows Restlet to lazily load resources in an OSGi environment.
 * This class may be used as the finder in a @see ResourceProvider.
 * 
 * @author Bryan Hunt
 * @author Wolfgang Werner
 * @deprecated Will be removed in next major release.
 */
@Deprecated
public class DynamicFinder extends Finder {
    private Bundle bundle;

    private String className;

    private LogService logService;

    private Class<? extends ServerResource> targetClass;

    /**
     * @param bundle
     *            the bundle containing the resource - must not be null
     * @param className
     *            the class name of the resource - must not be null
     */
    public DynamicFinder(Bundle bundle, String className) {
        this(bundle, className, null);
    }

    /**
     * @param bundle
     *            the bundle containg the resource - must not be null
     * @param className
     *            the class name of the resource - must not be null
     * @param logService
     *            the OSGi log service for logging errors - may be null
     */
    public DynamicFinder(Bundle bundle, String className, LogService logService) {
        if (bundle == null)
            throw new IllegalArgumentException("bundle must not be null");

        if (className == null)
            throw new IllegalArgumentException("className must not be null");

        this.bundle = bundle;
        this.className = className;
        this.logService = logService;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends ServerResource> getTargetClass() {
        if (targetClass == null) {
            try {
                targetClass = (Class<? extends ServerResource>) bundle
                        .loadClass(className);
            } catch (ClassNotFoundException e) {
                if (logService != null)
                    logService.log(LogService.LOG_ERROR,
                            "Failed to load class: '" + className + "'", e);
            }
        }

        return targetClass;
    }
}
