/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.bench;

import java.io.IOException;
import java.util.logging.Level;

import org.restlet.Client;
import org.restlet.engine.Edition;
import org.restlet.engine.Engine;
import org.restlet.engine.connector.ConnectorHelper;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

public class TestGetClient {

    public static void main(String[] args) throws IOException {
        ConnectorHelper<Client> helper;
        helper = new org.restlet.engine.connector.HttpClientHelper(null);
        Engine.getInstance().getRegisteredClients().add(0, helper);
        if (Edition.JSE.isCurrentEdition()) {
            Engine.setLogLevel(Level.FINE);
        }
        long startTime = System.currentTimeMillis();
        ClientResource resource = new ClientResource("http://localhost:8554/");
        
        try {
            Representation entity = resource.get();
            System.out.println("Status: " + resource.getStatus());

            long expectedSize = entity.getSize();
            long receivedSize = entity.exhaust();

            System.out.println("Size expected: " + expectedSize);
            System.out.println("Size consumed: " + receivedSize);

            if ((expectedSize != -1) && (expectedSize != receivedSize)) {
                System.out.println("ERROR: SOME BYTES WERE LOST!");
            }
        } catch (ResourceException e) {
            System.out.println("Status: " + resource.getStatus());
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Duration: " + (endTime - startTime) + " ms");
    }

}
