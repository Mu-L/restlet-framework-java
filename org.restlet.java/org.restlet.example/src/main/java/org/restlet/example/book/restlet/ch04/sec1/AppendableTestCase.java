/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch04.sec1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.junit.jupiter.api.Test;
import org.restlet.engine.io.IoUtils;
import org.restlet.representation.AppendableRepresentation;

/**
 * Test the appendable representation.
 */
public class AppendableTestCase {

    @Test
    public void testAppendable() throws IOException {
        // Create the representation
        AppendableRepresentation ar = new AppendableRepresentation();
        ar.append("abcd");
        ar.append("1234");

        // Get its content as text
        assertEquals("abcd1234", ar.getText());

        // Append a new line character
        ar.append('\n');

        // Write its content to the console's output stream
        ar.write(System.out);

        // Copy its content as an input stream to the console
        IoUtils.copy(ar.getStream(), System.out);

        // Write its content to the console's writer
        Writer writer = new OutputStreamWriter(System.out);
        ar.write(writer);

        // Copy its content as a reader to the console
        IoUtils.copy(ar.getReader(), writer);
    }

}
