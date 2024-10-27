/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.restlet.data.Reference;
import org.restlet.engine.util.AlphaNumericComparator;
import org.restlet.resource.Directory;
import org.restlet.test.RestletTestCase;

/**
 * Test case for the alphanum algorithm used by {@link Directory}.
 *
 * @author Davide Angelocola
 */
public class AlphaNumericComparatorTestCase extends RestletTestCase {

    private static List<Reference> refs(String... uris) {
        List<Reference> result = new LinkedList<>();

        for (String uri : uris) {
            result.add(new Reference(uri));
        }

        return result;
    }

    private static List<Reference> unsorted = refs("1", "2", "3", "1.0", "1.1",
            "1.1.1", "2.0", "2.2", "2.2.2", "3.0", "3.3");

    private static List<Reference> expected = refs("1", "1.0", "1.1", "1.1.1",
            "2", "2.0", "2.2", "2.2.2", "3", "3.0", "3.3");

    @Test
    public void testBug() {
        List<Reference> result = new ArrayList<>(unsorted);
        Collections.sort(result, new AlphaNumericComparator());
        assertEquals(expected, result);
    }

    @Test
    public void test02() {
        AlphaNumericComparator anc = new AlphaNumericComparator();
        System.out.println(anc.compare("Intel 5000X", "Intel 5500"));
        System.out.println(anc.compare("66", "3"));
        System.out.println(anc.compare("200", "66"));
        System.out.println(anc.compare("18", "2"));
    }

}
