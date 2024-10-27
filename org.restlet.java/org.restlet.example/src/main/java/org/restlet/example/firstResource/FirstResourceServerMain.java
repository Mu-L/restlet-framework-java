/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.firstResource;

import org.restlet.Component;
import org.restlet.data.Protocol;

public class FirstResourceServerMain {

    public static void main(String[] args) throws Exception {
        // Create a new Component.
        Component component = new Component();

        // Add a new HTTP server listening on port 8111.
        component.getServers().add(Protocol.HTTP, 8111);

        component.getDefaultHost().attach("/firstResource",
                new FirstResourceApplication());

        // Start the component.
        component.start();
    }

}
