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
import org.restlet.routing.Filter;

/**
 * This class provides an implementation of {@link FilterProvider}. You register
 * this class as an OSGi declarative service. The service declaration should
 * look like:
 * <p>
 * 
 * <pre>
 * {@code
 * <?xml version="1.0" encoding="UTF-8"?>
 * <scr:component xmlns:scr="http://www.osgi.org/xmlns/scr/v1.1.0" immediate="true" name="org.example.app.filter">
 *   <implementation class="org.restlet.ext.osgi.BaseFilterProvider"/>
 *   <service>
 *     <provide interface="org.restlet.ext.osgi.FilterProvider"/>
 *   </service>
 * </scr:component>
 * }
 * </pre>
 * 
 * </p>
 * <p>
 * The referenced services are:
 * <ul>
 * <li>FilterProvider - optional - policy="static" cardinality="1..1"</li>
 * </ul>
 * </p>
 * <p>
 * The provided services are:
 * <ul>
 * <li>FilterProvider</li>
 * </ul>
 * </p>
 * <p>
 * Since filter providers have a reference to filter provider, filters can be
 * chained together. To get the filters in the desired order, add a service
 * property to a filter, and then place a target filter on the reference
 * declaration. For example:
 * 
 * <pre>
 * <reference bind="bindFilterProvider" cardinality="1..1" target="(type=authFilter)" interface="org.restlet.ext.osgi.FilterProvider" name="FilterProvider" policy="static" unbind="unbindFilterProvider"/>
 * </pre>
 * 
 * </p>
 * 
 * @author Bryan Hunt
 * @deprecated Will be removed in next major release.
 */
@Deprecated
public abstract class BaseFilterProvider extends BaseRestletProvider implements
        FilterProvider {
    private Filter filter;

    /**
     * Called to construct the actual filter instance.
     * 
     * @return the newly constructed filter instance.
     */
    protected abstract Filter createFilter(Context context);

    @Override
    public Filter getFilter() {
        return filter;
    }

    @Override
    protected Restlet getFilteredRestlet() {
        return filter;
    }

    @Override
    public Restlet getInboundRoot(Context context) {
        if (filter == null)
            filter = createFilter(context);

        Restlet inboundRoot = super.getInboundRoot(context);
        return inboundRoot != null ? inboundRoot : filter;
    }
}
