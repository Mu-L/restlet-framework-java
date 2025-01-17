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
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Resource that manages a list of items.
 * 
 */
public class ItemsResource extends BaseResource {

    /**
     * Handle POST requests: create a new item.
     */
    @Post
    public Representation acceptItem(Representation entity) {
        Representation result = null;
        // Parse the given representation and retrieve pairs of
        // "name=value" tokens.
        Form form = new Form(entity);
        String itemName = form.getFirstValue("name");
        String itemDescription = form.getFirstValue("description");

        // Register the new item if one is not already registered.
        if (!getItems().containsKey(itemName)
                && getItems().putIfAbsent(itemName,
                        new Item(itemName, itemDescription)) == null) {
            // Set the response's status and entity
            setStatus(Status.SUCCESS_CREATED);
            Representation rep = new StringRepresentation("Item created",
                    MediaType.TEXT_PLAIN);
            // Indicates where is located the new resource.
            rep.setLocationRef(getRequest().getResourceRef().getIdentifier()
                    + "/" + itemName);
            result = rep;
        } else { // Item is already registered.
            setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            result = generateErrorRepresentation("Item " + itemName
                    + " already exists.", "1");
        }

        return result;
    }

    /**
     * Generate an XML representation of an error response.
     * 
     * @param errorMessage
     *            the error message.
     * @param errorCode
     *            the error code.
     */
    private Representation generateErrorRepresentation(String errorMessage,
            String errorCode) {
        DomRepresentation result = null;
        // This is an error
        // Generate the output representation
        try {
            result = new DomRepresentation(MediaType.TEXT_XML);
            // Generate a DOM document representing the list of
            // items.
            Document d = result.getDocument();

            Element eltError = d.createElement("error");

            Element eltCode = d.createElement("code");
            eltCode.appendChild(d.createTextNode(errorCode));
            eltError.appendChild(eltCode);

            Element eltMessage = d.createElement("message");
            eltMessage.appendChild(d.createTextNode(errorMessage));
            eltError.appendChild(eltMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Returns a listing of all registered items.
     */
    @Get("xml")
    public Representation toXml() {
        // Generate the right representation according to its media type.
        try {
            DomRepresentation representation = new DomRepresentation(
                    MediaType.TEXT_XML);

            // Generate a DOM document representing the list of
            // items.
            Document d = representation.getDocument();
            Element r = d.createElement("items");
            d.appendChild(r);
            for (Item item : getItems().values()) {
                Element eltItem = d.createElement("item");

                Element eltName = d.createElement("name");
                eltName.appendChild(d.createTextNode(item.getName()));
                eltItem.appendChild(eltName);

                Element eltDescription = d.createElement("description");
                eltDescription.appendChild(d.createTextNode(item
                        .getDescription()));
                eltItem.appendChild(eltDescription);

                r.appendChild(eltItem);
            }
            d.normalizeDocument();

            // Returns the XML representation of this document.
            return representation;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
