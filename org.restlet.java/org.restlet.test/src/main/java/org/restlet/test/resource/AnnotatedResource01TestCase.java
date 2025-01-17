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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.engine.Engine;
import org.restlet.ext.jackson.JacksonConverter;
import org.restlet.representation.ObjectRepresentation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Finder;
import org.restlet.resource.ResourceException;
import org.restlet.test.RestletTestCase;

/**
 * Test the annotated resources, client and server sides.
 *
 * @author Jerome Louvel
 */
public class AnnotatedResource01TestCase extends RestletTestCase {

    private ClientResource clientResource;

    private MyResource01 myResource;

    @BeforeEach
    protected void setUpEach() throws Exception {
        Engine.getInstance().getRegisteredConverters().clear();
        Engine.getInstance().getRegisteredConverters().add(new JacksonConverter());
        Engine.getInstance().registerDefaultConverters();
        Finder finder = new Finder();
        finder.setTargetClass(MyServerResource01.class);

        this.clientResource = new ClientResource("http://local");
        this.clientResource.setNext(finder);
        this.myResource = clientResource.wrap(MyResource01.class);
    }

    @AfterEach
    protected void tearDownEach() throws Exception {
        clientResource = null;
        myResource = null;
    }

    @Test
    public void testDelete() {
        assertEquals("Done", myResource.remove());
    }

    @Test
    public void testGet() throws IOException, ResourceException {
        MyBean myBean = myResource.represent();
        assertNotNull(myBean);
        assertEquals("myName", myBean.getName());
        assertEquals("myDescription", myBean.getDescription());

        String result = clientResource.get(MediaType.TEXT_XML).getText();
        assertEquals(
                "<MyBean><description>myDescription</description><name>myName</name></MyBean>",
                result);

        result = clientResource.get(MediaType.APPLICATION_XML).getText();
        assertEquals(
                "<MyBean><description>myDescription</description><name>myName</name></MyBean>",
                result);

        result = clientResource.get(MediaType.APPLICATION_ALL_XML).getText();
        assertEquals(
                "<MyBean><description>myDescription</description><name>myName</name></MyBean>",
                result);

        result = clientResource.get(MediaType.APPLICATION_JSON).getText();
        assertEquals("{\"description\":\"myDescription\",\"name\":\"myName\"}",
                result);

        ObjectRepresentation.VARIANT_OBJECT_XML_SUPPORTED = true;
        result = clientResource.get(MediaType.APPLICATION_JAVA_OBJECT_XML).getText();
        assertTrue(result
                .startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                && result.contains("<java version=\""));
        ObjectRepresentation.VARIANT_OBJECT_XML_SUPPORTED = false;
    }

    @Test
    public void testOptions() {
        assertEquals("MyDescription", myResource.describe());
    }

    @Test
    public void testPost() {
        MyBean myBean = new MyBean("myName", "myDescription");
        assertTrue(myResource.accept(myBean));
    }

    @Test
    public void testPut() throws ResourceException {
        // Get current representation
        MyBean myBean = myResource.represent();
        assertNotNull(myBean);

        // Put new representation
        MyBean newBean = new MyBean("newName", "newDescription");
        String result = myResource.store(newBean);
        assertEquals("Done", result);

        // Attempt to send an unknown entity
        try {
            clientResource.put(new StringRepresentation("wxyz", MediaType.APPLICATION_GNU_ZIP));
        } catch (ResourceException re) {
            assertEquals(Status.CLIENT_ERROR_UNSUPPORTED_MEDIA_TYPE, re.getStatus());
        }
    }

}
