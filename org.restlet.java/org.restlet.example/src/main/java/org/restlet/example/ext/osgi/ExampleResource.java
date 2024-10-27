/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.ext.osgi;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 * 
 * @author Bryan Hunt
 */
public class ExampleResource extends ServerResource {
    @Get
    @Override
    public String toString() {
        return "Example";
    }
}
