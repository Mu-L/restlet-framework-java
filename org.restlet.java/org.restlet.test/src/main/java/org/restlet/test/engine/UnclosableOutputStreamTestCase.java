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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.jupiter.api.Test;
import org.restlet.engine.io.UnclosableOutputStream;
import org.restlet.test.RestletTestCase;

/**
 * Unit tests for the HTTP KeepAlive.
 * 
 * @author Kevin Conaway
 */
public class UnclosableOutputStreamTestCase extends RestletTestCase {

    static class MockOutputStream extends OutputStream {
        boolean closed = false;

        @Override
        public void close() throws IOException {
            this.closed = true;
        }

        @Override
        public void write(int b) throws IOException {

        }
    }

    @Test
    public void testClose() throws IOException {
        final MockOutputStream stream = new MockOutputStream();
        final OutputStream out = new UnclosableOutputStream(stream);
        out.close();

        assertFalse(stream.closed);
        stream.close();
        assertTrue(stream.closed);
    }

    @Test
    public void testWrite() throws IOException {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        final OutputStream out = new UnclosableOutputStream(stream);

        out.write('a');
        assertEquals("a", stream.toString());

        out.write(new byte[] { 'b', 'c' });
        assertEquals("abc", stream.toString());

        out.write(new byte[] { 'd', 'e', 'f', 'g' }, 0, 2);
        assertEquals("abcde", stream.toString());

        out.close();
    }

}
