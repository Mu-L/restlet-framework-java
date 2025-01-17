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
import java.io.Writer;
import java.util.Map;

import org.restlet.data.Reference;
import org.restlet.ext.rdf.Graph;
import org.restlet.ext.rdf.GraphHandler;
import org.restlet.ext.rdf.Link;
import org.restlet.ext.rdf.Literal;
import org.restlet.ext.rdf.internal.RdfConstants;
import org.restlet.ext.rdf.internal.turtle.Context;

/**
 * Handler of RDF content according to the N3 notation.
 * 
 * @author Thierry Boileau
 * @deprecated Will be removed in next major release.
 */
@Deprecated
public class RdfN3Writer extends GraphHandler {

    /** The current context object. */
    private Context context;

    /** The preceding predicate used for factorization matter. */
    private Reference precPredicate;

    /** The preceding source used for factorization matter. */
    private Reference precSource;

    /** The character writer. */
    private Writer writer;

    /** Indicates if the end of the statement is to be written. */
    private boolean writingExtraDot;

    /**
     * Constructor.
     * 
     * @param writer
     *            The character writer to write to.
     * @throws IOException
     */
    public RdfN3Writer(Writer writer) throws IOException {
        super();
        this.context = new Context();
        this.writer = writer;

        Map<String, String> prefixes = context.getPrefixes();
        prefixes.put(RdfConstants.RDF_SCHEMA.toString(), "rdf");
        prefixes.put(RdfConstants.RDF_SYNTAX.toString(), "rdfs");
        prefixes.put("http://www.w3.org/2000/10/swap/grammar/bnf#", "cfg");
        prefixes.put("http://www.w3.org/2000/10/swap/grammar/n3#", "n3");
        prefixes.put("http://www.w3.org/2000/10/swap/list#", "list");
        prefixes.put("http://www.w3.org/2000/10/swap/pim/doc#", "doc");
        prefixes.put("http://www.w3.org/2002/07/owl#", "owl");
        prefixes.put("http://www.w3.org/2000/10/swap/log#", "log");
        prefixes.put("http://purl.org/dc/elements/1.1/", "dc");
        prefixes.put("http://www.w3.org/2001/XMLSchema#", "type");

        for (String key : prefixes.keySet()) {
            this.writer.append("@prefix ").append(prefixes.get(key))
                    .append(": <").append(key).append(">.\n");
        }

        this.writer.append("@keywords a, is, of, has.\n");
    }

    @Override
    public void endGraph() throws IOException {
        this.writer.write(".\n");
        this.writer.flush();
    }

    @Override
    public void link(Graph source, Reference typeRef, Literal target) {
        try {
            this.writer.write("{");
            write(source);
            this.writer.write("} ");
            write(typeRef, this.context.getPrefixes());
            this.writer.write(" ");
            write(target);

            this.precSource = null;
            this.precPredicate = typeRef;
            this.writingExtraDot = true;
        } catch (IOException e) {
            org.restlet.Context.getCurrentLogger().warning(
                    "Cannot write the representation of a statement due to "
                            + e.getMessage());
        }
    }

    @Override
    public void link(Graph source, Reference typeRef, Reference target) {
        try {
            this.writer.write("{");
            write(source);
            this.writer.write("} ");
            write(typeRef, this.context.getPrefixes());
            this.writer.write(" ");
            write(target, this.context.getPrefixes());

            this.precSource = null;
            this.precPredicate = typeRef;
            this.writingExtraDot = true;
        } catch (IOException e) {
            org.restlet.Context.getCurrentLogger().warning(
                    "Cannot write the representation of a statement due to "
                            + e.getMessage());
        }
    }

    @Override
    public void link(Reference source, Reference typeRef, Literal target) {
        try {
            if (source.equals(this.precSource)) {
                if (typeRef.equals(this.precPredicate)) {
                    this.writer.write(", ");
                } else {
                    this.writer.write("; ");
                    write(typeRef, this.context.getPrefixes());
                    this.writer.write(" ");
                }
            } else {
                if (this.writingExtraDot) {
                    this.writer.write(".\n");
                }
                write(source, this.context.getPrefixes());
                this.writer.write(" ");
                write(typeRef, this.context.getPrefixes());
                this.writer.write(" ");
            }
            write(target);

            this.precSource = source;
            this.precPredicate = typeRef;
            this.writingExtraDot = true;
        } catch (IOException e) {
            org.restlet.Context.getCurrentLogger().warning(
                    "Cannot write the representation of a statement due to "
                            + e.getMessage());
        }
    }

    @Override
    public void link(Reference source, Reference typeRef, Reference target) {
        try {
            if (source.equals(this.precSource)) {
                if (typeRef.equals(this.precPredicate)) {
                    this.writer.write(", ");
                } else {
                    this.writer.write("; ");
                    write(typeRef, this.context.getPrefixes());
                    this.writer.write(" ");
                }
            } else {
                if (this.writingExtraDot) {
                    this.writer.write(".\n");
                }
                write(source, this.context.getPrefixes());
                this.writer.write(" ");
                write(typeRef, this.context.getPrefixes());
                this.writer.write(" ");
            }
            write(target, this.context.getPrefixes());

            this.precSource = source;
            this.precPredicate = typeRef;
            this.writingExtraDot = true;
        } catch (IOException e) {
            org.restlet.Context.getCurrentLogger().warning(
                    "Cannot write the representation of a statement due to "
                            + e.getMessage());
        }
    }

    /**
     * Write the representation of the given graph of links.
     * 
     * @param linkset
     *            the given graph of links.
     * @throws IOException
     */
    private void write(Graph linkset) throws IOException {
        for (Link link : linkset) {
            if (link.hasReferenceSource()) {
                if (link.hasReferenceTarget()) {
                    link(link.getSourceAsReference(), link.getTypeRef(),
                            link.getTargetAsReference());
                } else if (link.hasLiteralTarget()) {
                    link(link.getSourceAsReference(), link.getTypeRef(),
                            link.getTargetAsLiteral());
                } else if (link.hasLinkTarget()) {
                    // TODO Hande source as link.
                } else {
                    org.restlet.Context
                            .getCurrentLogger()
                            .warning(
                                    "Cannot write the representation of a statement due to the fact that the object is neither a Reference nor a literal.");
                }
            } else if (link.hasGraphSource()) {
                this.writingExtraDot = false;
                if (link.hasReferenceTarget()) {
                    link(link.getSourceAsGraph(), link.getTypeRef(),
                            link.getTargetAsReference());
                } else if (link.hasLiteralTarget()) {
                    link(link.getSourceAsGraph(), link.getTypeRef(),
                            link.getTargetAsLiteral());
                } else if (link.hasLinkTarget()) {
                    // TODO Handle source as link.
                } else {
                    org.restlet.Context
                            .getCurrentLogger()
                            .warning(
                                    "Cannot write the representation of a statement due to the fact that the object is neither a Reference nor a literal.");
                }
                this.writer.write(".\n");
            }
            this.precSource = link.getSourceAsReference();
            this.precPredicate = link.getTypeRef();
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
        if (literal.getValue().contains("\n")) {
            this.writer.write("\"");
            this.writer.write("\"");
            this.writer.write(literal.getValue());
            this.writer.write("\"");
            this.writer.write("\"");
        } else {
            this.writer.write(literal.getValue());
        }

        this.writer.write("\"");
        if (literal.getDatatypeRef() != null) {
            this.writer.write("^^");
            write(literal.getDatatypeRef(), context.getPrefixes());
        }
        if (literal.getLanguage() != null) {
            this.writer.write("@");
            this.writer.write(literal.getLanguage().toString());
        }
    }

    /**
     * Writes the representation of a given reference.
     * 
     * @param reference
     *            The reference to write.
     * @param prefixes
     *            The map of known namespaces.
     * @throws IOException
     */
    private void write(Reference reference, Map<String, String> prefixes)
            throws IOException {
        String uri = reference.toString();
        if (Link.isBlankRef(reference)) {
            this.writer.write(uri);
        } else {
            boolean found = false;
            for (String key : prefixes.keySet()) {
                if (uri.startsWith(key)) {
                    found = true;
                    this.writer.append(prefixes.get(key));
                    this.writer.append(":");
                    this.writer.append(uri.substring(key.length()));
                    break;
                }
            }
            if (!found) {
                this.writer.append("<");
                this.writer.append(uri);
                this.writer.append(">");
            }
        }
    }

}
