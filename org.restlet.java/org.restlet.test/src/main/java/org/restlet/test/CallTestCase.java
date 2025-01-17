/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.ClientInfo;
import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.engine.adapter.Call;

/**
 * Test {@link org.restlet.engine.adapter.Call}.
 * 
 * @author Lars Heuer (heuer[at]semagia.com)
 */
public class CallTestCase extends RestletTestCase {
    /**
     * Returns a connector call.
     * 
     * @return A connector call instance.
     */
    protected Call getCall() {
        return new Call() {

            @Override
            protected boolean isClientKeepAlive() {
                return false;
            }

            @Override
            protected boolean isServerKeepAlive() {
                return false;
            }

        };
    }

    /**
     * Returns a reference with the specified URI.
     * 
     * @param uri
     *            The URI.
     * @return Reference instance.
     */
    protected Reference getReference(String uri) {
        return new Reference(uri);
    }

    /**
     * Returns a request.
     * 
     * @return Request instance.
     */
    protected Request getRequest() {
        return new Request();
    }

    /**
     * Returns a response.
     * 
     * @param request
     *            The associated request.
     * @return Response instance.
     */
    protected Response getResponse(Request request) {
        return new Response(request);
    }

    /**
     * Tests context's base reference getting/setting.
     */
    @Test
    public void testBaseRef() {
        final Request request = getRequest();
        final String resourceRefURI = "http://restlet.org/path/to/resource";
        final Reference resourceRef = getReference(resourceRefURI);
        request.setResourceRef(resourceRefURI);
        assertEquals(resourceRef, request.getResourceRef());
        
        String uri = "http://restlet.org/path";
        Reference reference = getReference(uri);
        request.getResourceRef().setBaseRef(uri);
        assertEquals(uri, request.getResourceRef().getBaseRef().toString());
        assertEquals(reference, request.getResourceRef().getBaseRef());
        
        uri = "http://restlet.org/path/to";
        reference = getReference(uri);
        request.getResourceRef().setBaseRef(uri);
        assertEquals(uri, request.getResourceRef().getBaseRef().toString());
        assertEquals(reference, request.getResourceRef().getBaseRef());
    }

    /**
     * Tests client address getting/setting.
     */
    @Test
    public void testClientAddress() {
        final ClientInfo client = getRequest().getClientInfo();
        String address = "127.0.0.1";
        client.setAddress(address);
        assertEquals(address, client.getAddress());
        assertEquals(0, client.getForwardedAddresses().size());
    }

    /**
     * Tests client agent getting/setting.
     */
    @Test
    public void testClientAgent() {
        final ClientInfo client = getRequest().getClientInfo();
        String name = "Restlet";
        client.setAgent(name);
        assertEquals(name, client.getAgent());
        
        name = "Restlet Client";
        client.setAgent(name);
        assertEquals(name, client.getAgent());
    }

    /**
     * Tests client addresses getting/setting.
     */
    @Test
    public void testClientForwardedAddresses() {
        final ClientInfo client = getRequest().getClientInfo();
        String firstAddress = "127.0.0.1";
        final String secondAddress = "192.168.99.10";
        List<String> addresses = Arrays.asList(new String[] { firstAddress,
                secondAddress });
        client.getForwardedAddresses().addAll(addresses);
        assertEquals(addresses, client.getForwardedAddresses());
        
        client.getForwardedAddresses().clear();
        client.getForwardedAddresses().addAll(addresses);
        assertEquals(addresses, client.getForwardedAddresses());
    }

    /**
     * Tests method getting/setting.
     */
    @Test
    public void testMethod() {
        final Request request = getRequest();
        request.setMethod(Method.GET);
        assertEquals(Method.GET, request.getMethod());
        
        request.setMethod(Method.POST);
        assertEquals(Method.POST, request.getMethod());
    }

    /**
     * Tests redirection reference getting/setting.
     */
    @Test
    public void testRedirectionRef() {
        final Request request = getRequest();
        final Response response = getResponse(request);
        String uri = "http://restlet.org/";
        Reference reference = getReference(uri);
        response.setLocationRef(uri);
        assertEquals(reference, response.getLocationRef());
        
        uri = "http://restlet.org/something";
        reference = getReference(uri);
        response.setLocationRef(reference);
        assertEquals(reference, response.getLocationRef());
    }

    /**
     * Tests referrer reference getting/setting.
     */
    @Test
    public void testReferrerRef() {
        final Request request = getRequest();
        String uri = "http://restlet.org/";
        Reference reference = getReference(uri);
        request.setReferrerRef(uri);
        assertEquals(reference, request.getReferrerRef());
        
        uri = "http://restlet.org/something";
        reference = getReference(uri);
        request.setReferrerRef(reference);
        assertEquals(reference, request.getReferrerRef());
    }

    /**
     * Tests resource reference getting/setting.
     */
    @Test
    public void testResourceRef() {
        final Request request = getRequest();
        String uri = "http://restlet.org/";
        Reference reference = getReference(uri);
        request.setResourceRef(uri);
        assertEquals(reference, request.getResourceRef());
        uri = "http://restlet.org/something";
        reference = getReference(uri);
        request.setResourceRef(reference);
        assertEquals(reference, request.getResourceRef());
    }

    /**
     * Tests server address getting/setting.
     */
    @Test
    public void testServerAddress() {
        final Request request = getRequest();
        final Response response = getResponse(request);
        String address = "127.0.0.1";
        response.getServerInfo().setAddress(address);
        assertEquals(address, response.getServerInfo().getAddress());
        
        address = "192.168.99.10";
        response.getServerInfo().setAddress(address);
        assertEquals(address, response.getServerInfo().getAddress());
    }

    /**
     * Tests server agent getting/setting.
     */
    @Test
    public void testServerAgent() {
        final Request request = getRequest();
        final Response response = getResponse(request);
        String name = "Restlet";
        response.getServerInfo().setAgent(name);
        assertEquals(name, response.getServerInfo().getAgent());
        
        name = "Restlet Server";
        response.getServerInfo().setAgent(name);
        assertEquals(name, response.getServerInfo().getAgent());
    }

    /**
     * Tests status getting/setting.
     */
    @Test
    public void testStatus() {
        final Request request = getRequest();
        final Response response = getResponse(request);
        response.setStatus(Status.SUCCESS_OK);
        assertEquals(Status.SUCCESS_OK, response.getStatus());
        
        response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
        assertEquals(Status.CLIENT_ERROR_BAD_REQUEST, response.getStatus());
    }

}
