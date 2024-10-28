/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.ext.osgi;

import java.util.HashSet;

import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.routing.Router;
import org.restlet.routing.TemplateRoute;

/**
 * This class provides an implementation of {@link RouterProvider}. You register
 * this class as an OSGi declarative service. The service declaration should
 * look like:
 * <p>
 * 
 * <pre>
 * {@code
 * <?xml version="1.0" encoding="UTF-8"?>
 * <scr:component xmlns:scr="http://www.osgi.org/xmlns/scr/v1.1.0" immediate="true" name="org.example.app.router">
 *   <implementation class="org.restlet.ext.osgi.BaseRouterProvider"/>
 *   <service>
 *     <provide interface="org.restlet.ext.osgi.RouterProvider"/>
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
 * <li>DefaultResourceProvider - optional - policy="dynamic" cardinality="0..1"</li>
 * <li>DefaultRouterProvider - optional - policy="dynamic" cardinality="0..1"</li>
 * <li>DirectoryProvider - optional - policy="dynamic" cardinality="0..n"</li>
 * <li>ResourceProvider - optional - policy="dynamic" cardinality="0..n"</li>
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
public class BaseRouterProvider extends BaseRestletProvider implements
        RouterProvider {
    private RestletProvider defaultRestletProvider;

    private HashSet<DirectoryProvider> directoryProviders = new HashSet<DirectoryProvider>();

    private HashSet<ResourceProvider> resourceProviders = new HashSet<ResourceProvider>();

    private Router router;

    private void attachDirectory(DirectoryProvider directoryProvider) {
        router.attach(directoryProvider.getPath(),
                directoryProvider.getInboundRoot(router.getContext()));
    }

    private void attachResource(ResourceProvider resourceProvider) {
        for (String path : resourceProvider.getPaths()) {
            TemplateRoute templateRoute = router.attach(path,
                    resourceProvider.getInboundRoot(router.getContext()));
            templateRoute.setMatchingMode(resourceProvider.getMatchingMode());
        }
    }

    /**
     * Called by OSGi DS to inject the default resource provider service
     * 
     * @param resourceProvider
     *            the default resource provider
     */
    public void bindDefaultResourceProvider(ResourceProvider resourceProvider) {
        defaultRestletProvider = resourceProvider;

        if (router != null)
            router.attachDefault(resourceProvider.getInboundRoot(router
                    .getContext()));
    }

    /**
     * Called by OSGi DS to inject the default router provider service
     * 
     * @param routerProvider
     *            the default router provider
     */
    public void bindDefaultRouterProvider(RouterProvider routerProvider) {
        defaultRestletProvider = routerProvider;

        if (router != null)
            router.attachDefault(routerProvider.getInboundRoot(router
                    .getContext()));
    }

    /**
     * Called by OSGi DS to inject the directory provider service
     * 
     * @param directoryProvider
     *            the directory provider
     */
    public void bindDirectoryProvider(DirectoryProvider directoryProvider) {
        directoryProviders.add(directoryProvider);

        if (router != null)
            attachDirectory(directoryProvider);
    }

    /**
     * Called by OSGi DS to inject the resource provider service
     * 
     * @param resourceProvider
     *            the resource provider
     */
    public void bindResourceProvider(ResourceProvider resourceProvider) {
        resourceProviders.add(resourceProvider);

        if (router != null)
            attachResource(resourceProvider);
    }

    /**
     * 
     * @param the
     *            restlet application context
     * @return the newly created router instance
     */
    protected Router createRouter(Context context) {
        return new Router(context);
    }

    @Override
    protected Restlet getFilteredRestlet() {
        return router;
    }

    @Override
    public Restlet getInboundRoot(Context context) {
        if (router == null) {
            router = createRouter(context);

            for (ResourceProvider resourceProvider : resourceProviders)
                attachResource(resourceProvider);

            for (DirectoryProvider directoryProvider : directoryProviders)
                attachDirectory(directoryProvider);

            if (defaultRestletProvider != null)
                router.attachDefault(defaultRestletProvider
                        .getInboundRoot(context));
        }

        Restlet inboundRoot = super.getInboundRoot(context);
        return inboundRoot != null ? inboundRoot : router;
    }

    /**
     * Called by OSGi DS to un-inject the default resource provider service
     * 
     * @param resourceProvider
     *            the default resource provider
     */
    public void unbindDefaultResourceProvider(ResourceProvider resourceProvider) {
        if (defaultRestletProvider == resourceProvider) {
            defaultRestletProvider = null;

            if (router != null)
                router.detach(resourceProvider.getInboundRoot(router
                        .getContext()));
        }
    }

    /**
     * Called by OSGi DS to un-inject the default router provider service
     * 
     * @param routerProvider
     *            the default router provider
     */
    public void unbindDefaultRouterProvider(RouterProvider routerProvider) {
        if (defaultRestletProvider == routerProvider) {
            defaultRestletProvider = routerProvider;

            if (router != null)
                router.detach(routerProvider.getInboundRoot(router.getContext()));
        }
    }

    /**
     * Called by OSGi DS to un-inject the directory provider service
     * 
     * @param directoryProvider
     *            the directory provider
     */
    public void unbindDirectoryProvider(DirectoryProvider directoryProvider) {
        if (directoryProviders.remove(directoryProvider)) {
            if (router != null)
                router.detach(directoryProvider.getInboundRoot(router
                        .getContext()));
        }
    }

    /**
     * Called by OSGi DS to un-inject the resource provider service
     * 
     * @param resourceProvider
     *            the resource provider
     */
    public void unbindResourceProvider(ResourceProvider resourceProvider) {
        if (resourceProviders.remove(resourceProvider)) {
            if (router != null)
                router.detach(resourceProvider.getInboundRoot(router
                        .getContext()));
        }
    }
}
