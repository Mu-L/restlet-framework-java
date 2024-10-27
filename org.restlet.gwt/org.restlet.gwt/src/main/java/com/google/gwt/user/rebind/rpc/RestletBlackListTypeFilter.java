/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package com.google.gwt.user.rebind.rpc;

import com.google.gwt.core.ext.PropertyOracle;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;

/**
 * Inherits from the RPC black list filter.
 * 
 * @author Thierry Boileau
 */
public class RestletBlackListTypeFilter extends BlacklistTypeFilter {

    public RestletBlackListTypeFilter(TreeLogger logger,
            PropertyOracle propertyOracle) throws UnableToCompleteException {
        super(logger, propertyOracle);
    }

}
