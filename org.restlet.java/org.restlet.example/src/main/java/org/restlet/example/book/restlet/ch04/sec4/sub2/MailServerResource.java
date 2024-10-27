/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch04.sec4.sub2;

import java.util.HashMap;
import java.util.Map;

import org.restlet.data.LocalReference;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.ext.velocity.TemplateRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 * Resource corresponding to a mail received or sent with the parent mail
 * account. Leverages Velocity template engine.
 */
public class MailServerResource extends ServerResource {

    @Get
    public Representation toXml() throws Exception {
        // Create the mail bean
        Mail mail = new Mail();
        mail.setStatus("received");
        mail.setSubject("Message to self");
        mail.setContent("Doh!");
        mail.setAccountRef(new Reference(getReference(), "..").getTargetRef()
                .toString());

        // Prepare the data model
        Map<String, Object> dataModel = new HashMap<String, Object>();
        dataModel.put("mail", mail);

        // Load the Velocity template
        Representation mailVtl = new ClientResource(
                LocalReference.createClapReference(getClass().getPackage())
                        + "/Mail.vtl").get();

        // Wraps the bean with a Velocity representation
        return new TemplateRepresentation(mailVtl, dataModel,
                MediaType.TEXT_HTML);
    }

}
