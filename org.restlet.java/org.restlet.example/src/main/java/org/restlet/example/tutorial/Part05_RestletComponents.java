/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.tutorial;

import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 * Restlets components.
 * 
 * @author Jerome Louvel
 */
public class Part05_RestletComponents extends ServerResource {

    public static void main(String[] args) throws Exception {
        // Create a new Restlet component and add a HTTP server connector to it
        Component component = new Component();
        component.getServers().add(Protocol.HTTP, 8111);

        // Then attach it to the local host
        component.getDefaultHost().attach("/trace",
                Part05_RestletComponents.class);

        // Now, let's start the component!
        // Note that the HTTP server connector is also automatically started.
        component.start();
    }

    @Get
    public String toString() {
        // Print the requested URI path
        return "Resource URI  : " + getReference() + '\n' + "Root URI      : "
                + getRootRef() + '\n' + "Routed part   : "
                + getReference().getBaseRef() + '\n' + "Remaining part: "
                + getReference().getRemainingPart();
    }

}
