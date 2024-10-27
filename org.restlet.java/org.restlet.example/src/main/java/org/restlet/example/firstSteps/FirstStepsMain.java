/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.firstSteps;

import org.restlet.Component;
import org.restlet.data.Protocol;

public class FirstStepsMain {

    public static void main(String[] args) throws Exception {
        // Create a new Component.
        Component component = new Component();

        // Add a new HTTP server listening on port 8111.
        component.getServers().add(Protocol.HTTP, 8111);

        // Attach the sample application.
        component.getDefaultHost().attach("/firstSteps",
                new FirstStepsApplication());

        // Start the component.
        component.start();
    }
}
