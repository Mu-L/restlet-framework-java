/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.engine.security;

import org.restlet.data.ChallengeScheme;

/**
 * Implements the SDC adhoc authentication. This challenge scheme is used to
 * pass the credentials of the tunnel to which you want your SDC call to go
 * through.
 * 
 * @author Jerome Louvel
 */
public class SdcAuthenticatorHelper extends AuthenticatorHelper {

    /**
     * Constructor.
     */
    public SdcAuthenticatorHelper() {
        super(ChallengeScheme.valueOf("SDC"), true, false);
    }

}
