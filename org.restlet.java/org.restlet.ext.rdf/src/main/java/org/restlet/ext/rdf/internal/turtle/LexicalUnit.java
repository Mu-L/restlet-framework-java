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

/**
 * Represents a lexical unit inside a Turtle document.
 * 
 * @author Thierry Boileau
 */
public abstract class LexicalUnit {

    /** The content handler of the current Turtle document. */
    private RdfTurtleReader contentReader;

    /** The context maintained during the parsing. */
    private Context context;

    /** The parsed value as a simple string of characters. */
    private String value;

    /**
     * Constructor with arguments.
     * 
     * @param contentHandler
     *            The document's parent handler.
     * @param context
     *            The parsing context.
     */
    public LexicalUnit(RdfTurtleReader contentReader, Context context) {
        super();
        this.contentReader = contentReader;
        this.context = context;
    }

    /**
     * Constructor with value.
     * 
     * @param value
     *            The value of the current lexical unit.
     */
    public LexicalUnit(String value) {
        super();
        setValue(value);
    }

    /**
     * Returns the document's reader.
     * 
     * @return The document's reader.
     */
    public RdfTurtleReader getContentReader() {
        return contentReader;
    }

    /**
     * Returns the parsing context.
     * 
     * @return The parsing context.
     */
    public Context getContext() {
        return context;
    }

    /**
     * Returns the current value.
     * 
     * @return The current value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Contains the parsing logic of this lexical unit.
     * 
     * @throws IOException
     */
    public abstract void parse() throws IOException;

    /**
     * Resolves the current value as a reference or a literal or a graph of
     * links according to the current context.
     * 
     * @return The current value as a reference or a literal or a graph of links
     *         according to the current context.
     */
    public abstract Object resolve();

    /**
     * Sets the parsing context.
     * 
     * @param context
     *            The parsing context.
     */
    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * Sets the value.
     * 
     * @param value
     *            The current value.
     */
    public void setValue(String value) {
        this.value = value;
    }

}
