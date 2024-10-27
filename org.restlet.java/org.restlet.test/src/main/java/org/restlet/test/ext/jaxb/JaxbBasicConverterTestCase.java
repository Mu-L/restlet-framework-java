/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.ext.jaxb;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.restlet.data.MediaType;
import org.restlet.ext.jaxb.JaxbConverter;
import org.restlet.ext.jaxb.JaxbRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.test.RestletTestCase;

/**
 * Tests basic Conversion using the JaxbConverter
 *
 * @author Sanjay Acharya
 */
public class JaxbBasicConverterTestCase extends RestletTestCase {

    @Test
    public void testObjectionToRepresentation() {
        Representation rep = new JaxbConverter().toRepresentation(new Sample(), new Variant(
                MediaType.APPLICATION_XML), null);
        assertInstanceOf(JaxbRepresentation.class, rep);
    }

    @Test
    public void testRepresentationToObject() throws IOException {
        JaxbRepresentation<Sample> sampleRep = new JaxbRepresentation<>(
                MediaType.APPLICATION_XML, new Sample());
        Object rep = new JaxbConverter().toObject(sampleRep, Sample.class, null);
        assertInstanceOf(Sample.class, rep);
    }
}
