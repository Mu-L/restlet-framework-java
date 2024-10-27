/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.ext.rdf.internal.n3;

import java.io.IOException;

import org.restlet.ext.rdf.internal.turtle.Context;
import org.restlet.ext.rdf.internal.turtle.LexicalUnit;

/**
 * Allows to parse a formula in RDF N3 notation. Please note that this kind of
 * feature is not supported yet.
 * 
 * @author Thierry Boileau
 */
public class FormulaToken extends LexicalUnit {

    public FormulaToken(RdfN3Reader contentHandler, Context context)
            throws IOException {
        super(contentHandler, context);
        this.parse();
    }

    @Override
    public void parse() throws IOException {
        ((RdfN3Reader) getContentReader()).parseFormula(this);
    }

    @Override
    public Object resolve() {
        org.restlet.Context.getCurrentLogger().warning(
                "Formulae are not supported yet.");
        return null;
    }
}
