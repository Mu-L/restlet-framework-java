/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch05.sec2.digest;

import org.restlet.Client;
import org.restlet.Context;
import org.restlet.data.ChallengeRequest;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Parameter;
import org.restlet.data.Protocol;
import org.restlet.data.Status;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.restlet.util.Series;

/**
 * Mail client retrieving a mail then storing it again on the same resource.
 */
public class MailClient {

    public static void main(String[] args) throws Exception {
        // Create and configure HTTPS client
        Client client = new Client(new Context(), Protocol.HTTPS);
        Series<Parameter> parameters = client.getContext().getParameters();
        parameters.add("truststorePath",
                "src/org/restlet/example/book/restlet/ch05/clientTrust.jks");
        parameters.add("truststorePassword", "password");
        parameters.add("truststoreType", "JKS");

        // Create and configure client resource
        ClientResource clientResource = new ClientResource(
                "https://localhost:8183/accounts/chunkylover53/mails/123");
        clientResource.setNext(client);
        MailResource mailClient = clientResource.wrap(MailResource.class);

        try {
            // Obtain the authentication options via the challenge requests
            mailClient.retrieve();
        } catch (ResourceException re) {
            if (Status.CLIENT_ERROR_UNAUTHORIZED.equals(re.getStatus())) {
                // Retrieve the HTTP Digest hints from the server
                ChallengeRequest digestChallenge = null;

                for (ChallengeRequest challengeRequest : clientResource
                        .getChallengeRequests()) {
                    if (ChallengeScheme.HTTP_DIGEST.equals(challengeRequest
                            .getScheme())) {
                        digestChallenge = challengeRequest;
                        break;
                    }
                }

                // Configure the authentication credentials
                ChallengeResponse authentication = new ChallengeResponse(
                        digestChallenge, clientResource.getResponse(),
                        "chunkylover53", "pwd");
                clientResource.setChallengeResponse(authentication);
            }
        }

        // Communicate with remote resource
        mailClient.store(mailClient.retrieve());

        // Store HTTPS client
        client.stop();
    }
}
