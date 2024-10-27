/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.bench;

import java.io.IOException;

import org.restlet.data.MediaType;
import org.restlet.representation.FileRepresentation;
import org.restlet.representation.InputRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

public class TestPostChunkedClient {

    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();

        ClientResource resource = new ClientResource("http://localhost:8554/");
        FileRepresentation fr = new FileRepresentation("file:///c:/test.mpg",
                MediaType.VIDEO_MPEG);
        System.out.println("Size sent: " + fr.getSize());
        InputRepresentation ir = new InputRepresentation(fr.getStream(),
                fr.getMediaType());

        try {
            resource.post(ir);
        } catch (ResourceException e) {
            // Nothing
        }

        System.out.println("Status: " + resource.getStatus());
        long endTime = System.currentTimeMillis();
        System.out.println("Duration: " + (endTime - startTime) + " ms");
    }

}
