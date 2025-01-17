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
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.CharacterSet;
import org.restlet.data.Encoding;
import org.restlet.data.Header;
import org.restlet.data.Language;
import org.restlet.data.MediaType;
import org.restlet.data.Metadata;
import org.restlet.data.Method;
import org.restlet.data.Preference;
import org.restlet.data.Reference;
import org.restlet.engine.application.TunnelFilter;
import org.restlet.engine.header.HeaderConstants;
import org.restlet.test.RestletTestCase;
import org.restlet.util.Series;

/**
 * Tests cases for the tunnel filter.
 */
public class TunnelFilterTestCase extends RestletTestCase {

    /** . */
    private static final String EFFECTED = "http://example.org/adf.asdf/af.html";

    /** . */
    private static final String QUERY = "http://example.org/?start=2013-11-26T03%3A45%2B1300";

    /** . */
    private static final String QUERY_PREF = "http://example.org/?start=2013-11-26T03%3A45%2B1300&media=txt";

    /** . */
    private static final String START_REF_FOR_PATH_TEST = "http://www.example.com/abc/def/";

    /** . */
    private static final String UNEFFECTED = "http://example.org/abc.def/af.ab";

    private List<Preference<CharacterSet>> accCharsets;

    private List<Preference<Encoding>> accEncodings;

    private List<Preference<Language>> accLanguages;

    private List<Preference<MediaType>> accMediaTypes;

    private String lastCreatedReference;

    private Request request;

    private Response response;

    private TunnelFilter tunnelFilter;

    private String userAgent;

    void assertCharSets(CharacterSet... characterSets) {
        assertEqualSet(this.accCharsets, characterSets);
    }

    void assertEncodings(Encoding... encodings) {
        assertEqualSet(this.accEncodings, encodings);
    }

    @SafeVarargs
    final <A extends Metadata> void assertEqualSet(List<? extends Preference<A>> actual, A... expected) {
        if (actual.size() != expected.length) {
            System.out.println("Is:     " + actual);
            System.out.println("Should: " + Arrays.asList(expected));
        }
        assertEquals(actual.size(), expected.length);
        boolean contained = false;
        for (final Metadata exp : expected) {
            for (final Preference<? extends Metadata> act : actual) {
                if (exp.equals(act.getMetadata())) {
                    contained = true;
                    break;
                }
            }
            if (!contained) {
                final String message = exp
                        + " should be in, but is missing in " + actual;
                fail(message);
            }
        }
    }

    void assertLanguages(Language... languages) {
        assertEqualSet(this.accLanguages, languages);
    }

    void assertMediaTypes(MediaType... mediaTypes) {
        assertEqualSet(this.accMediaTypes, mediaTypes);
    }

    void assertMethod(Method method) {
        assertEquals(this.request.getMethod(), method);
    }

    void assertNotSameMethod(Method method) {
        assertNotSame(this.request.getMethod(), method);
    }

    /**
     * @param expectedCut
     * @param expectedExtensions
     */
    private void check(String expectedCut, String expectedExtensions) {
        final Reference resourceRef = this.request.getResourceRef();
        assertEquals(expectedCut, resourceRef.toString());

        final Reference originalRef = this.request.getOriginalRef();
        assertEquals(originalRef, new Reference(this.lastCreatedReference));
        assertEquals(expectedCut, resourceRef.toString());
        assertEquals(expectedExtensions, resourceRef.getExtensions());
    }

    /**
     *
     * @param expectedSubPathCut
     *            if null, the same as subPathOrig
     * @param expectedExtension
     *            if null, then same as "" for this test
     */
    private void checkFromPath(String expectedSubPathCut,
                               String expectedExtension) {
        if (expectedSubPathCut == null) {
            check(this.lastCreatedReference, expectedExtension);
        } else {
            check(START_REF_FOR_PATH_TEST + expectedSubPathCut,
                    expectedExtension);
        }
    }

    /**
     * @see #createGetFromPath(String)
     * @see #createRequest(Method, String)
     */
    void createGet(String reference) {
        createRequest(Method.GET, reference);
    }

    /**
     *
     * @param subPathToCheck
     * @see #createGet(String)
     * @see #createRequest(Method, String)
     */
    private void createGetFromPath(String subPathToCheck) {
        createGet(START_REF_FOR_PATH_TEST + subPathToCheck);
    }

    /**
     *
     */
    void createPost(String reference) {
        createRequest(Method.POST, reference);
    }

    /**
     * Creates a {@link Request} and put it into {@link #request}.<br>
     * To use the methods provided by the test case class use ever the provided
     * create methods to create a request.
     *
     * @param method
     * @param reference
     * @see #createPost(String)
     * @see #createGet(String)
     * @see #createGetFromPath(String)
     */
    void createRequest(Method method, String reference) {
        this.request = new Request(method, reference);
        this.request.setOriginalRef(new Reference(reference));
        this.response = new Response(this.request);
        this.lastCreatedReference = reference;
        setPrefs();
        this.request.getClientInfo().setAgent(this.userAgent);
    }

    private void extensionTunnelOff() {
        final Application application = this.tunnelFilter.getApplication();
        application.getTunnelService().setExtensionsTunnel(false);
    }

    /**
     * Call this method to filter the current request
     */
    private void filter() {
        this.tunnelFilter.beforeHandle(this.request, this.response);
        setPrefs();
    }

    private void setPrefs() {
        this.accMediaTypes = this.request.getClientInfo()
                .getAcceptedMediaTypes();
        this.accLanguages = this.request.getClientInfo().getAcceptedLanguages();
        this.accCharsets = this.request.getClientInfo()
                .getAcceptedCharacterSets();
        this.accEncodings = this.request.getClientInfo().getAcceptedEncodings();
    }

    @BeforeEach
    public void setUpEach() throws Exception {
        Application app = new Application(new Context());
        Application.setCurrent(app);
        this.tunnelFilter = new TunnelFilter(app.getContext());
        this.tunnelFilter.getApplication().getTunnelService()
                .setExtensionsTunnel(true);
    }

    @AfterEach
    protected void tearDownEach() throws Exception {
        this.tunnelFilter = null;
        this.request = null;
        this.response = null;
    }

    @Test
    public void testExtMappingOff1() {
        extensionTunnelOff();
        createGet(UNEFFECTED);
        this.accLanguages
                .add(new Preference<>(Language.valueOf("ajh")));
        this.accMediaTypes.add(new Preference<>(
                MediaType.APPLICATION_STUFFIT));
        filter();
        assertEquals(UNEFFECTED, this.request.getResourceRef().toString());
        assertLanguages(Language.valueOf("ajh"));
        assertMediaTypes(MediaType.APPLICATION_STUFFIT);
        assertCharSets();
        assertEncodings();
    }

    @Test
    public void testExtMappingOff2() {
        extensionTunnelOff();
        createGet(EFFECTED);
        this.accLanguages
                .add(new Preference<>(Language.valueOf("ajh")));
        this.accMediaTypes.add(new Preference<>(
                MediaType.APPLICATION_STUFFIT));
        filter();
        assertEquals(EFFECTED, this.request.getResourceRef().toString());
        assertLanguages(Language.valueOf("ajh"));
        assertMediaTypes(MediaType.APPLICATION_STUFFIT);
        assertCharSets();
        assertEncodings();
    }

    @Test
    public void testExtMappingOn() {
        createGet(UNEFFECTED);
        filter();
        check(UNEFFECTED, "ab");
        assertLanguages();
        assertCharSets();
        assertCharSets();
        assertMediaTypes();

        createGet(EFFECTED);
        filter();
        check("http://example.org/adf.asdf/af", null);
        assertMediaTypes(MediaType.TEXT_HTML);
        assertLanguages();
        assertCharSets();
        assertCharSets();

        createGetFromPath("afhhh");
        filter();
        checkFromPath(null, null);
        assertEqualSet(this.accMediaTypes);
        assertLanguages();
        assertEncodings();
        assertCharSets();

        createGetFromPath("hksf.afsdf");
        filter();
        checkFromPath(null, "afsdf");
        assertMediaTypes();
        assertLanguages();
        assertEncodings();
        assertCharSets();

        createGetFromPath("hksf.afsdf.html");
        filter();
        checkFromPath("hksf.afsdf", "afsdf");
        assertMediaTypes(MediaType.TEXT_HTML);
        assertLanguages();
        assertEncodings();
        assertCharSets();

        createGetFromPath("hksf.afsdf.html.txt");
        filter();
        checkFromPath("hksf.afsdf.html", "afsdf.html");
        assertMediaTypes(MediaType.TEXT_PLAIN);
        assertLanguages();
        assertEncodings();
        assertCharSets();

        createGetFromPath("hksf.html.afsdf.txt");
        filter();
        checkFromPath("hksf.html.afsdf", "html.afsdf");
        assertMediaTypes(MediaType.TEXT_PLAIN);
        assertLanguages();
        assertEncodings();
        assertCharSets();

        createGetFromPath("hksf.html.afsdf.txt.en.fr");
        filter();
        checkFromPath("hksf.html.afsdf.txt.en", "html.afsdf.txt.en");
        // Take care about the fact that only one extension per metadata "type"
        // is allowed: ie only one Language, one encoding, one media type, etc.
        // assertMediaTypes(MediaType.TEXT_PLAIN);
        assertMediaTypes();
        assertLanguages(Language.FRENCH);
        assertEncodings();
        assertCharSets();

        createGetFromPath("hksf.html.afsdf.txt.en");
        filter();
        checkFromPath("hksf.html.afsdf", "html.afsdf");
        assertMediaTypes(MediaType.TEXT_PLAIN);
        assertLanguages(Language.ENGLISH);
        assertEncodings();
        assertCharSets();

        createGet(START_REF_FOR_PATH_TEST);
        filter();
        checkFromPath(null, null);
        assertMediaTypes();
        assertLanguages();
        assertEncodings();
        assertCharSets();
    }

    @Test
    public void testMethodTunnelingViaHeader() {
        tunnelFilter.getTunnelService().setMethodTunnel(true);
        Map<String, Object> attributesHeader = new HashMap<>();
        Series<Header> headers = new Series<>(Header.class);
        headers.add(HeaderConstants.HEADER_X_HTTP_METHOD_OVERRIDE,
                Method.GET.getName());
        headers.add(HeaderConstants.HEADER_X_FORWARDED_FOR, "TEST");
        attributesHeader.put(HeaderConstants.ATTRIBUTE_HEADERS, headers);

        createGet(UNEFFECTED);
        this.request.setAttributes(attributesHeader);
        filter();
        assertMethod(Method.GET);

        createPost(UNEFFECTED);
        filter();
        assertMethod(Method.POST);

        createPost(UNEFFECTED);
        tunnelFilter.getTunnelService().setMethodHeader(
                HeaderConstants.HEADER_X_FORWARDED_FOR);
        this.request.setAttributes(attributesHeader);
        filter();
        assertNotSameMethod(Method.PUT);

        createPost(UNEFFECTED);
        tunnelFilter.getTunnelService().setMethodHeader(
                HeaderConstants.HEADER_X_FORWARDED_FOR);
        tunnelFilter.getTunnelService().setHeadersTunnel(false);
        this.request.setAttributes(attributesHeader);
        filter();
        assertMethod(Method.POST);

    }

    @Test
    public void testWithMatrixParam() {
        createGet(EFFECTED + ";abcdef");
        filter();
        check("http://example.org/adf.asdf/af;abcdef", null);
        assertMediaTypes(MediaType.TEXT_HTML);
        assertLanguages();
        assertCharSets();
        assertCharSets();
    }

    @Test
    public void testMethodTunnelingViaUserAgent() {
        tunnelFilter.getTunnelService().setExtensionsTunnel(false);
        tunnelFilter.getTunnelService().setHeadersTunnel(false);
        tunnelFilter.getTunnelService().setMethodTunnel(false);
        tunnelFilter.getTunnelService().setPreferencesTunnel(false);
        tunnelFilter.getTunnelService().setQueryTunnel(false);
        tunnelFilter.getTunnelService().setUserAgentTunnel(true);

        createGet(UNEFFECTED);
        this.accMediaTypes.add(new Preference<>(
                MediaType.APPLICATION_ZIP));
        filter();
        assertEquals(UNEFFECTED, this.request.getResourceRef().toString());
        assertMediaTypes(MediaType.APPLICATION_ZIP);
        assertCharSets();
        assertEncodings();

        this.userAgent = "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0)";
        createGet(UNEFFECTED);
        this.accMediaTypes.add(new Preference<>(
                MediaType.APPLICATION_ZIP));
        filter();
        assertEquals(UNEFFECTED, this.request.getResourceRef().toString());
        assertMediaTypes(MediaType.TEXT_HTML, MediaType.APPLICATION_XHTML,
                MediaType.APPLICATION_XML, MediaType.ALL);
    }

    @Test
    public void testMethodTunnelingViaQuery() {
        tunnelFilter.getTunnelService().setExtensionsTunnel(false);
        tunnelFilter.getTunnelService().setHeadersTunnel(false);
        tunnelFilter.getTunnelService().setMethodTunnel(false);
        tunnelFilter.getTunnelService().setPreferencesTunnel(true);
        tunnelFilter.getTunnelService().setQueryTunnel(true);
        tunnelFilter.getTunnelService().setUserAgentTunnel(false);

        createGet(QUERY);
        this.tunnelFilter.beforeHandle(this.request, this.response);

        assertEquals(QUERY, this.request.getResourceRef().toString());

        createGet(QUERY_PREF);
        this.tunnelFilter.beforeHandle(this.request, this.response);
        assertEquals(QUERY, this.request.getResourceRef().toString());
        assertMediaTypes(MediaType.TEXT_PLAIN);
    }
}
