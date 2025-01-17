/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.client.engine.adapter;

import java.io.InputStream;

import org.restlet.client.Uniform;
import org.restlet.client.data.Reference;
import org.restlet.client.data.Status;
import org.restlet.client.engine.io.StringInputStream;
import org.restlet.client.util.Series;

import com.google.gwt.http.client.Header;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;

/**
 * HTTP client connector call based on GWT's HTTP module.
 * 
 * @author Jerome Louvel
 */
public class GwtClientCall extends ClientCall {

	/**
	 * Special reason phrase in case of error.
	 */
	private String errorReasonPhrase;

	/**
	 * Special status code set when an error occurs and we don't have a Response
	 * object.
	 */
	private int errorStatusCode;

	/** The wrapped HTTP request builder. */
	private final RequestBuilder requestBuilder;

	/** The GWT response. */
	private Response response;

	/** Indicates if the response headers were added. */
	private boolean responseHeadersAdded;

	/**
	 * Constructor.
	 * 
	 * @param helper
	 *            The parent HTTP client helper.
	 * @param method
	 *            The method name.
	 * @param requestUri
	 *            The request URI.
	 * @param hasEntity
	 *            Indicates if the call will have an entity to send to the
	 *            server.
	 */
	public GwtClientCall(GwtHttpClientHelper helper, String method,
			String requestUri, boolean hasEntity) {
		super(helper, method, requestUri);

		Reference requestRef = new Reference(requestUri);
		if (requestRef.isRelative()
				|| requestRef.getScheme().startsWith("http")) {
			this.requestBuilder = new RequestBuilder(method, requestUri) {
			};
			this.requestBuilder.setTimeoutMillis(getHelper()
					.getSocketConnectTimeoutMs());
			this.responseHeadersAdded = false;
		} else {
			throw new IllegalArgumentException(
					"Only HTTP or HTTPS resource URIs are allowed here");
		}
	}

	/**
	 * Returns a special reason phrase in case of error.
	 * 
	 * @return A special reason phrase in case of error.
	 */
	private String getErrorReasonPhrase() {
		return errorReasonPhrase;
	}

	/**
	 * Returns a special status code set when an error occurs and we don't have
	 * a Response object.
	 * 
	 * @return A special error status code.
	 */
	private int getErrorStatusCode() {
		return this.errorStatusCode;
	}

	/**
	 * Returns the HTTP client helper.
	 * 
	 * @return The HTTP client helper.
	 */
	@Override
	public GwtHttpClientHelper getHelper() {
		return (GwtHttpClientHelper) super.getHelper();
	}

	/**
	 * Returns the response reason phrase.
	 * 
	 * @return The response reason phrase.
	 */
	@Override
	public String getReasonPhrase() {
		return (getResponse() == null) ? getErrorReasonPhrase() : getResponse()
				.getStatusText();
	}

	/**
	 * Returns the GWT request builder.
	 * 
	 * @return The GWT request builder.
	 */
	public RequestBuilder getRequestBuilder() {
		return this.requestBuilder;
	}

	@Override
	public String getRequestEntityString() {
		return getRequestBuilder().getRequestData();
	}

	/**
	 * Returns the GWT response.
	 * 
	 * @return The GWT response.
	 */
	public Response getResponse() {
		return this.response;
	}

	@Override
	public InputStream getResponseEntityStream(long size) {
		return (getResponse() == null) ? null : new StringInputStream(
				getResponse().getText());
	}

	/**
	 * Returns the modifiable list of response headers.
	 * 
	 * @return The modifiable list of response headers.
	 */
	@Override
	public Series<org.restlet.client.data.Header> getResponseHeaders() {
		final Series<org.restlet.client.data.Header> result = super
				.getResponseHeaders();

		if (!this.responseHeadersAdded && (getResponse() != null)) {
			Header[] headers = getResponse().getHeaders();

			for (int i = 0; i < headers.length; i++) {
				if (headers[i] != null) {
					result.add(headers[i].getName(), headers[i].getValue());
				}
			}

			this.responseHeadersAdded = true;
		}

		return result;
	}

	@Override
	public String getServerAddress() {
		return new Reference(getRequestBuilder().getUrl()).getHostIdentifier();
	}

	@Override
	public int getStatusCode() {
		int result = -1;
		if (getResponse() == null) {
			result = getErrorStatusCode();
		} else if (1223 == getResponse().getStatusCode()) {
			//  IE XMLHTTP implementations turn 204 response code into bogus 1223 status code
			result = 204;
		} else {
			result = getResponse().getStatusCode();
		}
		return result;
	}

	@Override
	public void sendRequest(final org.restlet.client.Request request,
			final org.restlet.client.Response response,
			final Uniform onResponseCallback) throws Exception {
		getRequestBuilder().setRequestData(request.isEntityAvailable() ? request.getEntity().getText() : null);

		// Set the request headers
		for (final org.restlet.client.data.Header header : getRequestHeaders()) {
			getRequestBuilder().setHeader(header.getName(),
					getRequestHeaders().getValues(header.getName()));
		}

		// Set the current call as the callback handler
		getRequestBuilder().setCallback(new RequestCallback() {

			public void onError(com.google.gwt.http.client.Request gwtRequest,
					Throwable exception) {
				setErrorStatusCode(Status.CONNECTOR_ERROR_INTERNAL.getCode());
				setErrorReasonPhrase(exception == null ? "Unknown GWT HTTP communication error."
						: exception.getMessage());
				onResponseCallback.handle(request, response);
			}

			public void onResponseReceived(
					com.google.gwt.http.client.Request gwtRequest,
					Response gwtResponse) {
				setResponse(gwtResponse);
				onResponseCallback.handle(request, response);
			}

		});

		// Send the request
		getRequestBuilder().send();
	}

	/**
	 * Sets a special reason phrase in case of error.
	 * 
	 * @param errorReasonPhrase
	 *            Special reason phrase in case of error.
	 */
	private void setErrorReasonPhrase(String errorReasonPhrase) {
		this.errorReasonPhrase = errorReasonPhrase;
	}

	/**
	 * Sets a special status code set when an error occurs and we don't have a
	 * Response object.
	 * 
	 * @param errorStatusCode
	 *            Special error status code.
	 */
	private void setErrorStatusCode(int errorStatusCode) {
		this.errorStatusCode = errorStatusCode;
	}

	/**
	 * Sets the GWT response.
	 * 
	 * @param response
	 *            The GWT response.
	 */
	public void setResponse(Response response) {
		this.response = response;
	}

}
