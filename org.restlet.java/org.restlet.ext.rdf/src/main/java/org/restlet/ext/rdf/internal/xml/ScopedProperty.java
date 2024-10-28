/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.ext.rdf.internal.xml;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to handle properties that have a scope such as the base URI, the
 * xml:lang property.
 * 
 * @param <E>
 *            The type of the property.
 * @author Thierry Boileau
 * @deprecated Will be removed in next major release.
 */
@Deprecated
class ScopedProperty<E> {
    private int[] depths;

    private int size;

    private List<E> values;

    /**
     * Constructor.
     */
    public ScopedProperty() {
        super();
        this.depths = new int[10];
        this.values = new ArrayList<E>();
        this.size = 0;
    }

    /**
     * Constructor.
     * 
     * @param value
     *            Value.
     */
    public ScopedProperty(E value) {
        this();
        add(value);
        incrDepth();
    }

    /**
     * Add a new value.
     * 
     * @param value
     *            The value to be added.
     */
    public void add(E value) {
        this.values.add(value);
        if (this.size == this.depths.length) {
            int[] temp = new int[2 * this.depths.length];
            System.arraycopy(this.depths, 0, temp, 0, this.depths.length);
            this.depths = temp;
        }
        this.size++;
        this.depths[size - 1] = 0;
    }

    /**
     * Decrements the depth of the current value, and remove it if necessary.
     */
    public void decrDepth() {
        if (this.size > 0) {
            this.depths[size - 1]--;
            if (this.depths[size - 1] < 0) {
                this.size--;
                this.values.remove(size);
            }
        }
    }

    /**
     * Returns the current value.
     * 
     * @return The current value.
     */
    public E getValue() {
        if (this.size > 0) {
            return this.values.get(this.size - 1);
        }
        return null;
    }

    /**
     * Increments the depth of the current value.
     */
    public void incrDepth() {
        if (this.size > 0) {
            this.depths[size - 1]++;
        }
    }
}
