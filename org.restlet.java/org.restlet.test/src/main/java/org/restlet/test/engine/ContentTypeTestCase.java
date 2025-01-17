/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.restlet.engine.header.ContentType;
import org.restlet.test.RestletTestCase;

/**
 * Test {@link ContentType}
 * 
 * @author Jerome Louvel
 */
public class ContentTypeTestCase extends RestletTestCase {

    @Test
    public void testParsingInvalid() {
        String h1 = "application/docbook+xml; version='my version 1.0'";

        try {
            new ContentType(h1);
            fail("Shouldn't reach this point");
        } catch (IllegalArgumentException iae) {
            // OK
        }
    }

    @Test
    public void testParsing() {
        String h1 = "application/docbook+xml; version=\"my version 1.0\"";
        String h2 = "application/docbook+xml; version='my%20version%201.0'";

        ContentType ct1 = new ContentType(h1);
        ContentType ct2 = new ContentType(h2);

        assertEquals(h1, ct1.getMediaType().getName());
        assertEquals("my version 1.0", ct1.getMediaType().getParameters()
                .getFirstValue("version"));

        assertEquals(h2, ct2.getMediaType().getName());
        assertEquals("'my%20version%201.0'", ct2.getMediaType().getParameters()
                .getFirstValue("version"));
    }

}
