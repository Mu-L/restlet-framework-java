/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.ext.osgi;

import java.util.Dictionary;

import org.osgi.service.http.HttpContext;
import org.restlet.Application;
import org.restlet.Context;

/**
 * This is an OSGi service interface for registering Restlet applications with a
 * server servlet. Users are expected to register an instance as an OSGi
 * service. It is recommended that you use the {@link BaseApplicationProvider}
 * implementation. You may extend it if necessary, or for complete control,
 * provide your own implementation of {@link ApplicationProvider}. A server
 * servlet will be created and registered with the web container at the
 * specified alias. The application will then be registered with the servlet.
 * 
 * @author Bryan Hunt
 * @author Wolfgang Werner
 * @deprecated Will be removed in next major release.
 */
@Deprecated
public interface ApplicationProvider {
    /**
     * The key constant that may be used to look up the
     * {@link javax.servlet.ServletConfig} instance from the child context
     */
    String SERVLET_CONFIG_ATTRIBUTE = "javax.servlet.ServletConfig";

    /**
     * The key constant that may be used to look up the
     * {@link javax.servlet.ServletContext} instance from the child context
     */
    String SERVLET_CONTEXT_ATTRIBUTE = "org.restlet.ext.servlet.ServletContext";

    /**
     * @return the application to be register at the specified alias.
     */
    Application createApplication(Context context);

    /**
     * The alias is passed to
     * {@link org.osgi.service.http.HttpService#registerServlet(String alias, Servlet servlet, Dictionary initparams, HttpContext context)}
     * when the servlet is registered.
     * 
     * @return the alias used to register with the server servlet.
     */
    String getAlias();

    /**
     * The context is passed to
     * {@link org.osgi.service.http.HttpService#registerServlet(String alias, Servlet servlet, Dictionary initparams, HttpContext context)}
     * when the servlet is registered.
     * 
     * @return the context to use with the server servlet.
     */
    HttpContext getContext();

    /**
     * The parameters are passed to
     * {@link org.osgi.service.http.HttpService#registerServlet(String alias, Servlet servlet, Dictionary initparams, HttpContext context)}
     * when the servlet is registered.
     * 
     * @return the initialization parameters to use with the server servlet.
     */
    Dictionary<String, Object> getInitParms();
}
