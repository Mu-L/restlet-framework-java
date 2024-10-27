/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.ext.rdf;

import java.util.Objects;

import org.restlet.engine.util.SystemUtils;

/**
 * Relationship between two typed objects.
 * 
 * @author Jerome Louvel
 * 
 * @param <T>
 *            The first object's type.
 * @param <U>
 *            The second object's type.
 */
public class Couple<T, U> {

    /** The first object. */
    private volatile T first;

    /** The second object. */
    private volatile U second;

    /**
     * Constructor.
     * 
     * @param first
     *            The first object.
     * @param second
     *            The second object.
     */
    public Couple(T first, U second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Couple)) {
            return false;
        }

        Couple<?, ?> that = (Couple<?, ?>) other;

        return Objects.equals(getFirst(), that.getFirst())
                && Objects.equals(getSecond(), that.getSecond());
    }

    /**
     * Returns the first object.
     * 
     * @return The first object.
     */
    public T getFirst() {
        return first;
    }

    /**
     * Returns the second object.
     * 
     * @return The second object.
     */
    public U getSecond() {
        return second;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return SystemUtils.hashCode(getFirst(), getSecond());
    }

    /**
     * Sets the first object.
     * 
     * @param first
     *            The first object.
     */
    public void setFirst(T first) {
        this.first = first;
    }

    /**
     * Sets the second object.
     * 
     * @param second
     *            The second object.
     */
    public void setSecond(U second) {
        this.second = second;
    }

    @Override
    public String toString() {
        return "(" + getFirst() + "," + getSecond() + ")";
    }
}
