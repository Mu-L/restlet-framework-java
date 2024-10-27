/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch07.sec4.sub4;

import org.restlet.Component;
import org.restlet.data.Protocol;

/**
 * Server setting cache control information.
 */
public class CachingServer {

    public static void main(String[] args) throws Exception {
        Component component = new Component();
        component.getDefaultHost().attachDefault(CachingServerResource.class);
        component.getServers().add(Protocol.HTTP, 8111);
        component.start();
    }

}
