/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Parameter;
import org.restlet.test.RestletTestCase;
import org.restlet.util.Series;

/**
 * Test {@link org.restlet.data.MediaType}.
 * 
 * @author Jerome Louvel
 */
public class MediaTypeTestCase extends RestletTestCase {
    protected final static String DEFAULT_SCHEME = "http";

    protected final static String DEFAULT_SCHEMEPART = "//";

    /**
     * Makes sure that a {@link MediaType} instance initialized on the specified
     * name has the expected values.
     * 
     * @param name
     *            type to analyze.
     * @param main
     *            expected main type.
     * @param sub
     *            expected subtype.
     * @param concrete
     *            expected 'concrete' flag.
     */
    public void assertMediaType(String name, String main, String sub,
            boolean concrete) {
        MediaType type;

        type = new MediaType(name);
        assertEquals(main, type.getMainType());
        assertEquals(sub, type.getSubType());
        assertEquals(concrete, type.isConcrete());
    }

    /**
     * Makes sure concrete types are properly initialized.
     */
    @Test
    public void testConcrete() {
        assertMediaType("application/xml", "application", "xml", true);
        assertMediaType("application/ xml ", "application", "xml", true);
        assertMediaType(" application /xml", "application", "xml", true);
        assertMediaType(" application / xml ", "application", "xml", true);
        assertMediaType("application/atom+xml;type=entry", "application",
                "atom+xml", true);
    }

    /**
     * Makes sure concrete types are properly initialized.
     */
    @Test
    public void testParameters() {
        MediaType mt = MediaType.valueOf("application/atom+xml;type=entry");
        assertEquals("entry", mt.getParameters().getFirstValue("type"));

        mt = MediaType
                .valueOf("multipart/x-mixed-replace; boundary=\"My boundary\"");
        assertEquals("\"My boundary\"",
                mt.getParameters().getFirstValue("boundary"));
    }

    /**
     * Equality tests.
     */
    @Test
    public void testEquals() {
        MediaType mt1 = new MediaType("application/xml");
        MediaType mt2 = MediaType.APPLICATION_XML;
        assertEquals(mt1, mt2);
        assertEquals(mt1, mt2);

        final Series<Parameter> mediaParams1 = new Form();
        mediaParams1.add(new Parameter("charset", "ISO-8859-1"));
        final MediaType mt1Bis = new MediaType("application/xml", mediaParams1);

        final Series<Parameter> mediaParams2 = new Form();
        mediaParams2.add(new Parameter("charset", "ISO-8859-1"));
        final MediaType mt2Bis = new MediaType("application/xml", mediaParams2);

        final Series<Parameter> mediaParams3 = new Form();
        mediaParams3.add(new Parameter("charset", "ISO-8859-15"));
        final MediaType mt3 = new MediaType("application/xml", mediaParams3);

        assertEquals(mt1Bis, mt2Bis);
        assertEquals(mt1, mt2);
        assertTrue(mt1Bis.equals(mt1, true));
        assertTrue(mt1Bis.equals(mt2, true));
        assertTrue(mt1Bis.equals(mt3, true));

        mt1 = new MediaType("application/*");
        mt2 = MediaType.APPLICATION_ALL;
        assertEquals(mt1, mt2);
        assertEquals(mt1, mt2);
    }

    /**
     * Test inclusion.
     */
    @Test
    public void testIncludes() {
        MediaType mt1 = MediaType.APPLICATION_ALL;
        MediaType mt2 = MediaType.APPLICATION_XML;
        assertTrue(mt1.includes(mt1));
        assertTrue(mt2.includes(mt2));
        assertTrue(mt1.includes(mt2));
        assertFalse(mt2.includes(mt1));

        mt1 = MediaType.APPLICATION_ALL_XML;
        mt2 = MediaType.APPLICATION_XML;
        assertTrue(mt1.includes(mt1));
        assertTrue(mt2.includes(mt2));
        assertTrue(mt1.includes(mt2));
        assertFalse(mt2.includes(mt1));

        mt1 = MediaType.APPLICATION_ALL_XML;
        mt2 = MediaType.APPLICATION_ATOMPUB_SERVICE;
        assertTrue(mt1.includes(mt1));
        assertTrue(mt2.includes(mt2));
        assertTrue(mt1.includes(mt2));
        assertFalse(mt2.includes(mt1));

        mt1 = MediaType.IMAGE_ALL;
        mt2 = MediaType.APPLICATION_OCTET_STREAM;
        assertFalse(mt1.includes(mt2));
        assertFalse(mt2.includes(mt1));

        assertFalse(mt1.includes(null));

        /*
         * test inclusion for media types with parameters. The rule is: media
         * type A includes media type B iff for each parameter name/value pair
         * in A, B contains the same parameter name/value pair
         */

        // set up test data

        MediaType typeWithNoParams = new MediaType("application/sometype");

        Series<Parameter> singleParam = new Series<>(Parameter.class);
        singleParam.add(new Parameter("name1", "value1"));
        MediaType typeWithSingleParam = new MediaType("application/sometype",
                singleParam);

        Series<Parameter> singleMatchingParam = new Series<>(
                Parameter.class);
        singleMatchingParam.add(new Parameter("name1", "value1"));
        MediaType typeWithSingleMatchingParam = new MediaType(
                "application/sometype", singleMatchingParam);

        Series<Parameter> singleNonMatchingParamValue = new Series<>(
                Parameter.class);
        singleNonMatchingParamValue.add(new Parameter("name1", "value2"));
        MediaType typeWithSingleNonMatchingParamValue = new MediaType(
                "application/sometype", singleNonMatchingParamValue);

        Series<Parameter> singleNonMatchingParamName = new Series<>(
                Parameter.class);
        singleNonMatchingParamName.add(new Parameter("name2", "value2"));
        MediaType typeWithSingleNonMatchingParamName = new MediaType(
                "application/sometype", singleNonMatchingParamName);

        Series<Parameter> twoParamsOneMatches = new Series<>(
                Parameter.class);
        twoParamsOneMatches.add(new Parameter("name1", "value1"));
        twoParamsOneMatches.add(new Parameter("name2", "value2"));
        MediaType typeWithTwoParamsOneMatches = new MediaType(
                "application/sometype", twoParamsOneMatches);

        // SCENARIO 1: test whether type with no params includes type with one
        // param

        assertTrue(typeWithNoParams.includes(typeWithSingleParam, true));
        assertTrue(typeWithNoParams.includes(typeWithSingleParam, false));

        // SCENARIO 2: test whether type with one param includes type with no
        // params

        assertTrue(typeWithSingleParam.includes(typeWithNoParams, true));
        assertFalse(typeWithSingleParam.includes(typeWithNoParams, false));

        // SCENARIO 3: test whether type with single param includes type with
        // matching single param.
        // Note that this is distinct from testing whether a type includes
        // itself, as there is a special check for that.
        assertTrue(typeWithSingleParam.includes(typeWithSingleMatchingParam,
                true));
        assertTrue(typeWithSingleParam.includes(typeWithSingleMatchingParam,
                false));

        // SCENARIO 4: test whether type with single param includes type with
        // single param having different name
        assertTrue(typeWithSingleParam.includes(
                typeWithSingleNonMatchingParamName, true));
        assertFalse(typeWithSingleParam.includes(
                typeWithSingleNonMatchingParamName, false));

        // SCENARIO 5: test whether type with single param includes type with
        // single param having same name but different value
        assertTrue(typeWithSingleParam.includes(
                typeWithSingleNonMatchingParamValue, true));
        assertFalse(typeWithSingleParam.includes(
                typeWithSingleNonMatchingParamValue, false));

        // SCENARIO 6: test whether type with single param includes type with
        // two params, one matching
        assertTrue(typeWithSingleParam.includes(typeWithTwoParamsOneMatches,
                true));
        assertTrue(typeWithSingleParam.includes(typeWithTwoParamsOneMatches,
                false));

        // SCENARIO 7: test whether type with two params includes type with
        // single matching param
        assertTrue(typeWithTwoParamsOneMatches.includes(typeWithSingleParam,
                true));
        assertFalse(typeWithTwoParamsOneMatches.includes(typeWithSingleParam,
                false));
    }

    @Test
    public void testMostSpecificMediaType() {
        assertEquals(MediaType.TEXT_ALL,
                MediaType.getMostSpecific(MediaType.ALL, MediaType.TEXT_ALL));
        assertEquals(MediaType.TEXT_ALL,
                MediaType.getMostSpecific(MediaType.TEXT_ALL, MediaType.ALL));

        assertEquals(MediaType.TEXT_PLAIN, MediaType.getMostSpecific(
                MediaType.ALL, MediaType.TEXT_ALL, MediaType.TEXT_PLAIN));
        assertEquals(MediaType.TEXT_PLAIN, MediaType.getMostSpecific(
                MediaType.ALL, MediaType.TEXT_PLAIN, MediaType.TEXT_ALL));
        assertEquals(MediaType.TEXT_PLAIN, MediaType.getMostSpecific(
                MediaType.TEXT_ALL, MediaType.ALL, MediaType.TEXT_PLAIN));
        assertEquals(MediaType.TEXT_PLAIN, MediaType.getMostSpecific(
                MediaType.TEXT_ALL, MediaType.TEXT_PLAIN, MediaType.ALL));
        assertEquals(MediaType.TEXT_PLAIN, MediaType.getMostSpecific(
                MediaType.TEXT_PLAIN, MediaType.ALL, MediaType.TEXT_ALL));
        assertEquals(MediaType.TEXT_PLAIN, MediaType.getMostSpecific(
                MediaType.TEXT_PLAIN, MediaType.TEXT_ALL, MediaType.ALL));
    }

    /**
     * Makes sure that 'abstract' types are properly initialised.
     */
    @Test
    public void testNotConcrete() {
        // */*
        assertMediaType("", "*", "*", false);
        assertMediaType("  ", "*", "*", false);
        assertMediaType("*/", "*", "*", false);
        assertMediaType("*/  ", "*", "*", false);
        assertMediaType(" * /", "*", "*", false);
        assertMediaType("/*", "*", "*", false);
        assertMediaType("  /*", "*", "*", false);
        assertMediaType("/ * ", "*", "*", false);
        assertMediaType("  / * ", "*", "*", false);
        assertMediaType("*/*", "*", "*", false);
        assertMediaType(" * /*", "*", "*", false);
        assertMediaType("*/ * ", "*", "*", false);
        assertMediaType(" * / * ", "*", "*", false);

        // */xml
        assertMediaType("/xml", "*", "xml", false);
        assertMediaType("/ xml ", "*", "xml", false);
        assertMediaType("  /xml", "*", "xml", false);
        assertMediaType("  / xml ", "*", "xml", false);
        assertMediaType("*/xml", "*", "xml", false);
        assertMediaType(" * /xml", "*", "xml", false);
        assertMediaType("*/ xml ", "*", "xml", false);
        assertMediaType(" * / xml ", "*", "xml", false);

        // application/*
        assertMediaType("application", "application", "*", false);
        assertMediaType(" application ", "application", "*", false);
        assertMediaType("application/", "application", "*", false);
        assertMediaType(" application /", "application", "*", false);
        assertMediaType(" application /  ", "application", "*", false);
        assertMediaType("application/*", "application", "*", false);
        assertMediaType(" application /*", "application", "*", false);
        assertMediaType("application/ * ", "application", "*", false);
        assertMediaType(" application /*", "application", "*", false);
    }

    /**
     * Test references that are unequal.
     */
    @Test
    public void testUnEquals() {
        MediaType mt1 = new MediaType("application/xml");
        MediaType mt2 = new MediaType("application/xml2");
        assertNotEquals(mt1, mt2);

        final Series<Parameter> mediaParams1 = new Form();
        mediaParams1.add(new Parameter("charset", "ISO-8859-1"));
        final MediaType mt1Bis = new MediaType("application/xml", mediaParams1);

        final Series<Parameter> mediaParams3 = new Form();
        mediaParams3.add(new Parameter("charset", "ISO-8859-15"));
        final MediaType mt3 = new MediaType("application/xml", mediaParams3);

        assertNotEquals(mt1Bis, mt1);
        assertNotEquals(mt1Bis, mt3);

        mt1 = new MediaType("application/1");
        mt2 = MediaType.APPLICATION_ALL;
        assertNotEquals(mt1, mt2);
    }

    /**
     * Testing {@link MediaType#valueOf(String)} and
     * {@link MediaType#register(String, String)}
     */
    @Test
    public void testValueOf() {
        assertSame(MediaType.APPLICATION_XML,
                MediaType.valueOf("application/xml"));
        assertSame(MediaType.ALL, MediaType.valueOf("*/*"));
        final MediaType newType = MediaType
                .valueOf("application/x-restlet-test");
        assertEquals("application", newType.getMainType());
        assertEquals("x-restlet-test", newType.getSubType());
        assertEquals("application/x-restlet-test", newType.getName());

        // Should not have got registered by call to valueOf() alone
        assertNotSame(newType, MediaType.valueOf("application/x-restlet-test"));

        final MediaType registeredType = MediaType.register(
                "application/x-restlet-test", "Restlet testcase");
        assertNotSame(newType, registeredType); // didn't touch old value
        assertEquals("application/x-restlet-test", registeredType.getName());
        assertEquals("Restlet testcase", registeredType.getDescription());

        // Later valueOf calls always returns the registered type
        assertSame(registeredType,
                MediaType.valueOf("application/x-restlet-test"));
        assertSame(registeredType,
                MediaType.valueOf("application/x-restlet-test"));

        // Test toString() equivalence
        MediaType mediaType = MediaType
                .valueOf("application/atom+xml; name=value");
        assertEquals("application/atom+xml; name=value", mediaType.toString());
        assertEquals(MediaType.APPLICATION_ATOM, mediaType.getParent());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testUnmodifiable() {
        Form form = new Form();
        form.add("name1", "value1");

        try {
            Series<Parameter> unmodifiableForm = (Series<Parameter>) Series
                    .unmodifiableSeries(form);
            unmodifiableForm.add("name2", "value2");
            fail("The series should be unmodifiable now");
        } catch (UnsupportedOperationException uoe) {
            // As expected
        }
    }
}
