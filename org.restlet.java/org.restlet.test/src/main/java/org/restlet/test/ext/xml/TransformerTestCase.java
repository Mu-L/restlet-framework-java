/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.ext.xml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayOutputStream;

import org.junit.jupiter.api.Test;
import org.restlet.Component;
import org.restlet.data.MediaType;
import org.restlet.ext.xml.TransformRepresentation;
import org.restlet.ext.xml.Transformer;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.test.RestletTestCase;

/**
 * Test case for the Transformer class.
 * 
 * @author Jerome Louvel
 */
public class TransformerTestCase extends RestletTestCase {

    class FailureTracker {
        boolean allOk = true;

        final StringBuffer trackedMessages = new StringBuffer();

        void report() {
            if (!this.allOk) {
                fail("TRACKER REPORT: \n" + this.trackedMessages.toString());
            }
        }

        void trackFailure(String message) {
            System.err.println(message);
            this.trackedMessages.append(message + "\n");
            this.allOk = false;
        }

        void trackFailure(String message, int index, Throwable e) {
            e.printStackTrace();
            trackFailure(message + " " + index + ": " + e.getMessage());
        }
    }

    final String output = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><buyer>cust123</buyer>23.45";

    // Create a source XML document
    final Representation source = new StringRepresentation(
            "<?xml version=\"1.0\"?>" + "<purchase id=\"p001\">"
                    + "<customer db=\"cust123\"/>" + "<product db=\"prod345\">"
                    + "<amount>23.45</amount>" + "</product>" + "</purchase>",
            MediaType.TEXT_XML);

    // Create a transform XSLT sheet
    final Representation xslt = new StringRepresentation(
            "<?xml version=\"1.0\"?>"
                    + "<xsl:transform xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\">"
                    + "<xsl:template match =\"customer\">"
                    + "<buyer><xsl:value-of select=\"@db\"/></buyer>"
                    + "</xsl:template>" + "</xsl:transform>",
            MediaType.TEXT_XML);

    /**
     * This was removed from the automatically tested method because it is too
     * consuming.
     */
    @Test
    public void parallelTestTransform() {
        Component comp = new Component();
        final TransformRepresentation tr = new TransformRepresentation(
                comp.getContext(), this.source, this.xslt);
        final FailureTracker tracker = new FailureTracker();

        final int testVolume = 5000;
        final Thread[] parallelTransform = new Thread[testVolume];
        for (int i = 0; i < parallelTransform.length; i++) {
            final int index = i;
            parallelTransform[i] = new Thread() {

                @Override
                public void run() {
                    try {
                        final ByteArrayOutputStream out = new ByteArrayOutputStream();
                        tr.write(out);
                        final String result = out.toString();
                        assertEquals(TransformerTestCase.this.output, result);
                        out.close();

                    } catch (Throwable e) {
                        tracker.trackFailure(
                                "Exception during write in thread ", index, e);
                    }
                }
            };
        }

        for (final Thread pt : parallelTransform) {
            pt.start();
        }

        tracker.report();
    }

    @Test
    public void testTransform() throws Exception {
        final Transformer transformer = new Transformer(
                Transformer.MODE_REQUEST, this.xslt);
        final String result = transformer.transform(this.source).getText();

        assertEquals(this.output, result);
    }

}
