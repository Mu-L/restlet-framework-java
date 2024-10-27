/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch05.sec3.common;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

/**
 * User account resource.
 */
public interface AccountResource {

    /**
     * Represents the account as a simple string with the owner name for now.
     * 
     * @return The account representation.
     */
    @Get
    String represent();

    /**
     * Stores the new value for the identified account.
     * 
     * @param account
     *            The identified account.
     */
    @Put
    void store(String account);

    /**
     * Deletes the identified account by setting its value to null.
     */
    @Delete
    void remove();

}
