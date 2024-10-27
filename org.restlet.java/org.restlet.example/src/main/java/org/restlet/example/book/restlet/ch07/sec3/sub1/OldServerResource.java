/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch07.sec3.sub1;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 * Resource that simply redirects to the port 8111.
 */
public class OldServerResource extends ServerResource {

    @Get
    public String redirect() {
        // Sets the response status to 301 (Moved Permanently)
        redirectPermanent("http://localhost:8111/");

        System.out.println("Redirecting client to new location...");

        // Add explanation message entity
        return "Resource moved... \n";
    }
}
