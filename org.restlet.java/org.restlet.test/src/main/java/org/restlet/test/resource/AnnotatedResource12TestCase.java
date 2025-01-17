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
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.restlet.data.Form;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Finder;
import org.restlet.test.RestletTestCase;

/**
 * Test the annotated resources, client and server sides.
 *
 * @author Jerome Louvel
 */
public class AnnotatedResource12TestCase extends RestletTestCase {

    private ClientResource clientResource;

    private MyResource12 myResource;

    @BeforeEach
    protected void setUpEach() throws Exception {
        Finder finder = new Finder();
        finder.setTargetClass(MyServerResource12.class);

        this.clientResource = new ClientResource("http://local");
        this.clientResource.setNext(finder);
        this.myResource = clientResource.wrap(MyResource12.class);
    }

    @AfterEach
    protected void tearDownEach() throws Exception {
        clientResource = null;
        myResource = null;
    }

    @Test
    public void testPutGet() {
        Form myForm = myResource.represent();
        assertNull(myForm);

        myForm = new Form();
        myForm.add("param1", "value1");
        myForm.add("param2", "value2");
        myResource.store(myForm);

        myForm = myResource.represent();
        assertNotNull(myForm);
        assertEquals("value1", myForm.getFirstValue("param1"));
        assertEquals("value2", myForm.getFirstValue("param2"));
    }

}
