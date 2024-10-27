/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package com.google.gwt.emul.java.util.concurrent;

import java.util.TreeMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Emulate the ConcurrentHashMap class, especially for the GWT module.
 * 
 * @author Thierry Boileau
 */
public class ConcurrentHashMap<K, V> extends TreeMap<K, V> implements
        ConcurrentMap<K, V> {

    /** */
    private static final long serialVersionUID = 1L;

    public V putIfAbsent(K key, V value) {
        if (!containsKey(key)) {
            return put(key, value);
        } else {
            return get(key);
        }

    }

    public boolean remove(Object key, Object value) {
        boolean result = false;
        if (containsKey(key) && get(key).equals(value)) {
            remove(key);
            result = true;
        }
        return result;
    }

    public V replace(K key, V value) {
        V result = null;

        if (containsKey(key)) {
            result = put(key, value);
        }

        return result;
    }

    public boolean replace(K key, V oldValue, V newValue) {
        boolean result = false;

        if (containsKey(key) && get(key).equals(oldValue)) {
            put(key, newValue);
            result = true;
        }

        return result;
    }

}
