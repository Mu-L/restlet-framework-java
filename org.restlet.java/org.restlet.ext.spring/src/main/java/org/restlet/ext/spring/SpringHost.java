/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.ext.spring;

import java.util.Map;

import org.restlet.Component;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.resource.ServerResource;
import org.restlet.routing.VirtualHost;

/**
 * Virtual host that is easily configurable with Spring. Here is a usage
 * example:
 * 
 * <pre>
 *     &lt;bean id=&quot;virtualHost&quot; class=&quot;org.restlet.ext.spring.SpringHost&quot;&gt;
 *         &lt;constructor-arg ref=&quot;component&quot; /&gt;
 *         &lt;property name=&quot;hostDomain&quot;
 *                 value=&quot;mydomain.com|www.mydomain.com&quot; /&gt;
 *         &lt;property name=&quot;attachments&quot;&gt;
 *             &lt;map&gt;
 *                 &lt;entry key=&quot;/&quot;&gt;
 *                     &lt;ref bean=&quot;application&quot; /&gt;
 *                 &lt;/entry&gt;
 *             &lt;/map&gt;
 *         &lt;/property&gt;
 *     &lt;/bean&gt;
 * </pre>
 * 
 * Concurrency note: instances of this class or its subclasses can be invoked by
 * several threads at the same time and therefore must be thread-safe. You
 * should be especially careful when storing state in member variables.
 * 
 * @author Jerome Louvel
 */
public class SpringHost extends VirtualHost {

    /**
     * Constructor.
     * 
     * @param component
     *            The parent component.
     */
    public SpringHost(Component component) {
        super(component.getContext());
    }

    /**
     * Constructor.
     * 
     * @param context
     *            The parent context.
     */
    public SpringHost(Context context) {
        super(context);
    }

    /**
     * Sets a route to attach. The keys is the URI template and the value can be
     * either Restlet instance, {@link ServerResource} subclasse (as
     * {@link Class} instances or as qualified class names).
     * 
     * @param path
     *            The attachment URI path.
     * @param route
     *            The route object to attach.
     */
    public void setAttachment(String path, Object route) {
        if (route instanceof Restlet) {
            checkContext((Restlet) route);
        }

        SpringRouter.setAttachment(this, path, route);
    }

    /**
     * Sets the map of routes to attach. The map keys are the URI templates and
     * the values can be either Restlet instances, {@link ServerResource}
     * subclasses (as {@link Class} instances or as qualified class names).
     * 
     * @param routes
     *            The map of routes to attach.
     */
    public void setAttachments(Map<String, Object> routes) {
        for (String key : routes.keySet()) {
            setAttachment(key, routes.get(key));
        }
    }

    /**
     * Sets the default route to attach. The route can be either Restlet
     * instances, {@link ServerResource} subclasses (as {@link Class} instances
     * or as qualified class names).
     * 
     * @param route
     *            The default route to attach.
     */
    public void setDefaultAttachment(Object route) {
        setAttachment("", route);
    }

}
