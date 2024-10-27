/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.resource;

public class GenericServerResource16<E> extends
        AbstractGenericAnnotatedServerResource<E> {

    @Override
    public E addResponse(E representation) {
        return representation;
    };

}
