/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch05.sec5;

import org.restlet.representation.DigesterRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 * Returns a textual representation with its MD5 digest metadata.
 */
public class VerifiedServerResource extends ServerResource {

    @Get
    public Representation represent() throws Exception {
        // Wraps the StringRepresentation
        DigesterRepresentation result = new DigesterRepresentation(
                new StringRepresentation("hello, world"));
        result.exhaust();

        // Compute representation's digest.
        result.setDigest(result.computeDigest());
        return result;
    }
}
