/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.ext.rdf.internal.xml;

import java.io.IOException;

import org.restlet.ext.rdf.GraphHandler;
import org.restlet.ext.rdf.internal.RdfReader;
import org.restlet.ext.xml.SaxRepresentation;
import org.restlet.representation.Representation;

/**
 * Handler of RDF content according to the RDF/XML format.
 * 
 * @author Thierry Boileau
 */
public class RdfXmlReader extends RdfReader {

    /**
     * Constructor.
     * 
     * @param rdfRepresentation
     *            The representation to read.
     * @param graphHandler
     *            The graph handler invoked during the parsing.
     * @throws IOException
     */
    public RdfXmlReader(Representation rdfRepresentation,
            GraphHandler graphHandler) {
        super(rdfRepresentation, graphHandler);
    }

    /**
     * Parses the current representation.
     * 
     * @throws Exception
     */
    public void parse() throws IOException {
        SaxRepresentation saxRepresentation;
        if (getRdfRepresentation() instanceof SaxRepresentation) {
            saxRepresentation = (SaxRepresentation) getRdfRepresentation();
        } else {
            saxRepresentation = new SaxRepresentation(getRdfRepresentation());
            // Transmit the identifier used as a base for the resolution of
            // relative URIs.
            saxRepresentation.setLocationRef(getRdfRepresentation()
                    .getLocationRef());
        }

        saxRepresentation.parse(new ContentReader(saxRepresentation,
                getGraphHandler()));
    }

}
