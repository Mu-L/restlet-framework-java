/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch07.sec4.sub1;

import java.io.IOException;
import java.io.Writer;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.WriterRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 * Resource that generates content dynamically.
 */
public class DynamicContentServerResource extends ServerResource {

    @Get
    public Representation getDynamicContent() {
        // Inline sub class of WriterRepresentation that writes
        // its dynamic content.
        Representation result = new WriterRepresentation(MediaType.TEXT_PLAIN) {

            @Override
            public void write(Writer writer) throws IOException {
                for (int i = 0; i < 10000; i++) {
                    writer.append("0123456789\n");
                }
            }
        };

        return result;
    }

}
