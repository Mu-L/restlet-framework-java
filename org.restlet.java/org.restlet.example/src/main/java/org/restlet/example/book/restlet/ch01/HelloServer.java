/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch01;

import org.restlet.Server;
import org.restlet.data.Protocol;

/**
 * Creates and launches a HTTP server listening on port 8111, and transmitting
 * all calls to the HelloServerResource class.
 */
public class HelloServer {

    public static void main(String[] args) throws Exception {
        Server helloServer = new Server(Protocol.HTTP, 8111,
                HelloServerResource.class);
        helloServer.start();
    }
}
