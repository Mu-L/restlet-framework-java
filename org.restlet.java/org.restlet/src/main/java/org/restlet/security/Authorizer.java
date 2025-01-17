/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.security;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.ClientInfo;
import org.restlet.data.Status;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Filter;

/**
 * Filter authorizing inbound request. It can be attached to protect a set of
 * downstream {@link Restlet} and {@link ServerResource} objects.
 * 
 * @author Jerome Louvel
 */
public abstract class Authorizer extends Filter {

	/** Authorizer returning true all the time. */
	public static final Authorizer ALWAYS = new Authorizer() {
		@Override
		public boolean authorize(Request request, Response response) {
			return true;
		}
	};

	/**
	 * Authorizer returning true for all authenticated requests. For unauthenticated
	 * requests, it sets the response's status to
	 * {@link Status#CLIENT_ERROR_UNAUTHORIZED} instead of the default
	 * {@link Status#CLIENT_ERROR_FORBIDDEN}.
	 * 
	 * @see ClientInfo#isAuthenticated()
	 */
	public static final Authorizer AUTHENTICATED = new Authorizer() {
		@Override
		public boolean authorize(Request request, Response response) {
			return request.getClientInfo().isAuthenticated();
		}

		@Override
		protected int unauthorized(Request request, Response response) {
			response.setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
			return STOP;
		}
	};

	/** Authorizer returning false all the time. */
	public static final Authorizer NEVER = new Authorizer() {
		@Override
		public boolean authorize(Request request, Response response) {
			return false;
		}
	};

	/** The identifier unique within an application. */
	private volatile String identifier;

	/**
	 * Default constructor.
	 */
	public Authorizer() {
	}

	/**
	 * Constructor.
	 * 
	 * @param identifier The identifier unique within an application.
	 */
	public Authorizer(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * Attempts to authorize the request.
	 * 
	 * @param request  The request sent.
	 * @param response The response to update.
	 * @return True if the authorization succeeded.
	 */
	protected abstract boolean authorize(Request request, Response response);

	/**
	 * Invoked upon successful authorization. Returns {@link Filter#CONTINUE} by
	 * default.
	 * 
	 * @param request  The request sent.
	 * @param response The response to update.
	 * @return The filter continuation code.
	 */
	protected int authorized(Request request, Response response) {
		return CONTINUE;
	}

	@Override
	protected int beforeHandle(Request request, Response response) {
		if (authorize(request, response)) {
			return authorized(request, response);
		}

		return unauthorized(request, response);
	}

	/**
	 * Returns the identifier unique within an application.
	 * 
	 * @return The identifier unique within an application.
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Sets the identifier unique within an application.
	 * 
	 * @param identifier The identifier unique within an application.
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * Invoked upon failed authorization. Sets the status to
	 * {@link Status#CLIENT_ERROR_FORBIDDEN} and returns {@link Filter#STOP} by
	 * default.
	 * 
	 * @param request  The request sent.
	 * @param response The response to update.
	 * @return The filter continuation code.
	 */
	protected int unauthorized(Request request, Response response) {
		response.setStatus(Status.CLIENT_ERROR_FORBIDDEN);
		return STOP;
	}

}
