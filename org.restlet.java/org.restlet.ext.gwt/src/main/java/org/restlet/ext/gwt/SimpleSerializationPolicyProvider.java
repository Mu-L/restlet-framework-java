/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.ext.gwt;

import com.google.gwt.user.server.rpc.SerializationPolicy;
import com.google.gwt.user.server.rpc.SerializationPolicyProvider;

/**
 * Serialization policy provider that return the
 * {@link SimpleSerializationPolicy} default instance all the time.
 * 
 * @author Jerome Louvel
 * @deprecated Will be removed in next major release.
 */
@Deprecated
public class SimpleSerializationPolicyProvider implements
        SerializationPolicyProvider {

    /**
     * Simple policy provider that always returns
     * {@link SimpleSerializationPolicy#getInstance()}.
     */
    public SerializationPolicy getSerializationPolicy(String moduleBaseURL,
            String serializationPolicyStrongName) {
        return SimpleSerializationPolicy.getInstance();
    }

}
