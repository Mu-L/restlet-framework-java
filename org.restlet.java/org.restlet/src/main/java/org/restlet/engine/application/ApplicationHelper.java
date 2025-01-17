/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.engine.application;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.restlet.Application;
import org.restlet.Client;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.engine.CompositeHelper;
import org.restlet.routing.Filter;
import org.restlet.service.Service;

/**
 * Application implementation.
 * 
 * @author Jerome Louvel
 */
public class ApplicationHelper extends CompositeHelper<Application> {
	/**
	 * Constructor.
	 * 
	 * @param application The application to help.
	 */
	public ApplicationHelper(Application application) {
		super(application);
	}

	/**
	 * In addition to the default behavior, it saves the current application
	 * instance into the current thread.
	 * 
	 * @param request  The request to handle.
	 * @param response The response to update.
	 */
	@Override
	public void handle(Request request, Response response) {
		// Save the current application
		// Plan to move current application as attribute of the Response in incoming 2.5
		// release
		final Application currentApplication = getHelped() != null ? getHelped() : Application.getCurrent();
		Application.setCurrent(currentApplication);

		// Actually handle call
		try {
			super.handle(request, response);
		} finally {
			// restore the current application
			Application.setCurrent(currentApplication);
		}
	}

	/**
	 * Sets the context.
	 * 
	 * @param context The context.
	 */
	public void setContext(Context context) {
		if (context != null) {
			setOutboundNext(context.getClientDispatcher());
		}
	}

	/** Start hook. */
	@Override
	public synchronized void start() throws Exception {
		Filter filter = null;

		for (Service service : getHelped().getServices()) {
			if (service.isEnabled()) {
				// Attach the service inbound filters
				filter = service.createInboundFilter((getContext() == null) ? null : getContext().createChildContext());

				if (filter != null) {
					addInboundFilter(filter);
				}

				// Attach the service outbound filters
				filter = service
						.createOutboundFilter((getContext() == null) ? null : getContext().createChildContext());

				if (filter != null) {
					addOutboundFilter(filter);
				}
			}
		}

		// Attach the Application's server root Restlet
		setInboundNext(getHelped().getInboundRoot());

		if (getOutboundNext() == null) {
			// Warn about chaining problem
			getLogger().fine(
					"By default, an application should be attached to a parent component in order to let application's outbound root handle calls properly.");
			setOutboundNext(new Restlet() {
				Map<Protocol, Client> clients = new ConcurrentHashMap<Protocol, Client>();

				@Override
				public void handle(Request request, Response response) {
					Protocol rProtocol = request.getProtocol();
					Reference rReference = request.getResourceRef();
					Protocol protocol = (rProtocol != null) ? rProtocol
							: (rReference != null) ? rReference.getSchemeProtocol() : null;

					if (protocol != null) {
						Client c = clients.get(protocol);

						if (c == null) {
							c = new Client(protocol);
							clients.put(protocol, c);
							getLogger().fine("Added runtime client for protocol: " + protocol.getName());
						}

						c.handle(request, response);
					} else {
						response.setStatus(Status.SERVER_ERROR_INTERNAL,
								"The server isn't properly configured to handle client calls.");
						getLogger()
								.warning("There is no protocol detected for this request: " + request.getResourceRef());
					}
				}

				@Override
				public synchronized void stop() throws Exception {
					super.stop();
					for (Client client : clients.values()) {
						client.stop();
					}
				}
			});
		}
	}

	@Override
	public synchronized void stop() throws Exception {
		clear();
	}

	@Override
	public void update() throws Exception {
	}

}
