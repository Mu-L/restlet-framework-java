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

import org.restlet.ext.rdf.Literal;
import org.restlet.ext.rdf.internal.RdfConstants;

/**
 * Represents a still unidentified Turtle token.
 * 
 * @author Thierry Boileau
 * @deprecated Will be removed in next major release.
 */
@Deprecated
public class Token extends LexicalUnit {

    /**
     * Constructor with arguments.
     * 
     * @param contentHandler
     *            The document's parent handler.
     * @param context
     *            The parsing context.
     */
    public Token(RdfTurtleReader contentHandler, Context context)
            throws IOException {
        super(contentHandler, context);
        this.parse();
    }

    /**
     * Constructor with value.
     * 
     * @param value
     *            The value of the current lexical unit.
     */
    public Token(String value) {
        super(value);
    }

    @Override
    public void parse() throws IOException {
        getContentReader().parseToken(this);
    }

    @Override
    public Object resolve() {
        Object result = null;
        if ((getContext() != null) && getContext().isQName(getValue())) {
            result = (getContext() != null) ? getContext().resolve(getValue())
                    : getValue();
        } else {
            // Must be a literal
            if (getValue().charAt(0) > '0' && getValue().charAt(0) < '9') {
                if (getValue().contains(".")) {
                    // Consider it as a float
                    result = new Literal(getValue(),
                            RdfConstants.XML_SCHEMA_TYPE_FLOAT);
                } else {
                    // Consider it as an integer
                    result = new Literal(getValue(),
                            RdfConstants.XML_SCHEMA_TYPE_INTEGER);
                }
            } else {
                org.restlet.Context.getCurrentLogger().warning(
                        "Cannot identify this token value: " + getValue());
                if (getContentReader() != null) {
                    org.restlet.Context.getCurrentLogger().warning(
                            getContentReader().getParsingMessage());
                }
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return getValue();
    }

}
