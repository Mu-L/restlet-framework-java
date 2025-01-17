/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.engine.component;

import org.restlet.Context;
import org.restlet.engine.log.LogUtils;
import org.restlet.engine.util.ChildContext;

/**
 * Context allowing access to the component's connectors.
 * 
 * @author Jerome Louvel
 */
public class ComponentContext extends Context {

	/** The component helper. */
	private volatile ComponentHelper componentHelper;

	/**
	 * Constructor.
	 * 
	 * @param componentHelper The component helper.
	 */
	public ComponentContext(ComponentHelper componentHelper) {
		super(LogUtils.getLoggerName("org.restlet", componentHelper.getHelped()));
		this.componentHelper = componentHelper;
		setClientDispatcher(new ComponentClientDispatcher(this));
		setServerDispatcher(new ComponentServerDispatcher(this));
		setExecutorService(componentHelper.getHelped().getTaskService());
	}

	@Override
	public Context createChildContext() {
		return new ChildContext(getComponentHelper().getHelped().getContext());
	}

	/**
	 * Returns the component helper.
	 * 
	 * @return The component helper.
	 */
	protected ComponentHelper getComponentHelper() {
		return this.componentHelper;
	}

	/**
	 * Sets the component helper.
	 * 
	 * @param componentHelper The component helper.
	 */
	protected void setComponentHelper(ComponentHelper componentHelper) {
		this.componentHelper = componentHelper;
	}
}
