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

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.restlet.Request;
import org.restlet.data.ClientInfo;
import org.restlet.data.Language;
import org.restlet.data.MediaType;
import org.restlet.data.Preference;
import org.restlet.representation.Variant;
import org.restlet.service.ConnegService;
import org.restlet.service.MetadataService;
import org.restlet.test.RestletTestCase;

/**
 * Test {@link org.restlet.data.ClientInfo} for content negotiation.
 * 
 * @author Jerome Louvel
 */
public class ClientInfoTestCase extends RestletTestCase {

    /**
     * Conneg tests.
     */
    @Test
    public void testConneg() {
        MetadataService ms = new MetadataService();
        ConnegService connegService = new ConnegService();
        Request request = new Request();
        ClientInfo ci = request.getClientInfo();
        ci.getAcceptedLanguages().add(
                new Preference<>(Language.ENGLISH_US, 1.0F));
        ci.getAcceptedLanguages().add(
                new Preference<>(Language.FRENCH_FRANCE, 0.9F));
        ci.getAcceptedMediaTypes().add(
                new Preference<>(MediaType.TEXT_XML, 1.0F));

        List<Variant> variants = new ArrayList<>();
        variants.add(new Variant(MediaType.TEXT_XML, Language.ENGLISH_US));
        variants.add(new Variant(MediaType.TEXT_XML, Language.FRENCH_FRANCE));
        Variant pv = connegService.getPreferredVariant(variants, request, ms);

        assertEquals(MediaType.TEXT_XML, pv.getMediaType());
        assertEquals(Language.ENGLISH_US, pv.getLanguages().get(0));

        // Leveraging parent languages
        variants.clear();
        variants.add(new Variant(MediaType.TEXT_XML, Language.ENGLISH));
        variants.add(new Variant(MediaType.TEXT_XML, Language.FRENCH));
        pv = connegService.getPreferredVariant(variants, request, ms);

        assertEquals(MediaType.TEXT_XML, pv.getMediaType());
        assertEquals(Language.ENGLISH, pv.getLanguages().get(0));

        // Testing quality priority over parent metadata
        variants.clear();
        variants.add(new Variant(MediaType.TEXT_PLAIN, Language.ENGLISH));
        variants.add(new Variant(MediaType.TEXT_XML, Language.FRENCH_FRANCE));
        pv = connegService.getPreferredVariant(variants, request, ms);

        assertEquals(MediaType.TEXT_XML, pv.getMediaType());
        assertEquals(Language.FRENCH_FRANCE, pv.getLanguages().get(0));

        // Testing quality priority over parent metadata
        variants.clear();
        variants.add(new Variant(MediaType.APPLICATION_XML, Language.ENGLISH_US));
        variants.add(new Variant(MediaType.TEXT_XML, Language.FRENCH_FRANCE));
        pv = connegService.getPreferredVariant(variants, request, ms);

        assertEquals(MediaType.TEXT_XML, pv.getMediaType());
        assertEquals(Language.FRENCH_FRANCE, pv.getLanguages().get(0));

        // Leveraging parent media types
        variants.clear();
        variants.add(new Variant(MediaType.APPLICATION_XML, Language.ENGLISH_US));
        variants.add(new Variant(MediaType.APPLICATION_XML,
                Language.FRENCH_FRANCE));
        pv = connegService.getPreferredVariant(variants, request, ms);

        assertEquals(MediaType.APPLICATION_XML, pv.getMediaType());
        assertEquals(Language.ENGLISH_US, pv.getLanguages().get(0));

    }

    /**
     * Conneg tests for IE which accepts all media types.
     */
    @Test
    public void testConnegIO() {
        ClientInfo ci = new ClientInfo();
        ci.getAcceptedMediaTypes().add(
                new Preference<>(MediaType.ALL, 1.0F));

        List<MediaType> types = new ArrayList<>();
        types.add(MediaType.TEXT_XML);
        types.add(MediaType.APPLICATION_JSON);
        MediaType pmt = ci.getPreferredMediaType(types);

        assertEquals(MediaType.TEXT_XML, pmt);
    }
}
