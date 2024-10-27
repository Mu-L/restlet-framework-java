/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch05.sec4.common;

public class ContactRepresentation {

    private String firstName;

    private String lastName;

    private String login;

    private String nickName;

    private String senderName;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getLogin() {
        return login;
    }

    public String getNickName() {
        return nickName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

}
