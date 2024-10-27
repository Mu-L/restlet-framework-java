/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch05.sec5;

import org.restlet.Server;
import org.restlet.data.Protocol;

/**
 * Server exposing a resource capable of computing a digest on its
 * representations.
 */
public class VerificationServer {

    public static void main(String[] args) throws Exception {
        // Instantiating the HTTP server and listening on port 8111
        new Server(Protocol.HTTP, 8111, VerifiedServerResource.class).start();
    }

}
