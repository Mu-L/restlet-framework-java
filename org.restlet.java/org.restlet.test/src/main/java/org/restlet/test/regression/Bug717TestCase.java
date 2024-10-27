/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.regression;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.testng.AssertJUnit.assertNotNull;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;
import org.restlet.data.MediaType;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.ext.xml.SaxRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.xml.sax.InputSource;

/**
 * Simple test case to illustrate defect #717 and validate the fix when applied.
 */
public class Bug717TestCase {

    private static final String RESTLET_XML = "<?xml version=\"1.0\"?>\n"
            + "<component xmlns=\"http://restlet.org/schemas/2.0/Component\">\n"
            + "<server protocol=\"HTTP\" port=\"9090\"/>\n"
            + "<server protocol=\"HTTP\" port=\"9091\"/>\n"
            + "<defaultHost hostPort=\"9091\">\n"
            + "<attach uriPattern=\"/abcd\" targetClass=\"org.restlet.test.HelloWorldApplication\"/>\n"
            + "</defaultHost>\n"
            + "<host hostPort=\"9090\">\n"
            + "<attach uriPattern=\"/efgh\" targetClass=\"org.restlet.test.HelloWorldApplication\"/>\n"
            + "</host>\n" + "</component>\n";

    @Test
    public void test() throws IOException {
        InputStream inStr = getClass().getResourceAsStream(
                "/org/restlet/Component.xsd");
        assertNotNull("Component.xsd input stream MUST NOT be null", inStr);

        InputSource inSrc = new InputSource(inStr);
        assertNotNull("Component.xsd SAX input source MUST NOT be null", inSrc);

        SaxRepresentation schema = new SaxRepresentation(
                MediaType.APPLICATION_W3C_SCHEMA, inSrc);
        assertNotNull("Component.xsd SAX Representation MUST NOT be null",
                schema);

        Representation xmlRep = new StringRepresentation(RESTLET_XML);
        assertNotNull("Restlet.xml SAX Representation MUST NOT be null", xmlRep);

        DomRepresentation xml = new DomRepresentation(xmlRep);
        assertNotNull("Restlet.xml DOM Representation MUST NOT be null", xml);

        try {
            xml.validate(schema);
            assertTrue(true);
        } catch (Exception x) {
            x.printStackTrace(System.err);
            fail("Bug717TestCase - Failed validating a correct restlet.xml "
                    + "representation against the current Component W3C schema: "
                    + x.getLocalizedMessage());
        }
    }
}
