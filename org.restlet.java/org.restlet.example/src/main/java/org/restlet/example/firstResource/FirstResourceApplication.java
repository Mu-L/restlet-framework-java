/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.firstResource;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class FirstResourceApplication extends Application {

    /** The list of items is persisted in memory. */
    private final ConcurrentMap<String, Item> items = new ConcurrentHashMap<String, Item>();

    /**
     * Creates a root Restlet that will receive all incoming calls.
     */
    @Override
    public Restlet createInboundRoot() {
        // Create a router Restlet that defines routes.
        Router router = new Router(getContext());

        // Defines a route for the resource "list of items"
        router.attach("/items", ItemsResource.class);
        // Defines a route for the resource "item"
        router.attach("/items/{itemName}", ItemResource.class);

        return router;
    }

    /**
     * Returns the list of registered items.
     * 
     * @return the list of registered items.
     */
    public ConcurrentMap<String, Item> getItems() {
        return items;
    }
}
