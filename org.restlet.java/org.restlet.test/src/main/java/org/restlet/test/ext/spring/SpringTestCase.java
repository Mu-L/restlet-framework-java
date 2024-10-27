/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.ext.spring;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.restlet.Component;
import org.restlet.Server;
import org.restlet.test.RestletTestCase;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Unit test case for the Spring extension.
 * 
 * @author Jerome Louvel
 */
public class SpringTestCase extends RestletTestCase {

    @Test
    public void testSpring() throws Exception {
        // Load the Spring container
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
                "org/restlet/test/ext/spring/SpringTestCase.xml");

        // Start the Restlet component
        Component component = (Component) ctx.getBean("component");
        component.start();
        Thread.sleep(500);
        component.stop();
        ctx.close();
    }

    @Test
    public void testSpringServerProperties() {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
                "org/restlet/test/ext/spring/SpringTestCase.xml");
        Server server = (Server) ctx.getBean("server");

        assertEquals("value1", server.getContext().getParameters()
                .getFirstValue("key1"));
        assertEquals("value2", server.getContext().getParameters()
                .getFirstValue("key2"));
        ctx.close();
    }

}
