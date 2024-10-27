/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.ext.crypto;

import org.restlet.Context;
import org.restlet.data.ChallengeScheme;
import org.restlet.ext.crypto.internal.AwsVerifier;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.LocalVerifier;
import org.restlet.security.Verifier;

/**
 * Authenticator supporting the {@link ChallengeScheme#HTTP_AWS_S3} scheme.
 * 
 * @author Jean-Philippe Steinmetz <caskater47@gmail.com>
 */
public class AwsAuthenticator extends ChallengeAuthenticator {
    /**
     * Creates a new HttpAwsS3Authenticator instance.
     * 
     * @param context
     *            The context
     * @param optional
     *            Indicates if the authentication success is optional
     * @param realm
     *            The authentication realm
     */
    public AwsAuthenticator(Context context, boolean optional, String realm) {
        this(context, optional, realm, new AwsVerifier(null));
    }

    /**
     * Creates a new HttpAwsS3Authenticator instance.
     * 
     * @param context
     *            The context
     * @param optional
     *            Indicates if the authentication success is optional
     * @param realm
     *            The authentication realm
     * @param verifier
     */
    public AwsAuthenticator(Context context, boolean optional, String realm,
            Verifier verifier) {
        super(context, optional, ChallengeScheme.HTTP_AWS_S3, realm, verifier);
    }

    /**
     * Creates a new HttpAwsS3Authenticator instance.
     * 
     * @param context
     *            The context
     * @param realm
     *            The authentication realm
     */
    public AwsAuthenticator(Context context, String realm) {
        this(context, false, realm);
    }

    /**
     * Returns the maximum age of a request, in milliseconds, before it is
     * considered stale.
     * <p>
     * A negative or zero value indicates no age restriction. The default value
     * is 15 minutes.
     */
    public long getMaxRequestAge() {
        return getVerifier().getMaxRequestAge();
    }

    @Override
    public AwsVerifier getVerifier() {
        return (AwsVerifier) super.getVerifier();
    }

    /**
     * Returns the secret verifier that will be wrapped by the real verifier
     * supporting all the HTTP AWS verifications.
     * 
     * @return the local wrapped verifier
     */
    public LocalVerifier getWrappedVerifier() {
        return getVerifier().getWrappedVerifier();
    }

    /**
     * Sets the maximum age of a request, in milliseconds, before it is
     * considered stale.
     * <p>
     * A negative or zero value indicates no age restriction. The default value
     * is 15 minutes.
     */
    public void setMaxRequestAge(long value) {
        getVerifier().setMaxRequestAge(value);
    }

    /**
     * Sets the internal verifier. In general you shouldn't replace it but
     * instead set the {@code wrappedVerifier} via the
     * {@link #setWrappedVerifier(LocalVerifier)} method.
     */
    @Override
    public void setVerifier(Verifier verifier) {
        if (!(verifier instanceof AwsVerifier))
            throw new IllegalArgumentException();

        super.setVerifier(verifier);
    }

    /**
     * Sets the secret verifier that will be wrapped by the real verifier
     * supporting all the HTTP AWS verifications.
     * 
     * @param verifier
     *            The local verifier to wrap
     */
    public void setWrappedVerifier(LocalVerifier verifier) {
        getVerifier().setWrappedVerifier(verifier);
    }
}
