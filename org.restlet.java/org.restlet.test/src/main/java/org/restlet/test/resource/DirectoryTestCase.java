/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.resource;

import static java.io.File.createTempFile;
import static java.lang.System.getProperty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.restlet.data.Language.FRENCH;
import static org.restlet.data.Language.SPANISH;
import static org.restlet.data.LocalReference.createFileReference;
import static org.restlet.data.Method.DELETE;
import static org.restlet.data.Method.GET;
import static org.restlet.data.Method.HEAD;
import static org.restlet.data.Method.PUT;
import static org.restlet.data.Protocol.FILE;
import static org.restlet.data.Status.CLIENT_ERROR_BAD_REQUEST;
import static org.restlet.data.Status.CLIENT_ERROR_FORBIDDEN;
import static org.restlet.data.Status.CLIENT_ERROR_METHOD_NOT_ALLOWED;
import static org.restlet.data.Status.CLIENT_ERROR_NOT_FOUND;
import static org.restlet.data.Status.REDIRECTION_SEE_OTHER;
import static org.restlet.data.Status.SUCCESS_CREATED;
import static org.restlet.data.Status.SUCCESS_NO_CONTENT;
import static org.restlet.data.Status.SUCCESS_OK;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Header;
import org.restlet.data.Language;
import org.restlet.data.Metadata;
import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.engine.Engine;
import org.restlet.engine.header.HeaderConstants;
import org.restlet.engine.io.IoUtils;
import org.restlet.engine.util.ReferenceUtils;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Directory;
import org.restlet.test.RestletTestCase;

/**
 * Unit tests for the Directory class.
 * 
 * @author Thierry Boileau
 */
public class DirectoryTestCase extends RestletTestCase {

    String webSiteURL = "http://myapplication/";

    String baseFileUrl = this.webSiteURL.concat("fichier.txt");

    String baseFileUrlEn = this.webSiteURL.concat("fichier.txt.en");

    String baseFileUrlFr = this.webSiteURL.concat("fichier.txt.fr");

    String baseFileUrlFrBis = this.webSiteURL.concat("fichier.fr.txt");

    String percentEncodedFileUrl = this.webSiteURL.concat(Reference
            .encode("a new %file.txt.fr"));

    String percentEncodedFileUrlBis = this.webSiteURL
            .concat("a+new%20%25file.txt.fr");

    /** Tests the creation of directory with unknown parent directories. */
    String testCreationDirectory = webSiteURL.concat("dir/does/not/exist");

    /** Tests the creation of file with unknown parent directories. */
    String testCreationFile = webSiteURL.concat("file/does/not/exist.xml");

    /** Tests the creation of text file with unknown parent directories. */
    String testCreationTextFile = webSiteURL
            .concat("text/file/does/not/exist.txt");

    private static Component clientComponent;
    private static MyApplication application;
    File testDir;

    @BeforeAll
    public static void setUp() throws Exception {
        Engine.getInstance().getRegisteredConverters().clear();
        Engine.getInstance().registerDefaultConverters();

        // Create a new Restlet component
        clientComponent = new Component();
        clientComponent.getClients().add(FILE);

        // Create a temporary directory for the tests
        File testDir = new File(getProperty("java.io.tmpdir"), "DirectoryTestCase/tests1" + new Date().getTime());

        // Create an application
        application = new MyApplication(testDir);
        Application.setCurrent(application);
        // Attach the application to the component and start it
        clientComponent.getDefaultHost().attach("", application);
        // Now, let's start the component!
        clientComponent.start();
    }

    @AfterAll
    public static void tearDown() throws Exception {
        // Now, let's stop the component!
        clientComponent.stop();
    }

    @AfterEach
    public void afterEach() {
        IoUtils.delete(this.testDir, true);
    }

    @Test
    public void testDirectoryWithExtensionTunnelAndIndexName() throws IOException {
        application.getTunnelService().setExtensionsTunnel(true);
        this.testDir = Files.createTempDirectory("testDirectoryWithExtensionTunnelAndIndexName").toFile();
        application.setTestDirectory(testDir);

        // Test the directory Restlet with an index name
        testDirectory(application, application.getDirectory(), "index");
    }

    @Test
    public void testDirectoryWithExtensionTunnelAndWithoutIndexName() throws IOException {
        application.getTunnelService().setExtensionsTunnel(true);
        this.testDir = Files.createTempDirectory("testDirectoryWithExtensionTunnelAndWithoutIndexName").toFile();
        application.setTestDirectory(testDir);

        // Test the directory Restlet with no index name
        testDirectory(application, application.getDirectory(), "");
    }

    @Test
    public void testDirectoryWithoutExtensionTunnelAndIndexName() throws IOException {
        application.getTunnelService().setExtensionsTunnel(false);
        this.testDir = Files.createTempDirectory("testDirectoryWithoutExtensionTunnelAndIndexName").toFile();
        application.setTestDirectory(testDir);

        // Test the directory Restlet with an index name
        testDirectory(application, application.getDirectory(), "index");
    }

    @Test
    public void testDirectoryWithoutExtensionTunnelAndWithoutIndexName() throws IOException {
        application.getTunnelService().setExtensionsTunnel(false);
        this.testDir = Files.createTempDirectory("testDirectoryWithoutExtensionTunnelAndWithoutIndexName").toFile();
        application.setTestDirectory(testDir);

        // Test the directory Restlet with no index name
        testDirectory(application, application.getDirectory(), "");
    }

    @Test
    public void testDirectoryDeeplyAccessible() throws IOException {
        this.testDir = Files.createTempDirectory("testDirectoryDeeplyAccessible").toFile();
        application.setTestDirectory(testDir);

        final File testDirectory = new File(this.testDir, "dir/subDir");
        testDirectory.mkdirs();
        final File testFile = createTempFile("test", ".txt", testDirectory);

        application.getDirectory().setDeeplyAccessible(true);
        application.getDirectory().setListingAllowed(true);
        Response response = new TestRequest(this.webSiteURL, "dir/subDir/")
                .baseRef(this.webSiteURL)
                .handle(GET);
        assertEquals(SUCCESS_OK, response.getStatus());

        response = new TestRequest(this.webSiteURL, "dir/subDir/", testFile.getName())
                .baseRef(this.webSiteURL)
                .handle(GET);
        assertEquals(SUCCESS_NO_CONTENT, response.getStatus());

        application.getDirectory().setDeeplyAccessible(false);
        response = new TestRequest(this.webSiteURL, "dir/subDir/")
                .baseRef(this.webSiteURL)
                .handle(GET);
        assertEquals(CLIENT_ERROR_NOT_FOUND, response.getStatus());

        response = new TestRequest(this.webSiteURL, "dir/subDir/", testFile.getName())
                .baseRef(this.webSiteURL)
                .handle(GET);
        assertEquals(CLIENT_ERROR_NOT_FOUND, response.getStatus());
    }

    @Test
    public void testParentDirectoryInaccessible() throws IOException {
        application.getTunnelService().setExtensionsTunnel(false);
        this.testDir = Files.createTempDirectory("testParentDirectoryInaccessible").toFile();

        File childDir = new File(testDir, "child dir");
        childDir.mkdir();

        application.setTestDirectory(childDir);

        final File testFile = new File(childDir, "file.txt");
        assertTrue(testFile.createNewFile());

        final File privateFile = new File(testDir, "private.txt");
        assertTrue(privateFile.createNewFile());

        application.getDirectory().setDeeplyAccessible(true);
        application.getDirectory().setListingAllowed(true);

        Response response;

        response = new TestRequest(this.webSiteURL, "file.txt")
                .baseRef(this.webSiteURL)
                .handle(GET);

        // assert no content as the file is empty
        assertEquals(Status.SUCCESS_NO_CONTENT, response.getStatus());

        response = new TestRequest(this.webSiteURL, "%2e%2e/child%20dir/file.txt")
                .baseRef(this.webSiteURL)
                .handle(GET);

        // assert no content as the file is empty
        assertEquals(SUCCESS_NO_CONTENT, response.getStatus());

        response = new TestRequest(this.webSiteURL, "%2e%2e%2fchild%20dir/file.txt")
                .baseRef(this.webSiteURL)
                .handle(GET);

        // assert no content as the file is empty
        assertEquals(SUCCESS_NO_CONTENT, response.getStatus());

        response = new TestRequest(this.webSiteURL, "../child%20dir/file.txt")
                .baseRef(this.webSiteURL)
                .handle(GET);

        // assert no content as the file is empty
        assertEquals(Status.SUCCESS_NO_CONTENT, response.getStatus());

        response = new TestRequest(this.webSiteURL, "..")
                .baseRef(this.webSiteURL)
                .handle(GET);
        assertEquals(CLIENT_ERROR_FORBIDDEN, response.getStatus());

        response = new TestRequest(this.webSiteURL, "../")
                .baseRef(this.webSiteURL)
                .handle(GET);
        assertEquals(CLIENT_ERROR_FORBIDDEN, response.getStatus());

        response = new TestRequest(this.webSiteURL, "../private.txt")
                .baseRef(this.webSiteURL)
                .handle(GET);

        assertEquals(CLIENT_ERROR_FORBIDDEN, response.getStatus());

        response = new TestRequest(this.webSiteURL, "%2e%2e")
                .baseRef(this.webSiteURL)
                .handle(GET);
        assertEquals(CLIENT_ERROR_FORBIDDEN, response.getStatus());

        response = new TestRequest(this.webSiteURL, "%2e%2e/")
                .baseRef(this.webSiteURL)
                .handle(GET);
        assertEquals(CLIENT_ERROR_FORBIDDEN, response.getStatus());

        response = new TestRequest(this.webSiteURL, "%2e%2e%2f")
                .baseRef(this.webSiteURL)
                .handle(GET);
        assertEquals(CLIENT_ERROR_FORBIDDEN, response.getStatus());

        response = new TestRequest(this.webSiteURL, "%2e%2e/private.txt")
                .baseRef(this.webSiteURL)
                .handle(GET);
        assertEquals(CLIENT_ERROR_FORBIDDEN, response.getStatus());

        response = new TestRequest(this.webSiteURL, "%2e%2e%2fprivate.txt")
                .baseRef(this.webSiteURL)
                .handle(GET);
        assertEquals(CLIENT_ERROR_FORBIDDEN, response.getStatus());
    }

    /**
     * Test content negotiation based on client preferences.
     */
    @Test
    public void testUserPreferences() throws IOException {
        application.getTunnelService().setExtensionsTunnel(true); // Allow extensions tunneling
        application.getMetadataService().setDefaultLanguage(Language.ENGLISH); // default language is not es or fr.
        this.testDir = Files.createTempDirectory("testUserPreferences").toFile();
        System.out.println(this.testDir.getAbsolutePath());
        application.setTestDirectory(testDir);
        application.getDirectory().setModifiable(true);

        final String testFileUrl = this.webSiteURL.concat("test");
        final String testTxtFileUrl = this.webSiteURL.concat("test.txt");
        final String testFrTxtFileUrl = this.webSiteURL.concat("test.fr.txt");
        final String testEsTxtFileUrl = this.webSiteURL.concat("test.es.txt");

        // Create two files
        Response response = new TestRequest(testFrTxtFileUrl)
                .baseRef(this.webSiteURL)
                .entity("fr")
                .handle(PUT);
        assertEquals(SUCCESS_CREATED, response.getStatus());

        response = new TestRequest(testEsTxtFileUrl)
                .baseRef(this.webSiteURL)
                .entity("es")
                .handle(PUT);
        assertEquals(SUCCESS_CREATED, response.getStatus());

        response = new TestRequest(testFileUrl)
                .baseRef(this.webSiteURL)
                .accept(SPANISH)
                .handle(GET);

        assertEquals(SUCCESS_OK, response.getStatus());
        assertEquals("es", response.getEntityAsText());

        response = new TestRequest(testFileUrl)
                .baseRef(this.webSiteURL)
                .accept(FRENCH)
                .handle(GET);

        assertEquals(SUCCESS_OK, response.getStatus());
        assertEquals("fr", response.getEntityAsText());

        response = new TestRequest(testTxtFileUrl)
                .baseRef(this.webSiteURL)
                .accept(SPANISH)
                .handle(GET);
        assertEquals(SUCCESS_OK, response.getStatus());
        assertEquals("es", response.getEntityAsText());

        response = new TestRequest(testTxtFileUrl)
                .baseRef(this.webSiteURL)
                .accept(FRENCH)
                .handle(GET);
        assertEquals(SUCCESS_OK, response.getStatus());
        assertEquals("fr", response.getEntityAsText());
    }

    /**
     * Internal class used for test purpose
     *
     * @author Thierry Boileau
     */
    private static class MyApplication extends Application {

        Directory directory;

        /**
         * Constructor.
         *
         * @param testDirectory
         *            The test directory.
         */
        public MyApplication(File testDirectory) {
            // Create a DirectoryHandler that manages a local Directory
            this.directory = new Directory(getContext(), createFileReference(testDirectory));
            this.directory.setNegotiatingContent(true);
        }

        @Override
        public Restlet createInboundRoot() {
            return this.directory;
        }

        public Directory getDirectory() {
            return this.directory;
        }

        public void setTestDirectory(File testDirectory) {
            final String rootIdentifier = createFileReference(testDirectory).getIdentifier();

            resetDirectoryToDefault();

            if (rootIdentifier.endsWith("/")) {
                this.directory.setRootRef(new Reference(rootIdentifier));
            } else {
                this.directory.setRootRef(new Reference(rootIdentifier + "/"));
            }
        }

        private void resetDirectoryToDefault() {
            this.directory.setDeeplyAccessible(true);
            this.directory.setIndexName("index");
            this.directory.setListingAllowed(false);
            this.directory.setModifiable(false);
            this.directory.setNegotiatingContent(true);
        }

    }

    private static class TestRequest {
        Request request = new Request();

        public TestRequest(String... resourceRefSegments) {
            StringBuilder sb = new StringBuilder();
            for (String segment : resourceRefSegments) {
                sb.append(segment);
            }
            request.setResourceRef(new Reference(sb.toString()));
        }

        protected TestRequest baseRef(String baseRef) {
            request.getResourceRef().setBaseRef(baseRef);
            return this;
        }

        protected TestRequest entity(String entity) {
            request.setEntity(new StringRepresentation(entity));
            return this;
        }

        protected TestRequest entityLanguage(Language language) {
            request.getEntity().getLanguages().add(language);
            return this;
        }

        protected TestRequest accept(Metadata metadata) {
            request.getClientInfo().accept(metadata);
            return this;
        }

        protected TestRequest header(Header header) {
            request.getHeaders().add(header);
            return this;
        }

        protected TestRequest query(String name, String value) {
            request.getResourceRef().addQueryParameter(name, value);
            return this;
        }

        protected Response handle(Method method) {
            final Response response = new Response(request);
            request.setMethod(method);
            request.setOriginalRef(ReferenceUtils.getOriginalRef(request.getResourceRef(), request.getHeaders()));
            Application.getCurrent().handle(request, response);
            return response;
        }
    }

    /**
     * Helper
     *
     * @param directory
     * @param indexName
     * @throws IOException
     */
    private void testDirectory(MyApplication application, Directory directory, String indexName) throws IOException {
        // Create a temporary file for the tests (the tests directory is not empty)
        final File testFile = File.createTempFile("test", ".txt", this.testDir);
        // Create a temporary directory
        final File testDirectory = new File(this.testDir, "try");
        testDirectory.mkdir();

        final String testFileUrl = this.webSiteURL.concat(testFile.getName());
        final String testDirectoryUrl = this.webSiteURL.concat(testDirectory.getName());

        directory.setIndexName(indexName);
        // Test 1a : directory does not allow to GET its content
        directory.setListingAllowed(false);
        Response response = new TestRequest(this.webSiteURL)
                .baseRef(this.webSiteURL)
                .handle(GET);
        assertEquals(CLIENT_ERROR_NOT_FOUND, response.getStatus());

        // Test 1b : directory allows to GET its content
        directory.setListingAllowed(true);
        response = new TestRequest(this.webSiteURL)
                .baseRef(this.webSiteURL)
                .handle(GET);
        assertEquals(SUCCESS_OK, response.getStatus());
        // should list all files in the directory (at least the temporary file generated before)
        assertTrue(response.getEntityAsText().contains(testFile.getName()));

        // Test 2a : tests the HEAD method
        response = new TestRequest(testFileUrl)
                .baseRef(this.webSiteURL)
                .handle(GET);
        assertEquals(SUCCESS_NO_CONTENT, response.getStatus());

        response = new TestRequest(testFileUrl)
                .baseRef(this.webSiteURL)
                .handle(HEAD);
        assertEquals(SUCCESS_NO_CONTENT, response.getStatus());

        // Test 2b : try to GET a file that does not exist
        response = new TestRequest(this.webSiteURL, "123456.txt")
                .baseRef(this.webSiteURL)
                .handle(GET);
        assertEquals(CLIENT_ERROR_NOT_FOUND, response.getStatus());

        // Test 3a : try to put a new representation, but the directory is read only
        directory.setModifiable(false);
        response = new TestRequest(this.baseFileUrl)
                .baseRef(this.webSiteURL)
                .entity("this is test 3a")
                .handle(PUT);
        assertEquals(CLIENT_ERROR_METHOD_NOT_ALLOWED, response.getStatus());

        // Test 3b : try to put a new representation, the directory is no more
        // read only
        directory.setModifiable(true);
        response = new TestRequest(this.baseFileUrlFr)
                .baseRef(this.webSiteURL)
                .entity("this is test 3b")
                .entityLanguage(FRENCH)
                .handle(PUT);
        assertEquals(SUCCESS_CREATED, response.getStatus());

        response = new TestRequest(this.baseFileUrl)
                .baseRef(this.webSiteURL)
                .query("x", "y")
                .handle(GET);
        assertEquals(SUCCESS_OK, response.getStatus());
        assertEquals("this is test 3b", response.getEntityAsText());

        // Test 4 : Try to get the representation of the new file
        response = new TestRequest(this.baseFileUrl)
                .baseRef(this.webSiteURL)
                .handle(GET);
        assertEquals(SUCCESS_OK, response.getStatus());
        assertEquals("this is test 3b", response.getEntityAsText());

        // Test 5 : add a new representation of the same base file
        response = new TestRequest(this.baseFileUrlEn)
                .baseRef(this.webSiteURL)
                .entity("this is a test - En")
                .handle(PUT);
        assertEquals(SUCCESS_CREATED, response.getStatus());
        response = new TestRequest(this.baseFileUrl)
                .baseRef(this.webSiteURL)
                .handle(HEAD);
        assertEquals(SUCCESS_OK, response.getStatus());
        response = new TestRequest(this.baseFileUrlEn)
                .baseRef(this.webSiteURL)
                .handle(HEAD);
        assertEquals(SUCCESS_OK, response.getStatus());

        // Test 6a : delete a file
        response = new TestRequest(testFileUrl)
                .baseRef(this.webSiteURL)
                .handle(DELETE);
        assertEquals(SUCCESS_NO_CONTENT, response.getStatus());

        response = new TestRequest(testFileUrl)
                .baseRef(this.webSiteURL)
                .handle(HEAD);
        assertEquals(CLIENT_ERROR_NOT_FOUND, response.getStatus());

        // Test 6b : delete a file that does not exist
        response = new TestRequest(testFileUrl)
                .baseRef(this.webSiteURL)
                .handle(DELETE);
        assertEquals(CLIENT_ERROR_NOT_FOUND, response.getStatus());

        // Test 6c : delete a directory (without and with trailing slash)
        // Distinct behaviors if an index has been defined or not
        if (indexName.isEmpty()) {
            response = new TestRequest(testDirectoryUrl)
                    .baseRef(this.webSiteURL)
                    .handle(DELETE);
            assertEquals(REDIRECTION_SEE_OTHER, response.getStatus());

            response = new TestRequest(response.getLocationRef().getIdentifier())
                    .baseRef(response.getLocationRef().getIdentifier())
                    .handle(DELETE);
            assertEquals(CLIENT_ERROR_FORBIDDEN, response.getStatus());

            response = new TestRequest(this.webSiteURL)
                    .baseRef(this.webSiteURL)
                    .handle(DELETE);
            assertEquals(CLIENT_ERROR_FORBIDDEN, response.getStatus());
        } else {
            // As there is no index file in the directory, the response must
            // return Status.CLIENT_ERROR_NOT_FOUND
            response = new TestRequest(testDirectoryUrl, "/")
                    .baseRef(this.webSiteURL)
                    .handle(DELETE);
            assertEquals(CLIENT_ERROR_NOT_FOUND, response.getStatus());
            response = new TestRequest(this.webSiteURL)
                    .baseRef(this.webSiteURL)
                    .handle(DELETE);
            assertEquals(CLIENT_ERROR_NOT_FOUND, response.getStatus());
        }

        // Test 7a : put one representation of the base file (in French language)
        response = new TestRequest(this.baseFileUrlFr)
                .baseRef(this.webSiteURL)
                .entity("message de test")
                .handle(PUT);
        assertTrue(response.getStatus().isSuccess());

        // Test 7b : put another representation of the base file (in French
        // language) but the extensions are mixed
        // and there is no content negotiation
        directory.setNegotiatingContent(false);
        response = new TestRequest(this.baseFileUrlFrBis)
                .baseRef(this.webSiteURL)
                .entity("message de test")
                .handle(PUT);
        assertEquals(SUCCESS_NO_CONTENT, response.getStatus());
        // The 2 resources in French must be present (the same actually)
        response = new TestRequest(this.baseFileUrlFr)
                .baseRef(this.webSiteURL)
                .handle(HEAD);
        assertEquals(Status.SUCCESS_OK, response.getStatus());
        response = new TestRequest(this.baseFileUrlFrBis)
                .baseRef(this.webSiteURL)
                .handle(HEAD);
        assertEquals(Status.SUCCESS_OK, response.getStatus());

        // Test 7c : delete the file representation of the resources with no content negotiation
        // The 2 French resources are deleted (there were only one)
        response = new TestRequest(this.baseFileUrlFr)
                .baseRef(this.webSiteURL)
                .handle(DELETE);
        assertEquals(SUCCESS_NO_CONTENT, response.getStatus());

        response = new TestRequest(this.baseFileUrlFr)
                .baseRef(this.webSiteURL)
                .handle(HEAD);
        assertEquals(CLIENT_ERROR_NOT_FOUND, response.getStatus());

        response = new TestRequest(this.baseFileUrlFrBis)
                .baseRef(this.webSiteURL)
                .handle(HEAD);
        assertEquals(CLIENT_ERROR_NOT_FOUND, response.getStatus());

        response = new TestRequest(this.baseFileUrlFrBis)
                .baseRef(this.webSiteURL)
                .handle(DELETE);
        assertEquals(CLIENT_ERROR_NOT_FOUND, response.getStatus());

        // Test 7d : put another representation of the base file (in French
        // language) but the extensions are mixed
        // and there is content negotiation
        directory.setNegotiatingContent(true);
        response = new TestRequest(this.baseFileUrlFr)
                .baseRef(this.webSiteURL)
                .entity("message de test")
                .handle(PUT);
        assertEquals(SUCCESS_CREATED, response.getStatus());
        response = new TestRequest(this.baseFileUrlFrBis)
                .baseRef(this.webSiteURL)
                .entity("message de test Bis")
                .handle(PUT);
        assertEquals(SUCCESS_NO_CONTENT, response.getStatus());
        // only one resource in French must be present
        response = new TestRequest(this.baseFileUrlFr)
                .baseRef(this.webSiteURL)
                .handle(HEAD);
        assertEquals(SUCCESS_OK, response.getStatus());
        response = new TestRequest(this.baseFileUrlFrBis)
                .baseRef(this.webSiteURL)
                .handle(HEAD);
        assertEquals(SUCCESS_OK, response.getStatus());

        // TBOI : not sure this test is correct
        // Check if only one resource has been created
        directory.setNegotiatingContent(false);
        response = new TestRequest(this.baseFileUrlFr)
                .baseRef(this.webSiteURL)
                .handle(HEAD);
        assertEquals(SUCCESS_OK, response.getStatus());

        // Test 7e : delete the file representation of the resources with content negotiation
        directory.setNegotiatingContent(true);
        response = new TestRequest(this.baseFileUrlFr)
                .baseRef(this.webSiteURL)
                .handle(DELETE);
        assertEquals(SUCCESS_NO_CONTENT, response.getStatus());

        response = new TestRequest(this.baseFileUrlFr)
                .baseRef(this.webSiteURL)
                .handle(HEAD);
        if (application.getTunnelService().isExtensionsTunnel()) {
            assertEquals(SUCCESS_OK, response.getStatus());
        } else {
            assertEquals(CLIENT_ERROR_NOT_FOUND, response.getStatus());
        }

        response = new TestRequest(this.baseFileUrlFrBis)
                .baseRef(this.webSiteURL)
                .handle(HEAD);
        if (application.getTunnelService().isExtensionsTunnel()) {
            assertEquals(SUCCESS_OK, response.getStatus());
        } else {
            assertEquals(CLIENT_ERROR_NOT_FOUND, response.getStatus());
        }

        // Test 8 : must delete the English representation
        response = new TestRequest(this.baseFileUrlFr)
                .baseRef(this.webSiteURL)
                .handle(DELETE);
        response = new TestRequest(this.baseFileUrlEn)
                .baseRef(this.webSiteURL)
                .handle(DELETE);
        assertEquals(SUCCESS_NO_CONTENT, response.getStatus());

        // Test 9a : put a new representation, the resource's URI contains
        // percent-encoded characters
        directory.setModifiable(true);

        response = new TestRequest(this.percentEncodedFileUrl)
                .baseRef(this.webSiteURL)
                .entity("this is test 9a")
                .entityLanguage(FRENCH)
                .handle(PUT);
        assertEquals(SUCCESS_CREATED, response.getStatus());

        // Test 9b : Try to get the representation of the new file
        response = new TestRequest(this.percentEncodedFileUrl)
                .baseRef(this.webSiteURL)
                .handle(GET);
        assertEquals(SUCCESS_OK, response.getStatus());
        assertTrue(response.getEntityAsText().equals("this is test 9a"));

        // Test 9c : Try to get the representation of the new file with an
        // equivalent URI
        response = new TestRequest(this.percentEncodedFileUrlBis)
                .baseRef(this.webSiteURL)
                .handle(GET);
        assertEquals(SUCCESS_OK, response.getStatus());
        assertTrue(response.getEntityAsText().equals("this is test 9a"));

        // Test 9d : Try to delete the file
        response = new TestRequest(this.percentEncodedFileUrl)
                .baseRef(this.webSiteURL)
                .handle(DELETE);
        assertEquals(SUCCESS_NO_CONTENT, response.getStatus());

        // Test 10a : Try to create a directory with an unknown hierarchy of
        // parent directories.
        response = new TestRequest(this.testCreationDirectory)
                .baseRef(this.webSiteURL)
                .entity("useless entity")
                .handle(PUT);
        assertEquals(SUCCESS_CREATED, response.getStatus());

        // Test 10b : Try to create a directory (with the trailing "/") with an
        // unknown hierarchy of parent directories.
        response = new TestRequest(this.testCreationDirectory, "/")
                .baseRef(this.webSiteURL)
                .entity("useless entity")
                .handle(PUT);
        assertEquals(SUCCESS_NO_CONTENT, response.getStatus());

        // Test 10c : Try to create a file with an unknown hierarchy of
        // parent directories. The name and the metadata of the provided entity
        // don't match
        response = new TestRequest(testCreationFile)
                .baseRef(this.webSiteURL)
                .entity("file entity")
                .handle(PUT);
        assertEquals(CLIENT_ERROR_BAD_REQUEST, response.getStatus());

        // Test 10d : Try to create a file with an unknown hierarchy of
        // parent directories. The name and the metadata of the provided entity
        // match
        response = new TestRequest(testCreationTextFile)
                .baseRef(this.webSiteURL)
                .entity("file entity")
                .handle(PUT);
        assertEquals(SUCCESS_CREATED, response.getStatus());

        // Test 11 : redirection for Directory
        response = new TestRequest(testDirectoryUrl)
                .baseRef(this.webSiteURL)
                .entity("file entity")
                .handle(GET);
        assertEquals(REDIRECTION_SEE_OTHER, response.getStatus());
        assertEquals("http://myapplication/try/", response.getLocationRef().toString());

        // Test 12 : redirection for Directory with proxy forwarding
        response = new TestRequest(testDirectoryUrl)
                .baseRef(this.webSiteURL)
                .header(new Header(HeaderConstants.HEADER_X_FORWARDED_PROTO, "https"))
                .header(new Header(HeaderConstants.HEADER_X_FORWARDED_PORT, "123"))
                .handle(GET);
        assertEquals(REDIRECTION_SEE_OTHER, response.getStatus());
        assertEquals("https://myapplication:123/try/", response.getLocationRef().toString());

        // Test 13 : creation of resource with proxy forwarding
        response = new TestRequest(this.percentEncodedFileUrl)
                .baseRef(this.webSiteURL)
                .entity("this is test")
                .handle(PUT);
        assertEquals(SUCCESS_CREATED, response.getStatus());

        response = new TestRequest(this.percentEncodedFileUrl)
                .baseRef(this.webSiteURL)
                .header(new Header(HeaderConstants.HEADER_X_FORWARDED_PROTO, "https"))
                .header(new Header(HeaderConstants.HEADER_X_FORWARDED_PORT, "123"))
                .handle(GET);
        assertEquals("https://myapplication:123/a%20new%20%25file.txt.fr", response.getEntity().getLocationRef()
                .toString());
    }

}
