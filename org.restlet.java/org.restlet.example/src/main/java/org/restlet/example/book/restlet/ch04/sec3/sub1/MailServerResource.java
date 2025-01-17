/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch04.sec3.sub1;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Reference;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

/**
 * Resource corresponding to a mail received or sent with the parent mail
 * account. Leverages JSON.org extension.
 */
public class MailServerResource extends ServerResource {

    @Get
    public Representation toJson() throws JSONException {
        // Create a JSON object structure similar to a map
        JSONObject mailElt = new JSONObject();
        mailElt.put("status", "received");
        mailElt.put("subject", "Message to self");
        mailElt.put("content", "Doh!");
        mailElt.put("accountRef", new Reference(getReference(), "..")
                .getTargetRef().toString());
        return new JsonRepresentation(mailElt);
    }

    @Put
    public void store(JsonRepresentation mailRep) throws JSONException {
        // Parse the JSON representation to get the mail properties
        JSONObject mailElt = mailRep.getJsonObject();

        // Output the JSON element values
        System.out.println("Status: " + mailElt.getString("status"));
        System.out.println("Subject: " + mailElt.getString("subject"));
        System.out.println("Content: " + mailElt.getString("content"));
        System.out.println("Account URI: " + mailElt.getString("accountRef"));
    }
}
