/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.tutorial;

import org.restlet.resource.Get;

/**
 * Related to the part 12 of the tutorial.
 * 
 * @author Jerome Louvel
 */
public class OrdersResource extends UserResource {

    @Get
    public String toString() {
        return "Orders of user \"" + this.userName + "\"";
    }

}
