/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.restlet.data.MediaType;
import org.restlet.data.Preference;
import org.restlet.engine.header.PreferenceReader;
import org.restlet.engine.header.PreferenceWriter;
import org.restlet.test.RestletTestCase;

/**
 * Unit tests for the Preference related classes.
 * 
 * @author Jerome Louvel
 */
public class PreferencesTestCase extends RestletTestCase {
    /**
     * Tests the parsing of a single preference header.
     * 
     * @param headerValue
     *            The preference header.
     */
    private void testMediaType(String headerValue, boolean testEquals) {
        PreferenceReader<MediaType> pr = new PreferenceReader<>(
                PreferenceReader.TYPE_MEDIA_TYPE, headerValue);
        List<Preference<MediaType>> prefs = new ArrayList<>();
        pr.addValues(prefs);

        // Rewrite the header
        String newHeaderValue = PreferenceWriter.write(prefs);

        // Reread and rewrite the header (prevent formatting issues)
        pr = new PreferenceReader<>(PreferenceReader.TYPE_MEDIA_TYPE,
                headerValue);
        prefs = new ArrayList<>();
        pr.addValues(prefs);
        String newHeaderValue2 = PreferenceWriter.write(prefs);

        if (testEquals) {
            // Compare initial and new headers
            assertEquals(newHeaderValue, newHeaderValue2);
        }
    }

    /**
     * Tests the preferences parsing.
     */
    @Test
    public void testParsing() {
        testMediaType(
                "text/*;q=0.3, text/html;q=0.7, text/html;level=1, text/html;LEVEL=2;q=0.4;ext1, */*;q=0.5",
                true);
        testMediaType(
                "text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/*,,*/*;q=0.5",
                false);
    }
}
