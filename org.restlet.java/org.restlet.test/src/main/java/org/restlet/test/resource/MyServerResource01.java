/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.resource;

import org.restlet.Server;
import org.restlet.data.Protocol;
import org.restlet.resource.ServerResource;

/**
 * Sample server resource.
 * 
 * @author Jerome Louvel
 */
public class MyServerResource01 extends ServerResource implements MyResource01 {

    public static void main(String[] args) throws Exception {
        Server server = new Server(Protocol.HTTP, 8111);
        server.setNext(MyServerResource01.class);
        server.start();
    }

    private volatile MyBean myBean = new MyBean("myName", "myDescription");

    public boolean accept(MyBean bean) {
        return bean.equals(myBean);
    }

    public String describe() {
        return "MyDescription";
    }

    public String remove() {
        myBean = null;
        return "Done";
    }

    public MyBean represent() {
        return myBean;
    }

    public String store(MyBean bean) {
        myBean = bean;
        return "Done";
    }

}
