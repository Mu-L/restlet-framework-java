/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch02.sec5.sub5.server;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.restlet.example.book.restlet.ch02.sec5.sub5.common.AccountsResource;
import org.restlet.resource.ServerResource;

/**
 * Implementation of the resource containing the list of mail accounts.
 */
public class AccountsServerResource extends ServerResource implements
        AccountsResource {

    /** Static list of accounts stored in memory. */
    private static final List<String> accounts = new CopyOnWriteArrayList<String>();

    /**
     * Returns the static list of accounts stored in memory.
     * 
     * @return The static list of accounts.
     */
    public static List<String> getAccounts() {
        return accounts;
    }

    public String represent() {
        StringBuilder result = new StringBuilder();

        for (String account : getAccounts()) {
            result.append((account == null) ? "" : account).append('\n');
        }

        return result.toString();
    }

    public String add(String account) {
        getAccounts().add(account);
        return Integer.toString(getAccounts().indexOf(account));
    }
}
