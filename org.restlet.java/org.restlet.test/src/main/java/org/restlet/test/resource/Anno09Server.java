/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.resource;

public class Anno09Server {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        InternalConnectorTestCase anno9 = new AnnotatedResource09TestCase();
        anno9.initServer();
    }

}
