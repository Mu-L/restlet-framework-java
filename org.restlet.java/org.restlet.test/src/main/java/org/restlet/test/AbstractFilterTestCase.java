/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.routing.Filter;

/**
 * Tests where every Filter should run through.
 * 
 * @author Lars Heuer
 */
public abstract class AbstractFilterTestCase extends RestletTestCase {
    /**
     * Returns a Filter to be used for the tests.
     * 
     * @return Filter instance.
     */
    protected abstract Filter getFilter();

    /**
     * Returns a request.
     * 
     * @return Request instance.
     */
    protected abstract Request getRequest();

    /**
     * Returns a response.
     * 
     * @param request
     *            The associated request.
     * @return Response instance.
     */
    protected abstract Response getResponse(Request request);

    /**
     * Returns a restlet.
     * 
     * @return Restlet instance.
     */
    protected abstract Restlet getRestlet();

    /**
     * Test Restlet instance attaching/detaching.
     */
    @Test
    public void testAttachDetachInstance() throws Exception {
        final Filter filter = getFilter();
        assertFalse(filter.hasNext());
        filter.setNext(getRestlet());
        filter.start();
        assertTrue(filter.isStarted());
        assertFalse(filter.isStopped());
        final Request request = getRequest();
        final Response response = getResponse(request);
        filter.handle(request, response);
        assertTrue(filter.hasNext());
        filter.setNext((Restlet) null);
        assertFalse(filter.hasNext());
    }

    /**
     * Test not started Filter.
     */
    @Test
    public void testIllegalStartedState() {
        final Filter filter = getFilter();
        filter.setNext(getRestlet());
        assertTrue(filter.hasNext());
        assertFalse(filter.isStarted());
        assertTrue(filter.isStopped());
        final Request request = getRequest();
        final Response response = getResponse(request);
        try {
            filter.handle(request, response);

            if (!filter.isStarted()) {
                fail("Filter handles call without being started");
            }
        } catch (Exception ex) {
            // noop.
        }
    }

    /**
     * Test with null target.
     */
    @Test
    public void testIllegalTarget() throws Exception {
        final Filter filter = getFilter();
        filter.start();
        assertTrue(filter.isStarted());
        assertFalse(filter.isStopped());
        assertFalse(filter.hasNext());
        final Request request = getRequest();
        final Response response = getResponse(request);
        try {
            filter.handle(request, response);
            fail("Filter handles call without a target");
        } catch (Exception ex) {
            // noop.
        }
    }

}
