/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.resource;

public class LightContact {

    private String email1;

    private String firstName;

    private String lastName;

    public LightContact(String email, String firstName, String lastName) {
        super();
        this.email1 = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getEmail1() {
        return email1;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setEmail1(String email) {
        this.email1 = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}
