/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.representation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.restlet.Application;
import org.restlet.Client;
import org.restlet.Component;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.data.Status;
import org.restlet.representation.DigesterRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.test.RestletTestCase;

/**
 * Test {@link org.restlet.engine.util.DateUtils}.
 * 
 * @author Thierry Boileau
 */
public class DigesterRepresentationTestCase extends RestletTestCase {

    /** Component used for the tests. */
    private Component component;

    /**
     * Internal class used for test purpose.
     * 
     */
    private static class TestDigestApplication extends Application {

        @Override
        public Restlet createInboundRoot() {
            Restlet restlet = new Restlet() {
                @Override
                public void handle(Request request, Response response) {
                    Representation rep = request.getEntity();
                    try {
                        // Such representation computes the digest while
                        // consuming the wrapped representation.
                        DigesterRepresentation digester = new DigesterRepresentation(
                                rep);
                        digester.exhaust();

                        if (digester.checkDigest()) {
                            response.setStatus(Status.SUCCESS_OK);
                            StringRepresentation f = new StringRepresentation(
                                    "9876543210");
                            digester = new DigesterRepresentation(f);
                            // Consume first
                            digester.exhaust();
                            // Set the digest
                            digester.setDigest(digester.computeDigest());
                            response.setEntity(digester);
                        } else {
                            response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                        }
                    } catch (Exception e1) {
                    }
                }

            };
            return restlet;
        }
    }

    @BeforeEach
    protected void setUpEach() throws Exception {
        component = new Component();
        component.getServers().add(Protocol.HTTP, TEST_PORT);
        component.getDefaultHost().attach(new TestDigestApplication());
        component.start();
    }

    @AfterEach
    protected void tearDownEach() throws Exception {
        component.stop();
        component = null;
    }

    /**
     * Tests partial Get requests.
     *
     */
    @Test
    public void testGet() {
        Client client = new Client(Protocol.HTTP);

        // Test partial Get.
        Request request = new Request(Method.PUT, "http://localhost:"
                + TEST_PORT + "/");
        StringRepresentation rep = new StringRepresentation("0123456789");
        try {
            DigesterRepresentation digester = new DigesterRepresentation(rep);
            // Such representation computes the digest while
            // consuming the wrapped representation.
            digester.exhaust();
            // Set the digest with the computed one
            digester.setDigest(digester.computeDigest());
            request.setEntity(digester);

            Response response = client.handle(request);

            assertEquals(Status.SUCCESS_OK, response.getStatus());
            digester = new DigesterRepresentation(response.getEntity());
            digester.exhaust();
            assertTrue(digester.checkDigest());

            client.stop();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
