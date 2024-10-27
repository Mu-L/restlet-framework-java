/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch05.sec2.verifier.certificate;

import org.restlet.resource.Get;
import org.restlet.resource.Put;

/**
 * Annotated mail resource interface
 */
public interface MailResource {

    @Get
    Mail retrieve();

    @Put
    void store(Mail mail);

}
