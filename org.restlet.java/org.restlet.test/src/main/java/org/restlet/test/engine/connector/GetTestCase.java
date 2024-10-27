/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.engine.connector;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.restlet.Application;
import org.restlet.Client;
import org.restlet.Component;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Router;

/**
 * Test that a simple get works for all the connectors.
 *
 * @author Kevin Conaway
 */
public class GetTestCase extends BaseConnectorsTestCase {

    public static class GetTestResource extends ServerResource {
        @Get
        public String toString() {
            return "Hello world";
        }
    }

    @Override
    protected void call(String uri) throws Exception {
        Request request = new Request(Method.GET, uri);
        Client c = new Client(Protocol.HTTP);
        Response r = c.handle(request);
        assertEquals(
                Status.SUCCESS_OK, r.getStatus(),
                r.getStatus().getDescription()
        );
        assertEquals("Hello world", r.getEntity().getText());
        c.stop();
    }

    @Override
    protected Application createApplication(Component component) {
        final Application application = new Application() {
            @Override
            public Restlet createInboundRoot() {
                final Router router = new Router(getContext());
                router.attach("/test", GetTestResource.class);
                return router;
            }
        };

        return application;
    }
}
