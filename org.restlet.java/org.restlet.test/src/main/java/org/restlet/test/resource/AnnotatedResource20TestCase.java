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
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.restlet.Application;
import org.restlet.data.MediaType;
import org.restlet.engine.Engine;
import org.restlet.ext.jackson.JacksonConverter;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.restlet.test.RestletTestCase;

/**
 * Test the annotated resources, client and server sides.
 * 
 * @author Jerome Louvel
 */
public class AnnotatedResource20TestCase extends RestletTestCase {

    private ClientResource clientResource;

    private MyResource20 myResource;

    @BeforeEach
    protected void setUpEach() throws Exception {
        Engine.getInstance().getRegisteredConverters().clear();
        Engine.getInstance().getRegisteredConverters().add(new JacksonConverter());
        Engine.getInstance().registerDefaultConverters();

        // Hosts resources into an Application because we need some services for
        // handling content negotiation, conversion of exceptions, etc.
        Application application = new Application();
        application.setInboundRoot(MyServerResource20.class);

        this.clientResource = new ClientResource("http://local");
        this.clientResource.accept(MediaType.APPLICATION_JSON);
        this.clientResource.setNext(application);
        this.myResource = clientResource.wrap(MyResource20.class);
    }

    @AfterEach
    protected void tearDownEach() throws Exception {
        clientResource = null;
        myResource = null;
    }

    @Test
    public void testGet() throws IOException {
        try {
            myResource.represent();
            fail("Exception should be thrown");
        } catch (MyException01 e) {
            assertEquals(400, clientResource.getStatus().getCode());
        } catch (ResourceException e) {
            fail("Exception should be MyException01", e);
        }
    }

    @Test
    public void testGetAndSerializeException() throws IOException {
        try {
            myResource.representAndSerializeException();
            fail("Exception should be thrown");
        } catch (MyException02 e) {
            assertEquals("my custom error", e.getCustomProperty());
            assertEquals(400, clientResource.getStatus().getCode());
        } catch (ResourceException e) {
            fail("Exception should be MyException02", e);
        }
    }
}
