/*
 * Copyright 2005-2006 Noelios Consulting.
 *
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the "License").  You may not use this file except
 * in compliance with the License.
 *
 * You can obtain a copy of the license at
 * http://www.opensource.org/licenses/cddl1.txt
 * See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * HEADER in each file and include the License file at
 * http://www.opensource.org/licenses/cddl1.txt
 * If applicable, add the following below this CDDL
 * HEADER, with the fields enclosed by brackets "[]"
 * replaced with your own identifying information:
 * Portions Copyright [yyyy] [name of copyright owner]
 */

package com.noelios.restlet.container;

import org.restlet.Container;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Representation;

import com.noelios.restlet.StatusFilter;

/**
 * Status filter that tries to obtain ouput representation from an application.
 * 
 * @author Jerome Louvel (contact@noelios.com)
 */
public class ContainerStatusFilter extends StatusFilter {
	/** The container. */
	private Container container;

	/**
	 * Constructor.
	 * 
	 * @param container
	 *            The container.
	 */
	public ContainerStatusFilter(Container container) {
		super(container.getContext(), container.getStatusService()
				.isOverwrite(), container.getStatusService().getContactEmail(),
				"/");
		this.container = container;
	}

	/**
	 * Returns the container.
	 * 
	 * @return The container.
	 */
	public Container getContainer() {
		return this.container;
	}

	/**
	 * Returns a representation for the given status.<br/> In order to
	 * customize the default representation, this method can be overriden.
	 * 
	 * @param status
	 *            The status to represent.
	 * @param request
	 *            The request handled.
	 * @param response
	 *            The response updated.
	 * @return The representation of the given status.
	 */
	public Representation getRepresentation(Status status, Request request,
			Response response) {
		Representation result = getContainer().getStatusService()
				.getRepresentation(status, request, response);
		if (result == null)
			result = super.getRepresentation(status, request, response);
		return result;
	}

}
