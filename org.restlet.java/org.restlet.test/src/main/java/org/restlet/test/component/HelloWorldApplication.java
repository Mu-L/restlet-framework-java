/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.component;

import org.restlet.Application;
import org.restlet.Restlet;

public class HelloWorldApplication extends Application {

    @Override
    public synchronized Restlet createInboundRoot() {
        return new HelloWorldRestlet();
    }
}