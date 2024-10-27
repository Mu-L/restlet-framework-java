/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.resource;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

/**
 * Sample server resource for testing annotated PUT methods.
 * 
 * @author Jerome Louvel
 */
public class MyServerResource14 extends ServerResource {

    @Put
    public Representation store1(Representation rep) {
        return new StringRepresentation("*", MediaType.TEXT_PLAIN);
    }

    @Put("xml")
    public Representation store2(Representation rep) {
        return new StringRepresentation("xml", MediaType.APPLICATION_XML);
    }

    @Put("xml:json")
    public Representation store3(Representation rep) {
        return new StringRepresentation("xml:json", MediaType.APPLICATION_JSON);
    }

    @Put("json")
    public Representation store4(Representation rep) {
        return new StringRepresentation("json", MediaType.APPLICATION_JSON);
    }
}
