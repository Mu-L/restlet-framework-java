/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch02.sec5.sub3;

import org.restlet.resource.ClientResource;

/**
 * Illustrating features of client resources.
 */
public class MailClient {

    public static void main(String[] args) throws Exception {
        ClientResource mailRoot = new ClientResource("http://localhost:8111/");
        mailRoot.get().write(System.out);

        String result = mailRoot.get(String.class);
        System.out.println("\n" + result);
    }

}
