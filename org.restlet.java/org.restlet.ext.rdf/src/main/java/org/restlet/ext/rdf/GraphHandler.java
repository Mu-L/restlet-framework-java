/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.ext.rdf;

import java.io.IOException;

import org.restlet.data.Reference;

/**
 * Handler for the content of a {@link Graph}. List of callbacks used when
 * parsing or writing a representation of a RDF graph.
 * @deprecated Will be removed in next major release.
 */
@Deprecated
public abstract class GraphHandler {

    /**
     * Callback method used after the graph is parsed or written. Does nothing
     * by default.
     * 
     * @throws IOException
     */
    public void endGraph() throws IOException {

    }

    /**
     * Callback method used at the end of a Namespace mapping. Does nothing by
     * default.
     * 
     * @param prefix
     *            The Namespace prefix.
     */
    public void endPrefixMapping(String prefix) {

    }

    /**
     * Callback method used when a link is parsed or written.
     * 
     * @param source
     *            The source or subject of the link.
     * @param typeRef
     *            The type reference of the link.
     * @param target
     *            The target or object of the link.
     */
    public abstract void link(Graph source, Reference typeRef, Literal target);

    /**
     * Callback method used when a link is parsed or written.
     * 
     * @param source
     *            The source or subject of the link.
     * @param typeRef
     *            The type reference of the link.
     * @param target
     *            The target or object of the link.
     */
    public abstract void link(Graph source, Reference typeRef, Reference target);

    /**
     * Callback method used when a link is parsed or written.
     * 
     * @param source
     *            The source or subject of the link.
     * @param typeRef
     *            The type reference of the link.
     * @param target
     *            The target or object of the link.
     */
    public abstract void link(Reference source, Reference typeRef,
            Literal target);

    /**
     * Callback method used when a link is parsed or written.
     * 
     * @param source
     *            The source or subject of the link.
     * @param typeRef
     *            The type reference of the link.
     * @param target
     *            The target or object of the link.
     */
    public abstract void link(Reference source, Reference typeRef,
            Reference target);

    /**
     * Callback method used before the graph is parsed or written. Does nothing
     * by default.
     * 
     * @throws IOException
     */
    public void startGraph() throws IOException {

    }

    /**
     * Callback method used at the start of a Namespace mapping. Does nothing by
     * default.
     * 
     * @param prefix
     *            The Namespace prefix being declared.
     * @param reference
     *            The Namespace URI mapped to the prefix.
     */
    public void startPrefixMapping(String prefix, Reference reference) {

    }

}
