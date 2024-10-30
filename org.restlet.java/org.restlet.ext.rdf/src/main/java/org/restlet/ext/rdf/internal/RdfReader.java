/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.ext.rdf.internal;

import java.io.IOException;

import org.restlet.ext.rdf.GraphHandler;
import org.restlet.representation.Representation;

/**
 * Super class of all RDF readers.
 * 
 * @deprecated Will be removed in next major release.
 */
@Deprecated
public abstract class RdfReader {
    /** The graph handler invoked when parsing. */
    private GraphHandler graphHandler;

    /** The representation to read. */
    private Representation rdfRepresentation;

    /**
     * Constructor.
     * 
     * @param rdfRepresentation
     *            The representation to read.
     * @param graphHandler
     *            The graph handler invoked during the parsing.
     * @throws IOException
     */
    public RdfReader(Representation rdfRepresentation, GraphHandler graphHandler) {
        super();
        this.rdfRepresentation = rdfRepresentation;
        this.graphHandler = graphHandler;
    }

    /**
     * Returns the graph handler invoked when parsing.
     * 
     * @return The graph handler invoked when parsing.
     */
    public GraphHandler getGraphHandler() {
        return graphHandler;
    }

    /**
     * Returns the representation to read.
     * 
     * @return The representation to read.
     */
    public Representation getRdfRepresentation() {
        return rdfRepresentation;
    }

    /**
     * Parses the content.
     * 
     * @throws Exception
     */
    public abstract void parse() throws Exception;

}
