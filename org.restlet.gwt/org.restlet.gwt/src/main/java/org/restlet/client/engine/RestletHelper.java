/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.client.engine;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import org.restlet.client.Context;
import org.restlet.client.Request;
import org.restlet.client.Response;
import org.restlet.client.Restlet;
import org.restlet.client.data.Parameter;
import org.restlet.client.service.MetadataService;
import org.restlet.client.util.Series;

/**
 * Delegate used by API classes to get support from the implementation classes.
 * Note that this is an SPI class that is not intended for public usage.
 * 
 * @author Jerome Louvel
 */
public abstract class RestletHelper<T extends Restlet> extends Helper {

    /**
     * The map of attributes exchanged between the API and the Engine via this
     * helper.
     */
    private final Map<String, Object> attributes;

    /**
     * The helped Restlet.
     */
    private volatile T helped;

    /**
     * Constructor.
     * 
     * @param helped
     *            The helped Restlet.
     */
    public RestletHelper(T helped) {
        this.attributes = new ConcurrentHashMap<String, Object>();
        this.helped = helped;
    }

    /**
     * Returns the map of attributes exchanged between the API and the Engine
     * via this helper.
     * 
     * @return The map of attributes.
     */
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    /**
     * Returns the helped Restlet context.
     * 
     * @return The helped Restlet context.
     */
    public Context getContext() {
        return getHelped().getContext();
    }

    /**
     * Returns the helped Restlet.
     * 
     * @return The helped Restlet.
     */
    public T getHelped() {
        return this.helped;
    }

    /**
     * Returns the helped Restlet parameters.
     * 
     * @return The helped Restlet parameters.
     */
    public Series<Parameter> getHelpedParameters() {
        Series<Parameter> result = null;

        if ((getHelped() != null) && (getHelped().getContext() != null)) {
            result = getHelped().getContext().getParameters();
        } else {
             result = new org.restlet.client.engine.util.ParameterSeries();
        }

        return result;
    }

    /**
     * Returns the helped Restlet logger.
     * 
     * @return The helped Restlet logger.
     */
    public Logger getLogger() {
        if (getHelped() != null && getHelped().getContext() != null) {
            return getHelped().getContext().getLogger();
        }
        return Context.getCurrentLogger();
    }

    /**
     * Returns the metadata service. If the parent application doesn't exist, a
     * new instance is created.
     * 
     * @return The metadata service.
     */
    public MetadataService getMetadataService() {
        MetadataService result = null;


        if (result == null) {
            result = new MetadataService();
        }

        return result;
    }

    /**
     * Handles a call.
     * 
     * @param request
     *            The request to handle.
     * @param response
     *            The response to update.
     */
    public void handle(Request request, Response response) {
    }

    /**
     * Sets the helped Restlet.
     * 
     * @param helpedRestlet
     *            The helped Restlet.
     */
    public void setHelped(T helpedRestlet) {
        this.helped = helpedRestlet;
    }

    /** Start callback. */
    public abstract void start() throws Exception;

    /** Stop callback. */
    public abstract void stop() throws Exception;

    /**
     * Update callback with less impact than a {@link #stop()} followed by a
     * {@link #start()}.
     */
    public abstract void update() throws Exception;
}
