/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.ext.odata.internal.edm;

import org.restlet.engine.util.SystemUtils;
import org.restlet.ext.odata.internal.reflect.ReflectUtils;

/**
 * Base class for all EDM concepts that have a name.
 * 
 * @author Thierry Boileau
 */
public class NamedObject {

    /** The name of the EDM concept. */
    private final String name;

    /** The name's value as a valid Java identifier. */
    private final String normalizedName;

    /**
     * Constructor.
     * 
     * @param name
     *            The name of the entity.
     */
    public NamedObject(String name) {
        super();
        this.name = name;
        this.normalizedName = ReflectUtils.normalize(name);
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof NamedObject) {
            NamedObject object = (NamedObject) obj;
            result = object.getName().equals(this.name);
        }
        return result;
    }

    /**
     * Returns the name.
     * 
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the name following the the java naming rules.
     * 
     * @see <a
     *      href="http://java.sun.com/docs/books/jls/second_edition/html/lexical.doc.html#40625">
     *      Identifiers</a>
     * @return The name following the the java naming rules.
     */
    public String getNormalizedName() {
        return normalizedName;
    }

    @Override
    public int hashCode() {
        return SystemUtils.hashCode(this.name);
    }

    @Override
    public String toString() {
        return this.getClass() + " " + this.name;
    }

}
