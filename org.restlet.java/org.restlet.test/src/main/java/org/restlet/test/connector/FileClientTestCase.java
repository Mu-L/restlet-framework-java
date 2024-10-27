/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.connector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.restlet.data.Language;
import org.restlet.data.LocalReference;
import org.restlet.data.Status;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.restlet.test.RestletTestCase;

/**
 * Unit test case for the File client connector.
 * 
 * @author Jerome Louvel
 */
public class FileClientTestCase extends RestletTestCase {

    @Test
    public void testFileClient() throws IOException {
        String text = "Test content\r\nLine 2\r\nLine2";
        LocalReference fr = LocalReference
                .createFileReference(File.createTempFile("Restlet", ".txt."
                        + Language.DEFAULT.getName()));
        ClientResource resource = new ClientResource(fr);

        try {
            // Update the text of the temporary file
            resource.put(new StringRepresentation(text));
        } catch (ResourceException e) {
        }
        assertTrue(resource.getStatus().isSuccess());

        try {
            // Get the text and compare to the original
            resource.get();
        } catch (ResourceException e) {
        }
        assertEquals(Status.SUCCESS_OK, resource.getStatus());

        try {
            // Delete the file
            resource.delete();
        } catch (ResourceException e) {
        }
        assertEquals(Status.SUCCESS_NO_CONTENT, resource.getStatus());
    }
}
