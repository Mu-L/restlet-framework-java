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
 * This is the base class for the other providers. It handles the filtering if a
 * filter provider has been bound. Users should typically not extend this class,
 * but instead extend an appropriate base provider.
 * 
 * @author Bryan Hunt
 * 
 */
public abstract class BaseRestletProvider implements RestletProvider {
    private FilterProvider filterProvider;

    /**
     * Called by OSGi DS to inject the filter provider service
     * 
     * @param filterProvider
     *            the filter provider service
     */
    public void bindFilterProvider(FilterProvider filterProvider) {
        this.filterProvider = filterProvider;
    }

    /**
     * Called by getInboundRoot() to determine the filtered restlet that is next
     * in the chain.
     * 
     * @return the restlet to be filtered
     */
    protected abstract Restlet getFilteredRestlet();

    @Override
    public Restlet getInboundRoot(Context context) {
        Restlet inboundRoot = null;

        if (filterProvider != null) {
            inboundRoot = filterProvider.getInboundRoot(context);
            filterProvider.getFilter().setNext(getFilteredRestlet());
        }

        return inboundRoot;
    }

    /**
     * Called by OSGi DS to un-inject the filter provider service
     * 
     * @param filterProvider
     *            the filter provider service
     */
    public void unbindFilterProvider(FilterProvider filterProvider) {
        if (this.filterProvider == filterProvider)
            this.filterProvider = null;
    }
}
