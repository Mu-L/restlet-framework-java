/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch01;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 * Simple "hello, world" server resource.
 */
public class HelloServerResource extends ServerResource {

    @Get("txt")
    public String represent() {
        return "hello, world";
    }
}
