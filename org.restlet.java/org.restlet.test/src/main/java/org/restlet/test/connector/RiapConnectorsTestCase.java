/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.connector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.routing.Router;
import org.restlet.test.RestletTestCase;

/**
 * Unit test case for the RIAP Internal routing protocol.
 */
public class RiapConnectorsTestCase extends RestletTestCase {

    /**
     * Test the RIAP client and server connectors.
     */
    @Test
    public void testRiapConnectors() {
        Component component = new Component();
        component.getServers().add(Protocol.RIAP);
        component.getClients().add(Protocol.RIAP);

        Application app = new Application() {
            @Override
            public Restlet createInboundRoot() {
                Router router = new Router(getContext());
                router.attach("/testA", new Restlet(getContext()) {

                    @Override
                    public void handle(Request request, Response response) {
                        response.setEntity("hello, world", MediaType.TEXT_PLAIN);
                    }

                });
                router.attach("/testB", new Restlet(getContext()) {
                    public void handle(Request request, Response response) {
                        ClientResource resource = new ClientResource(
                                "riap://component/app/testA");
                        try {
                            response.setEntity(resource.get().getText(),
                                    MediaType.TEXT_PLAIN);
                        } catch (Exception e) {
                        }
                    }

                });
                return router;
            }
        };

        // Attach the private application
        component.getInternalRouter().attach("/app", app);

        try {
            component.start();

            ClientResource res = new ClientResource(
                    "riap://component/app/testA");
            Representation rep = res.get();
            assertEquals("hello, world", rep.getText());

            res = new ClientResource("riap://component/app/testB");
            rep = res.get();
            assertEquals("hello, world", rep.getText());

            component.stop();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
