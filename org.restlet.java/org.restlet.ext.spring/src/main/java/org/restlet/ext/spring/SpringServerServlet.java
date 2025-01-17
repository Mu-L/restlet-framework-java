/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.ext.spring;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Context;
import org.restlet.ext.servlet.ServerServlet;
import org.springframework.beans.BeansException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Spring specific ServerServlet adapter. This class is similar to the
 * ServerServlet, but instead of creating the used Restlet Application and
 * Restlet Component, it lookups them up from the SpringContext which is found
 * in the ServletContext.
 * 
 * If the Application or Component beans can't be found, the default behavior of
 * the parent class is used.
 * 
 * @author Florian Schwarz
 */
public class SpringServerServlet extends ServerServlet {

    /**
     * Name of the Servlet parameter containing a bean-id of the application to
     * use.
     */
    public static final String APPLICATION_BEAN_PARAM_NAME = "org.restlet.application";

    /**
     * Name of the Servlet parameter containing a bean-id of the component to
     * use.
     */
    public static final String Component_BEAN_PARAM_NAME = "org.restlet.component";

    private static final long serialVersionUID = 110030403435929871L;

    /**
     * Lookups the single Restlet Application used by this Servlet from the
     * SpringContext inside the ServletContext. The bean name looked up is
     * {@link #APPLICATION_BEAN_PARAM_NAME}.
     * 
     * @param parentContext
     *            The parent component context.
     * @return The Restlet-Application to use.
     */
    @Override
    public Application createApplication(Context parentContext) {
        Application application = null;

        final String applicationBeanName = getInitParameter(
                SpringServerServlet.APPLICATION_BEAN_PARAM_NAME, null);
        application = (Application) getWebApplicationContext().getBean(
                applicationBeanName);

        if (application != null) {
            // Set the context based on the Servlet's context
            application.setContext(parentContext.createChildContext());
        } else {
            application = super.createApplication(parentContext);
        }

        return application;
    }

    /**
     * Lookups the single Restlet Component used by this Servlet from Spring's
     * Context available inside the ServletContext. The bean name looked up is
     * {@link #Component_BEAN_PARAM_NAME}.
     * 
     * @return The Restlet-Component to use.
     */
    @Override
    public Component createComponent() {
        Component component = null;
        final String componentBeanName = getInitParameter(
                Component_BEAN_PARAM_NAME, null);

        // Not mentioned in the Spring JavaDocs, but getBean surely fails if
        // the argument is null.
        if (componentBeanName != null) {
            try {
                component = (Component) getWebApplicationContext().getBean(
                        componentBeanName);
            } catch (BeansException be) {
                // The bean has not been found, let the parent create it.
            }
        }

        if (component == null) {
            component = super.createComponent();
        }

        return component;
    }

    /**
     * Get the Spring WebApplicationContext from the ServletContext. (by hand
     * would be webApplicationContext applicationContext =
     * (WebApplicationContext)
     * getServletContext().getAttribute(WebApplicationContext
     * .ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);)
     * 
     * @return The Spring WebApplicationContext.
     */
    public WebApplicationContext getWebApplicationContext() {
        return WebApplicationContextUtils
                .getRequiredWebApplicationContext(getServletContext());
    }

}
