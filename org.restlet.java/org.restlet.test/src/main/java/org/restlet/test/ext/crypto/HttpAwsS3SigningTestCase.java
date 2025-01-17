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
import org.restlet.data.Header;
import org.restlet.data.Method;
import org.restlet.engine.header.HeaderConstants;
import org.restlet.ext.crypto.internal.AwsUtils;
import org.restlet.test.RestletTestCase;
import org.restlet.util.Series;

/**
 * Unit test for {@link AwsUtils}. Test cases are taken from the examples
 * provided from <a href=
 * "http://docs.amazonwebservices.com/AmazonS3/latest/index.html?RESTAuthentication.html"
 * >Authenticating REST Requests</a>
 *
 * @author Jean-Philippe Steinmetz <caskater47@gmail.com>
 */
public class HttpAwsS3SigningTestCase extends RestletTestCase {
    private static final String ACCESS_KEY = "uV3F3YluFJax1cknvbcGwgjvx4QpvB+leU8dUj2o";

    private Request getRequest;

    private Request putRequest;

    private Request uploadRequest;

    @BeforeEach
    public void setUpEach() throws Exception {
        getRequest = new Request();
        Series<Header> headers = new Series<Header>(Header.class);
        getRequest.getAttributes().put(HeaderConstants.ATTRIBUTE_HEADERS,
                headers);
        headers.add(HeaderConstants.HEADER_DATE,
                "Tue, 27 Mar 2007 19:36:42 +0000");
        getRequest.setMethod(Method.GET);
        getRequest
                .setResourceRef("http://johnsmith.s3.amazonaws.com/photos/puppy.jpg");

        putRequest = new Request();
        headers = new Series<Header>(Header.class);
        putRequest.getAttributes().put(HeaderConstants.ATTRIBUTE_HEADERS,
                headers);
        headers.add(HeaderConstants.HEADER_CONTENT_LENGTH, "94328");
        headers.add(HeaderConstants.HEADER_CONTENT_TYPE, "image/jpeg");
        headers.add(HeaderConstants.HEADER_DATE,
                "Tue, 27 Mar 2007 21:15:45 +0000");
        putRequest.setMethod(Method.PUT);
        putRequest
                .setResourceRef("http://johnsmith.s3.amazonaws.com/photos/puppy.jpg");

        uploadRequest = new Request();
        headers = new Series<Header>(Header.class);
        uploadRequest.getAttributes().put(HeaderConstants.ATTRIBUTE_HEADERS,
                headers);
        headers.add(HeaderConstants.HEADER_CONTENT_LENGTH, "5913339");
        headers.add(HeaderConstants.HEADER_CONTENT_MD5,
                "4gJE4saaMU4BqNR0kLY+lw==");
        headers.add(HeaderConstants.HEADER_CONTENT_TYPE,
                "application/x-download");
        headers.add(HeaderConstants.HEADER_DATE,
                "Tue, 27 Mar 2007 21:06:08 +0000");
        uploadRequest.setMethod(Method.PUT);
        uploadRequest
                .setResourceRef("http://static.johnsmith.net:8080/db-backup.dat.gz");
        headers.add("x-amz-acl", "public-read");
        headers.add("X-Amz-Meta-ReviewedBy", "joe@johnsmith.net");
        headers.add("X-Amz-Meta-ReviewedBy", "jane@johnsmith.net");
        headers.add("X-Amz-Meta-FileChecksum", "0x02661779");
        headers.add("X-Amz-Meta-ChecksumAlgorithm", "crc32");
    }

    @AfterEach
    public void tearDownEach() throws Exception {
        getRequest = null;
        putRequest = null;
        uploadRequest = null;
    }

    @Test
    public void testGetCanonicalizedAmzHeaders() {
        Series<Header> headers = getRequest.getHeaders();
        String expected = "";
        String actual = AwsUtils.getCanonicalizedAmzHeaders(headers);
        assertEquals(expected, actual);

        headers = uploadRequest.getHeaders();
        expected = "x-amz-acl:public-read\n"
                + "x-amz-meta-checksumalgorithm:crc32\n"
                + "x-amz-meta-filechecksum:0x02661779\n"
                + "x-amz-meta-reviewedby:joe@johnsmith.net,jane@johnsmith.net\n";
        actual = AwsUtils.getCanonicalizedAmzHeaders(headers);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetCanonicalizedResourceName() {
        String result = AwsUtils.getCanonicalizedResourceName(getRequest
                .getResourceRef());
        assertEquals("/johnsmith/photos/puppy.jpg", result);
    }

    @Test
    public void testGetSignature() {
        String result = AwsUtils.getS3Signature(getRequest,
                ACCESS_KEY.toCharArray());
        assertEquals("xXjDGYUmKxnwqr5KXNPGldn5LbA=", result);

        result = AwsUtils.getS3Signature(putRequest, ACCESS_KEY.toCharArray());
        assertEquals("hcicpDDvL9SsO6AkvxqmIWkmOuQ=", result);

        result = AwsUtils.getS3Signature(uploadRequest,
                ACCESS_KEY.toCharArray());
        assertEquals("C0FlOtU8Ylb9KDTpZqYkZPX91iI=", result);
    }

    @Test
    public void testGetStringToSign() {
        String expected = "GET\n" + "\n" + "\n"
                + "Tue, 27 Mar 2007 19:36:42 +0000\n"
                + "/johnsmith/photos/puppy.jpg";
        String actual = AwsUtils.getS3StringToSign(getRequest);
        assertEquals(expected, actual);

        expected = "PUT\n" + "\n" + "image/jpeg\n"
                + "Tue, 27 Mar 2007 21:15:45 +0000\n"
                + "/johnsmith/photos/puppy.jpg";
        actual = AwsUtils.getS3StringToSign(putRequest);
        assertEquals(expected, actual);

        expected = "PUT\n" + "4gJE4saaMU4BqNR0kLY+lw==\n"
                + "application/x-download\n"
                + "Tue, 27 Mar 2007 21:06:08 +0000\n"
                + "x-amz-acl:public-read\n"
                + "x-amz-meta-checksumalgorithm:crc32\n"
                + "x-amz-meta-filechecksum:0x02661779\n"
                + "x-amz-meta-reviewedby:"
                + "joe@johnsmith.net,jane@johnsmith.net\n"
                + "/static.johnsmith.net/db-backup.dat.gz";
        actual = AwsUtils.getS3StringToSign(uploadRequest);
        assertEquals(expected, actual);
    }
}
