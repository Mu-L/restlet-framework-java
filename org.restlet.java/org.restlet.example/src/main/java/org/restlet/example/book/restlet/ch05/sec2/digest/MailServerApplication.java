/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch05.sec2.digest;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.Server;
import org.restlet.data.Parameter;
import org.restlet.data.Protocol;
import org.restlet.ext.crypto.DigestAuthenticator;
import org.restlet.routing.Router;
import org.restlet.security.MapVerifier;
import org.restlet.util.Series;

/**
 * Application using HTTP Digest authentication to protect its resources.
 */
public class MailServerApplication extends Application {

    /**
     * Launches the application with an HTTP server.
     * 
     * @param args
     *            The arguments.
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Component mailServer = new Component();

        // Configure the HTTPS server with the SSL certificates
        Server server = mailServer.getServers().add(Protocol.HTTPS, 8183);
        Series<Parameter> parameters = server.getContext().getParameters();
        parameters.add("keystorePath",
                "src/org/restlet/example/book/restlet/ch05/serverKey.jks");
        parameters.add("keystorePassword", "password");
        parameters.add("keystoreType", "JKS");
        parameters.add("keyPassword", "password");

        mailServer.getDefaultHost().attach(new MailServerApplication());
        mailServer.start();
    }

    /**
     * Creates a root Router to dispatch call to server resources.
     */
    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());
        router.attach("/accounts/{accountId}/mails/{mailId}",
                MailServerResource.class);

        // Create the HTTP Digest authenticator
        DigestAuthenticator authenticator = new DigestAuthenticator(
                getContext(), "My Realm", "My Server Key");

        // Set the credentials verifier
        MapVerifier verifier = new MapVerifier();
        verifier.getLocalSecrets().put("chunkylover53", "pwd".toCharArray());
        authenticator.setWrappedVerifier(verifier);

        // Chain the authenticator with the router
        authenticator.setNext(router);
        return authenticator;
    }
}
