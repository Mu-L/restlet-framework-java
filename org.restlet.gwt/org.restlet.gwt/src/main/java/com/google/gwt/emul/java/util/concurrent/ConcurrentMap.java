/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package com.google.gwt.emul.java.util.concurrent;

import java.util.Map;

/**
 * Emulate the ConcurrentMap class, especially for the GWT module.
 * 
 * @author Thierry Boileau
 */
public interface ConcurrentMap<K, V> extends Map<K, V> {

    /**
     * If the specified key is not already associated with a value, associate it
     * with the given value.
     * 
     * @param key
     *            The key.
     * @param value
     *            The value
     */
    V putIfAbsent(K key, V value);

    /**
     * Remove entry for key only if currently mapped to given value.
     * 
     * @param key
     *            The key.
     * @param value
     *            The value.
     * @return True if the value was removed, false otherwise
     */
    boolean remove(Object key, Object value);

    /**
     * Replace entry for key only if currently mapped to some value.
     * 
     * @param key
     *            The key
     * @param value
     * @return Previous value associated with specified key, or null if there
     *         was no mapping for key. A null return can also indicate that the
     *         map previously associated null with the specified key, if the
     *         implementation supports null values.
     */
    V replace(K key, V value);

    /**
     * Replace entry for key only if currently mapped to given value.
     * 
     * @param key
     *            The key.
     * @param oldValue
     *            The current value.
     * @param newValue
     *            The new value.
     * @return True if the value was replaced
     */
    boolean replace(K key, V oldValue, V newValue);

}
