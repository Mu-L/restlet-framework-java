/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.connector;

import org.junit.jupiter.api.Test;
import org.restlet.Restlet;
import org.restlet.Server;
import org.restlet.data.Protocol;
import org.restlet.test.RestletTestCase;

/**
 * Test the ability of a connector to be restarted.
 * 
 * @author Jerome Louvel
 */
public class RestartTestCase extends RestletTestCase {

	@Test
    public void testRestart() throws Exception {
        final int waitTime = 100;

        final Server connector = new Server(Protocol.HTTP, TEST_PORT,
                (Restlet) null);

        System.out.print("Starting connector... ");
        connector.start();
        System.out.println("done");
        Thread.sleep(waitTime);

        System.out.print("Stopping connector... ");
        connector.stop();
        System.out.println("done");
        Thread.sleep(waitTime);

        System.out.print("Restarting connector... ");
        connector.start();
        System.out.println("done");
        Thread.sleep(waitTime);
        connector.stop();
    }

}
