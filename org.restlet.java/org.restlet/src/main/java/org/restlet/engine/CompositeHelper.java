/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.engine;

import java.util.logging.Level;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Status;
import org.restlet.routing.Filter;

/**
 * Chain helper serving as base class for Application and Component helpers.
 * 
 * @author Jerome Louvel
 */
public abstract class CompositeHelper<T extends Restlet> extends RestletHelper<T> {

	/** The first inbound filter. */
	private volatile Filter firstInboundFilter;

	/** The first outbound Filter. */
	private volatile Filter firstOutboundFilter;

	/** The next Restlet after the inbound chain. */
	private volatile Restlet inboundNext;

	/** The last inbound filter. */
	private volatile Filter lastInboundFilter;

	/** The last outbound filter. */
	private volatile Filter lastOutboundFilter;

	/** The next Restlet after the outbound chain. */
	private volatile Restlet outboundNext;

	/**
	 * Constructor.
	 * 
	 * @param helped The helped Restlet.
	 */
	public CompositeHelper(T helped) {
		super(helped);
		this.inboundNext = null;
		this.firstInboundFilter = null;
		this.firstOutboundFilter = null;
		this.lastInboundFilter = null;
		this.lastOutboundFilter = null;
		this.outboundNext = null;
	}

	/**
	 * Adds a new inbound filter to the chain.
	 * 
	 * @param filter The inbound filter to add.
	 */
	protected synchronized void addInboundFilter(Filter filter) {
		Restlet next = getInboundNext();

		if (getFirstInboundFilter() == null) {
			setFirstInboundFilter(filter);
		} else if (getLastInboundFilter() != null) {
			getLastInboundFilter().setNext(filter);
		}

		setLastInboundFilter(filter);
		setInboundNext(next);
	}

	/**
	 * Adds a new outbound filter to the chain.
	 * 
	 * @param filter The outbound filter to add.
	 */
	protected synchronized void addOutboundFilter(Filter filter) {
		Restlet next = getOutboundNext();

		if (getFirstOutboundFilter() == null) {
			setFirstOutboundFilter(filter);
		} else if (getLastOutboundFilter() != null) {
			getLastOutboundFilter().setNext(filter);
		}

		setLastOutboundFilter(filter);
		setOutboundNext(next);
	}

	/**
	 * Clears the chain. Sets the first and last filters to null.
	 */
	public void clear() {
		setFirstInboundFilter(null);
		setFirstOutboundFilter(null);
		setInboundNext(null);
		setLastInboundFilter(null);
		setLastOutboundFilter(null);
		setOutboundNext(null);
	}

	/**
	 * Returns the first inbound filter.
	 * 
	 * @return The first inbound filter.
	 */
	public Filter getFirstInboundFilter() {
		return firstInboundFilter;
	}

	/**
	 * Returns the first outbound filter.
	 * 
	 * @return The first outbound filter.
	 */
	public Filter getFirstOutboundFilter() {
		return firstOutboundFilter;
	}

	/**
	 * Returns the next Restlet in the inbound chain.
	 * 
	 * @return The next Restlet in the inbound chain.
	 */
	protected synchronized Restlet getInboundNext() {
		Restlet result = null;

		if (getLastInboundFilter() != null) {
			result = getLastInboundFilter().getNext();
		} else {
			result = this.inboundNext;
		}

		return result;
	}

	/**
	 * Returns the last inbound filter.
	 * 
	 * @return the last inbound filter.
	 */
	protected Filter getLastInboundFilter() {
		return this.lastInboundFilter;
	}

	/**
	 * Returns the last outbound filter.
	 * 
	 * @return the last outbound filter.
	 */
	protected Filter getLastOutboundFilter() {
		return this.lastOutboundFilter;
	}

	/**
	 * Returns the next Restlet in the outbound chain.
	 * 
	 * @return The next Restlet in the outbound chain.
	 */
	public synchronized Restlet getOutboundNext() {
		Restlet result = null;

		if (getLastOutboundFilter() != null) {
			result = getLastOutboundFilter().getNext();
		} else {
			result = this.outboundNext;
		}

		return result;
	}

	@Override
	public void handle(Request request, Response response) {
		super.handle(request, response);

		if (getFirstInboundFilter() != null) {
			getFirstInboundFilter().handle(request, response);
		} else {
			final Restlet next = this.inboundNext;
			if (next != null) {
				next.handle(request, response);
			} else {
				response.setStatus(Status.SERVER_ERROR_INTERNAL);
				getHelped().getLogger().log(Level.SEVERE, "The " + getHelped().getClass().getName()
						+ " class has no Restlet defined to process calls. Maybe it wasn't properly started.");
			}
		}
	}

	/**
	 * Sets the first inbound filter.
	 * 
	 * @param firstInboundFilter The first inbound filter.
	 */
	protected void setFirstInboundFilter(Filter firstInboundFilter) {
		this.firstInboundFilter = firstInboundFilter;
	}

	/**
	 * Sets the first outbound filter.
	 * 
	 * @param firstOutboundFilter The first outbound filter.
	 */
	protected void setFirstOutboundFilter(Filter firstOutboundFilter) {
		this.firstOutboundFilter = firstOutboundFilter;
	}

	/**
	 * Sets the next Restlet after the inbound chain.
	 * 
	 * @param next The Restlet to process after the inbound chain.
	 */
	protected synchronized void setInboundNext(Restlet next) {
		if (getLastInboundFilter() != null) {
			getLastInboundFilter().setNext(next);
		}

		this.inboundNext = next;
	}

	/**
	 * Sets the last inbound filter.
	 * 
	 * @param last The last inbound filter.
	 */
	protected void setLastInboundFilter(Filter last) {
		this.lastInboundFilter = last;
	}

	/**
	 * Sets the last outbound filter.
	 * 
	 * @param last The last outbound filter.
	 */
	protected void setLastOutboundFilter(Filter last) {
		this.lastOutboundFilter = last;
	}

	/**
	 * Sets the next Restlet after the outbound chain.
	 * 
	 * @param next The Restlet to process after the outbound chain.
	 */
	protected synchronized void setOutboundNext(Restlet next) {
		if (getLastOutboundFilter() != null) {
			getLastOutboundFilter().setNext(next);
		}

		this.outboundNext = next;
	}

}
