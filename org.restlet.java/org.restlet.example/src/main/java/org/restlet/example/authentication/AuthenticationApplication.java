/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.authentication;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Protocol;
import org.restlet.resource.Directory;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.MapVerifier;

public class AuthenticationApplication extends Application {

    public static void main(String[] args) throws Exception {
        Component c = new Component();
        // server listening on pport 8182
        c.getServers().add(Protocol.HTTP, 8182);
        // client connector required by the Directory.
        c.getClients().add(Protocol.FILE);
        c.getDefaultHost().attach(new AuthenticationApplication());

        c.start();
    }

    @Override
    public Restlet createInboundRoot() {
        // Create a simple password verifier
        MapVerifier verifier = new MapVerifier();
        verifier.getLocalSecrets().put("scott", "tiger".toCharArray());

        // Create a guard
        ChallengeAuthenticator guard = new ChallengeAuthenticator(getContext(),
                ChallengeScheme.HTTP_BASIC, "Tutorial");
        guard.setVerifier(verifier);

        // Create a Directory able to return a deep hierarchy of files
        Directory directory = new Directory(getContext(), "file:///tmp");
        directory.setListingAllowed(true);
        guard.setNext(directory);

        return guard;
    }

}
