/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.authentication;

import java.io.IOException;

import org.restlet.data.ChallengeRequest;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Status;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

public class HttpDigestAuthenticationClient {

    public static void main(String[] args) throws ResourceException,
            IOException {
        // Prepare the request
        ClientResource cr = new ClientResource("http://localhost:8182/");

        ChallengeRequest c1 = null;
        // first try: unauthenticated request
        try {
            cr.get();
        } catch (ResourceException re) {
            if (Status.CLIENT_ERROR_UNAUTHORIZED.equals(cr.getStatus())) {
                c1 = getDigestChallengeRequest(cr);
            }
        }

        // second try: authenticated request
        if (c1 != null) {
            ChallengeResponse c2 = new ChallengeResponse(c1, cr.getResponse(),
                    "scott", "tiger".toCharArray());
            cr.setChallengeResponse(c2);
            cr.get().write(System.out);
        }
    }

    private static ChallengeRequest getDigestChallengeRequest(ClientResource cr) {
        ChallengeRequest c1 = null;
        for (ChallengeRequest challengeRequest : cr.getChallengeRequests()) {
            if (ChallengeScheme.HTTP_DIGEST
                    .equals(challengeRequest.getScheme())) {
                c1 = challengeRequest;
                break;
            }
        }
        return c1;
    }
}
