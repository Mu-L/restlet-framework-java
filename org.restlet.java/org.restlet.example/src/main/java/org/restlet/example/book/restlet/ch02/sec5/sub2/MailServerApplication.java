/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch02.sec5.sub2;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.Server;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;

/**
 * Routing to server resources.
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
        Server mailServer = new Server(Protocol.HTTP, 8111);
        mailServer.setNext(new MailServerApplication());
        mailServer.start();
    }

    /**
     * Constructor.
     */
    public MailServerApplication() {
        setName("RESTful Mail Server");
        setDescription("Example for 'Restlet in Action' book");
        setOwner("QlikTech International AB");
        setAuthor("The Restlet Team");
    }

    /**
     * Creates a root Router to dispatch call to server resources.
     */
    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());
        router.attach("http://localhost:8111/", RootServerResource.class);
        router.attach("http://localhost:8111/accounts/",
                AccountsServerResource.class);
        router.attach("http://localhost:8111/accounts/{accountId}",
                AccountServerResource.class);
        return router;
    }
}
