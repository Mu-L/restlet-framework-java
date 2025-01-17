/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.tutorial;

import static org.restlet.example.tutorial.Constants.ROOT_URI;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.resource.Directory;
import org.restlet.routing.Router;
import org.restlet.routing.Template;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.MapVerifier;

/**
 * Routers and hierarchical URIs
 * 
 * @author Jerome Louvel
 */
public class Part11_Routing extends Application {

    /**
     * Run the example as a standalone component.
     * 
     * @param args
     *            The optional arguments.
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // Create a component
        Component component = new Component();
        component.getServers().add(Protocol.HTTP, 8111);
        component.getClients().add(Protocol.FILE);

        // Create an application
        Application application = new Part11_Routing();

        // Attach the application to the component and start it
        component.getDefaultHost().attach(application);
        component.start();
    }

    @Override
    public Restlet createInboundRoot() {
        // Create a root router
        Router router = new Router(getContext());

        // Create a simple password verifier
        MapVerifier verifier = new MapVerifier();
        verifier.getLocalSecrets().put("scott", "tiger".toCharArray());

        // Create a Guard
        // Attach a guard to secure access to the directory
        ChallengeAuthenticator guard = new ChallengeAuthenticator(getContext(),
                ChallengeScheme.HTTP_BASIC, "Tutorial");
        guard.setVerifier(verifier);
        router.attach("/docs/", guard).setMatchingMode(
                Template.MODE_STARTS_WITH);

        // Create a directory able to expose a hierarchy of files
        Directory directory = new Directory(getContext(), ROOT_URI);
        guard.setNext(directory);

        // Create the account handler
        Restlet account = new Restlet() {
            @Override
            public void handle(Request request, Response response) {
                // Print the requested URI path
                String message = "Account of user \""
                        + request.getAttributes().get("user") + "\"";
                response.setEntity(message, MediaType.TEXT_PLAIN);
            }
        };

        // Create the orders handler
        Restlet orders = new Restlet(getContext()) {
            @Override
            public void handle(Request request, Response response) {
                // Print the user name of the requested orders
                String message = "Orders of user \""
                        + request.getAttributes().get("user") + "\"";
                response.setEntity(message, MediaType.TEXT_PLAIN);
            }
        };

        // Create the order handler
        Restlet order = new Restlet(getContext()) {
            @Override
            public void handle(Request request, Response response) {
                // Print the user name of the requested orders
                String message = "Order \""
                        + request.getAttributes().get("order")
                        + "\" for user \""
                        + request.getAttributes().get("user") + "\"";
                response.setEntity(message, MediaType.TEXT_PLAIN);
            }
        };

        // Attach the handlers to the root router
        router.attach("/users/{user}", account);
        router.attach("/users/{user}/orders", orders);
        router.attach("/users/{user}/orders/{order}", order);

        // Return the root router
        return router;
    }

}
