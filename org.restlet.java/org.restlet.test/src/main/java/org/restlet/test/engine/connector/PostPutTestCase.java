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
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.restlet.Application;
import org.restlet.Client;
import org.restlet.Component;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.representation.Representation;

/**
 * Unit tests for POST and PUT requests.
 * 
 * @author Jerome Louvel
 */
public class PostPutTestCase extends BaseConnectorsTestCase {

    @Override
    protected void call(String uri) throws Exception {
        Client client = new Client(Protocol.HTTP);
        testCall(client, Method.POST, uri);
        testCall(client, Method.PUT, uri);
        client.stop();
    }

    @Override
    protected Application createApplication(final Component component) {
        Application application = new Application() {
            @Override
            public Restlet createInboundRoot() {
                final Restlet trace = new Restlet(getContext()) {
                    @Override
                    public void handle(Request request, Response response) {
                        Representation entity = request.getEntity();
                        if (entity != null) {
                            Form form = new Form(entity);
                            response.setEntity(form.getWebRepresentation());
                        }
                    }
                };

                return trace;
            }
        };

        return application;
    }

    private void testCall(Client client, Method method, String uri) {
        Form inputForm = new Form();
        inputForm.add("a", "a");
        inputForm.add("b", "b");

        Request request = new Request(method, uri);
        request.setEntity(inputForm.getWebRepresentation());

        Response response = client.handle(request);
        Representation entity = response.getEntity();
        assertNotNull(entity);

        Form outputForm = new Form(entity);
        assertEquals(2, outputForm.size());
        assertEquals("a", outputForm.getFirstValue("a"));
        assertEquals("b", outputForm.getFirstValue("b"));
    }

}
