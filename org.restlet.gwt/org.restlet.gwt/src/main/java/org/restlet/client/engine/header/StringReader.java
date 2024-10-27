/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.client.engine.header;

import java.io.IOException;
import java.util.Collection;

import org.restlet.client.data.Header;

/**
 * String header reader.
 * 
 * @author Manuel Boillod
 */
public class StringReader extends HeaderReader<String> {

    /**
     * Adds values to the given collection.
     * 
     * @param header
     *            The header to read.
     * @param collection
     *            The collection to update.
     */
    public static void addValues(Header header, Collection<String> collection) {
        new StringReader(header.getValue()).addValues(collection);
    }

    /**
     * Constructor.
     * 
     * @param header
     *            The header to read.
     */
    public StringReader(String header) {
        super(header);
    }

    @Override
    public String readValue() throws IOException {
        return readToken();
    }

}
