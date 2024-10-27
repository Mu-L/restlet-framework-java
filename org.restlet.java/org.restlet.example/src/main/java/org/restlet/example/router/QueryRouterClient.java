/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.router;

import org.restlet.Application;
import org.restlet.data.Status;
import org.restlet.resource.ClientResource;

public class QueryRouterClient extends Application {
    public static void main(String[] args) throws Exception {
        ClientResource cr = new ClientResource("http://localhost:8182/path");
        cr.addQueryParameter("q", "hello");
        cr.get().write(System.out);

        cr = new ClientResource("http://localhost:8182/path");
        cr.addQueryParameter("q", "bye");
        cr.get().write(System.out);

        cr = new ClientResource("http://localhost:8182/path");
        cr.addQueryParameter("q", "test");
        try {
            cr.get();
        } catch (Exception e) {
            if (Status.CLIENT_ERROR_NOT_FOUND.equals(cr.getStatus())) {
                System.out.println("fine.");
            } else {
                System.out.println("Should be 404 not found response.");
            }
        }
    }
}
