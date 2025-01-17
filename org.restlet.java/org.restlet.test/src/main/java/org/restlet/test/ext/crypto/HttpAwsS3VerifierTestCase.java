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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.restlet.Request;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Header;
import org.restlet.data.Method;
import org.restlet.engine.header.HeaderConstants;
import org.restlet.ext.crypto.internal.AwsUtils;
import org.restlet.ext.crypto.internal.AwsVerifier;
import org.restlet.security.LocalVerifier;
import org.restlet.security.Verifier;
import org.restlet.test.RestletTestCase;
import org.restlet.util.Series;

/**
 * Unit tests for {@link AwsVerifier}.
 *
 * @author Jean-Philippe Steinmetz <caskater47@gmail.com>
 */
public class HttpAwsS3VerifierTestCase extends RestletTestCase {
    private static final String ACCESS_ID = "0PN5J17HBGZHT7JJ3X82";

    private static final String ACCESS_KEY = "uV3F3YluFJax1cknvbcGwgjvx4QpvB+leU8dUj2o";

    private AwsVerifier awsVerifier;

    private LocalVerifier localVerifier;

    private Request createRequest() {
        Request request = new Request();
        Series<Header> headers = new Series<Header>(Header.class);
        request.getAttributes().put(HeaderConstants.ATTRIBUTE_HEADERS, headers);
        request.setMethod(Method.GET);
        request.setResourceRef("http://johnsmith.s3.amazonaws.com/photos/puppy.jpg");

        return request;
    }

    @BeforeEach
    public void setUpEach() throws Exception {
        localVerifier = new LocalVerifier() {
            @Override
            public char[] getLocalSecret(String identifier) {
                if (ACCESS_ID.equals(identifier))
                    return ACCESS_KEY.toCharArray();
                else
                    return "password".toCharArray();
            }
        };

        awsVerifier = new AwsVerifier(localVerifier);
    }

    @AfterEach
    public void tearDownEach() throws Exception {
        awsVerifier = null;
        localVerifier = null;
    }

    @Test
    public void testVerify() {
        Request request = createRequest();
        @SuppressWarnings("unchecked")
        Series<Header> headers = (Series<Header>) request.getAttributes().get(
                HeaderConstants.ATTRIBUTE_HEADERS);

        // Test for missing due to no challenge response
        assertEquals(Verifier.RESULT_MISSING, awsVerifier.verify(request, null));

        ChallengeResponse cr = new ChallengeResponse(
                ChallengeScheme.HTTP_AWS_S3);
        request.setChallengeResponse(cr);

        // Test missing due to no identifier
        assertEquals(Verifier.RESULT_MISSING, awsVerifier.verify(request, null));

        // Test authentication with bad credentials
        String sig = AwsUtils.getS3Signature(request,
                "badpassword".toCharArray());
        cr.setRawValue(ACCESS_ID + ":" + sig);
        assertEquals(Verifier.RESULT_INVALID, awsVerifier.verify(request, null));

        // Test authentication with valid credentials
        sig = AwsUtils.getS3Signature(request, ACCESS_KEY.toCharArray());
        cr.setRawValue(ACCESS_ID + ":" + sig);
        assertEquals(Verifier.RESULT_VALID, awsVerifier.verify(request, null));

        // Test invalid due to no date header
        headers.removeAll(HeaderConstants.HEADER_DATE, true);
        assertEquals(Verifier.RESULT_INVALID, awsVerifier.verify(request, null));

        // Test stale due to out of date header
        headers.add(HeaderConstants.HEADER_DATE,
                "Tue, 27 Mar 1999 19:36:42 +0000");
        assertEquals(Verifier.RESULT_STALE, awsVerifier.verify(request, null));
    }
}
