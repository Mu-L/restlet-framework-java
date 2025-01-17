/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.engine.util;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Protocol;
import org.restlet.routing.Filter;
import org.restlet.routing.Template;

/**
 * Filter that resolves URI templates in the target resource URI reference using
 * the request attributes.
 * 
 * Concurrency note: instances of this class or its subclasses can be invoked by
 * several threads at the same time and therefore must be thread-safe. You
 * should be especially careful when storing state as member variables.
 * 
 * @author Jerome Louvel
 */
public class TemplateDispatcher extends Filter {

	/**
	 * If the response entity comes back with no identifier, automatically set the
	 * request's resource reference's identifier. This is very useful to resolve
	 * relative references in XSLT for example.
	 */
	@Override
	protected void afterHandle(Request request, Response response) {
		if ((response.getEntity() != null) && (response.getEntity().getLocationRef() == null)) {
			response.getEntity().setLocationRef(request.getResourceRef().getTargetRef().toString());
		}
	}

	/**
	 * Handles the call after resolving any URI template on the request's target
	 * resource reference.
	 * 
	 * @param request  The request to handle.
	 * @param response The response to update.
	 */
	public int beforeHandle(Request request, Response response) {
		// Associate the response to the current thread
		Protocol protocol = request.getProtocol();

		if (protocol == null) {
			throw new UnsupportedOperationException("Unable to determine the protocol to use for this call.");
		}

		String targetUri = request.getResourceRef().toString(true, false);

		if (targetUri.contains("{")) {
			// Template URI detected, create the template
			Template template = new Template(targetUri);

			// Set the formatted target URI
			request.setResourceRef(template.format(request, response));
		}

		request.setOriginalRef(ReferenceUtils.getOriginalRef(request.getResourceRef(), request.getHeaders()));
		return CONTINUE;
	}

}
