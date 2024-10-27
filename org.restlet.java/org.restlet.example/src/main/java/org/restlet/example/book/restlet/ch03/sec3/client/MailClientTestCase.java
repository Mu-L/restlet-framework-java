/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch03.sec3.client;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Method;
import org.restlet.example.book.restlet.ch03.sec3.server.MailServerComponent;

/**
 * Mail client JUnit test case.
 */
public class MailClientTestCase {

    /**
     * Unit test for virtual hosts.
     * 
     * @throws Exception
     */
    @Test
    public void testVirtualHost() throws Exception {

        // Instantiate our Restlet component
        MailServerComponent component = new MailServerComponent();

        // Prepare a mock HTTP call
        Request request = new Request();
        request.setMethod(Method.GET);
        request.setResourceRef("http://www.rmep.org/accounts/");
        request.setHostRef("http://www.rmep.org");
        Response response = new Response(request);
        response.getServerInfo().setAddress("1.2.3.10");
        response.getServerInfo().setPort(80);
        component.handle(request, response);

        // Test if response was successful
        assertTrue(response.getStatus().isSuccess());
    }

}
