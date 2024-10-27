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

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Finder;
import org.restlet.resource.ResourceException;
import org.restlet.test.RestletTestCase;

/**
 * Test the annotated resources, client and server sides.
 * 
 * @author Jerome Louvel
 */
public class AnnotatedResource02TestCase extends RestletTestCase {

    private ClientResource clientResource;

    @BeforeEach
    protected void setUpEach() throws Exception {
        Finder finder = new Finder();
        finder.setTargetClass(MyResource02.class);

        this.clientResource = new ClientResource("http://local");
        this.clientResource.setNext(finder);
    }

    @AfterEach
    protected void tearDownEach() throws Exception {
        clientResource = null;
    }

    @Test
    public void testGet() throws IOException, ResourceException {
        Representation result = clientResource.get(MediaType.APPLICATION_ATOM);
        assertNotNull(result);
        assertEquals("<content/>", result.getText());
        assertEquals(MediaType.TEXT_XML, result.getMediaType());
    }

}
