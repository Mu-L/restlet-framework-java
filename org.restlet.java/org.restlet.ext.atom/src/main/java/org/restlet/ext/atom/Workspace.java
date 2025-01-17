/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.ext.atom;

import static org.restlet.ext.atom.Feed.ATOM_NAMESPACE;
import static org.restlet.ext.atom.Service.APP_NAMESPACE;

import java.util.ArrayList;
import java.util.List;

import org.restlet.data.Reference;
import org.restlet.ext.xml.XmlWriter;
import org.xml.sax.SAXException;

/**
 * Workspace containing collections of members entries.
 * 
 * @author Jerome Louvel
 */
public class Workspace {

    /**
     * The base reference used to resolve relative references found within the
     * scope of the xml:base attribute.
     */
    private volatile Reference baseReference;

    /**
     * The list of collections.
     */
    private volatile List<Collection> collections;

    /**
     * The parent service.
     */
    private volatile Service service;

    /**
     * The title.
     */
    private volatile String title;

    /**
     * Constructor.
     * 
     * @param service
     *            The parent service.
     */
    public Workspace(Service service) {
        this(service, null);
    }

    /**
     * Constructor.
     * 
     * @param service
     *            The parent service.
     * @param title
     *            The title.
     */
    public Workspace(Service service, String title) {
        this.service = service;
        this.title = title;
    }

    /**
     * Returns the base reference used to resolve relative references found
     * within the scope of the xml:base attribute.
     * 
     * @return The base reference used to resolve relative references found
     *         within the scope of the xml:base attribute.
     */
    public Reference getBaseReference() {
        return baseReference;
    }

    /**
     * Returns the list of collections.
     * 
     * @return The list of collections.
     */
    public List<Collection> getCollections() {
        if (this.collections == null) {
            this.collections = new ArrayList<Collection>();
        }

        return this.collections;
    }

    /**
     * Returns the parent service.
     * 
     * @return The parent service.
     */
    public Service getService() {
        return this.service;
    }

    /**
     * Returns the title.
     * 
     * @return The title.
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Sets the base reference used to resolve relative references found within
     * the scope of the xml:base attribute.
     * 
     * @param baseReference
     *            The base reference used to resolve relative references found
     *            within the scope of the xml:base attribute.
     */
    public void setBaseReference(Reference baseReference) {
        this.baseReference = baseReference;
    }

    /**
     * Sets the parent service.
     * 
     * @param service
     *            The parent service.
     */
    public void setService(Service service) {
        this.service = service;
    }

    /**
     * Sets the title.
     * 
     * @param title
     *            The title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Writes the current object as an XML element using the given SAX writer.
     * 
     * @param writer
     *            The SAX writer.
     * @throws SAXException
     */
    public void writeElement(XmlWriter writer) throws SAXException {
        writer.startElement(APP_NAMESPACE, "workspace");

        if (getTitle() != null) {
            writer.dataElement(ATOM_NAMESPACE, "title", getTitle());
        }

        for (final Collection collection : getCollections()) {
            collection.writeElement(writer);
        }

        writer.endElement(APP_NAMESPACE, "workspace");
    }

}
