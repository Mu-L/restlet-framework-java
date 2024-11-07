/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.client.engine.adapter;

import java.util.logging.Level;

import org.restlet.client.Client;
import org.restlet.client.Request;
import org.restlet.client.Response;
import org.restlet.client.data.Status;
import org.restlet.client.engine.connector.ClientHelper;

/**
 * Base HTTP client connector. Here is the list of parameters that are
 * supported. They should be set in the Client's context before it is started:
 * <table>
 * <tr>
 * <th>Parameter name</th>
 * <th>Value type</th>
 * <th>Default value</th>
 * <th>Description</th>
 * </tr>
 * <tr>
 * <td>adapter</td>
 * <td>String</td>
 * <td>org.restlet.client.engine.adapter.ClientAdapter</td>
 * <td>Class name of the adapter of low-level HTTP calls into high level
 * requests and responses.</td>
 * </tr>
 * </table>
 * 
 * @author Jerome Louvel
 */
public abstract class HttpClientHelper extends ClientHelper {

    /** The adapter from uniform calls to HTTP calls. */
    private volatile ClientAdapter adapter;

    /**
     * Constructor.
     * 
     * @param client
     *            The client to help.
     */
    public HttpClientHelper(Client client) {
        super(client);
        this.adapter = null;
    }

    /**
     * Creates a low-level HTTP client call from a high-level request.
     * 
     * @param request
     *            The high-level request.
     * @return A low-level HTTP client call.
     */
    public abstract ClientCall create(Request request);

    /**
     * Returns the adapter from uniform calls to HTTP calls.
     * 
     * @return the adapter from uniform calls to HTTP calls.
     */
    public ClientAdapter getAdapter() throws Exception {
        if (this.adapter == null) {

             this.adapter = new ClientAdapter(getContext());
        }

        return this.adapter;
    }

    /**
     * Returns the connection timeout. Defaults to 15000.
     * 
     * @return The connection timeout.
     */
    public int getSocketConnectTimeoutMs() {
        int result = 0;

        if (getHelpedParameters().getNames().contains("socketConnectTimeoutMs")) {
            result = Integer.parseInt(getHelpedParameters().getFirstValue(
                    "socketConnectTimeoutMs", "15000"));
        }

        return result;
    }

    @Override
    public void handle(Request request, Response response) {
        try {
            ClientCall clientCall = getAdapter().toSpecific(this, request);
            getAdapter().commit(clientCall, request, response);
        } catch (Exception e) {
            getLogger().log(Level.INFO,
                    "Error while handling an HTTP client call", e);
            response.setStatus(Status.CONNECTOR_ERROR_INTERNAL, e);
        }
    }

    /**
     * Sets the adapter from uniform calls to HTTP calls.
     * 
     * @param adapter
     *            The adapter to set.
     */
    public void setAdapter(ClientAdapter adapter) {
        this.adapter = adapter;
    }
}