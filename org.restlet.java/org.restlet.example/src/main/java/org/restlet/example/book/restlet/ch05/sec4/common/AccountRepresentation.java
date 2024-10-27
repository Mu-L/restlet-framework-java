/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch05.sec4.common;

import java.util.List;

public class AccountRepresentation {

    private List<String> contactRefs;

    private List<String> mailRefs;

    private List<String> feedRefs;

    public AccountRepresentation() {
    }

    public List<String> getContactRefs() {
        return contactRefs;
    }

    public List<String> getFeedRefs() {
        return feedRefs;
    }

    public List<String> getMailRefs() {
        return mailRefs;
    }

    public void setContactRefs(List<String> contactRefs) {
        this.contactRefs = contactRefs;
    }

    public void setFeedRefs(List<String> feedRefs) {
        this.feedRefs = feedRefs;
    }

    public void setMailRefs(List<String> mailRefs) {
        this.mailRefs = mailRefs;
    }

}
