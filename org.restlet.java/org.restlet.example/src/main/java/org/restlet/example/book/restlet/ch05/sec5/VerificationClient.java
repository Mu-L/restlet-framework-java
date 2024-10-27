/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch05.sec5;

import org.restlet.representation.DigesterRepresentation;
import org.restlet.resource.ClientResource;

/**
 * Client retrieving a representation and checking the validity of its digest.
 */
public class VerificationClient {

    public static void main(String[] args) throws Exception {
        ClientResource resource = new ClientResource("http://localhost:8111/");

        // The Digester helps computing the digest while reading or writing the
        // representation's content.
        DigesterRepresentation rep = new DigesterRepresentation(resource.get());
        rep.write(System.out);

        if (rep.checkDigest()) {
            System.out.println("\nContent checked.");
        } else {
            System.out.println("\nContent not checked.");
        }
    }

}
