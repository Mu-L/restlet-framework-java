/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.routing;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.routing.Filter;
import org.restlet.test.AbstractFilterTestCase;
import org.restlet.test.MockFilter;
import org.restlet.test.MockRestlet;

/**
 * Test {@link org.restlet.routing.Filter}.
 * 
 * @author Lars Heuer (heuer[at]semagia.com) <a
 *         href="http://www.semagia.com/">Semagia</a>
 */
public class FilterTestCase extends AbstractFilterTestCase {
    @Override
    protected Filter getFilter() {
        return new MockFilter(null);
    }

    @Override
    protected Request getRequest() {
        return new Request();
    }

    @Override
    protected Response getResponse(Request request) {
        return new Response(request);
    }

    @Override
    protected Restlet getRestlet() {
        return new MockRestlet(null);
    }

}
