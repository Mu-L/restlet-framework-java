/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.restlet.data.MediaType.*;

import java.io.IOException;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Finder;
import org.restlet.test.RestletTestCase;

/**
 * Test the annotated resources, client and server sides.
 *
 * @author Jerome Louvel
 */
public class AnnotatedResource14TestCase extends RestletTestCase {

    private ClientResource clientResource;

    @BeforeEach
    protected void setUpEach() throws Exception {
        Finder finder = new Finder();
        finder.setTargetClass(MyServerResource14.class);

        this.clientResource = new ClientResource("http://local");
        this.clientResource.setNext(finder);
    }

    @AfterEach
    protected void tearDownEach() throws Exception {
        clientResource = null;
    }

    static Stream<Arguments> argumentsProvider() {
        return Stream.of(
                arguments(APPLICATION_JSON, APPLICATION_JSON, "json"),
                arguments(APPLICATION_XML, APPLICATION_JSON, "xml:json"),
                arguments(APPLICATION_XML, APPLICATION_XML, "xml"),
                arguments(TEXT_PLAIN, null, "*"),
                arguments(TEXT_PLAIN, TEXT_PLAIN, "*")
        );
    }

    @ParameterizedTest
    @MethodSource("argumentsProvider")
    public void testQuery(final MediaType requestEntityMediaType, final MediaType responseMediaTypePreference, final String responseEntityAsText) throws IOException {
        StringRepresentation requestEntity = new StringRepresentation("test", requestEntityMediaType);

        Representation rep = responseMediaTypePreference == null
                ? clientResource.put(requestEntity)
                : clientResource.put(requestEntity, responseMediaTypePreference);

        assertNotNull(rep);
        if (responseMediaTypePreference != null) {
            assertEquals(responseMediaTypePreference, rep.getMediaType());
        }
        assertEquals(responseEntityAsText, rep.getText());
    }
}
