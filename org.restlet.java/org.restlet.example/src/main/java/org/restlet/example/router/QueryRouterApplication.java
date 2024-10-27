/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.router;

import java.util.Map;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;
import org.restlet.routing.TemplateRoute;
import org.restlet.routing.Variable;

public class QueryRouterApplication extends Application {
    public static void main(String[] args) throws Exception {
        Component c = new Component();
        c.getServers().add(Protocol.HTTP, 8182);
        c.getDefaultHost().attach(new QueryRouterApplication());

        c.start();
    }

    @Override
    public Restlet createInboundRoot() {
        Router router = new QueryRouter(getContext());
        // restlet attached to /path?q=hello
        Restlet restletHello = new Restlet() {
            @Override
            public void handle(Request request, Response response) {
                response.setEntity("Hello.", MediaType.TEXT_PLAIN);
            }
        };

        // restlet attached to /path?q=bye
        Restlet restletBye = new Restlet() {
            @Override
            public void handle(Request request, Response response) {
                response.setEntity("Bye", MediaType.TEXT_PLAIN);
            }
        };

        // Defines two routes on the same path, but distinct query parameters.
        TemplateRoute logonRoute = router.attach("/path", restletHello);
        Map<String, Variable> logonVariables = logonRoute.getTemplate()
                .getVariables();
        // Take care of using the same query parameter names
        logonVariables.put("q", new Variable(Variable.TYPE_URI_QUERY, "hello",
                true, true));

        TemplateRoute logoutRoute = router.attach("/path", restletBye);
        Map<String, Variable> logoutVariables = logoutRoute.getTemplate()
                .getVariables();
        // Take care of using the same query parameter names
        logoutVariables.put("q", new Variable(Variable.TYPE_URI_QUERY, "bye",
                true, true));

        return router;
    }
}
