/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.client.resource;

import org.restlet.client.Request;
import org.restlet.client.Response;
import org.restlet.client.data.Status;

/**
 * Encapsulates a response status and the optional cause as a checked exception.
 * <p>
 * Note that this class must implement java.io.Serializable, because it extends
 * RuntimeException. To avoid warnings, it provides a serialVersionUID and has
 * its non-serializable fields marked transient. The default serialization thus
 * obtained is minimal, and may not be what the user expects.
 * </p>
 *
 * @author Jerome Louvel
 */
public class ResourceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** The status associated to this exception. */
    private final transient Status status;

    /** The request associated to this exception. Could be null. */
    private final transient Request request;

    /** The response associated to this exception. Could be null.  */
    private final transient Response response;

    /**
     * Constructor.
     * 
     * @param code
     *            The specification code of the encapsulated status.
     */
    public ResourceException(int code) {
        this(new Status(code));
    }

    /**
     * Constructor.
     * 
     * @param code
     *            The specification code of the encapsulated status.
     * @param reasonPhrase
     *            The short reason phrase displayed next to the status code in a
     *            HTTP response.
     */
    public ResourceException(int code, String reasonPhrase) {
        this(new Status(code, reasonPhrase));
    }

    /**
     * Constructor.
     * 
     * @param code
     *            The specification code of the encapsulated status.
     * @param reasonPhrase
     *            The short reason phrase displayed next to the status code in a
     *            HTTP response.
     * @param description
     *            The description of the encapsulated status.
     */
    public ResourceException(int code, String reasonPhrase, String description) {
        this(new Status(code, reasonPhrase, description));
    }

    /**
     * Constructor.
     * 
     * @param code
     *            The specification code of the encapsulated status.
     * @param name
     *            The name of the encapsulated status.
     * @param description
     *            The description of the encapsulated status.
     * @param uri
     *            The URI of the specification describing the method.
     */
    public ResourceException(int code, String name, String description, String uri) {
        this(new Status(code, name, description, uri));
    }

    /**
     * Constructor.
     * 
     * @param code
     *            The specification code of the encapsulated status.
     * @param reasonPhrase
     *            The short reason phrase displayed next to the status code in a
     *            HTTP response.
     * @param description
     *            The description of the encapsulated status.
     * @param uri
     *            The URI of the specification describing the method.
     * @param cause
     *            The wrapped cause error or exception.
     */
    public ResourceException(int code, String reasonPhrase, String description, String uri, Throwable cause) {
        this(new Status(code, cause, reasonPhrase, description, uri), cause);
    }

    /**
     * Constructor.
     * 
     * @param code
     *            The specification code of the encapsulated status.
     * @param cause
     *            The wrapped cause error or exception.
     */
    public ResourceException(int code, Throwable cause) {
        this(new Status(code, cause), cause);
    }

    /**
     * Constructor.
     * 
     * @param code
     *            The specification code.
     * @param throwable
     *            The related error or exception.
     * @param reasonPhrase
     *            The short reason phrase displayed next to the status code in a
     *            HTTP response.
     */
    public ResourceException(int code, Throwable throwable, String reasonPhrase) {
        this(new Status(code, throwable, reasonPhrase, null, null));
    }

    /**
     * Constructor.
     * 
     * @param code
     *            The specification code.
     * @param throwable
     *            The related error or exception.
     * @param reasonPhrase
     *            The short reason phrase displayed next to the status code in a
     *            HTTP response.
     * @param description
     *            The longer description.
     */
    public ResourceException(int code, Throwable throwable, String reasonPhrase, String description) {
        this(new Status(code, throwable, reasonPhrase, description, null));
    }

    /**
     * Constructor.
     * 
     * @param code
     *            The specification code.
     * @param throwable
     *            The related error or exception.
     * @param reasonPhrase
     *            The short reason phrase displayed next to the status code in a
     *            HTTP response.
     * @param description
     *            The longer description.
     * @param uri
     *            The URI of the specification describing the method.
     */
    public ResourceException(int code, Throwable throwable, String reasonPhrase, String description, String uri) {
        this(new Status(code, throwable, reasonPhrase, description, uri));
    }

    /**
     * Constructor.
     * 
     * @param status
     *            The status to associate.
     */
    public ResourceException(Status status) {
        this(status, (status == null) ? null : status.getThrowable());
    }

    /**
     * Constructor.
     * 
     * @param status
     *            The status to associate.
     * @deprecated use constructor with status, request and response instead.
     */
    @Deprecated
    public ResourceException(Status status, Resource resource) {
        this(status, (status == null) ? null : status.getThrowable(), resource.getRequest(), resource.getResponse());
    }

    /**
     * Constructor.
     *
     * @param status
     *            The status to associate.
     */
    public ResourceException(Status status, Request request, Response response) {
        this(status, (status == null) ? null : status.getThrowable(), request, response);
    }

    /**
     * Constructor.
     *
     * @param status
     *            The status to copy.
     * @param description
     *            The description of the encapsulated status.
     */
    public ResourceException(Status status, String description) {
        this(new Status(status, description));
    }

    /**
     * Constructor.
     * 
     * @param status
     *            The status to copy.
     * @param description
     *            The description of the encapsulated status.
     * @param cause
     *            The wrapped cause error or exception.
     */
    public ResourceException(Status status, String description, Throwable cause) {
        this(new Status(status, cause, null, description), cause);
    }

    /**
     * Constructor.
     *
     * @param status
     *            The status to associate.
     * @param cause
     *            The wrapped cause error or exception.
     */
    public ResourceException(Status status, Throwable cause) {
        this(status, cause, null, null);
    }

    /**
     * Constructor.
     *
     * @param status
     *            The status to associate.
     * @param cause
     *            The wrapped cause error or exception.
     */
    public ResourceException(Status status, Throwable cause, Request request, Response response) {
        super((status == null) ? null : status.toString(), cause);
        this.status = status;
        this.request = request;
        this.response = response;
    }

    /**
     * Constructor that set the status to
     * {@link org.restlet.client.data.Status#SERVER_ERROR_INTERNAL} including the
     * related error or exception.
     *
     * @param cause
     *            The wrapped cause error or exception.
     */
    public ResourceException(Throwable cause) {
        this(new Status(Status.SERVER_ERROR_INTERNAL, cause), cause);
    }

    /**
     * Returns the request associated to this exception.
     *
     * @return The request associated to this exception.
     */
    public Request getRequest() {
        return this.request;
    }

    /**
     * Returns the response associated to this exception.
     *
     * @return The response associated to this exception.
     */
    public Response getResponse() {
        return this.response;
    }

    /**
     * Returns the status associated to this exception.
     *
     * @return The status associated to this exception.
     */
    public Status getStatus() {
        return this.status;
    }
}
