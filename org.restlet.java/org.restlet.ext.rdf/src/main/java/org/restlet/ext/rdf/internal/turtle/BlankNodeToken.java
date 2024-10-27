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
import java.util.ArrayList;
import java.util.List;

import org.restlet.data.Reference;
import org.restlet.ext.rdf.Link;

/**
 * Represents a blank node inside a RDF N3 document. Contains all the logic to
 * parse a blank node in N3 documents.
 * 
 * @author Thierry Boileau
 */
public class BlankNodeToken extends LexicalUnit {

    /** List of lexical units contained by this blank node. */
    private List<LexicalUnit> lexicalUnits;

    /** Indicates if the given blank node has been already resolved. */
    private boolean resolved = false;

    /**
     * Constructor. The blank node is given a new identifier thanks to the
     * context.
     * 
     * @param contentHandler
     *            The parent content handler.
     * @param context
     *            The context used to resolved references.
     * @throws IOException
     */
    public BlankNodeToken(RdfTurtleReader contentHandler, Context context)
            throws IOException {
        super(contentHandler, context);
        lexicalUnits = new ArrayList<LexicalUnit>();
        this.setValue("_:" + contentHandler.newBlankNodeId());
        lexicalUnits.add(this);
        this.parse();
    }

    /**
     * Constructor
     * 
     * @param value
     *            The value of this blank node.
     */
    public BlankNodeToken(String value) {
        super(value);
        this.resolved = true;
    }

    public List<LexicalUnit> getLexicalUnits() {
        return lexicalUnits;
    }

    @Override
    public void parse() throws IOException {
        getContentReader().parseBlankNode(this);
    }

    @Override
    public Object resolve() {
        if (!this.resolved) {
            this.resolved = true;
            if (getContentReader() != null) {
                getContentReader().generateLinks(lexicalUnits);
            }
        }

        if (getValue() != null) {
            if (getValue().startsWith("_:")) {
                return new Reference(getValue());
            }

            return Link.createBlankRef(getValue());
        }

        org.restlet.Context.getCurrentLogger().warning(
                "A blank node has been detected with a null value.");

        return null;
    }
}
