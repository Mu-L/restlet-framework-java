/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch05.sec4.server;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.ChallengeScheme;
import org.restlet.routing.Router;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.Role;
import org.restlet.security.RoleAuthorizer;

/**
 * The reusable mail server application.
 */
public class MailServerApplication extends Application {

    /**
     * Constructor.
     */
    public MailServerApplication() {
        setName("RESTful Mail Server application");
        setDescription("Example application for 'Restlet in Action' book");
        setOwner("QlikTech International AB");
        setAuthor("The Restlet Team");

        // Declare the supported roles
        getRoles().add(new Role(this, "CFO"));
        getRoles().add(new Role(this, "User"));
    }

    /**
     * Creates a root Router to dispatch call to server resources.
     */
    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());
        router.attach("/", RootServerResource.class);
        router.attach("/accounts/", AccountsServerResource.class);
        router.attach("/accounts/{accountId}", AccountServerResource.class);
        router.attach("/accounts/{accountId}/files", FilesServerResource.class);

        RoleAuthorizer authorizer = new RoleAuthorizer();
        authorizer.getAuthorizedRoles().add(getRole("CFO"));
        authorizer.setNext(router);

        ChallengeAuthenticator authenticator = new ChallengeAuthenticator(
                getContext(), ChallengeScheme.HTTP_BASIC, "My Realm");
        authenticator.setNext(authorizer);

        return authenticator;
    }
}
