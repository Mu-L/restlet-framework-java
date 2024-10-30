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
 * Relationship between three typed objects.
 * 
 * @author Jerome Louvel
 * 
 * @param <T>
 *            The first object's type.
 * @param <U>
 *            The second object's type.
 * @param <V>
 *            The third object's type.
 * @deprecated Will be removed in next major release.
 */
@Deprecated
public class Triple<T, U, V> {

    /** The first object. */
    private volatile T first;

    /** The second object. */
    private volatile U second;

    /** The third object. */
    private volatile V third;

    /**
     * Constructor.
     * 
     * @param first
     *            The first object.
     * @param second
     *            The second object.
     * @param third
     *            The third object.
     */
    public Triple(T first, U second, V third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Triple)) {
            return false;
        }

        Triple<?, ?, ?> that = (Triple<?, ?, ?>) other;

        return Objects.equals(getFirst(), that.getFirst())
                && Objects.equals(getSecond(), that.getSecond())
                && Objects.equals(getThird(), that.getThird());
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

    /**
     * Returns the third object.
     * 
     * @return The third object.
     */
    public V getThird() {
        return third;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return SystemUtils.hashCode(getFirst(), getSecond(), getThird());
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

    /**
     * Sets the third object.
     * 
     * @param third
     *            The third object.
     */
    public void setThird(V third) {
        this.third = third;
    }

    @Override
    public String toString() {
        return "(" + getFirst() + "," + getSecond() + "," + getThird() + ")";
    }
}
