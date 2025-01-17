/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.ext.servlet.internal;

import javax.servlet.ServletContext;

import org.restlet.Client;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Protocol;
import org.restlet.engine.local.Entity;
import org.restlet.engine.local.EntityClientHelper;

/**
 * Client connector based on a Servlet context (JEE Web application context).
 * Here is a sample resource URI:<br>
 * 
 * <pre>
 * war:///path/to/my/resource/entry.txt
 * </pre>
 * 
 * <br>
 * You can note that there is no authority which is denoted by the sequence of
 * three "/" characters. This connector is designed to be used inside a context
 * (e.g. inside a servlet based application) and subconsequently does not
 * require the use of a authority. Such URI are "relative" to the root of the
 * servlet context.<br>
 * Here is a sample code excerpt that illustrates the way to use this connector:
 * 
 * <code>Response response = getContext().getClientDispatcher().get("war:///myDir/test.txt");
 if (response.isEntityAvailable()) {
 //Do what you want to do.
 }
 </code>
 * 
 * @author Jerome Louvel
 */
public class ServletWarClientHelper extends EntityClientHelper {

    /** The Servlet context to use. */
    private volatile ServletContext servletContext;

    /**
     * Constructor.
     * 
     * @param client
     *            The client to help.
     * @param servletContext
     *            The Servlet context.
     */
    public ServletWarClientHelper(Client client, ServletContext servletContext) {
        super(client);
        getProtocols().clear();
        getProtocols().add(Protocol.WAR);
        this.servletContext = servletContext;
    }

    @Override
    public Entity getEntity(String decodedPath) {
        return new ServletWarEntity(getServletContext(), decodedPath,
                getMetadataService());
    }

    /**
     * Returns the Servlet context.
     * 
     * @return The Servlet context.
     */
    public ServletContext getServletContext() {
        return this.servletContext;
    }

    @Override
    public void handleLocal(Request request, Response response,
            String decodedPath) {
        String scheme = request.getResourceRef().getScheme();

        if (Protocol.WAR.getSchemeName().equalsIgnoreCase(scheme)) {
            super.handleLocal(request, response, decodedPath);
        } else {
            throw new IllegalArgumentException(
                    "Protocol \""
                            + scheme
                            + "\" not supported by the connector. Only WAR is supported.");
        }
    }

}
