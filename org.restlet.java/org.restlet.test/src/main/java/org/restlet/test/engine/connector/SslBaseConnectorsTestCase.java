/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.engine.connector;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.restlet.Application;
import org.restlet.Client;
import org.restlet.Component;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Server;
import org.restlet.data.Method;
import org.restlet.data.Parameter;
import org.restlet.data.Protocol;
import org.restlet.engine.Engine;
import org.restlet.engine.connector.ConnectorHelper;
import org.restlet.engine.io.IoUtils;
import org.restlet.engine.local.ClapClientHelper;
import org.restlet.test.RestletTestCase;
import org.restlet.util.Series;

/**
 * Base test case that will call an abstract method for several client/server
 * connectors configurations. (Modified for SSL support.)
 * 
 * @author Kevin Conaway
 * @author Bruno Harbulot
 * @author Jerome Louvel
 */
@SuppressWarnings("unused")
public abstract class SslBaseConnectorsTestCase extends RestletTestCase {

    private Component component;

    private final boolean enabledClientApache = true;

    private final boolean enabledClientInternal = true;

    private final boolean enabledClientJetty = false;

    private final boolean enabledServerInternal = true;

    private final boolean enabledServerJetty = false;

    private final File testDir = new File(System.getProperty("java.io.tmpdir"),
            "SslBaseConnectorsTestCase");

    protected final File testKeystoreFile = new File(testDir, "dummy.jks");

    protected abstract void call(String uri) throws Exception;

    protected void configureSslClientParameters(Context context) {
        Series<Parameter> parameters = context.getParameters();
        parameters.add("truststorePath", testKeystoreFile.getPath());
        parameters.add("truststorePassword", "testtest");
    }

    protected void configureSslServerParameters(Context context) {
        Series<Parameter> parameters = context.getParameters();
        parameters.add("keystorePath", testKeystoreFile.getPath());
        parameters.add("keystorePassword", "testtest");
        parameters.add("keyPassword", "testtest");
        parameters.add("truststorePath", testKeystoreFile.getPath());
        parameters.add("truststorePassword", "testtest");
        // parameters.add("tracing", "true");
    }

    protected abstract Application createApplication(Component component);

    // Helper methods
    private void runTest(ConnectorHelper<Server> server,
            ConnectorHelper<Client> client) throws Exception {
        Engine engine = Engine.register(false);
        engine.getRegisteredClients().add(new ClapClientHelper(null));
        engine.getRegisteredServers().add(server);
        engine.getRegisteredClients().add(client);
        String uri = start();

        try {
            call(uri);
        } finally {
            stop();
        }
    }

    @BeforeEach
    public void setUpEach() throws Exception {
        try {
            if (!testKeystoreFile.exists()) {
                // Prepare a temporary directory for the tests
                IoUtils.delete(this.testDir, true);
                this.testDir.mkdir();
                // Copy the keystore into the test directory
                Response response = new Client(Protocol.CLAP)
                        .handle(new Request(Method.GET,
                                "clap://class/org/restlet/test/engine/connector/dummy.jks"));

                if (response.getEntity() != null) {
                    OutputStream outputStream = new FileOutputStream(
                            testKeystoreFile);
                    response.getEntity().write(outputStream);
                    outputStream.flush();
                    outputStream.close();
                } else {
                    throw new Exception(
                            "Unable to find the dummy.jks file in the classpath.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected String start() throws Exception {
        this.component = new Component();

        final Server server = this.component.getServers()
                .add(Protocol.HTTPS, 0);
        configureSslServerParameters(server.getContext());
        final Application application = createApplication(this.component);

        this.component.getDefaultHost().attach(application);
        this.component.start();

        return "https://localhost:" + server.getEphemeralPort() + "/test";
    }

    protected void stop() throws Exception {
        if ((this.component != null) && this.component.isStarted()) {
            this.component.stop();
        }
        this.component = null;
    }

    @AfterEach
    protected void tearDownEach() throws Exception {
        IoUtils.delete(this.testKeystoreFile);
        IoUtils.delete(this.testDir, true);

        // Restore a clean engine
        org.restlet.engine.Engine.register();
    }

    @Test
    public void testSslInternalAndApache() throws Exception {
        if (this.enabledServerInternal && this.enabledClientApache) {
            runTest(new org.restlet.engine.connector.HttpsServerHelper(null),
                    new org.restlet.ext.httpclient.HttpClientHelper(null));
        }
    }

    @Test
    public void testSslInternalAndInternal() throws Exception {
        if (this.enabledServerInternal && this.enabledClientInternal) {
            runTest(new org.restlet.engine.connector.HttpsServerHelper(null),
                    new org.restlet.engine.connector.HttpClientHelper(null));
        }
    }

    @Test
    public void testSslInternalAndJetty() throws Exception {
        if (this.enabledServerInternal && this.enabledClientJetty) {
            runTest(new org.restlet.engine.connector.HttpsServerHelper(null),
                    new org.restlet.ext.jetty.HttpClientHelper(null));
        }
    }

    @Test
    public void testSslJettyAndApache() throws Exception {
        if (this.enabledServerJetty && this.enabledClientApache) {
            runTest(new org.restlet.ext.jetty.HttpsServerHelper(null),
                    new org.restlet.ext.httpclient.HttpClientHelper(null));
        }
    }

    @Test
    public void testSslJettyAndInternal() throws Exception {
        if (this.enabledServerJetty && this.enabledClientInternal) {
            runTest(new org.restlet.ext.jetty.HttpsServerHelper(null),
                    new org.restlet.engine.connector.HttpClientHelper(null));
        }
    }

    @Test
    public void testSslJettyAndJetty() throws Exception {
        if (this.enabledServerJetty && this.enabledClientJetty) {
            runTest(new org.restlet.ext.jetty.HttpsServerHelper(null),
                    new org.restlet.ext.jetty.HttpClientHelper(null));
        }
    }
}
