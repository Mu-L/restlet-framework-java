/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch07.sec4.sub1;

import org.restlet.Server;
import org.restlet.data.Protocol;

public class DynamicContentServer {

    public static void main(String[] args) throws Exception {
        // Instantiating the HTTP server and listening on port 8111
        new Server(Protocol.HTTP, 8111, DynamicContentServerResource.class)
                .start();
    }

}
