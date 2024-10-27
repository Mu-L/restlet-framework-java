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
public class HelloResource extends ServerResource {
    @Get("txt")
    public String sayHello() {
        return "Hello RESTful World";
    }

    @Get("html")
    public String getDocument() {
        StringBuilder html = new StringBuilder();
        html.append("<html>\n");
        html.append("  <body>\n");
        html.append("    <h2>Hello RESTful World</h2>\n");
        html.append("   </body>\n");
        html.append("</html>\n");
        return html.toString();
    }
}
