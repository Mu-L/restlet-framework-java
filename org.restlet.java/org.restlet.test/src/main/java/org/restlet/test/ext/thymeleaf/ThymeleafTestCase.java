/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.ext.thymeleaf;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;
import org.restlet.data.MediaType;
import org.restlet.ext.thymeleaf.TemplateRepresentation;
import org.restlet.test.RestletTestCase;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

/**
 * Unit test for the Thymeleaf extension.
 * 
 * @author Thierry Boileau
 */
public class ThymeleafTestCase extends RestletTestCase {

    @Test
    public void testTemplate() throws Exception {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("org/restlet/test/ext/thymeleaf/");
        templateResolver.setSuffix(".html");
        templateResolver.setCacheTTLMs(3600000L);
        final Map<String, Object> map = new TreeMap<String, Object>();
        map.put("welcome", "Hello, world");

        final String result = new TemplateRepresentation("test",
                TemplateRepresentation.createTemplateEngine(templateResolver),
                Locale.getDefault(), map, MediaType.TEXT_PLAIN).getText();
        assertTrue(result.contains("Hello, world"));
    }

}
