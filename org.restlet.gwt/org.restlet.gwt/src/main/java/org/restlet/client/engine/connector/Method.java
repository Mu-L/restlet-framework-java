/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.client.engine.connector;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.restlet.client.resource.Delete;
import org.restlet.client.resource.Get;
import org.restlet.client.resource.Options;
import org.restlet.client.resource.Patch;
import org.restlet.client.resource.Post;
import org.restlet.client.resource.Put;

/**
 * Meta annotation to declare method annotations.
 * 
 * @see Get
 * @see Post
 * @see Put
 * @see Delete
 * @see Options
 * @see Patch
 * @author Jerome Louvel
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Method {

    /**
     * Method name identified by the underlying annotation.
     */
    String value();

}
