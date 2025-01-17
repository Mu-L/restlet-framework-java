/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch05.sec3.client;

import org.restlet.Client;
import org.restlet.Context;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Protocol;
import org.restlet.example.book.restlet.ch02.sec5.sub5.common.AccountResource;
import org.restlet.example.book.restlet.ch02.sec5.sub5.common.AccountsResource;
import org.restlet.example.book.restlet.ch02.sec5.sub5.common.RootResource;
import org.restlet.resource.ClientResource;

/**
 * Mail client.
 */
public class MailClient {

    /**
     * Mail client interacting with the RESTful mail server.
     * 
     * @param args
     *            The optional arguments.
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        System.out.println("\n1) Set-up the service client resource\n");
        Client client = new Client(new Context(), Protocol.HTTP);
        ClientResource service = new ClientResource("http://localhost:8111");
        service.setChallengeResponse(new ChallengeResponse(
                ChallengeScheme.HTTP_BASIC, "chunkylover53", "pwd"));
        service.setNext(client);

        System.out.println("\n2) Display the root resource\n");
        RootResource mailRoot = service.getChild("/", RootResource.class);
        System.out.println(mailRoot.represent());

        System.out.println("\n3) Display the initial list of accounts\n");
        AccountsResource mailAccounts = service.getChild("/accounts/",
                AccountsResource.class);
        String list = mailAccounts.represent();
        System.out.println(list == null ? "<empty>\n" : list);

        System.out.println("4) Adds new accounts\n");
        mailAccounts.add("Homer Simpson");
        mailAccounts.add("Marjorie Simpson");
        mailAccounts.add("Bart Simpson");
        System.out.println("Three accounts added !");

        System.out.println("\n5) Display the updated list of accounts\n");
        System.out.println(mailAccounts.represent());

        System.out.println("6) Display the second account\n");
        AccountResource mailAccount = service.getChild("/accounts/1",
                AccountResource.class);
        System.out.println(mailAccount.represent());

        System.out
                .println("\n7) Update the individual account and display it again\n");
        mailAccount.store("Marge Simpson");
        System.out.println(mailAccount.represent());

        System.out
                .println("\n8) Delete the first account and display the list again\n");
        mailAccount = service.getChild("/accounts/0", AccountResource.class);
        mailAccount.remove();
        System.out.println(mailAccounts.represent());
    }

}
