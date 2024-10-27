/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch05.sec2.basic;

/**
 * The mail representation bean.
 */
public class Mail {

    private String status;

    private String subject;

    private String content;

    private String accountRef;

    public String getAccountRef() {
        return accountRef;
    }

    public String getContent() {
        return content;
    }

    public String getStatus() {
        return status;
    }

    public String getSubject() {
        return subject;
    }

    public void setAccountRef(String accountRef) {
        this.accountRef = accountRef;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

}
