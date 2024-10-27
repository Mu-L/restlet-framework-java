/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch02.sec5.sub4;

import org.restlet.resource.ServerResource;

/**
 * Implementing the Java annotated resource interface.
 */
public class RootServerResource extends ServerResource implements RootResource {

    public String represent() {
        return "This is the root resource";
    }

    public String describe() {
        throw new RuntimeException("Not yet implemented");
    }

}
