/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch02.sec5.sub4;

import org.restlet.resource.ClientResource;

/**
 * Creating dynamic proxies based on annotated Java interfaces.
 */
public class MailClient {

    public static void main(String[] args) throws Exception {
        RootResource mailRoot = ClientResource.create("http://localhost:8111/",
                RootResource.class);
        String result = mailRoot.represent();
        System.out.println(result);
    }

}
