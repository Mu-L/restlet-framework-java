/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.resource;

import java.util.logging.Level;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.restlet.Application;
import org.restlet.Client;
import org.restlet.Component;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Server;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.engine.Engine;
import org.restlet.test.RestletTestCase;

/**
 * All test cases relying on a client and a server should inherit from this
 * class.
 * 
 * @author Jerome Louvel
 */
public abstract class InternalConnectorTestCase extends RestletTestCase {

    private Component c;

    private Client client;

    private String uri;

    public InternalConnectorTestCase() {
        super();
    }

    protected abstract Application createApplication(final String path);

    protected Request createRequest(Method method) {
        return new Request(method, getUri());
    }

    public Component getC() {
        return c;
    }

    public Client getClient() {
        return client;
    }

    public String getUri() {
        return uri;
    }

    protected Response handle(Request request) {
        return getClient().handle(request);
    }

    public void initClient() {
        setUpEngine();
        setUpCommon();
        setUpClient(8888, "/test");
    }

    public void initServer() throws Exception {
        setUpEngine();
        setUpCommon();
        setUpServer(8888, "/test");
    }

    protected void releaseResponse(Response response) {
        response.getEntity().release();
    }

    @BeforeEach
    protected void setUpEach() throws Exception {
        setUpCommon();
        int serverPort = setUpServer(0, "/test");
        setUpClient(serverPort, "/test");
    }

    protected void setUpClient(int serverPort, String path) {
        this.client = new Client(Protocol.HTTP);
        this.uri = "http://localhost:" + serverPort + path;
        this.client = new Client(Protocol.HTTP);
    }

    protected void setUpCommon() {
        Engine.setLogLevel(Level.INFO);
        Engine.getInstance().getRegisteredConverters().clear();
        Engine.getInstance().registerDefaultConverters();
    }

    protected int setUpServer(int suggestedPort, String path) throws Exception {
        c = new Component();
        final Server server = c.getServers().add(Protocol.HTTP, suggestedPort);
        c.getDefaultHost().attach(createApplication(path));
        c.start();
        return server.getActualPort();
    }

    @AfterEach
    protected void tearDownEach() throws Exception {
        tearDownClient();
        tearDownServer();
    }

    protected void tearDownClient() throws Exception {
        client.stop();
        client = null;
    }

    protected void tearDownServer() throws Exception {
        c.stop();
        c = null;
    }

}