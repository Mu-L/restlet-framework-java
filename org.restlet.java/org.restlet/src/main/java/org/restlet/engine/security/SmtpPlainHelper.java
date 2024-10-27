/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.engine.security;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

import org.restlet.Request;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Header;
import org.restlet.engine.header.ChallengeWriter;
import org.restlet.engine.io.IoUtils;
import org.restlet.util.Series;

/**
 * Implements the SMTP PLAIN authentication.
 * 
 * @author Jerome Louvel
 */
public class SmtpPlainHelper extends AuthenticatorHelper {

	/**
	 * Constructor.
	 */
	public SmtpPlainHelper() {
		super(ChallengeScheme.SMTP_PLAIN, true, false);
	}

	@Override
	public void formatResponse(ChallengeWriter cw, ChallengeResponse challenge, Request request,
			Series<Header> httpHeaders) {
		try {
			final CharArrayWriter credentials = new CharArrayWriter();
			credentials.write("^@");
			credentials.write(challenge.getIdentifier());
			credentials.write("^@");
			credentials.write(challenge.getSecret());
			cw.append(Base64.getEncoder().encodeToString(IoUtils.toByteArray(credentials.toCharArray(), "US-ASCII")));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unsupported encoding, unable to encode credentials");
		} catch (IOException e) {
			throw new RuntimeException("Unexpected exception, unable to encode credentials", e);
		}
	}

}
