/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.ext.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.restlet.data.Status.SUCCESS_OK;

import org.junit.jupiter.api.Test;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.ext.json.JsonpFilter;
import org.restlet.ext.json.JsonpRepresentation;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.test.RestletTestCase;

/**
 * Test case for the {@link JsonpFilter} class.
 *
 * @author Cyril Lakech
 */
public class JsonpFilterTestCase extends RestletTestCase {

    @Test
    public void testAfterHandle() {

        JsonpFilter filter = new JsonpFilter(null);

        Reference ref = new Reference();
        final String callback = "callback";
        ref.addQueryParameter(callback, "test");
        Request request = new Request(Method.GET, ref);
        Response response = new Response(request);
        final String jsonString = "{\"attribute\": value}";
        response.setEntity(new JsonRepresentation(jsonString));

        filter.afterHandle(request, response);

        Representation actual = response.getEntity();
        Representation expected = new JsonpRepresentation(callback, SUCCESS_OK,
                new JsonRepresentation(jsonString));

        assertInstanceOf(JsonpRepresentation.class, actual);
        assertEquals(expected, actual);
        assertEquals(SUCCESS_OK, ((JsonpRepresentation) actual).getStatus());
    }

    @Test
    public void testAfterHandleText() {

        JsonpFilter filter = new JsonpFilter(null);

        Reference ref = new Reference();
        final String callback = "callback";
        ref.addQueryParameter(callback, "test");
        Request request = new Request(Method.GET, ref);
        Response response = new Response(request);
        final String jsonString = "{\"attribute\": value}";
        response.setEntity(new JsonRepresentation(jsonString));

        filter.afterHandle(request, response);

        Representation actual = response.getEntity();
        Representation expected = new JsonpRepresentation(callback, SUCCESS_OK,
                new StringRepresentation(jsonString, MediaType.TEXT_HTML));

        assertInstanceOf(JsonpRepresentation.class, actual);
        assertEquals(expected, actual);
        assertEquals(SUCCESS_OK, ((JsonpRepresentation) actual).getStatus());
    }

    @Test
    public void testAfterHandle_without_callback_should_return_entity_unchanged() {

        JsonpFilter filter = new JsonpFilter(null);

        Reference ref = new Reference();
        Request request = new Request(Method.GET, ref);
        Response response = new Response(request);
        final String jsonString = "{\"attribute\": value}";
        final JsonRepresentation expected = new JsonRepresentation(jsonString);
        response.setEntity(expected);

        filter.afterHandle(request, response);

        Representation actual = response.getEntity();

        assertEquals(expected, actual);
    }

    @Test
    public void testAfterHandle_with_other_mediatype_should_return_entity_unchanged()
            throws Exception {

        JsonpFilter filter = new JsonpFilter(null);

        Reference ref = new Reference();
        final String callback = "callback";
        ref.addQueryParameter(callback, "test");
        Request request = new Request(Method.GET, ref);
        Response response = new Response(request);
        final DomRepresentation expected = new DomRepresentation(
                MediaType.APPLICATION_XML);
        response.setEntity(expected);

        filter.afterHandle(request, response);

        Representation actual = response.getEntity();

        assertEquals(expected, actual);
    }
}
