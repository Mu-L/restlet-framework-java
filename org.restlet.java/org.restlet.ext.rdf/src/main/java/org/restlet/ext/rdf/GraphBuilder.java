/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.ext.rdf;

import org.restlet.data.Reference;

/**
 * Graph handler used when parsing an RDF representation. It completes the inner
 * set of links with all detected ones.
 * @deprecated Will be removed in next major release.
 */
@Deprecated
public class GraphBuilder extends GraphHandler {

    /** The inner graph of links. */
    private Graph linkSet;

    /**
     * 
     * @param linkSet
     *            The graph of links.
     */
    public GraphBuilder(Graph linkSet) {
        super();
        this.linkSet = linkSet;
    }

    @Override
    public void link(Graph source, Reference typeRef, Literal target) {
        if (source != null && typeRef != null && target != null) {
            this.linkSet.add(source, typeRef, target);
        }
    }

    @Override
    public void link(Graph source, Reference typeRef, Reference target) {
        if (source != null && typeRef != null && target != null) {
            this.linkSet.add(source, typeRef, target);
        }
    }

    @Override
    public void link(Reference source, Reference typeRef, Literal target) {
        if (source != null && typeRef != null && target != null) {
            this.linkSet.add(source, typeRef, target);
        }
    }

    @Override
    public void link(Reference source, Reference typeRef, Reference target) {
        if (source != null && typeRef != null && target != null) {
            this.linkSet.add(source, typeRef, target);
        }
    }

}
