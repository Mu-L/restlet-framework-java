/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.client.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Map wrapper. Modifiable map that delegates all methods to a wrapped map. This
 * allows an easy subclassing.
 * 
 * @author Jerome Louvel
 * @see <a href="http://c2.com/cgi/wiki?DecoratorPattern">The decorator (aka
 *      wrapper) pattern</a>
 * @see java.util.Collections
 * @see java.util.List
 */
public class WrapperMap<K, V> implements Map<K, V> {
    /** The delegate map. */
    private final Map<K, V> delegate;

    /**
     * Constructor.
     */
    public WrapperMap() {
        this.delegate = new ConcurrentHashMap<K, V>();
    }

    /**
     * Constructor.
     * 
     * @param delegate
     *            The delegate list.
     */
    public WrapperMap(Map<K, V> delegate) {
        this.delegate = delegate;
    }

    /**
     * Removes all mappings from this Map.
     */
    public void clear() {
        getDelegate().clear();
    }

    /**
     * Returns true if this map contains a mapping for the specified key.
     * 
     * @param key
     *            The key to look up.
     * @return True if this map contains a mapping for the specified key.
     */
    public boolean containsKey(Object key) {
        return getDelegate().containsKey(key);
    }

    /**
     * Returns true if this map maps one or more keys to the specified value.
     * 
     * @param value
     *            The value to look up
     * @return True if this map maps one or more keys to the specified value.
     */
    public boolean containsValue(Object value) {
        return getDelegate().containsValue(value);
    }

    /**
     * Returns a set view of the mappings contained in this map.
     * 
     * @return A set view of the mappings contained in this map.
     */
    public Set<Entry<K, V>> entrySet() {
        return getDelegate().entrySet();
    }

    /**
     * Compares the specified object with this map for equality.
     * 
     * @param o
     *            Object to be compared for equality with this map.
     * @return True if the specified object is equal to this map.
     */
    @Override
    public boolean equals(Object o) {
        return getDelegate().equals(o);
    }

    /**
     * Returns the value to which this map maps the specified key.
     * 
     * @param key
     *            Key whose associated value is to be returned.
     * @return The value to which this map maps the specified key, or null if
     *         the map contains no mapping for this key.
     */
    public V get(Object key) {
        return getDelegate().get(key);
    }

    /**
     * Returns the delegate list.
     * 
     * @return The delegate list.
     */
    protected Map<K, V> getDelegate() {
        return this.delegate;
    }

    /**
     * Returns the hash code value for this map.
     * 
     * @return The hash code value for this map.
     */
    @Override
    public int hashCode() {
        return getDelegate().hashCode();
    }

    /**
     * Returns true if this map contains no key-value mappings.
     * 
     * @return True if this map contains no key-value mappings.
     */
    public boolean isEmpty() {
        return getDelegate().isEmpty();
    }

    /**
     * Returns a set view of the keys contained in this map.
     * 
     * @return A set view of the keys contained in this map.
     */
    public Set<K> keySet() {
        return getDelegate().keySet();
    }

    /**
     * Associates the specified value with the specified key in this map
     * (optional operation).
     * 
     * @param key
     *            Key with which the specified value is to be associated.
     * @param value
     *            Value to be associated with the specified key.
     * @return The previous value associated with specified key, or null if
     *         there was no mapping for key. A null return can also indicate
     *         that the map previously associated null with the specified key,
     *         if the implementation supports null values.
     */
    public V put(K key, V value) {
        return getDelegate().put(key, value);
    }

    /**
     * Copies all of the mappings from the specified map to this map (optional
     * operation).
     * 
     * @param t
     *            Mappings to be stored in this map.
     */
    public void putAll(Map<? extends K, ? extends V> t) {
        getDelegate().putAll(t);
    }

    /**
     * Removes the mapping for this key from this map if it is present (optional
     * operation).
     * 
     * @param key
     *            Key whose mapping is to be removed from the map.
     * @return The previous value associated with specified key, or null if
     *         there was no mapping for key.
     */
    public V remove(Object key) {
        return getDelegate().remove(key);
    }

    /**
     * Returns the number of key-value mappings in this map.
     * 
     * @return The number of key-value mappings in this map.
     */
    public int size() {
        return getDelegate().size();
    }

    /**
     * Returns a collection view of the values contained in this map.
     * 
     * @return A collection view of the values contained in this map.
     */
    public Collection<V> values() {
        return getDelegate().values();
    }

}
