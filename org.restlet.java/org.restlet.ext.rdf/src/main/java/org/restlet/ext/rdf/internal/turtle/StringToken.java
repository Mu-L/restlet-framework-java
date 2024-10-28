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

import org.restlet.data.Language;
import org.restlet.ext.rdf.Literal;

/**
 * Represents a string of characters. This string could have a type and a
 * language.
 * 
 * @author Thierry Boileau
 * @deprecated Will be removed in next major release.
 */
@Deprecated
public class StringToken extends LexicalUnit {
    /** The language of the value. */
    private String language;

    /** Does this string contains at least a new line character? */
    private boolean multiLines;

    /** The type of the represented value. */
    private String type;

    /**
     * Constructor with arguments.
     * 
     * @param contentHandler
     *            The document's parent handler.
     * @param context
     *            The parsing context.
     */
    public StringToken(RdfTurtleReader contentHandler, Context context)
            throws IOException {
        super(contentHandler, context);
        multiLines = false;
        this.parse();
    }

    public String getLanguage() {
        return language;
    }

    public String getType() {
        return type;
    }

    /**
     * Returns true if this string of characters contains at least one newline
     * character.
     * 
     * @return
     */
    public boolean isMultiLines() {
        return multiLines;
    }

    @Override
    public void parse() throws IOException {
        getContentReader().parseString(this);
    }

    @Override
    public Object resolve() {
        Literal result = new Literal(getValue());
        if (this.type != null) {
            result.setDatatypeRef(getContext().resolve(this.type));
        }
        if (this.language != null) {
            result.setLanguage(Language.valueOf(this.language));
        }
        return result;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setMultiLines(boolean multiLines) {
        this.multiLines = multiLines;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return getValue();
    }

}
