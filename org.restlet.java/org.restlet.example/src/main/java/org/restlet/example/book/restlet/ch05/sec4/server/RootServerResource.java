/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch05.sec4.server;

import org.restlet.example.book.restlet.ch02.sec5.sub5.common.RootResource;
import org.restlet.resource.ServerResource;

/**
 * Root resource implementation.
 */
public class RootServerResource extends ServerResource implements RootResource {

    public String represent() {
        return "Welcome to the " + getApplication().getName() + " !";
    }

}
