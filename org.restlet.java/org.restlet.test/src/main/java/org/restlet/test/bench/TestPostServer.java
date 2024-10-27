/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.bench;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.Server;
import org.restlet.data.Protocol;

public class TestPostServer {

    public static void main(String[] args) throws Exception {

        Server server = new Server(Protocol.HTTP, 8554, new Restlet() {
            int count = 0;

            @Override
            public void handle(Request request, Response response) {
                try {
                    System.out.println("Request received (" + (++count) + ")");
                    long expectedSize = request.getEntity().getSize();
                    long receivedSize = request.getEntity().exhaust();

                    System.out.println("Size expected: " + expectedSize);
                    System.out.println("Size consumed: " + receivedSize);

                    if ((expectedSize != -1) && (expectedSize != receivedSize)) {
                        System.out.println("ERROR: SOME BYTES WERE LOST!");
                    }
                    System.out
                            .println("--------------------------------------");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        server.start();
    }
}
