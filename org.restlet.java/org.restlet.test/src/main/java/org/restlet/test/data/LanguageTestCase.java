/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.data;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.restlet.data.Language;
import org.restlet.test.RestletTestCase;

/**
 * Test {@link org.restlet.data.Language}.
 *
 * @author Jerome Louvel
 */
public class LanguageTestCase extends RestletTestCase {

    /**
     * Testing {@link Language#valueOf(String)}
     */
    @Test
    public void testValueOf() {
        assertSame(Language.FRENCH_FRANCE, Language.valueOf("fr-fr"));
        assertSame(Language.ALL, Language.valueOf("*"));
    }

    @Test
    public void testUnmodifiable() {
        try {
            Language.FRENCH_FRANCE.getSubTags().add("foo");
            fail("The subtags shouldn't be modifiable");
        } catch (UnsupportedOperationException uoe) {
            // As expected
        }
    }
}
