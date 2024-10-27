/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch07.sec4.sub5;

import org.restlet.data.Tag;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

/**
 * Client issuing conditional requests.
 */
public class ConditionalClient {

    public static void main(String[] args) throws Exception {
        ClientResource resource = new ClientResource("http://localhost:8111/");

        // Get a representation
        Representation rep = resource.get();
        System.out.println(resource.getStatus());

        // Get an updated representation, if modified
        resource.getConditions().setModifiedSince(rep.getModificationDate());
        rep = resource.get();
        System.out.println(resource.getStatus());

        // Get an updated representation, if tag changed
        resource.getConditions().setModifiedSince(null);
        resource.getConditions().getNoneMatch().add(new Tag("xyz123"));
        rep = resource.get();
        System.out.println(resource.getStatus());

        // Put a new representation if tag has not changed
        resource.getConditions().getNoneMatch().clear();
        resource.getConditions().getMatch().add(rep.getTag());
        resource.put(rep);
        System.out.println(resource.getStatus());

        // Put a new representation when a different tag
        resource.getConditions().getMatch().clear();
        resource.getConditions().getMatch().add(new Tag("abcd7890"));
        resource.put(rep);
        System.out.println(resource.getStatus());
    }
}
