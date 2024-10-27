/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch07.sec1.sub1;

import org.restlet.data.Form;
import org.restlet.resource.ClientResource;

/**
 * Mail client updating a mail by submitting a form.
 */
public class MailClient {

    public static void main(String[] args) throws Exception {
        ClientResource mailClient = new ClientResource(
                "http://localhost:8111/accounts/chunkylover53/mails/123");

        Form form = new Form();
        form.add("subject", "Message to Jérôme");
        form.add("content", "Doh!\n\nAllo?");
        System.out.println(form.getWebRepresentation());
        mailClient.put(form).write(System.out);
    }

}
