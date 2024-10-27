/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch03.sec3.server;

import org.restlet.Component;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Creates a Restlet component and application based on a Spring XML
 * configuration and starts them.
 */
public class MailServerSpring {

    public static void main(String[] args) throws Exception {
        // Load the Spring container
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
                "org/restlet/example/book/restlet/ch03/sec3/server/component-spring.xml");

        // Start the Restlet component
        Component component = ctx.getBean("component", Component.class);
        component.start();
        ctx.close();
    }

}
