/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.data;

import static org.testng.AssertJUnit.assertSame;

import org.junit.jupiter.api.Test;
import org.restlet.data.Method;

/**
 * Test {@link org.restlet.data.Method}.
 * <p>
 * Note: this test purposefully does *not* extends RestletTestCase. The regression previously present in Restlet
 * (desribed in https://github.com/restlet/restlet-framework-java/issues/1130) depends on class initialization order and
 * vanishes when the Restlet/Engine class is initialized before the class Method.
 * 
 * @author Andreas Wundsam
 */
public class MethodTestCase {

    /**
     * validate that Method caching works, i.e., the value returned by
     * Method.valueOf("GET") is the cached constant Method.GET
     */
    @Test
    public void testCaching() {
        assertSame("Method.valueOf('GET') should return cached constant Method.GET ", Method.GET, Method.valueOf("GET"));
    }

}
