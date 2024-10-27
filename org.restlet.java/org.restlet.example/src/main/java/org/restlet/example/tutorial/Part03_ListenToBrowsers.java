/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.tutorial;

import org.restlet.Server;
import org.restlet.data.Protocol;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 * Listening to Web browsers.
 * 
 * @author Jerome Louvel
 */
public class Part03_ListenToBrowsers extends ServerResource {

    public static void main(String[] args) throws Exception {
        // Create the HTTP server and listen on port 8111
        new Server(Protocol.HTTP, 8111, Part03_ListenToBrowsers.class).start();
    }

    @Get
    public String toString() {
        return "hello, world";
    }

}
