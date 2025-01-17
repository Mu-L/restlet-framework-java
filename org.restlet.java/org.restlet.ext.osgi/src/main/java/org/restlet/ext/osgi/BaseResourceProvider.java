/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.ext.osgi;

import java.util.Dictionary;

import org.osgi.service.component.ComponentContext;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.resource.Finder;
import org.restlet.routing.Template;

/**
 * This class provides an implementation of {@link ResourceProvider}. You
 * register this class as an OSGi declarative service. It is expected that
 * clients will extend this class to create the Finder for the resource. This
 * allows the OSGi class loading mechanism to properly locate the resource
 * class. The service declaration should look like:
 * <p>
 * 
 * <pre>
 * {@code
 * <?xml version="1.0" encoding="UTF-8"?>
 * <scr:component xmlns:scr="http://www.osgi.org/xmlns/scr/v1.1.0" immediate="true" name="org.example.app.resource">
 *   <implementation class="org.restlet.ext.osgi.BaseResourceProvider"/>
 *   <service>
 *     <provide interface="org.restlet.ext.osgi.ResourceProvider"/>
 *   </service>
 * </scr:component>
 * }
 * </pre>
 * 
 * </p>
 * <p>
 * The service properties are:
 * <ul>
 * <li>paths - the path(s) to the resource relative to the application -
 * required - must not be null - may be a single value or a String[]</li>
 * <li>matchingMode - the URI matching mode - optional - defaults to
 * Template.MODE_EQUALS</li>
 * </ul>
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
 * 
 * @author Bryan Hunt
 * @deprecated Will be removed in next major release.
 */
@Deprecated
public abstract class BaseResourceProvider extends BaseRestletProvider
        implements ResourceProvider {
    private Finder finder;

    private Integer matchingMode;

    private String[] paths;

    /**
     * Called by OSGi DS to activate the service
     * 
     * @param context
     *            the OSGi service context
     */
    public void activate(ComponentContext context) {
        @SuppressWarnings("unchecked")
        Dictionary<String, Object> properties = context.getProperties();
        Object pathsProperty = properties.get("paths");

        if (pathsProperty instanceof String) {
            paths = new String[1];
            paths[0] = (String) pathsProperty;
        } else
            paths = (String[]) pathsProperty;

        matchingMode = (Integer) properties.get("matchingMode");

        if (matchingMode == null)
            matchingMode = Template.MODE_EQUALS;
    }

    /**
     * 
     * @param the
     *            restlet application context
     * @return the finder for the resource
     */
    protected abstract Finder createFinder(Context context);

    @Override
    protected Restlet getFilteredRestlet() {
        return finder;
    }

    @Override
    public Restlet getInboundRoot(Context context) {
        if (finder == null)
            finder = createFinder(context);

        Restlet inboundRoot = super.getInboundRoot(context);
        return inboundRoot != null ? inboundRoot : finder;
    }

    @Override
    public int getMatchingMode() {
        return matchingMode;
    }

    @Override
    public String[] getPaths() {
        return paths.clone();
    }
}
