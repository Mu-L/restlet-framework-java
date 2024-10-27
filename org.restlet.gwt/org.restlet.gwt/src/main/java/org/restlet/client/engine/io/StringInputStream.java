/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.client.engine.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * Input stream in which the bytes read are supplied by the contents of a string
 * based on the default character set.
 * 
 * @author Thierry Boileau
 */
public class StringInputStream extends InputStream {

    /** The next position to read. */
    private int position;

    /** The text to stream. */
    protected String text;

    /**
     * Constructor.
     */
    public StringInputStream() {
        super();
    }

    /**
     * Constructor.
     * 
     * @param text
     */
    public StringInputStream(String text) {
        super();
        this.position = 0;
        this.text = text;
    }

    /**
     * 
     * @return
     * @throws IOException
     */
    public int available() throws IOException {
        if (text != null) {
            return text.length();
        }

        return 0;
    }

    public void close() throws IOException {

    }

    /**
     * Returns the text to stream.
     * 
     * @return The text to stream.
     */
    public String getText() {
        return text;
    }

    /**
     * Reads the next character in the source text.
     * 
     * @return The next character or -1 if end of text is reached.
     * @throws IOException
     */
    public int read() throws IOException {
        return (this.position == this.text.length()) ? -1 : this.text
                .charAt(this.position++);
    }

    /**
     * 
     * @param cbuf
     * @return
     * @throws IOException
     */
    public int read(char[] cbuf) throws IOException {
        return read(cbuf, 0, cbuf.length);
    }

    /**
     * 
     * @param cbuf
     * @param off
     * @param len
     * @return
     * @throws IOException
     */
    public int read(char[] cbuf, int off, int len) throws IOException {
        if (position >= text.length())
            return -1;
        int n = Math.min(text.length() - position, len);
        text.getChars(position, position + n, cbuf, off);
        position += n;
        return n;
    }
}
