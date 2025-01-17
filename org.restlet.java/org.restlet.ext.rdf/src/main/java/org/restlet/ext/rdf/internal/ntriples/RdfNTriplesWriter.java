/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.ext.rdf.internal.ntriples;

import java.io.IOException;
import java.io.Writer;

import org.restlet.data.Reference;
import org.restlet.ext.rdf.Graph;
import org.restlet.ext.rdf.GraphHandler;
import org.restlet.ext.rdf.Link;
import org.restlet.ext.rdf.Literal;

/**
 * Handler of RDF content according to the N-Triples notation.
 * 
 * @author Thierry Boileau
 * @deprecated Will be removed in next major release.
 */
@Deprecated
public class RdfNTriplesWriter extends GraphHandler {

    /** The character writer. */
    private Writer writer;

    /**
     * Constructor.
     * 
     * @param writer
     *            The character writer.
     * @throws IOException
     */
    public RdfNTriplesWriter(Writer writer) throws IOException {
        super();
        this.writer = writer;
    }

    @Override
    public void endGraph() throws IOException {
        this.writer.flush();
    }

    @Override
    public void link(Graph source, Reference typeRef, Literal target) {
        org.restlet.Context.getCurrentLogger().warning(
                "Subjects as Graph are not supported in N-Triples.");
    }

    @Override
    public void link(Graph source, Reference typeRef, Reference target) {
        org.restlet.Context.getCurrentLogger().warning(
                "Subjects as Graph are not supported in N-Triples.");
    }

    @Override
    public void link(Reference source, Reference typeRef, Literal target) {
        try {
            write(source);
            this.writer.write(" ");
            write(typeRef);
            this.writer.write(" ");
            write(target);
            this.writer.write(".\n");
        } catch (IOException e) {
            org.restlet.Context.getCurrentLogger().warning(
                    "Cannot write the representation of a statement due to: "
                            + e.getMessage());
        }
    }

    @Override
    public void link(Reference source, Reference typeRef, Reference target) {
        try {
            write(source);
            this.writer.write(" ");
            write(typeRef);
            this.writer.write(" ");
            write(target);
            this.writer.write(".\n");
        } catch (IOException e) {
            org.restlet.Context.getCurrentLogger().warning(
                    "Cannot write the representation of a statement due to: "
                            + e.getMessage());
        }
    }

    /**
     * Writes the representation of a literal.
     * 
     * @param literal
     *            The literal to write.
     * @throws IOException
     */
    private void write(Literal literal) throws IOException {
        // Write it as a string
        this.writer.write("\"");
        this.writer.write(literal.getValue());
        this.writer.write("\"");
    }

    /**
     * Writes the representation of a given reference.
     * 
     * @param reference
     *            The reference to write.
     * @throws IOException
     */
    private void write(Reference reference) throws IOException {
        String uri = reference.toString();
        if (Link.isBlankRef(reference)) {
            this.writer.write(uri);
        } else {
            this.writer.append("<");
            this.writer.append(uri);
            this.writer.append(">");
        }
    }

}
