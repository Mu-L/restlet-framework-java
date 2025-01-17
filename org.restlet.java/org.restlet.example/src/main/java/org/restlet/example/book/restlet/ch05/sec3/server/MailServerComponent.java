/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch05.sec3.server;

import org.restlet.Component;
import org.restlet.Server;
import org.restlet.data.Protocol;
import org.restlet.security.MemoryRealm;
import org.restlet.security.User;

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

        // Add a CLAP client connector
        getClients().add(Protocol.CLAP);

        // Adds a HTTP server connector
        Server server = getServers().add(Protocol.HTTP, 8111);
        server.getContext().getParameters().set("tracing", "true");

        // Attach the application to the default virtual host
        MailServerApplication app = new MailServerApplication();
        getDefaultHost().attachDefault(app);

        // Configure the security realm
        MemoryRealm realm = new MemoryRealm();
        User homer = new User("chunkylover53", "pwd", "Homer", "Simpson",
                "homer@simpson.org");
        realm.getUsers().add(homer);
        realm.map(homer, app.getRole("CFO"));
        realm.map(homer, app.getRole("User"));

        User marge = new User("bretzels34", "pwd", "Marge", "Simpson",
                "marge@simpson.org");
        realm.getUsers().add(marge);
        realm.map(marge, app.getRole("User"));

        User bart = new User("jojo10", "pwd", "Marge", "Simpson",
                "bart@simpson.org");
        realm.getUsers().add(bart);
        realm.map(bart, app.getRole("User"));

        User lisa = new User("lisa1984", "pwd", "Marge", "Simpson",
                "lisa@simpson.org");
        realm.getUsers().add(lisa);
        realm.map(lisa, app.getRole("User"));

        // Set the realm's default enroler and verifier
        app.getContext().setDefaultEnroler(realm.getEnroler());
        app.getContext().setDefaultVerifier(realm.getVerifier());

        // Configure the log service
        getLogService().setLoggerName("MailServer.AccessLog");
        getLogService()
                .setLogPropertiesRef(
                        "clap://system/org/restlet/example/book/restlet/ch05/sec3/server/log.properties");
    }
}
