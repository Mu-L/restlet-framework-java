/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.tutorial;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;

/**
 * Reaching target Resources
 * 
 * @author Jerome Louvel
 */
public class Part12_ServerResources extends Application {

    /**
     * Run the example as a standalone component.
     * 
     * @param args
     *            The optional arguments.
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // Create a component
        Component component = new Component();
        component.getServers().add(Protocol.HTTP, 8111);

        // Create an application
        Application application = new Part12_ServerResources();

        // Attach the application to the component and start it
        component.getDefaultHost().attachDefault(application);
        component.start();
    }

    @Override
    public Restlet createInboundRoot() {
        // Create a router
        Router router = new Router(getContext());

        // Attach the resources to the router
        router.attach("/users/{user}", UserResource.class);
        router.attach("/users/{user}/orders", OrdersResource.class);
        router.attach("/users/{user}/orders/{order}", OrderResource.class);

        // Return the root router
        return router;
    }

}
