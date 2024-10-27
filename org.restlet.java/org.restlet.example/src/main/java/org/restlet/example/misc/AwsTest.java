/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.misc;

import org.restlet.Client;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Header;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.engine.header.HeaderConstants;
import org.restlet.representation.Representation;
import org.restlet.util.Series;

/**
 * Test the Amazon Web Service authentication.
 * 
 * @author Jerome Louvel
 */
public class AwsTest {
    public static void main(String[] args) throws Exception {
        // Prepare the request
        Request request = new Request(Method.GET,
                "http://s3.amazonaws.com/quotes/nelson");
        request.setChallengeResponse(new ChallengeResponse(
                ChallengeScheme.HTTP_AWS_S3, "44CF9590006BF252F707",
                "OtxrzxIsfpFjA7SwPzILwy8Bw21TLhquhboDYROV"));

        // Add some extra headers
        Series<Header> extraHeaders = new Series<Header>(Header.class);
        extraHeaders.add("X-Amz-Meta-Author", "foo@bar.com");
        extraHeaders.add("X-Amz-Magic", "abracadabra");

        // For the test we hard coded a special date header. Normally you don't
        // need this as the
        // HTTP client connector will automatically provide an accurate Date
        // header and use it
        // for authentication.
        // extraHeaders.add("X-Amz-Date", "Thu, 17 Nov 2005 18:49:58 GMT");
        request.getAttributes().put(HeaderConstants.ATTRIBUTE_HEADERS,
                extraHeaders);

        // Handle it using an HTTP client connector
        Client client = new Client(Protocol.HTTP);
        Response response = client.handle(request);

        // Write the response entity on the console
        Representation output = response.getEntity();
        output.write(System.out);
    }

}
