/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.authentication;

import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Status;
import org.restlet.resource.ClientResource;

public class AuthenticationClient {

    public static void main(String[] args) {
        // Prepare the request
        ClientResource resource = new ClientResource("http://localhost:8182/");

        // Add the client authentication to the call
        ChallengeScheme scheme = ChallengeScheme.HTTP_BASIC;
        ChallengeResponse authentication = new ChallengeResponse(scheme,
                "scott", "tiger");
        resource.setChallengeResponse(authentication);

        try {
            // Send the HTTP GET request
            resource.get();
            // Output the response entity on the JVM console
            resource.getResponseEntity().write(System.out);
        } catch (Exception e) {
            if (Status.CLIENT_ERROR_UNAUTHORIZED.equals(resource.getStatus())) {
                // Unauthorized access
                System.out
                        .println("Access unauthorized by the server, check your credentials");
            } else {
                // Unexpected status
                System.out.println("An unexpected status was returned: "
                        + resource.getStatus());
            }
        }
    }
}
