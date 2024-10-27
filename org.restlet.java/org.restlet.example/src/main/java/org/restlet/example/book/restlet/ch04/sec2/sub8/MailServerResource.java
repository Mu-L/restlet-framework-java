/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch04.sec2.sub8;

import java.io.IOException;

import org.restlet.data.Reference;
import org.restlet.ext.jaxb.JaxbRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

/**
 * Resource corresponding to a mail received or sent with the parent mail
 * account. Leverages JAXB extension.
 */
public class MailServerResource extends ServerResource {

    @Get
    public Representation toXml() {
        // Create the mail bean
        Mail mail = new Mail();
        mail.setStatus("received");
        mail.setSubject("Message to self");
        mail.setContent("Doh!");
        mail.setAccountRef(new Reference(getReference(), "..").getTargetRef()
                .toString());

        // Wraps the bean with a JAXB representation
        JaxbRepresentation<Mail> result = new JaxbRepresentation<Mail>(mail);
        result.setFormattedOutput(true);
        return result;
    }

    @Put
    public void store(Representation rep) throws IOException {
        // Parse XML representation to get the mail bean
        JaxbRepresentation<Mail> mailRep = new JaxbRepresentation<Mail>(rep,
                Mail.class);
        Mail mail = mailRep.getObject();

        // Output the XML element values
        System.out.println("Status: " + mail.getStatus());
        System.out.println("Subject: " + mail.getSubject());
        System.out.println("Content: " + mail.getContent());
        System.out.println("Account URI: " + mail.getAccountRef());
    }
}
