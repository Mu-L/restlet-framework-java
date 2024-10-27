/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch05.sec2.verifier.secret;

import org.restlet.security.SecretVerifier;

/**
 * Simple secret verifier that checks a hard-coded username and password.
 */
public class SimpleSecretVerifier extends SecretVerifier {
    @Override
    public int verify(String identifier, char[] secret) {
        return (("chunkylover53".equals(identifier)) && compare(
                "pwd".toCharArray(), secret)) ? RESULT_VALID : RESULT_INVALID;
    }
}
