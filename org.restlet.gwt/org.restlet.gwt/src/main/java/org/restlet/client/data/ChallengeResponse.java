/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.client.data;

import java.util.Objects;

import org.restlet.client.Request;
import org.restlet.client.engine.util.SystemUtils;
import org.restlet.client.util.Series;

/**
 * Authentication response sent by client to an origin server. This is typically
 * following a {@link ChallengeRequest} sent by the origin server to the client.<br>
 * <br>
 * Sometimes, it might be faster to preemptively issue a challenge response if
 * the client knows for sure that the target resource will require
 * authentication.<br>
 * <br>
 * Note that when used with HTTP connectors, this class maps to the
 * "Authorization" header.
 * 
 * @author Jerome Louvel
 */
public final class ChallengeResponse extends ChallengeMessage {

    /** The client nonce value. */
    private volatile String clientNonce;

    /**
     * The {@link Request#getResourceRef()} value duplicated here in case a
     * proxy changed it.
     */
    private volatile Reference digestRef;

    /** The user identifier, such as a login name or an access key. */
    private volatile String identifier;

    /** The chosen quality of protection. */
    private volatile String quality;

    /** The user secret, such as a password or a secret key. */
    private volatile char[] secret;

    /** The digest algorithm name optionally applied on the user secret. */
    private volatile String secretAlgorithm;

    /** The server nonce count. */
    private volatile int serverNounceCount;

    /**
     * The time when the response was issued, as returned by {@link System#currentTimeMillis()}.
     */
    private volatile long timeIssued;




    /**
     * Constructor with no credentials.
     * 
     * @param scheme
     *            The challenge scheme.
     */
    public ChallengeResponse(ChallengeScheme scheme) {
        this(scheme, null, (char[]) null);
    }

    /**
     * Constructor.
     * 
     * @param scheme
     *            The challenge scheme.
     * @param parameters
     *            The additional scheme parameters.
     * @param identifier
     *            The user identifier, such as a login name or an access key.
     * @param secret
     *            The user secret, such as a password or a secret key.
     * @param secretAlgorithm
     *            The digest algorithm name optionally applied on the user
     *            secret.
     * @param realm
     *            The authentication realm.
     * @param quality
     *            The chosen quality of protection.
     * @param digestRef
     *            The {@link Request#getResourceRef()} value duplicated here in
     *            case a proxy changed it.
     * @param digestAlgorithm
     *            The digest algorithm.
     * @param opaque
     *            An opaque string of data which should be returned by the
     *            client unchanged.
     * @param clientNonce
     *            The client nonce value.
     * @param serverNonce
     *            The server nonce.
     * @param serverNounceCount
     *            The server nonce count.
     * @param timeIssued
     *            The time when the response was issued, as returned by {@link System#currentTimeMillis()}.
     */
    public ChallengeResponse(ChallengeScheme scheme,
            Series<Parameter> parameters, String identifier, char[] secret,
            String secretAlgorithm, String realm, String quality,
            Reference digestRef, String digestAlgorithm, String opaque,
            String clientNonce, String serverNonce, int serverNounceCount,
            long timeIssued) {
        super(scheme, realm, parameters, digestAlgorithm, opaque, serverNonce);
        this.clientNonce = clientNonce;
        this.digestRef = digestRef;
        this.identifier = identifier;
        this.quality = quality;
        this.secret = secret;
        this.secretAlgorithm = secretAlgorithm;
        this.serverNounceCount = serverNounceCount;
        this.timeIssued = timeIssued;
    }

    /**
     * Constructor.
     * 
     * @param scheme
     *            The challenge scheme.
     * @param identifier
     *            The user identifier, such as a login name or an access key.
     * @param secret
     *            The user secret, such as a password or a secret key.
     */
    public ChallengeResponse(ChallengeScheme scheme, String identifier,
            char[] secret) {
        this(scheme, identifier, secret, null);
    }

    /**
     * Constructor.
     * 
     * @param scheme
     *            The challenge scheme.
     * @param identifier
     *            The user identifier, such as a login name or an access key.
     * @param parameters
     *            The additional scheme parameters.
     */
    public ChallengeResponse(ChallengeScheme scheme, String identifier,
            char[] secret, Series<Parameter> parameters) {
        this(scheme, parameters, identifier, secret, Digest.ALGORITHM_NONE,
                null, null, null, null, null, null, null, 0, 0L);
    }

    /**
     * Constructor.
     * 
     * @param scheme
     *            The challenge scheme.
     * @param identifier
     *            The user identifier, such as a login name or an access key.
     * @param parameters
     *            The additional scheme parameters.
     */
    public ChallengeResponse(ChallengeScheme scheme, String identifier,
            Series<Parameter> parameters) {
        this(scheme, identifier, null, parameters);
    }

    /**
     * Constructor.
     * 
     * @param scheme
     *            The challenge scheme.
     * @param identifier
     *            The user identifier, such as a login name or an access key.
     * @param secret
     *            The user secret, such as a password or a secret key.
     */
    public ChallengeResponse(ChallengeScheme scheme, String identifier,
            String secret) {
        this(scheme, identifier, (secret != null) ? secret.toCharArray() : null);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        // if obj == this no need to go further
        if (obj == this) {
            return true;
        }

        // if obj isn't a challenge request or is null don't evaluate further
        if (!(obj instanceof ChallengeResponse)) {
            return false;
        }

        ChallengeResponse that = (ChallengeResponse) obj;

        if (!Objects.equals(getRawValue(), that.getRawValue())
                || !Objects.equals(getIdentifier(), that.getIdentifier())
                || !Objects.equals(getScheme(), that.getScheme())) {
            return false;
        }

        if ((getSecret() == null)
                || (that.getSecret() == null)) {
            // check if both are null
            return (getSecret() == that.getSecret());
        }

        if (getSecret().length != that.getSecret().length) {
            return false;
        }

        boolean equals = true;
        for (int i = 0; equals && (i < getSecret().length); i++) {
            equals = (getSecret()[i] == that.getSecret()[i]);
        }
        return equals;
    }

    /**
     * Returns the client nonce.
     * 
     * @return The client nonce.
     */
    public String getClientNonce() {
        return this.clientNonce;
    }

    /**
     * Returns the {@link Request#getResourceRef()} value duplicated here in
     * case a proxy changed it.
     * 
     * @return The digest URI reference.
     */
    public Reference getDigestRef() {
        return digestRef;
    }

    /**
     * Returns the user identifier, such as a login name or an access key.
     * 
     * @return The user identifier, such as a login name or an access key.
     */
    public String getIdentifier() {
        return this.identifier;
    }


    /**
     * Returns the chosen quality of protection.
     * 
     * @return The chosen quality of protection.
     */
    public String getQuality() {
        return quality;
    }

    /**
     * Returns the user secret, such as a password or a secret key.
     * 
     * It is not recommended to use {@link String#String(char[])} for security
     * reasons.
     * 
     * @return The user secret, such as a password or a secret key.
     */
    public char[] getSecret() {
        return this.secret;
    }

    /**
     * Returns the digest algorithm name optionally applied on the user secret.
     * 
     * @return The digest algorithm name optionally applied on the user secret.
     */
    public String getSecretAlgorithm() {
        return secretAlgorithm;
    }

    /**
     * Returns the server nonce count.
     * 
     * @return The server nonce count.
     */
    public int getServerNounceCount() {
        return serverNounceCount;
    }


    /**
     * Returns the time when the response was issued, as returned by {@link System#currentTimeMillis()}.
     * 
     * @return The time when the response was issued.
     */
    public long getTimeIssued() {
        return timeIssued;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        // Note that the secret is simply discarded from hash code calculation
        // because we don't want it to be materialized as a string
        return SystemUtils
                .hashCode(getScheme(), getIdentifier(), getRawValue());
    }

    /**
     * Sets the client nonce.
     * 
     * @param clientNonce
     *            The client nonce.
     */
    public void setClientNonce(String clientNonce) {
        this.clientNonce = clientNonce;
    }

    /**
     * Sets the digest URI reference.
     * 
     * @param digestRef
     *            The digest URI reference.
     */
    public void setDigestRef(Reference digestRef) {
        this.digestRef = digestRef;
    }

    /**
     * Sets the user identifier, such as a login name or an access key.
     * 
     * @param identifier
     *            The user identifier, such as a login name or an access key.
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * Sets the chosen quality of protection.
     * 
     * @param quality
     *            The chosen quality of protection.
     */
    public void setQuality(String quality) {
        this.quality = quality;
    }

    /**
     * Sets the user secret, such as a password or a secret key.
     * 
     * @param secret
     *            The user secret, such as a password or a secret key.
     */
    public void setSecret(char[] secret) {
        this.secret = secret;
    }

    /**
     * Sets the user secret, such as a password or a secret key.
     * 
     * @param secret
     *            The user secret, such as a password or a secret key.
     */
    public void setSecret(String secret) {
        this.secret = (secret == null) ? null : secret.toCharArray();
    }

    /**
     * Sets the digest algorithm name optionally applied on the user secret.
     * 
     * @param secretDigestAlgorithm
     *            The digest algorithm name optionally applied on the user
     *            secret.
     */
    public void setSecretAlgorithm(String secretDigestAlgorithm) {
        this.secretAlgorithm = secretDigestAlgorithm;
    }

    /**
     * Sets the server nonce count.
     * 
     * @param serverNounceCount
     *            The server nonce count.
     */
    public void setServerNounceCount(int serverNounceCount) {
        this.serverNounceCount = serverNounceCount;
    }

    /**
     * Sets the time when the response was issued, as returned by {@link System#currentTimeMillis()}.
     * 
     * @param timeIssued
     *            The time when the response was issued.
     */
    public void setTimeIssued(long timeIssued) {
        this.timeIssued = timeIssued;
    }
}
