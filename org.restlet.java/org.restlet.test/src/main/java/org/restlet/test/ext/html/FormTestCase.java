/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.ext.html;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.restlet.data.CharacterSet;
import org.restlet.data.MediaType;
import org.restlet.ext.html.FormData;
import org.restlet.ext.html.FormDataSet;
import org.restlet.ext.html.internal.FormReader;
import org.restlet.test.RestletTestCase;
import org.restlet.util.Series;

/**
 * Unit tests for the Form class.
 * 
 * @author Jerome Louvel
 */
public class FormTestCase extends RestletTestCase {
    /**
     * Tests the cookies parsing.
     */
    @Test
    public void testParsing() throws IOException {
        FormDataSet form = new FormDataSet();
        form.add("name", "John D. Mitchell");
        form.add("email", "john@bob.net");
        form.add("email2", "joe@bob.net");
        String query = form.encode();

        Series<FormData> newFormData = new FormReader(query,
                CharacterSet.UTF_8, '&').read();

        FormDataSet newForm = new FormDataSet();
        newForm.getEntries().addAll(newFormData);
        String newQuery = newForm.encode();

        assertEquals(query, newQuery);
    }

    /**
     * Tests the multipart content-type.
     */
    @Test
    public void testContentType() {
        FormDataSet form = null;

        form = new FormDataSet();
        form.setMultipart(true);
        assertTrue(form.getMediaType().equals(MediaType.MULTIPART_FORM_DATA,
                true));

        form = new FormDataSet("test");
        assertTrue(form.isMultipart());
        assertTrue(form.getMediaType().equals(MediaType.MULTIPART_FORM_DATA,
                true));
        assertEquals(
                form.getMediaType().getParameters().getFirstValue("boundary"),
                "test");
        form = new FormDataSet();

        form.setMultipartBoundary("test2");
        assertTrue(form.isMultipart());
        assertTrue(form.getMediaType().equals(MediaType.MULTIPART_FORM_DATA,
                true));
        assertEquals(
                form.getMediaType().getParameters().getFirstValue("boundary"),
                "test2");
    }
}
