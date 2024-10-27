/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.engine;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.jupiter.api.Test;
import org.restlet.engine.util.DateUtils;
import org.restlet.test.RestletTestCase;

/**
 * Test {@link org.restlet.engine.util.DateUtils}.
 * 
 * @author Thierry Boileau
 */
public class ImmutableDateTestCase extends RestletTestCase {

	@Test
    public void test() {
        Date now = new Date();
        Calendar yesterdayCal = new GregorianCalendar();
        yesterdayCal.add(Calendar.DAY_OF_MONTH, -1);

        Date yesterday = yesterdayCal.getTime();

        assertTrue(now.after(yesterday));
        assertTrue(now.after(DateUtils.unmodifiable(yesterday)));
        assertTrue(DateUtils.unmodifiable(now).after(yesterday));
        assertTrue(DateUtils.unmodifiable(now).after(
                DateUtils.unmodifiable(yesterday)));

        assertTrue(yesterday.before(DateUtils.unmodifiable(now)));
        assertTrue(DateUtils.unmodifiable(yesterday).before(
                DateUtils.unmodifiable(now)));
        assertTrue(DateUtils.unmodifiable(yesterday).before(now));
    }

}
