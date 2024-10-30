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
import org.restlet.ext.rdf.internal.RdfConstants;

/**
 * Represents a list of Turtle tokens.
 * 
 * @author Thierry Boileau
 * @deprecated Will be removed in next major release.
 */
@Deprecated
public class ListToken extends LexicalUnit {
    /** The list of contained tokens. */
    List<LexicalUnit> lexicalUnits;

    /**
     * Constructor with arguments.
     * 
     * @param contentHandler
     *            The document's parent handler.
     * @param context
     *            The parsing context.
     */
    public ListToken(RdfTurtleReader contentHandler, Context context)
            throws IOException {
        super(contentHandler, context);
        lexicalUnits = new ArrayList<LexicalUnit>();
        this.parse();
    }

    public List<LexicalUnit> getLexicalUnits() {
        return lexicalUnits;
    }

    @Override
    public String getValue() {
        return lexicalUnits.toString();
    }

    @Override
    public void parse() throws IOException {
        getContentReader().parseList(this);
    }

    @Override
    public Object resolve() {
        Reference currentBlankNode = (Reference) new BlankNodeToken(
                getContentReader().newBlankNodeId()).resolve();
        for (LexicalUnit lexicalUnit : lexicalUnits) {
            Object element = lexicalUnit.resolve();

            if (element instanceof Reference) {
                getContentReader().link(currentBlankNode,
                        RdfConstants.LIST_FIRST, element);
            } else if (element instanceof String) {
                getContentReader().link(currentBlankNode,
                        RdfConstants.LIST_FIRST,
                        new Reference((String) element));
            } else {
                org.restlet.Context
                        .getCurrentLogger()
                        .warning(
                                "The list contains an element which is neither a Reference nor a literal.");
            }

            Reference restBlankNode = (Reference) new BlankNodeToken(
                    getContentReader().newBlankNodeId()).resolve();

            getContentReader().link(currentBlankNode, RdfConstants.LIST_REST,
                    restBlankNode);
            currentBlankNode = restBlankNode;
        }
        getContentReader().link(currentBlankNode, RdfConstants.LIST_REST,
                RdfConstants.OBJECT_NIL);

        return currentBlankNode;
    }
}
