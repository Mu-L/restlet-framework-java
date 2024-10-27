/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.ext.rdf.internal.turtle;

import java.io.IOException;

import org.restlet.data.Reference;

/**
 * Represents a URI token inside a RDF Turtle document.
 * 
 * @author Thierry Boileau
 */
public class UriToken extends LexicalUnit {
    /**
     * Constructor with arguments.
     * 
     * @param contentHandler
     *            The document's parent handler.
     * @param context
     *            The parsing context.
     */
    public UriToken(RdfTurtleReader contentHandler, Context context)
            throws IOException {
        super(contentHandler, context);
        this.parse();
    }

    @Override
    public void parse() throws IOException {
        getContentReader().parseUri(this);
    }

    @Override
    public Reference resolve() {
        return new Reference(getValue());
    }

    @Override
    public String toString() {
        return getValue();
    }
}
