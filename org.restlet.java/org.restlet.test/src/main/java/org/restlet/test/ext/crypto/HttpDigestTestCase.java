/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.ext.crypto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.Server;
import org.restlet.data.ChallengeRequest;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.data.Status;
import org.restlet.ext.crypto.DigestAuthenticator;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.restlet.routing.Router;
import org.restlet.security.MapVerifier;
import org.restlet.test.RestletTestCase;

/**
 * Restlet unit tests for HTTP DIGEST authentication client/server.
 *
 * @author Jerome Louvel
 */
public class HttpDigestTestCase extends RestletTestCase {

    private Component component;

    private int port;

    private static class MyApplication extends Application {
        @Override
        public Restlet createInboundRoot() {
            Router router = new Router(getContext());

            DigestAuthenticator da = new DigestAuthenticator(getContext(),
                    "TestRealm", "mySecretServerKey");
            MapVerifier mapVerifier = new MapVerifier();
            mapVerifier.getLocalSecrets().put("scott", "tiger".toCharArray());
            da.setWrappedVerifier(mapVerifier);

            Restlet restlet = new Restlet(getContext()) {
                @Override
                public void handle(Request request, Response response) {
                    response.setEntity("hello, world", MediaType.TEXT_PLAIN);
                }
            };
            da.setNext(restlet);
            router.attach("/", da);
            return router;
        }
    }

    @BeforeEach
    protected void setUpEach() throws Exception {
        component = new Component();
        Server server = component.getServers().add(Protocol.HTTP, 0);
        Application application = new MyApplication();
        component.getDefaultHost().attach(application);
        component.start();
        port = server.getEphemeralPort();
    }

    @AfterEach
    protected void tearDownEach() throws Exception {
        component.stop();
        component = null;
    }

    @Test
    public void testDigest() throws Exception {
        ClientResource cr = new ClientResource("http://localhost:" + port + "/");

        // Try unauthenticated request
        try {
            cr.get();
        } catch (ResourceException re) {
            assertEquals(Status.CLIENT_ERROR_UNAUTHORIZED, cr.getStatus());

            ChallengeRequest c1 = null;
            for (ChallengeRequest challengeRequest : cr.getChallengeRequests()) {
                if (ChallengeScheme.HTTP_DIGEST.equals(challengeRequest
                        .getScheme())) {
                    c1 = challengeRequest;
                    break;
                }
            }
            assertEquals(ChallengeScheme.HTTP_DIGEST, c1.getScheme());

            String realm = c1.getRealm();
            assertEquals("TestRealm", realm);

            // String opaque = c1.getParameters().getFirstValue("opaque");
            // String qop = c1.getParameters().getFirstValue("qop");
            // assertEquals(null, opaque);
            // assertEquals("auth", qop);

            // Try authenticated request
            cr.getRequest().setMethod(Method.GET);
            ChallengeResponse c2 = new ChallengeResponse(c1, cr.getResponse(),
                    "scott", "tiger".toCharArray());
            cr.setChallengeResponse(c2);
            cr.get();
            assertTrue(cr.getStatus().isSuccess());
        }
    }
}
