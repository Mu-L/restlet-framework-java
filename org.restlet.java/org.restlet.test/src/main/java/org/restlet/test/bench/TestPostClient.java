/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.bench;

import org.restlet.data.MediaType;
import org.restlet.representation.FileRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

public class TestPostClient {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        ClientResource resource = new ClientResource("http://localhost:8554/");

        FileRepresentation fr = new FileRepresentation("file:///c:/test.mpg",
                MediaType.VIDEO_MPEG);
        System.out.println("Size sent: " + fr.getSize());

        try {
            resource.post(fr);
        } catch (ResourceException e) {
            // Nothing
        }

        System.out.println("Status: " + resource.getStatus());
        long endTime = System.currentTimeMillis();
        System.out.println("Duration: " + (endTime - startTime) + " ms");
    }

}
