/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch07.sec1.sub3;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.resource.Directory;
import org.restlet.routing.Router;

/**
 * Application merging static web site and web service resources.
 */
public class MergeSitesServicesApplication extends Application {

    public static void main(String[] args) throws Exception {
        Component component = new Component();
        component.getServers().add(Protocol.HTTP, 8111);
        component.getClients().add(Protocol.FILE);
        component.getDefaultHost().attach(new MergeSitesServicesApplication());
        component.start();
    }

    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());

        // Serve static files (images, etc.)
        String rootUri = "file:///" + System.getProperty("user.home");
        Directory directory = new Directory(getContext(), rootUri);
        directory.setListingAllowed(true);
        router.attach("/home", directory);

        // Attach the hello web service
        router.attach("/hello", HelloServerResource.class);

        return router;
    }
}
