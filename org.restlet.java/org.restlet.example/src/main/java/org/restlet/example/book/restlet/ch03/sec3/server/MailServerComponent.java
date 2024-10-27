/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch03.sec3.server;

import org.restlet.Client;
import org.restlet.Component;
import org.restlet.Context;
import org.restlet.Server;
import org.restlet.data.Protocol;
import org.restlet.routing.VirtualHost;

/**
 * RESTful component containing the mail server application.
 */
public class MailServerComponent extends Component {

    /**
     * Launches the mail server component.
     * 
     * @param args
     *            The arguments.
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        new MailServerComponent().start();
    }

    /**
     * Constructor.
     * 
     * @throws Exception
     */
    public MailServerComponent() throws Exception {
        // Set basic properties
        setName("RESTful Mail Server component");
        setDescription("Example for 'Restlet in Action' book");
        setOwner("QlikTech International AB");
        setAuthor("The Restlet Team");

        // Add connectors
        getClients().add(new Client(Protocol.CLAP));
        Server server = new Server(new Context(), Protocol.HTTP, 8111);
        server.getContext().getParameters().set("tracing", "true");
        getServers().add(server);

        // Configure the default virtual host
        VirtualHost host = getDefaultHost();
        // host.setHostDomain("www\\.rmep\\.com|www\\.rmep\\.net|www\\.rmep\\.org");
        // host.setServerAddress("1\\.2\\.3\\.10|1\\.2\\.3\\.20");
        // host.setServerPort("80");

        // Attach the application to the default virtual host
        host.attachDefault(new MailServerApplication());

        // Configure the log service
        getLogService().setLoggerName("MailServer.AccessLog");
        getLogService()
                .setLogPropertiesRef(
                        "clap://system/org/restlet/example/book/restlet/ch03/sec3/server/log.properties");
    }
}
