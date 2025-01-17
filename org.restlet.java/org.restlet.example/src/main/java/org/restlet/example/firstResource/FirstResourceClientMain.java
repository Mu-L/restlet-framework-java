/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.firstResource;

import java.io.IOException;

import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

public class FirstResourceClientMain {

    public static void main(String[] args) throws IOException,
            ResourceException {
        // Define our Restlet client resources.
        ClientResource itemsResource = new ClientResource(
                "http://localhost:8111/firstResource/items");
        ClientResource itemResource = null;

        // Create a new item
        Item item = new Item("item1", "this is an item.");
        try {
            Representation r = itemsResource.post(getRepresentation(item));
            itemResource = new ClientResource(r.getLocationRef());
        } catch (ResourceException e) {
            System.out.println("Error  status: " + e.getStatus());
            System.out.println("Error message: " + e.getMessage());
        }
        // Consume the response's entity which releases the connection
        itemsResource.getResponseEntity().exhaust();

        if (itemResource != null) {
            // Prints the representation of the newly created resource.
            get(itemResource);

            // Prints the list of registered items.
            get(itemsResource);

            // Update the item
            item.setDescription("This is an other description");
            itemResource.put(getRepresentation(item));

            // Prints the list of registered items.
            get(itemsResource);

            // delete the item
            itemResource.delete();

            // Print the list of registered items.
            get(itemsResource);
        }
    }

    /**
     * Prints the resource's representation.
     * 
     * @param clientResource
     *            The Restlet client resource.
     * @throws IOException
     * @throws ResourceException
     */
    public static void get(ClientResource clientResource) throws IOException,
            ResourceException {
        try {
            clientResource.get().write(System.out);
        } catch (ResourceException e) {
            System.out.println("Error  status: " + e.getStatus());
            System.out.println("Error message: " + e.getMessage());
            // Consume the response's entity which releases the connection
            clientResource.getResponseEntity().exhaust();
        }
    }

    /**
     * Returns the Representation of an item.
     * 
     * @param item
     *            the item.
     * 
     * @return The Representation of the item.
     */
    public static Representation getRepresentation(Item item) {
        // Gathering informations into a Web form.
        Form form = new Form();
        form.add("name", item.getName());
        form.add("description", item.getDescription());
        return form.getWebRepresentation();
    }

}
