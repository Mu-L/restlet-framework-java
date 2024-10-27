/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.misc;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.resource.Directory;

/**
 * HTTP server exposing a Directory of resources based on a local CLAP client
 * (ClassLoader Access Protocol).
 * 
 * @author Jerome Louvel
 */
public class ClapTest {
    public static void main(String[] args) throws Exception {

        final Component component = new Component();
        component.getServers().add(Protocol.HTTP, 8111);
        component.getClients().add(Protocol.CLAP);

        final Application application = new Application() {

            @Override
            public Restlet createInboundRoot() {
                getConnectorService().getClientProtocols().add(Protocol.CLAP);
                getConnectorService().getServerProtocols().add(Protocol.HTTP);

                final Directory directory = new Directory(getContext(),
                        "clap://class");
                directory.setListingAllowed(true);
                directory.setDeeplyAccessible(true);

                return directory;
            }
        };

        component.getDefaultHost().attach(application);
        component.start();
    }
}
