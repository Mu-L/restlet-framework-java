/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.ext.gwt;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.SerializationPolicy;

/**
 * Serialization policy that allows the serialization of all the classes and
 * fields.
 * 
 * @author Jerome Louvel
 */
public class SimpleSerializationPolicy extends SerializationPolicy {

    private static final SimpleSerializationPolicy instance = new SimpleSerializationPolicy();

    /**
     * Returns the common instance of this simple policy file.
     * 
     * @return The common instance of this simple policy file.
     */
    public static SerializationPolicy getInstance() {
        return instance;
    }

    @Override
    public boolean shouldDeserializeFields(Class<?> clazz) {
        return (clazz != null);
    }

    @Override
    public boolean shouldSerializeFields(Class<?> clazz) {
        return (clazz != null);
    }

    @Override
    public void validateDeserialize(Class<?> clazz)
            throws SerializationException {
        // No validation
    }

    @Override
    public void validateSerialize(Class<?> clazz) throws SerializationException {
        // No validation
    }

}
