/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch02.sec4.sub2;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;

/**
 * Restlet that returns common request properties to the client.
 */
public class Tracer extends Restlet {

    public Tracer(Context context) {
        super(context);
    }

    @Override
    public void handle(Request request, Response response) {
        String entity = "Method       : " + request.getMethod()
                + "\nResource URI : " + request.getResourceRef()
                + "\nIP address   : " + request.getClientInfo().getAddress()
                + "\nAgent name   : " + request.getClientInfo().getAgentName()
                + "\nAgent version: "
                + request.getClientInfo().getAgentVersion();
        response.setEntity(entity, MediaType.TEXT_PLAIN);
    }

}
