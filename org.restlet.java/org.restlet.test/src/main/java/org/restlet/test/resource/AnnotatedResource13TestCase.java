/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.resource;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Finder;
import org.restlet.test.RestletTestCase;

/**
 * Test the annotated resources, client and server sides.
 * 
 * @author Jerome Louvel
 */
public class AnnotatedResource13TestCase extends RestletTestCase {

    private ClientResource clientResource;

    private MyResource13 myResource;

    @BeforeEach
    protected void setUpEach() throws Exception {
        Finder finder = new Finder();
        finder.setTargetClass(MyServerResource13.class);

        this.clientResource = new ClientResource("http://local");
        this.clientResource.setNext(finder);
        this.myResource = clientResource.wrap(MyResource13.class);
    }

    @AfterEach
    protected void tearDownEach() throws Exception {
        clientResource = null;
        myResource = null;
    }

    @Test
    public void testQuery() {
        Contact contact = myResource.retrieve();
        assertNotNull(contact);

        LightContact lightContact = myResource.retrieveLight();
        assertNotEquals(lightContact.getClass(), Contact.class);
        assertNotNull(lightContact);

        FullContact fullContact = myResource.retrieveFull();
        assertNotNull(fullContact);
    }

}
