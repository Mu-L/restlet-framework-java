/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.ext.rdf;

import org.restlet.data.Language;
import org.restlet.data.Reference;

/**
 * Literal as defined by RDF. Composed of the literal value, optional datatype
 * reference and language properties.
 * 
 * @author Jerome Louvel
 * @see <a href="http://www.w3.org/TR/rdf-concepts/#section-Graph-Literal">RDF
 *      literals</a>
 * @deprecated Will be removed in next major release.
 */
@Deprecated
public class Literal {

    /** The optional datatype reference. */
    private Reference datatypeRef;

    /** The optional language. */
    private Language language;

    /** The value. */
    private String value;

    /**
     * Constructor.
     * 
     * @param value
     *            The value.
     */
    public Literal(String value) {
        this(value, null, null);
    }

    /**
     * Constructor.
     * 
     * @param value
     *            The value.
     * @param datatypeRef
     *            The optional datatype reference.
     */
    public Literal(String value, Reference datatypeRef) {
        this(value, datatypeRef, null);
    }

    /**
     * Constructor.
     * 
     * @param value
     *            The value.
     * @param datatypeRef
     *            The optional datatype reference.
     * @param language
     *            The optional language.
     */
    public Literal(String value, Reference datatypeRef, Language language) {
        this.value = value;
        this.datatypeRef = datatypeRef;
        this.language = language;
    }

    /**
     * Returns the optional datatype reference.
     * 
     * @return The datatype reference or null.
     */
    public Reference getDatatypeRef() {
        return datatypeRef;
    }

    /**
     * Returns the optional language.
     * 
     * @return The language or null.
     */
    public Language getLanguage() {
        return language;
    }

    /**
     * Returns the value.
     * 
     * @return The value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Indicates if the literal is plain. Plain literals have a value and an
     * optional language tag.
     * 
     * @return True if the literal is plain.
     */
    public boolean isPlain() {
        return (getValue() != null) && (getDatatypeRef() == null);
    }

    /**
     * Indicates if the literal is types. Typed literals have a value and a
     * datatype reference.
     * 
     * @return True if the literal is typed.
     */
    public boolean isTyped() {
        return (getValue() != null) && (getDatatypeRef() != null);
    }

    /**
     * Sets the datatype reference.
     * 
     * @param datatypeRef
     *            The datatype reference.
     */
    public void setDatatypeRef(Reference datatypeRef) {
        this.datatypeRef = datatypeRef;
    }

    /**
     * Sets the language.
     * 
     * @param language
     *            The language.
     */
    public void setLanguage(Language language) {
        this.language = language;
    }

    /**
     * Sets the value.
     * 
     * @param value
     *            The value.
     */
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return getValue();
    }
}
