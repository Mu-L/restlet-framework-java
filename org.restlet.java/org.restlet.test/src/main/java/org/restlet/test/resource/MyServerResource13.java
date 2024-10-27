/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.resource;

import java.util.Date;

import org.restlet.resource.ServerResource;

/**
 * Sample server resource for modifier testing.
 * 
 * @author Jerome Louvel
 */
public class MyServerResource13 extends ServerResource implements MyResource13 {

    public LightContact retrieveLight() {
        return new LightContact("test@domain.com", "Scott", "Tiger");
    }

    public Contact retrieve() {
        return new Contact("test@domain.com", "Scott", "Tiger", new Date(),
                "test@perso.fr");
    }

    public FullContact retrieveFull() {
        return new FullContact("test@domain.com", "Scott", "Tiger", new Date(),
                "test@perso.fr", "1 Main Street", "Restlet city, 0102",
                "RESTland", "+123.456", "+789012");
    }

}
