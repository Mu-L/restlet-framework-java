/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.client.engine.adapter;

import java.io.IOException;
import java.util.logging.Level;

import org.restlet.client.Context;
import org.restlet.client.Request;
import org.restlet.client.Response;
import org.restlet.client.Uniform;
import org.restlet.client.data.Header;
import org.restlet.client.data.Method;
import org.restlet.client.data.Status;
import org.restlet.client.engine.header.HeaderConstants;
import org.restlet.client.engine.header.HeaderUtils;
import org.restlet.client.util.Series;

/**
 * Converter of high-level uniform calls into low-level HTTP client calls.
 * 
 * @author Jerome Louvel
 */
public class ClientAdapter extends Adapter {

    /**
     * Constructor.
     * 
     * @param context
     *            The context to use.
     */
    public ClientAdapter(Context context) {
        super(context);
    }

    /**
     * Commits the changes to a handled HTTP client call back into the original
     * uniform call. The default implementation first invokes the
     * "addResponseHeaders" then asks the "htppCall" to send the response back
     * to the client.
     * 
     * @param httpCall
     *            The original HTTP call.
     * @param request
     *            The high-level request.
     * @param response
     *            The high-level response.
     * @throws Exception
     */
    public void commit(final ClientCall httpCall, Request request,
            Response response) throws Exception {
        if (httpCall != null) {
            // Check if the call is asynchronous
            if (request.isAsynchronous()) {
                final Uniform userCallback = request.getOnResponse();

                // Send the request to the client
                httpCall.sendRequest(request, response, new Uniform() {
                    public void handle(Request request, Response response) {
                        try {
                            updateResponse(response,
                                    new Status(httpCall.getStatusCode(),
                                            httpCall.getReasonPhrase()),
                                    httpCall);

                            if (userCallback != null) {
                                userCallback.handle(request, response);
                            }
                        } catch (Throwable t) {
                            getLogger()
                                    .log(Level.WARNING,
                                            "Unexpected error or exception inside the user call back",
                                            t);
                        }
                    }
                });
            } else {
                System.err
                        .println("HTTP client calls must have a callback in the GWT edition");

            }
        }
    }

    /**
     * Reads the response headers of a handled HTTP client call to update the
     * original uniform call.
     * 
     * @param httpCall
     *            The handled HTTP client call.
     * @param response
     *            The high-level response to update.
     */
    protected void readResponseHeaders(ClientCall httpCall, Response response) {
        try {
            Series<Header> responseHeaders = httpCall.getResponseHeaders();

            // Put the response headers in the call's attributes map
            response.getAttributes().put(HeaderConstants.ATTRIBUTE_HEADERS, responseHeaders);

            HeaderUtils.copyResponseTransportHeaders(responseHeaders, response);
        } catch (Exception e) {
            getLogger()
                    .log(Level.FINE,
                            "An error occurred during the processing of the HTTP response.",
                            e);
            response.setStatus(Status.CONNECTOR_ERROR_INTERNAL, e);
        }
    }

    /**
     * Converts a low-level HTTP call into a high-level uniform call.
     * 
     * @param client
     *            The HTTP client that will handle the call.
     * @param request
     *            The high-level request.
     * @return A new high-level uniform call.
     */
    public ClientCall toSpecific(HttpClientHelper client, Request request) {
        // Create the low-level HTTP client call
        ClientCall result = client.create(request);

        // Add the headers
        if (result != null) {
            HeaderUtils.addGeneralHeaders(request, result.getRequestHeaders());

            if (request.getEntity() != null) {
                HeaderUtils.addEntityHeaders(request.getEntity(),
                        result.getRequestHeaders());
            }

            // NOTE: This must stay at the end because the AWS challenge
            // scheme requires access to all HTTP headers
            HeaderUtils.addRequestHeaders(request, result.getRequestHeaders());
        }

        return result;
    }

    /**
     * Updates the response with information from the lower-level HTTP client
     * call.
     * 
     * @param response
     *            The response to update.
     * @param status
     *            The response status to apply.
     * @param httpCall
     *            The source HTTP client call.
     * @throws IOException
     */
    public void updateResponse(Response response, Status status,
            ClientCall httpCall) {
        // Send the request to the client
        response.setStatus(status);

        // Get the server address
        response.getServerInfo().setAddress(httpCall.getServerAddress());
        response.getServerInfo().setPort(httpCall.getServerPort());

        // Read the response headers
        readResponseHeaders(httpCall, response);

        // Set the entity
        response.setEntity(httpCall.getResponseEntity(response));

        // Release the representation's content for some obvious cases
        if (response.getEntity() != null) {
            if (response.getEntity().isEmpty()) {
                response.getEntity().release();
            } else if (response.getRequest().getMethod().equals(Method.HEAD)) {
                response.getEntity().release();
            } else if (response.getStatus().equals(Status.SUCCESS_NO_CONTENT)) {
                response.getEntity().release();
            } else if (response.getStatus()
                    .equals(Status.SUCCESS_RESET_CONTENT)) {
                response.getEntity().release();
                response.setEntity(null);
            } else if (response.getStatus().equals(
                    Status.REDIRECTION_NOT_MODIFIED)) {
                response.getEntity().release();
            } else if (response.getStatus().isInformational()) {
                response.getEntity().release();
                response.setEntity(null);
            }
        }
    }
}
