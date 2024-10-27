/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch02.sec4.sub1;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Status;

/**
 * Filter that blocks specific IP addresses.
 */
public class Blocker extends org.restlet.routing.Filter {

    /** The set of blocked IP addresses. */
    private final Set<String> blockedAddresses;

    /**
     * Constructor.
     * 
     * @param context
     *            The parent context.
     */
    public Blocker(Context context) {
        super(context);
        this.blockedAddresses = new CopyOnWriteArraySet<String>();
    }

    /**
     * Pre-processing method testing if the client IP address is in the set of
     * blocked addresses.
     */
    @Override
    protected int beforeHandle(Request request, Response response) {
        int result = STOP;

        if (getBlockedAddresses()
                .contains(request.getClientInfo().getAddress())) {
            response.setStatus(Status.CLIENT_ERROR_FORBIDDEN,
                    "Your IP address was blocked");
        } else {
            result = CONTINUE;
        }

        return result;
    }

    /**
     * Returns the modifiable set of blocked IP addresses.
     * 
     * @return The modifiable set of blocked IP addresses.
     */
    public Set<String> getBlockedAddresses() {
        return blockedAddresses;
    }

}
