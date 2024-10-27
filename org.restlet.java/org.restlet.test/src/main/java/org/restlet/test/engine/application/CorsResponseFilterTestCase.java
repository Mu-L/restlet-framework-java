/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.engine.application;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Method;
import org.restlet.engine.application.CorsFilter;
import org.restlet.resource.Get;
import org.restlet.resource.Options;
import org.restlet.resource.ServerResource;
import org.restlet.test.RestletTestCase;

 /**
 * @author Manuel Boillod
 */
public class CorsResponseFilterTestCase extends RestletTestCase {

    private CorsFilter corsFilter;

    public static class DummyServerResource extends ServerResource {
        @Options
        public void doOption(){}
        @Get
        public void doGet(){}
    }

    @BeforeEach
    public void setUpEach() throws Exception {
        corsFilter = new CorsFilter();
        corsFilter.setNext(DummyServerResource.class);
    }

    // INVALID CORS REQUESTS

    @Test
    public void testGet_withoutOrigin() {
        Request request = new Request();
        request.setMethod(Method.GET);
        Response response = corsFilter.handle(request);
        assertNoCorsHeaders(response);
    }

    @Test
    public void testOption_withoutOrigin() {
        Request request = new Request();
        request.setMethod(Method.OPTIONS);
        Response response = corsFilter.handle(request);
        assertNoCorsHeaders(response);
    }

    @Test
    public void testOption_withoutRequestMethod() {
        Request request = new Request();
        request.setMethod(Method.OPTIONS);
        request.getHeaders().set("Origin", "localhost");
        Response response = corsFilter.handle(request);
        assertNoCorsHeaders(response);
    }

    // VALID CORS REQUESTS

    @Test
    public void testGet() {
        Request request = new Request();
        request.setMethod(Method.GET);
        request.getHeaders().set("Origin", "localhost");
        Response response = corsFilter.handle(request);
        assertEquals("*", response.getAccessControlAllowOrigin());
        assertNull(response.getAccessControlAllowCredentials());
        assertIsEmpty(response.getAccessControlAllowHeaders());
        assertIsEmpty(response.getAccessControlAllowMethods());
        assertIsEmpty(response.getAccessControlExposeHeaders());
    }

    @Test
    public void testGet_withAuthenticationAllowed() {
        corsFilter.setAllowedCredentials(true);

        Request request = new Request();
        request.setMethod(Method.GET);
        request.getHeaders().set("Origin", "localhost");
        Response response = corsFilter.handle(request);
        assertEquals("localhost", response.getAccessControlAllowOrigin());
        assertEquals(Boolean.TRUE, response.getAccessControlAllowCredentials());
        assertIsEmpty(response.getAccessControlAllowHeaders());
        assertIsEmpty(response.getAccessControlAllowMethods());
        assertIsEmpty(response.getAccessControlExposeHeaders());
    }

    @Test
    public void testOption_requestGet() {
        Request request = new Request();
        request.setMethod(Method.OPTIONS);
        request.getHeaders().set("Origin", "localhost");
        request.setAccessControlRequestMethod(Method.GET);
        Response response = corsFilter.handle(request);
        assertEquals("*", response.getAccessControlAllowOrigin());
        assertNull(response.getAccessControlAllowCredentials());
        assertIsEmpty(response.getAccessControlAllowHeaders());
        assertThat(response.getAccessControlAllowMethods(), contains(Method.GET, Method.OPTIONS));
        assertIsEmpty(response.getAccessControlExposeHeaders());
    }

    @Test
    public void testOption_requestGet_skippingResource() {
        corsFilter.setSkippingResourceForCorsOptions(true);

        Request request = new Request();
        request.setMethod(Method.OPTIONS);
        request.getHeaders().set("Origin", "localhost");
        request.setAccessControlRequestMethod(Method.GET);
        Response response = corsFilter.handle(request);
        assertEquals("*", response.getAccessControlAllowOrigin());
        assertNull(response.getAccessControlAllowCredentials());
        assertIsEmpty(response.getAccessControlAllowHeaders());
        assertThat(response.getAccessControlAllowMethods(), containsInAnyOrder(Method.GET, Method.POST, Method.PUT, Method.DELETE, Method.PATCH));
        assertIsEmpty(response.getAccessControlExposeHeaders());
    }

    @Test
    public void testOption_requestPost_skippingResource() {
        corsFilter.setSkippingResourceForCorsOptions(true);

        Request request = new Request();
        request.setMethod(Method.OPTIONS);
        request.getHeaders().set("Origin", "localhost");
        request.setAccessControlRequestMethod(Method.POST);
        Response response = corsFilter.handle(request);
        assertEquals("*", response.getAccessControlAllowOrigin());
        assertNull(response.getAccessControlAllowCredentials());
        assertIsEmpty(response.getAccessControlAllowHeaders());
        assertThat(response.getAccessControlAllowMethods(), containsInAnyOrder(Method.GET, Method.POST, Method.PUT, Method.DELETE, Method.PATCH));
        assertIsEmpty(response.getAccessControlExposeHeaders());
    }

    @Test
    public void testOption_requestGet_withAuthenticationAllowed() {
        corsFilter.setAllowedCredentials(true);

        Request request = new Request();
        request.setMethod(Method.OPTIONS);
        request.getHeaders().set("Origin", "localhost");
        request.setAccessControlRequestMethod(Method.GET);
        Response response = corsFilter.handle(request);
        assertEquals("localhost", response.getAccessControlAllowOrigin());
        assertEquals(Boolean.TRUE, response.getAccessControlAllowCredentials());
        assertIsEmpty(response.getAccessControlAllowHeaders());
        assertThat(response.getAccessControlAllowMethods(), contains(Method.GET, Method.OPTIONS));
        assertIsEmpty(response.getAccessControlExposeHeaders());
    }

    private void assertIsEmpty(Collection<?> collection) {
        assertNotNull(collection);
        assertTrue(collection.isEmpty());
    }

    public void assertNoCorsHeaders(Response response) {
        assertNull(response.getAccessControlAllowOrigin());
        assertNull(response.getAccessControlAllowCredentials());
        assertIsEmpty(response.getAccessControlAllowHeaders());
        assertIsEmpty(response.getAccessControlAllowMethods());
        assertIsEmpty(response.getAccessControlExposeHeaders());
    }

}
