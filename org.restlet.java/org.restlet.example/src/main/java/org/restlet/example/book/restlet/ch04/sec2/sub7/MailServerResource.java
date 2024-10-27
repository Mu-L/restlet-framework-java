/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch04.sec2.sub7;

import java.io.IOException;

import javax.xml.transform.OutputKeys;

import org.restlet.data.LocalReference;
import org.restlet.data.Reference;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.ext.xml.TransformRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Resource corresponding to a mail received or sent with the parent mail
 * account. Leverages XSLT transformation.
 */
public class MailServerResource extends ServerResource {

    @Get
    public Representation toXml() throws IOException {
        // Create a new DOM representation
        DomRepresentation rmepMail = new DomRepresentation();
        rmepMail.setIndenting(true);

        // Populate the DOM document
        Document doc = rmepMail.getDocument();

        Node mailElt = doc.createElement("mail");
        doc.appendChild(mailElt);

        Node statusElt = doc.createElement("status");
        statusElt.setTextContent("received");
        mailElt.appendChild(statusElt);

        Node subjectElt = doc.createElement("subject");
        subjectElt.setTextContent("Message to self");
        mailElt.appendChild(subjectElt);

        Node contentElt = doc.createElement("content");
        contentElt.setTextContent("Doh!");
        mailElt.appendChild(contentElt);

        Node accountRefElt = doc.createElement("accountRef");
        accountRefElt.setTextContent(new Reference(getReference(), "..")
                .getTargetRef().toString());
        mailElt.appendChild(accountRefElt);

        // Transform to another XML format using XSLT
        Representation transformSheet = new ClientResource(
                LocalReference.createClapReference(getClass().getPackage())
                        + "/Mail.xslt").get();

        TransformRepresentation result = new TransformRepresentation(rmepMail,
                transformSheet);
        result.getOutputProperties().put(OutputKeys.INDENT, "yes");
        return result;
    }

    @Put
    public void store(DomRepresentation mailRep) {
        // Retrieve the XML element using XPath expressions
        String subject = mailRep.getText("/email/head/subject");
        String content = mailRep.getText("/email/body");

        // Output the XML element values
        System.out.println("Subject: " + subject);
        System.out.println("Content: " + content);
    }
}
