/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.engine;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;
import org.restlet.engine.io.UnclosableInputStream;
import org.restlet.test.RestletTestCase;

/**
 * Unit tests for the HTTP KeepAlive.
 *
 * @author Kevin Conaway
 */
public class UnclosableInputStreamTestCase extends RestletTestCase {

    static class MockInputStream extends InputStream {
        boolean closed = false;

        @Override
        public void close() throws IOException {
            this.closed = true;
        }

        @Override
        public int read() throws IOException {
            return -1;
        }
    }

    @Test
    public void testClose() throws IOException {
        final MockInputStream mock = new MockInputStream();
        final InputStream keepalive = new UnclosableInputStream(mock);

        keepalive.close();
        assertFalse(mock.closed);
        mock.close();
        assertTrue(mock.closed);
    }
}
