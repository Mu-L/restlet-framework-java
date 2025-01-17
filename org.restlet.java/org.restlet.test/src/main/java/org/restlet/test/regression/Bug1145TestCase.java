/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.regression;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.restlet.Client;
import org.restlet.Component;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.data.Status;
import org.restlet.representation.StringRepresentation;
import org.restlet.test.RestletTestCase;

public class Bug1145TestCase extends RestletTestCase {
    public static class Bug1145TestCaseRestlet extends Restlet {
        @Override
        public void handle(Request request, Response response) {
            try {
                response.setAccessControlExposeHeaders(new HashSet<>(Arrays.asList("Modified")));
                response.setEntity(new StringRepresentation("NO-NPE", MediaType.TEXT_PLAIN));
            } catch (NullPointerException e) {
                response.setEntity(new StringRepresentation("NPE", MediaType.TEXT_PLAIN));
            }
        }
    }

    private Client client;

    private Component component;

    @BeforeEach
    public void setUpEach() throws Exception {
        this.client = new Client(Protocol.HTTP);

        if (this.component == null) {
            this.component = new Component();
            this.component.getServers().add(Protocol.HTTP, TEST_PORT);
            this.component.getDefaultHost().attachDefault(new Bug1145TestCaseRestlet());
        }

        if (!this.component.isStarted()) {
            this.component.start();
        }
    }

    @AfterEach
    public void tearDownEach() throws Exception {
        this.client.stop();
        this.component.stop();
        this.component = null;
    }

    @Test
    public void test0() throws Exception {
        Request request = new Request(Method.GET, "http://localhost:" + TEST_PORT);
        Response result = client.handle(request);
        assertEquals(Status.SUCCESS_OK, result.getStatus());
        assertEquals("NO-NPE", result.getEntity().getText());
    }
}
