/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch04.sec2.sub3;

import java.io.IOException;

import org.restlet.data.Reference;
import org.restlet.ext.xml.SaxRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Resource corresponding to a mail received or sent with the parent mail
 * account. Leverages the SAX API.
 */
public class MailServerResource extends ServerResource {

    @Get
    public Representation toXml() {
        // Create a new SAX representation
        SaxRepresentation result = new SaxRepresentation() {

            public void write(org.restlet.ext.xml.XmlWriter writer)
                    throws IOException {
                try {
                    // Start document
                    writer.startDocument();

                    // Append the root node
                    writer.startElement("mail");

                    // Append the child nodes and set their text content
                    writer.startElement("status");
                    writer.characters("received");
                    writer.endElement("status");

                    writer.startElement("subject");
                    writer.characters("Message to self");
                    writer.endElement("subject");

                    writer.startElement("content");
                    writer.characters("Doh!");
                    writer.endElement("content");

                    writer.startElement("accountRef");
                    writer.characters(new Reference(getReference(), "..")
                            .getTargetRef().toString());
                    writer.endElement("accountRef");

                    // End the root node
                    writer.endElement("mail");

                    // End the document
                    writer.endDocument();
                } catch (SAXException e) {
                    throw new IOException(e.getMessage());
                }
            };
        };
        result.setNamespaceAware(true);

        return result;
    }

    @Put
    public void store(SaxRepresentation mailRep) throws IOException {
        mailRep.parse(new DefaultHandler() {

            @Override
            public void startElement(String uri, String localName,
                    String qName, Attributes attributes) throws SAXException {
                // Output the XML element names
                if ("status".equals(localName)) {
                    System.out.print("Status: ");
                } else if ("subject".equals(localName)) {
                    System.out.print("Subject: ");
                } else if ("content".equals(localName)) {
                    System.out.print("Content: ");
                } else if ("accountRef".equals(localName)) {
                    System.out.print("Account URI: ");
                }
            }

            @Override
            public void characters(char[] ch, int start, int length)
                    throws SAXException {
                // Output the XML element values
                System.out.print(new String(ch, start, length));
            }

            @Override
            public void endElement(String uri, String localName, String qName)
                    throws SAXException {
                // Output a new line
                System.out.println();
            }

        });
    }
}
