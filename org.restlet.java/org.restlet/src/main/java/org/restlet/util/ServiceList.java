/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.util;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;

import org.restlet.Context;
import org.restlet.service.Service;

/**
 * Modifiable list of services.
 * 
 * @author Jerome Louvel
 */
public final class ServiceList extends WrapperList<Service> {

	/** The context. */
	private volatile Context context;

	/**
	 * Constructor.
	 * 
	 * @param context The context.
	 */
	public ServiceList(Context context) {
		super(new CopyOnWriteArrayList<Service>());
		this.context = context;
	}

	@Override
	public void add(int index, Service service) {
		service.setContext(getContext());
		super.add(index, service);
	}

	@Override
	public boolean add(Service service) {
		service.setContext(getContext());
		return super.add(service);
	}

	@Override
	public boolean addAll(Collection<? extends Service> services) {
		if (services != null) {
			for (Service service : services) {
				service.setContext(getContext());
			}
		}

		return super.addAll(services);
	}

	@Override
	public boolean addAll(int index, Collection<? extends Service> services) {
		if (services != null) {
			for (Service service : services) {
				service.setContext(getContext());
			}
		}

		return super.addAll(index, services);
	}

	/**
	 * Returns a service matching a given service class.
	 * 
	 * @param <T>   The service type.
	 * @param clazz The service class to match.
	 * @return The matched service instance.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Service> T get(Class<T> clazz) {
		for (Service service : this) {
			if (clazz.isAssignableFrom(service.getClass())) {
				return (T) service;
			}
		}

		return null;
	}

	/**
	 * Returns the context.
	 * 
	 * @return The context.
	 */
	public Context getContext() {
		return this.context;
	}

	/**
	 * Sets the list of services.
	 * 
	 * @param services The list of services.
	 */
	public synchronized void set(List<Service> services) {
		clear();

		if (services != null) {
			addAll(services);
		}
	}

	/**
	 * Replaces or adds a service. The replacement is based on the service class.
	 * 
	 * @param newService The new service to set.
	 */
	public synchronized void set(Service newService) {
		List<Service> services = new CopyOnWriteArrayList<Service>();
		Service service;
		boolean replaced = false;

		for (int i = 0; (i < size()); i++) {
			service = get(i);

			if (service != null) {
				if (service.getClass().isAssignableFrom(newService.getClass())) {
					try {
						service.stop();
					} catch (Exception e) {
						Context.getCurrentLogger().log(Level.WARNING, "Unable to stop service replaced", e);
					}

					services.add(newService);
					replaced = true;
				} else {
					services.add(service);
				}
			}
		}

		if (!replaced) {
			services.add(newService);
		}

		set(services);
	}

	/**
	 * Sets the context. By default, it also updates the context of already
	 * registered services.
	 * 
	 * @param context The context.
	 */
	public void setContext(Context context) {
		this.context = context;

		for (Service service : this) {
			service.setContext(context);
		}
	}

	/**
	 * Starts each service.
	 * 
	 * @throws Exception
	 */
	public void start() throws Exception {
		for (Service service : this) {
			service.start();
		}
	}

	/**
	 * Stops each service.
	 * 
	 * @throws Exception
	 */
	public void stop() throws Exception {
		for (Service service : this) {
			service.stop();
		}
	}

}
