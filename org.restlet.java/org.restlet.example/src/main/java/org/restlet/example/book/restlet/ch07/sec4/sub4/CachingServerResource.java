/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch07.sec4.sub4;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.restlet.data.CacheDirective;
import org.restlet.data.MediaType;
import org.restlet.data.Tag;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

/**
 * Resource setting cache control information.
 */
public class CachingServerResource extends ServerResource {

    @Get
    public Representation represent() {
        // Modification date (Fri, 17 Apr 2012 10:10:10 GMT) unchanged.
        Calendar cal = new GregorianCalendar(2012, 4, 17, 10, 10, 10);
        Representation result = new StringRepresentation("<a href="
                + getReference() + ">" + System.currentTimeMillis() + "</a>");
        result.setMediaType(MediaType.TEXT_HTML);
        result.setModificationDate(cal.getTime());

        // Expiration date (Fri, 17 Apr 2012 13:10:10 GMT) unchanged.
        cal.roll(Calendar.HOUR, 3);
        result.setExpirationDate(cal.getTime());

        // Setting E-Tag
        result.setTag(new Tag("xyz123"));

        // Setting a cache directive
        getResponse().getCacheDirectives().add(CacheDirective.publicInfo());

        return result;
    }

    @Put
    public void store(Representation entity) {
        System.out.println("Storing a new entity.");
    }
}
