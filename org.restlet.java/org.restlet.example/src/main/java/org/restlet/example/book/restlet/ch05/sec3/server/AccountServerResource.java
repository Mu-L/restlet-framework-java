/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch05.sec3.server;

import org.restlet.example.book.restlet.ch02.sec5.sub5.common.AccountResource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

/**
 * Implementation of a mail account resource.
 */
public class AccountServerResource extends ServerResource implements
        AccountResource {

    /** The account identifier. */
    private int accountId;

    /**
     * Retrieve the account identifier based on the URI path variable
     * "accountId" declared in the URI template attached to the application
     * router.
     */
    @Override
    protected void doInit() throws ResourceException {
        String accountIdAttribute = getAttribute("accountId");

        if (accountIdAttribute != null) {
            this.accountId = Integer.parseInt(getAttribute("accountId"));
        }
    }

    public String represent() {
        String result = AccountsServerResource.getAccounts()
                .get(this.accountId);

        if (isInRole("CFO")) {
            return result + " (CFO)";
        } else {
            return result;
        }

    }

    public void store(String account) {
        AccountsServerResource.getAccounts().set(this.accountId, account);
    }

    public void remove() {
        AccountsServerResource.getAccounts().remove(this.accountId);
    }
}
