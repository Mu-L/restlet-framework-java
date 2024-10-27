/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.tutorial;

import org.restlet.resource.ClientResource;

/**
 * Retrieving the content of a Web page (detailed).
 * 
 * @author Jerome Louvel
 */
public class Part02b_RetrieveContent {
    public static void main(String[] args) throws Exception {
        // Create the client resource
        ClientResource resource = new ClientResource("http://restlet.org");

        // Customize the referrer property
        resource.setReferrerRef("http://www.mysite.org");

        // Write the response entity on the console
        resource.get().write(System.out);
    }

}
