/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.resource;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.restlet.Application;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.restlet.routing.Router;

/**
 * Test annotated resource inheriting abstract super class that implements
 * several annotated interfaces.
 *
 * @author Thierry Boileau
 */
public class AnnotatedResource10TestCase extends InternalConnectorTestCase {

    protected Application createApplication(final String path) {
        return new Application() {
            @Override
            public Restlet createInboundRoot() {
                Router router = new Router(getContext());
                router.attach(path, MyResource10.class);
                return router;
            }
        };
    }

    /**
     * Test annotated methods.
     *
     * @throws IOException
     * @throws ResourceException
     */
    @Test
    public void test() throws IOException, ResourceException {
        Request request = createRequest(Method.GET);
        Response response = handle(request);
        assertEquals(Status.SUCCESS_OK, response.getStatus());
        assertEquals("asText", response.getEntity().getText());
        response.getEntity().release();

        request = createRequest(Method.POST);
        response = handle(request);
        assertEquals(Status.SUCCESS_OK, response.getStatus());
        assertEquals("accept", response.getEntity().getText());
        response.getEntity().release();
    }

}
