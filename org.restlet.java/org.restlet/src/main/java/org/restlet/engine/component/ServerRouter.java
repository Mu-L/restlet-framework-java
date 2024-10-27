/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.engine.component;

import java.util.logging.Level;

import org.restlet.Component;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Status;
import org.restlet.routing.Router;
import org.restlet.routing.VirtualHost;

/**
 * Router that collects calls from all server connectors and dispatches them to
 * the appropriate host routers. The host routers then dispatch them to the user
 * applications.
 * 
 * Concurrency note: instances of this class or its subclasses can be invoked by
 * several threads at the same time and therefore must be thread-safe. You
 * should be especially careful when storing state in member variables.
 * 
 * @author Jerome Louvel
 */
public class ServerRouter extends Router {

	/** The parent component. */
	private volatile Component component;

	/**
	 * Constructor.
	 * 
	 * @param component The parent component.
	 */
	public ServerRouter(Component component) {
		super((component == null) ? null : component.getContext().createChildContext());
		this.component = component;
		setRoutingMode(MODE_FIRST_MATCH);
	}

	/**
	 * Returns the parent component.
	 * 
	 * @return The parent component.
	 */
	private Component getComponent() {
		return this.component;
	}

	@Override
	protected void logRoute(org.restlet.routing.Route route) {
		if (getLogger().isLoggable(Level.FINE)) {
			if (route instanceof HostRoute) {
				VirtualHost vhost = ((HostRoute) route).getVirtualHost();

				if (getComponent().getDefaultHost() == vhost) {
					getLogger().fine("Default virtual host selected");
				} else {
					getLogger().fine("Virtual host selected: \"" + vhost.getHostScheme() + "\", \""
							+ vhost.getHostDomain() + "\", \"" + vhost.getHostPort() + "\"");
				}
			} else {
				super.logRoute(route);
			}
		}
	}

	/** Starts the Restlet. */
	@Override
	public synchronized void start() throws Exception {
		// Attach all virtual hosts
		for (VirtualHost host : getComponent().getHosts()) {
			getRoutes().add(new HostRoute(this, host));
		}

		// Also attach the default host if it exists
		if (getComponent().getDefaultHost() != null) {
			getRoutes().add(new HostRoute(this, getComponent().getDefaultHost()));
		}

		// If no host matches, display and error page with a precise message
		final Restlet noHostMatched = new Restlet(getComponent().getContext().createChildContext()) {
			@Override
			public void handle(Request request, Response response) {
				response.setStatus(Status.CLIENT_ERROR_NOT_FOUND, "No virtual host could handle the request");
			}
		};

		setDefaultRoute(new org.restlet.routing.TemplateRoute(this, "", noHostMatched));

		// Start the router
		super.start();
	}

	@Override
	public synchronized void stop() throws Exception {
		getRoutes().clear();
		super.stop();
	}
}
