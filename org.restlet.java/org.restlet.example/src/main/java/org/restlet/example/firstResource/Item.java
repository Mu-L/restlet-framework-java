/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.firstResource;

public class Item {
    /** A description of the item. */
    private String description;

    /** Name of the item. */
    private String name;

    public Item(String name) {
        super();
        setName(name);
    }

    public Item(String name, String description) {
        super();
        setName(name);
        setDescription(description);
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

}
