/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.bench;

import java.util.logging.Level;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.Server;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.engine.Edition;
import org.restlet.engine.Engine;
import org.restlet.engine.connector.ConnectorHelper;
import org.restlet.representation.FileRepresentation;

public class TestGetServer {

    public static void main(String[] args) throws Exception {
        ConnectorHelper<Server> helper;
        helper = new org.restlet.engine.connector.HttpServerHelper(null);
        Engine.getInstance().getRegisteredServers().add(0, helper);
        if (Edition.JSE.isCurrentEdition()) {
            Engine.setLogLevel(Level.FINE);
        }

        Server server = new Server(new Context(), Protocol.HTTP, 8554,
                new Restlet() {
                    @Override
                    public void handle(Request request, Response response) {
                        FileRepresentation fr = new FileRepresentation(
                                "file:///c:/RHDSetup.log", MediaType.TEXT_PLAIN);
                        System.out.println("Size sent: " + fr.getSize());
                        response.setEntity(fr);
                    }
                });

        server.start();
    }
}
