/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch07.sec1.sub3;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class HelloServerResource extends ServerResource {

    @Get("html")
    public String toHtml() {
        return "<html><body>hello, world</body></html>";
    }

    @Get("xml")
    public String toXml() {
        return "<txt>hello, world</txt>";
    }

    @Get("json")
    public String toJson() {
        return "{txt: \"hello, world\"}";
    }
}
