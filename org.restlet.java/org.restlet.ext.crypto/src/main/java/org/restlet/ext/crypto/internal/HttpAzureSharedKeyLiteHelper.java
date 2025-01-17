/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.ext.crypto.internal;

import java.util.Base64;
import java.util.Date;

import org.restlet.Request;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Form;
import org.restlet.data.Header;
import org.restlet.data.Parameter;
import org.restlet.data.Reference;
import org.restlet.engine.header.ChallengeWriter;
import org.restlet.engine.header.HeaderConstants;
import org.restlet.engine.io.IoUtils;
import org.restlet.engine.security.AuthenticatorHelper;
import org.restlet.engine.util.DateUtils;
import org.restlet.ext.crypto.DigestUtils;
import org.restlet.util.Series;

/**
 * Implements the Shared Key Lite authentication for Azure services. This
 * concerns Table storage on Azure Storage.<br>
 * <br>
 * More documentation is available <a
 * href="http://msdn.microsoft.com/en-us/library/dd179428.aspx">here</a>
 * 
 * @author Thierry Boileau
 */
public class HttpAzureSharedKeyLiteHelper extends AuthenticatorHelper {

    /**
     * Returns the canonicalized resource name.
     * 
     * @param resourceRef
     *            The resource reference.
     * @return The canonicalized resource name.
     */
    private static String getCanonicalizedResourceName(Reference resourceRef) {
        Form form = resourceRef.getQueryAsForm();
        Parameter param = form.getFirst("comp", true);

        if (param != null) {
            StringBuilder sb = new StringBuilder(resourceRef.getPath());
            return sb.append("?").append("comp=").append(param.getValue())
                    .toString();
        }

        return resourceRef.getPath();
    }

    /**
     * Constructor.
     */
    public HttpAzureSharedKeyLiteHelper() {
        super(ChallengeScheme.HTTP_AZURE_SHAREDKEY_LITE, true, false);
    }

    @Override
    public void formatResponse(ChallengeWriter cw, ChallengeResponse challenge,
            Request request, Series<Header> httpHeaders) {

        // Setup the Date header
        String date = "";

        if (httpHeaders.getFirstValue("x-ms-date", true) == null) {
            // X-ms-Date header didn't override the standard Date header
            date = httpHeaders.getFirstValue(HeaderConstants.HEADER_DATE, true);

            if (date == null) {
                // Add a fresh Date header
                date = DateUtils.format(new Date(),
                        DateUtils.FORMAT_RFC_1123.get(0));
                httpHeaders.add(HeaderConstants.HEADER_DATE, date);
            }
        } else {
            date = httpHeaders.getFirstValue("x-ms-date", true);
        }

        // Setup the canonicalized path
        String canonicalizedResource = getCanonicalizedResourceName(request
                .getResourceRef());

        // Setup the message part
        StringBuilder rest = new StringBuilder();
        rest.append(date).append('\n').append('/')
                .append(challenge.getIdentifier())
                .append(canonicalizedResource);

        // Append the SharedKey credentials
        cw.append(challenge.getIdentifier())
                .append(':')
                .append(Base64.getEncoder().encodeToString(
                        DigestUtils.toHMacSha256(rest.toString(),
                                Base64.getDecoder().decode(IoUtils.toByteArray(challenge.getSecret())))));
    }
}
