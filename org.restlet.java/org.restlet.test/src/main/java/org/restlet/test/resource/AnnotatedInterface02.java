/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.resource;

import org.restlet.resource.Post;

/**
 * Annotated interface that declares a single "Post" method.
 * 
 * @author Thierry Boileau
 * 
 */
public interface AnnotatedInterface02 {

    @Post
    String accept();

}
