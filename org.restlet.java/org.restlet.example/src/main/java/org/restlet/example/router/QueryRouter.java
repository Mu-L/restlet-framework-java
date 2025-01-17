/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.router;

import java.util.Map.Entry;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.routing.Route;
import org.restlet.routing.Router;
import org.restlet.routing.TemplateRoute;
import org.restlet.routing.Variable;

public class QueryRouter extends Router {
    public QueryRouter(Context context) {
        super(context);
        // Allows you to customize the routing logic.
        setRoutingMode(Router.MODE_CUSTOM);
        // Allows to calculate the score of all routes without taking into
        // account the query part
        setDefaultMatchingQuery(false);
    }

    /**
     * Mix the logic based on URI (except the query part) and the logic based on
     * query parameters.
     */
    @Override
    protected Route getCustom(Request request, Response response) {
        Form form = request.getResourceRef().getQueryAsForm();

        Route result = null;

        float bestScore = 0F;
        float score;

        for (Route route : getRoutes()) {
            TemplateRoute current = (TemplateRoute) route;
            // Logic based on the beginning of the route (i.e. all before the
            // query string)
            score = current.score(request, response);
            if ((score > bestScore)) {

                // Add the logic based on the variables values.
                // Check that all the variables values are correct
                boolean fit = true;
                for (Entry<String, Variable> entry : current.getTemplate()
                        .getVariables().entrySet()) {
                    String formValue = form.getFirstValue(entry.getKey());
                    if (formValue == null
                            || !formValue.equals(entry.getValue()
                                    .getDefaultValue())) {
                        fit = false;
                        break;
                    }
                }
                if (fit) {
                    bestScore = score;
                    result = current;
                }
            }
        }

        return result;
    }
}
