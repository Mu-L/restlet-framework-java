/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.data;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.restlet.data.LocalReference;
import org.restlet.test.RestletTestCase;

/**
 * Unit test case for the File Reference parsing.
 * 
 * @author Jerome Louvel
 */
public class FileReferenceTestCase extends RestletTestCase {

    @Test
    public void testCreation() {
        String path = "D:\\Restlet\\build.xml";
        LocalReference fr = LocalReference.createFileReference(path);
        fr.getFile();

        assertEquals("file", fr.getScheme());
        assertEquals("", fr.getAuthority());

        if (File.separatorChar == '\\') {
            assertEquals("/D%3A/Restlet/build.xml", fr.getPath());
        } else {
            assertEquals("/D%3A%5CRestlet%5Cbuild.xml", fr.getPath());
        }
    }
}
