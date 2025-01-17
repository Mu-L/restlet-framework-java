/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.ext.spring;

import java.util.logging.Level;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.resource.Finder;
import org.restlet.resource.ServerResource;

/**
 * Finder that is specialized for easier usage by Spring wiring services. The
 * idea is to create a singleton Spring bean based on that SpringFinder and
 * configure it using Spring's "lookup-method" element to return instances of a
 * "prototype" bean for {@link #create()}. Finally, attach the SpringFinder to
 * your Router. When the {@link #create()} method is invoked, a new instance of
 * your prototype bean will be created and returned. A sample XML for
 * "lookup-method":
 * 
 * <pre>
 *      &lt;bean id=&quot;myFinder&quot; class=&quot;org.restlet.ext.spring.SpringFinder&quot;&gt; 
 *              &lt;lookup-method name=&quot;create&quot; bean=&quot;myResource&quot;/&gt; 
 *      &lt;/bean&gt;
 *       
 *      &lt;bean id=&quot;myResource&quot; class=&quot;com.mycompany.rest.resource.MyResource&quot; scope=&quot;prototype&quot;&gt; 
 *              &lt;property name=&quot;aProperty&quot; value=&quot;anotherOne&quot;/&gt; 
 *              &lt;property name=&quot;oneMore&quot; value=&quot;true&quot;/&gt;
 *      &lt;/bean&gt;
 * </pre>
 * 
 * Note that the <a href="http://cglib.sourceforge.net/">Code Generation
 * Library</a> (cglib) will be required in order to use the Spring's lookup
 * method mechanism.<br>
 * <br>
 * Concurrency note: instances of this class or its subclasses can be invoked by
 * several threads at the same time and therefore must be thread-safe. You
 * should be especially careful when storing state in member variables.
 * 
 * @author Jerome Louvel
 */
public class SpringFinder extends Finder {

    /**
     * Constructor.
     */
    public SpringFinder() {
        super();
    }

    /**
     * Constructor.
     * 
     * @param context
     *            The parent context.
     */
    public SpringFinder(Context context) {
        super(context);
    }

    /**
     * Constructor.
     * 
     * @param context
     *            The context.
     * @param targetClass
     *            The target resource class.
     */
    public SpringFinder(Context context,
            Class<? extends ServerResource> targetClass) {
        super(context, targetClass);
    }

    /**
     * Constructor.
     * 
     * @param restlet
     *            The parent Restlet.
     */
    public SpringFinder(Restlet restlet) {
        super(restlet.getContext());
    }

    /**
     * Creates a new instance of the {@link ServerResource} class designated by
     * the "targetClass" property. This method is intended to be configured as a
     * lookup method in Spring.
     * 
     * @return The created resource or null.
     */
    public ServerResource create() {
        ServerResource result = null;

        if (getTargetClass() != null) {
            try {
                // Invoke the default constructor
                result = (ServerResource) getTargetClass().newInstance();
            } catch (Exception e) {
                getLogger()
                        .log(Level.WARNING,
                                "Exception while instantiating the target server resource.",
                                e);
            }
        }

        return result;
    }

    /**
     * Calls the {@link #create()} method that can be configured as a lookup
     * method in Spring. Overriding this method was necessary for direct calls
     * to it, for example by unit tests.
     */
    @Override
    public ServerResource create(Class<? extends ServerResource> targetClass,
            Request request, Response response) {
        return create(request, response);
    }

    @Override
    public org.restlet.resource.ServerResource create(Request request,
            Response response) {
        return create();
    }

}
