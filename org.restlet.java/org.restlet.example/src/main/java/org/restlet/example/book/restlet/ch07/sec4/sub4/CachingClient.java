/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch07.sec4.sub4;

import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

/**
 * Client displaying cache control information.
 */
public class CachingClient {

    public static void main(String[] args) throws Exception {
        ClientResource resource = new ClientResource("http://localhost:8111/");

        // Get a representation
        Representation rep = resource.get();

        // Display caching metadata
        System.out.println("Modified: " + rep.getModificationDate());
        System.out.println("Expires: " + rep.getExpirationDate());
        System.out.println("E-Tag: " + rep.getTag());
    }
}
