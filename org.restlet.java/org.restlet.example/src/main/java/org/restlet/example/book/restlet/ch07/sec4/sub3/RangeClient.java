/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch07.sec4.sub3;

import org.restlet.data.Range;
import org.restlet.resource.ClientResource;

public class RangeClient {

    public static void main(String[] args) throws Exception {
        ClientResource resource = new ClientResource("http://localhost:8111/");

        // Requesting the first five characters.
        resource.getRanges().add(new Range(0, 5));

        // Get the representation of the resource
        resource.get().write(System.out);
    }

}
