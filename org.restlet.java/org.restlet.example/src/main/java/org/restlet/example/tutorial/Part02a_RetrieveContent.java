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
 * Retrieving the content of a Web page.
 * 
 * @author Jerome Louvel
 */
public class Part02a_RetrieveContent {
    public static void main(String[] args) throws Exception {
        // Outputting the content of a Web page
        new ClientResource("http://restlet.org").get().write(System.out);
    }

}
