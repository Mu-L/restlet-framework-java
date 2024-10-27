/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.misc;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.Server;
import org.restlet.data.Header;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.engine.header.HeaderConstants;
import org.restlet.util.Series;

/**
 * Display the HTTP accept header sent by the Web browsers.
 * 
 * @author Jerome Louvel
 */
public class HeadersTest {
    public static void main(String[] args) throws Exception {
        final Restlet restlet = new Restlet() {
            @SuppressWarnings("unchecked")
            @Override
            public void handle(Request request, Response response) {
                // ------------------------------
                // Getting an HTTP request header
                // ------------------------------
                Series<Header> headers = (Series<Header>) request
                        .getAttributes().get(HeaderConstants.ATTRIBUTE_HEADERS);

                // The headers list contains all received HTTP headers, in raw
                // format.
                // Below, we simply display the standard "Accept" HTTP header.
                response.setEntity(
                        "Accept header: "
                                + headers.getFirstValue("accept", true),
                        MediaType.TEXT_PLAIN);

                // -----------------------
                // Adding response headers
                // -----------------------
                headers = new Series<Header>(Header.class);

                // Non-standard headers are allowed
                headers.add("X-Test", "Test value");

                // Standard HTTP headers are forbidden. If you happen to add one
                // like the "Location"
                // header below, it will be ignored and a warning message will
                // be displayed in the logs.
                headers.add("Location", "http://restlet.org");

                // Setting the additional headers into the shared call's
                // attribute
                response.getAttributes().put(HeaderConstants.ATTRIBUTE_HEADERS,
                        headers);
            }
        };

        // Create the HTTP server and listen on port 8111
        final Server server = new Server(Protocol.HTTP, 8111, restlet);
        server.start();
    }
}
