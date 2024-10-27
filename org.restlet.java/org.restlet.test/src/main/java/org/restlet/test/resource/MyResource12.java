/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.resource;

import org.restlet.data.Form;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

/**
 * Sample annotated interface.
 * 
 * @author Jerome Louvel
 */
public interface MyResource12 {

    @Get
    Form represent();

    @Put
    void store(Form form);

}
