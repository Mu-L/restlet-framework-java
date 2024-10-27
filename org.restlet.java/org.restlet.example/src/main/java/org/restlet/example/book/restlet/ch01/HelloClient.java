/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch01;

import org.restlet.resource.ClientResource;

/**
 * Creates and launches a HTTP client invoking the server listening on port
 * 8111, and writing the response entity on the console.
 */
public class HelloClient {

    public static void main(String[] args) throws Exception {
        ClientResource helloClientresource = new ClientResource(
                "http://localhost:8111/");
        helloClientresource.get().write(System.out);
    }
}
