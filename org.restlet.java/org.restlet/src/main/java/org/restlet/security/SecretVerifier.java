/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.security;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ClientInfo;

/**
 * Verifier of identifier/secret couples. By default, it extracts the identifier
 * and the secret from the {@link ChallengeResponse}. If the verification is
 * successful, it automatically adds a new {@link User} for the given
 * identifier.
 * 
 * @author Jerome Louvel
 */
public abstract class SecretVerifier implements Verifier {
	/**
	 * Compares that two secrets are equal and not null.
	 * 
	 * @param secret1 The input secret.
	 * @param secret2 The output secret.
	 * @return True if both are equal.
	 */
	public static boolean compare(char[] secret1, char[] secret2) {
		boolean result = false;

		if ((secret1 != null) && (secret2 != null)) {
			// None is null
			if (secret1.length == secret2.length) {
				boolean equals = true;

				for (int i = 0; (i < secret1.length) && equals; i++) {
					equals = (secret1[i] == secret2[i]);
				}

				result = equals;
			}
		}

		return result;
	}

	/**
	 * Called back to create a new user when valid credentials are provided.
	 * 
	 * @param identifier The user identifier.
	 * @param request    The request handled.
	 * @param response   The response handled.
	 * @return The {@link User} instance created.
	 */
	protected User createUser(String identifier, Request request, Response response) {
		return new User(identifier);
	}

	/**
	 * Returns the user identifier.
	 * 
	 * @param request  The request to inspect.
	 * @param response The response to inspect.
	 * @return The user identifier.
	 */
	protected String getIdentifier(Request request, Response response) {
		return request.getChallengeResponse().getIdentifier();
	}

	/**
	 * Returns the secret provided by the user.
	 * 
	 * @param request  The request to inspect.
	 * @param response The response to inspect.
	 * @return The secret provided by the user.
	 */
	protected char[] getSecret(Request request, Response response) {
		return request.getChallengeResponse().getSecret();
	}

	/**
	 * Verifies that the proposed secret is correct for the specified request. By
	 * default, it compares the inputSecret of the request's authentication response
	 * with the one obtain by the {@link ChallengeResponse#getSecret()} method and
	 * sets the {@link org.restlet.security.User} instance of the request's
	 * {@link ClientInfo} if successful.
	 * 
	 * @param request  The request to inspect.
	 * @param response The response to inspect.
	 * @return Result of the verification based on the RESULT_* constants.
	 */
	public int verify(Request request, Response response) {
		int result = RESULT_VALID;

		if (request.getChallengeResponse() == null) {
			result = RESULT_MISSING;
		} else {
			String identifier = getIdentifier(request, response);
			char[] secret = getSecret(request, response);
			result = verify(identifier, secret);

			if (result == RESULT_VALID) {
				request.getClientInfo().setUser(createUser(identifier, request, response));
			}
		}

		return result;
	}

	/**
	 * Verifies that the identifier/secret couple is valid. It throws an
	 * IllegalArgumentException in case the identifier is either null or does not
	 * identify a user.
	 * 
	 * @param identifier The user identifier to match.
	 * @param secret     The provided secret to verify.
	 * @return Result of the verification based on the RESULT_* constants.
	 */
	public abstract int verify(String identifier, char[] secret);

}
