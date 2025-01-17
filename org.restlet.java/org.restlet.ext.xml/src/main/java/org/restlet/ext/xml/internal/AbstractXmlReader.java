/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.ext.xml.internal;

import java.util.HashMap;

import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

/**
 * Abstract SAX XML Reader.
 * 
 * @author Warren Janssens
 */
public abstract class AbstractXmlReader implements XMLReader {

    /** The content handler. */
    private ContentHandler contentHandler;

    /** The entity resolver. */
    private EntityResolver entityResolver;

    /** The error handler. */
    private ErrorHandler errorHandler;

    /** The features map. */
    private final HashMap<String, Boolean> features;

    /** The DTD handler. */
    private DTDHandler handler;

    /** The properties map. */
    private final HashMap<String, Object> properties;

    /**
     * Default constructor.
     */
    public AbstractXmlReader() {
        this.features = new HashMap<String, Boolean>();
        this.properties = new HashMap<String, Object>();
        this.contentHandler = null;
        this.entityResolver = null;
        this.errorHandler = null;
        this.handler = null;
    }

    /**
     * Return the content handler.
     * 
     * @return The content handler.
     * @see XMLReader#getContentHandler()
     */
    public ContentHandler getContentHandler() {
        return contentHandler;
    }

    /**
     * Return the DTD handler.
     * 
     * @return The DTD handler.
     * @see XMLReader#getDTDHandler()
     */
    public DTDHandler getDTDHandler() {
        return handler;
    }

    /**
     * Return the entity resolver.
     * 
     * @return The entity resolver.
     * @see XMLReader#getEntityResolver()
     */
    public EntityResolver getEntityResolver() {
        return entityResolver;
    }

    /**
     * Return the error handler.
     * 
     * @return The error handler.
     * @see XMLReader#getErrorHandler()
     */
    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    /**
     * Returns the feature by name.
     * 
     * @param name
     *            The feature name.
     * @return The feature.
     * @see XMLReader#getFeature(String)
     */
    public boolean getFeature(String name) throws SAXNotRecognizedException,
            SAXNotSupportedException {
        final Boolean result = features.get(name);
        return result == null ? false : result.booleanValue();
    }

    /**
     * Returns the property by name.
     * 
     * @param name
     *            The property name.
     * @return The property.
     * @see XMLReader#getProperty(String)
     */
    public Object getProperty(String name) throws SAXNotRecognizedException,
            SAXNotSupportedException {
        return properties.get(name);
    }

    /**
     * Sets the content handler.
     * 
     * @param contentHandler
     *            The content handler.
     * @see XMLReader#setContentHandler(ContentHandler)
     */
    public void setContentHandler(ContentHandler contentHandler) {
        this.contentHandler = contentHandler;
    }

    /**
     * Sets the DTD handler.
     * 
     * @param handler
     *            The DTD handler.
     * @see XMLReader#setDTDHandler(DTDHandler)
     */
    public void setDTDHandler(DTDHandler handler) {
        this.handler = handler;
    }

    /**
     * Sets the entity resolver.
     * 
     * @param entityResolver
     *            The entity resolver.
     * @see XMLReader#setEntityResolver(EntityResolver)
     */
    public void setEntityResolver(EntityResolver entityResolver) {
        this.entityResolver = entityResolver;
    }

    /**
     * Sets the error handler.
     * 
     * @param errorHandler
     *            The error handler.
     * @see XMLReader#setErrorHandler(ErrorHandler)
     */
    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    /**
     * Sets a feature.
     * 
     * @param name
     *            The feature name.
     * @param value
     *            The feature value.
     * @see XMLReader#setFeature(String, boolean)
     */
    public void setFeature(String name, boolean value)
            throws SAXNotRecognizedException, SAXNotSupportedException {
        this.features.put(name, value);
    }

    /**
     * Sets a property.
     * 
     * @param name
     *            The property name.
     * @param value
     *            The property value.
     * @see XMLReader#setProperty(String, Object)
     */
    public void setProperty(String name, Object value)
            throws SAXNotRecognizedException, SAXNotSupportedException {
        this.properties.put(name, value);
    }

}
