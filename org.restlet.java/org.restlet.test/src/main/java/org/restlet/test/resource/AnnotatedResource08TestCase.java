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
import org.restlet.resource.ResourceException;
import org.restlet.test.RestletTestCase;

/**
 * Test the annotated resources, client and server sides.
 * 
 * @author Jerome Louvel
 */
public class AnnotatedResource08TestCase extends RestletTestCase {

    private ClientResource clientResource;

    @BeforeEach
    protected void setUpEach() throws Exception {
        Finder finder = new Finder();
        finder.setTargetClass(MyResource08.class);

        this.clientResource = new ClientResource("http://local");
        this.clientResource.setNext(finder);
    }

    @AfterEach
    protected void tearDownEach() throws Exception {
        clientResource = null;
    }

    @ParameterizedTest
    @MethodSource("argumentsProvider")
    public void testPost(final MediaType requestBodyMediaType, final String requestBody, final MediaType responseBodyMediaType, final String responseBody) throws IOException, ResourceException {
        Representation input = new StringRepresentation(requestBody, requestBodyMediaType);
        Representation result = clientResource.post(input, responseBodyMediaType);
        assertNotNull(result);
        assertEquals(responseBody, result.getText());
        assertEquals(responseBodyMediaType, result.getMediaType());
    }

    static Stream<Arguments> argumentsProvider() {
        return Stream.of(
                arguments(APPLICATION_XML, "root", APPLICATION_XML, "root1"),
                arguments(APPLICATION_XML, "root", APPLICATION_JSON, "root1"),
                arguments(APPLICATION_JSON, "root", APPLICATION_XML, "root1"),
                arguments(APPLICATION_WWW_FORM, "root", APPLICATION_WWW_FORM, "root2"),
                arguments(APPLICATION_WWW_FORM, "root", TEXT_HTML, "root2")
        );
    }

}
