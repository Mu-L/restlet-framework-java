/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.ext.guice;

import static com.google.inject.name.Names.named;

import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;
import javax.inject.Named;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.ext.guice.FinderFactory;
import org.restlet.ext.guice.RestletGuice;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Router;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * Demonstrates trivial use of FinderFactory and RestletGuice by starting a
 * component on port 8182 with a single application that routes to Finder
 * instances obtained from a FinderFactory. A command line argument controls
 * whether the Injector behind the FinderFactory is created explicitly with
 * RestletGuice or implicitly from the first use of RestletGuice.Module as a
 * FinderFactory.
 */
public class Main extends Application {

    public static class DefaultResource extends ServerResource {

        private final String path;

        @Inject
        DefaultResource(@Named(PATH_QUALIFIER)
        String path) {
            this.path = path;
        }

        @Get
        public String represent() {
            return String.format("Default resource, try %s -- mode is %s",
                    path, mode);
        }
    }

    public static class HelloServerResource extends ServerResource {

        private static final AtomicInteger count = new AtomicInteger();

        private final String msg;

        @Inject
        public HelloServerResource(@Named(MSG_QUALIFIER)
        String msg) {
            this.msg = msg;
        }

        @Get
        public String asString() {
            return String.format("%d: %s", count.incrementAndGet(), msg);
        }
    }

    enum Mode {
        /**
         * Injector is created implicitly by first use of RestletGuice.Module as
         * FinderFactory.
         */
        AUTO_INJECTOR,

        /**
         * Injector is created explicitly with RestletGuice.
         */
        EXPLICIT_INJECTOR
    }

    static final String HELLO_MSG = "Hello, Restlet 2.0 - Guice 2.0!";

    static final String HELLO_PATH = "/hello";

    /**
     * Whether to create Injector explicitly or automatically.
     */
    static volatile Mode mode = Mode.AUTO_INJECTOR;

    static final String MSG_QUALIFIER = "hello.message";

    static final String PATH_QUALIFIER = "hello.path";

    /**
     * Creates and starts component. Pass 'auto' or 'explicit' (or a prefix of
     * either) to control how the Injector is created. Default is auto.
     */
    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
            if ("explicit".startsWith(args[0])) {
                mode = Mode.EXPLICIT_INJECTOR;
            } else if (!"auto".startsWith(args[0])) {
                System.out
                        .println("Call with prefix of 'auto' (default) or 'explicit'");
                System.exit(1);
            }
        }

        Component component = new Component();
        component.getServers().add(Protocol.HTTP, 8182);
        component.getDefaultHost().attach(new Main());
        component.start();
    }

    @Override
    public Restlet createInboundRoot() {

        Module bindings = new AbstractModule() {
            protected void configure() {

                bind(ServerResource.class).annotatedWith(HelloWorld.class).to(
                        HelloServerResource.class);

                bindConstant().annotatedWith(named(MSG_QUALIFIER))
                        .to(HELLO_MSG);

                bindConstant().annotatedWith(named(PATH_QUALIFIER)).to(
                        HELLO_PATH);
            }
        };

        FinderFactory ff = null;
        switch (mode) {
        case EXPLICIT_INJECTOR:
            Injector injector = RestletGuice.createInjector(bindings);
            ff = injector.getInstance(FinderFactory.class);
            break;

        case AUTO_INJECTOR:
        default:
            ff = new RestletGuice.Module(bindings);
            break;
        }

        assert ff != null : "Must specify Injector creation mode.";

        Router router = new Router(getContext());

        // Route HELLO_PATH to whatever is bound to ServerResource
        // annotated with @HelloWorld.
        router.attach(HELLO_PATH,
                ff.finder(ServerResource.class, HelloWorld.class));

        // Everything else goes to DefaultResource.
        router.attachDefault(ff.finder(DefaultResource.class));

        return router;
    }
}
