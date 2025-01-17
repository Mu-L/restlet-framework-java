/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.client.ext.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import org.restlet.client.data.MediaType;
import org.restlet.client.engine.io.StringInputStream;
import org.restlet.client.representation.Representation;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.XMLParser;

/**
 * XML representation based on an XML DOM document. DOM is a standard XML object
 * model defined by the W3C.
 * 
 * @author Jerome Louvel
 */
public class DomRepresentation extends Representation {
    /** The wrapped DOM document. */
    private Document dom;

    /** The source XML representation. */
    private Representation xmlRepresentation;

    /**
     * Constructor for an empty document.
     * 
     * @param mediaType
     *            The representation's media type.
     */
    public DomRepresentation(MediaType mediaType) {
        super(mediaType);
        this.dom = XMLParser.createDocument();
    }

    /**
     * Constructor from an existing DOM document.
     * 
     * @param mediaType
     *            The representation's media type.
     * @param xmlDocument
     *            The source DOM document.
     */
    public DomRepresentation(MediaType mediaType, Document xmlDocument) {
        super(mediaType);
        this.dom = xmlDocument;
    }

    /**
     * Constructor.
     * 
     * @param xmlRepresentation
     *            A source XML representation to parse.
     */
    public DomRepresentation(Representation xmlRepresentation) {
        super((xmlRepresentation == null) ? null : xmlRepresentation
                .getMediaType());
        this.xmlRepresentation = xmlRepresentation;
    }

    /**
     * Returns the wrapped DOM document. If no document is defined yet, it
     * attempts to parse the XML representation eventually given at construction
     * time. Otherwise, it just creates a new document.
     * 
     * @return The wrapped DOM document.
     * @throws IOException
     */
    public Document getDocument() throws IOException {
        if (this.dom == null) {
            if (this.xmlRepresentation != null) {
                this.dom = XMLParser.parse(this.xmlRepresentation.getText());
            } else {
                this.dom = XMLParser.createDocument();
            }
        }

        return this.dom;
    }

    @Override
    public StringReader getReader() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InputStream getStream() throws IOException {
        return new StringInputStream(getText());
    }

    @Override
    public String getText() throws IOException {
        return (getDocument() != null) ? getDocument().toString() : null;
    }

    /**
     * Releases the wrapped DOM document and the source XML representation if
     * they have been defined.
     */
    @Override
    public void release() {
        setDocument(null);

        if (this.xmlRepresentation != null) {
            this.xmlRepresentation.release();
        }

        super.release();
    }

    /**
     * Sets the wrapped DOM document.
     * 
     * @param dom
     *            The wrapped DOM document.
     */
    public void setDocument(Document dom) {
        this.dom = dom;
    }
}
