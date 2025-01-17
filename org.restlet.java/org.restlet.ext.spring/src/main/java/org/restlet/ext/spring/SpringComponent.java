/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.ext.spring;

import java.util.ArrayList;
import java.util.List;

import org.restlet.Client;
import org.restlet.Restlet;
import org.restlet.Server;
import org.restlet.data.Protocol;

/**
 * Component that is easily configurable from Spring. Here is a usage example:
 * 
 * <pre>
 * &lt;bean id=&quot;component&quot;
 *         class=&quot;org.restlet.ext.spring.SpringComponent&quot;&gt;
 *         &lt;property name=&quot;clientsList&quot;&gt;
 *                 &lt;list&gt;
 *                         &lt;value&gt;file&lt;/value&gt;
 *                 &lt;/list&gt;
 *         &lt;/property&gt;
 *         &lt;property name=&quot;server&quot; ref=&quot;server&quot; /&gt;
 *         &lt;property name=&quot;defaultTarget&quot; ref=&quot;application&quot; /&gt;
 *         &lt;property name=&quot;hosts&quot;&gt;
 *                 &lt;list&gt;
 *                         &lt;ref bean=&quot;virtualHost&quot; /&gt;
 *                 &lt;/list&gt;
 *         &lt;/property&gt;
 * &lt;/bean&gt;
 * 
 * &lt;bean id=&quot;component.context&quot;
 *         class=&quot;org.springframework.beans.factory.config.PropertyPathFactoryBean&quot; /&gt;
 * 
 * &lt;bean id=&quot;server&quot; class=&quot;org.restlet.ext.spring.SpringServer&quot;&gt;
 *         &lt;constructor-arg value=&quot;http&quot; /&gt;
 *         &lt;constructor-arg value=&quot;8111&quot; /&gt;
 *         &lt;property name=&quot;parameters&quot;&gt;
 *                 &lt;props&gt;
 *                         &lt;prop key=&quot;key1&quot;&gt;value1&lt;/prop&gt;
 *                         &lt;prop key=&quot;key2&quot;&gt;value2&lt;/prop&gt;
 *                 &lt;/props&gt;
 *         &lt;/property&gt;
 * &lt;/bean&gt;
 * </pre>
 * 
 * Concurrency note: instances of this class or its subclasses can be invoked by
 * several threads at the same time and therefore must be thread-safe. You
 * should be especially careful when storing state in member variables.
 * 
 * @see <a href="http://www.springframework.org/">Spring home page</a>
 * @author Jerome Louvel
 */
public class SpringComponent extends org.restlet.Component {

    /**
     * Adds a client to the list of connectors. The value can be either a
     * protocol name, a Protocol instance or a Client instance.
     * 
     * @param clientInfo
     *            The client info.
     */
    public void setClient(Object clientInfo) {
        final List<Object> clients = new ArrayList<Object>();
        clients.add(clientInfo);
        setClientsList(clients);
    }

    /**
     * Sets the list of clients, either as protocol names, Protocol instances or
     * Client instances.
     * 
     * @param clients
     *            The list of clients.
     */
    public synchronized void setClientsList(List<Object> clients) {
        for (final Object client : clients) {
            if (client instanceof String) {
                getClients().add(Protocol.valueOf((String) client));
            } else if (client instanceof Protocol) {
                getClients().add((Protocol) client);
            } else if (client instanceof Client) {
                getClients().add((Client) client);
            } else {
                getLogger()
                        .warning(
                                "Unknown object found in the clients list. Only instances of String, org.restlet.data.Protocol and org.restlet.Client are allowed.");
            }
        }
    }

    /**
     * Attaches a target Restlet to the default host.
     * 
     * @param target
     *            The target Restlet.
     */
    public void setDefaultTarget(Restlet target) {
        getDefaultHost().attach(target);
    }

    /**
     * Adds a server to the list of connectors. The value can be either a
     * protocol name, a Protocol instance or a Server instance.
     * 
     * @param serverInfo
     *            The server info.
     */
    public void setServer(Object serverInfo) {
        final List<Object> servers = new ArrayList<Object>();
        servers.add(serverInfo);
        setServersList(servers);
    }

    /**
     * Sets the list of servers, either as protocol names, Protocol instances or
     * Server instances.
     * 
     * @param serversInfo
     *            The list of servers.
     */
    public void setServersList(List<Object> serversInfo) {
        for (final Object serverInfo : serversInfo) {
            if (serverInfo instanceof String) {
                getServers().add(Protocol.valueOf((String) serverInfo));
            } else if (serverInfo instanceof Protocol) {
                getServers().add((Protocol) serverInfo);
            } else if (serverInfo instanceof Server) {
                getServers().add((Server) serverInfo);
            } else {
                getLogger()
                        .warning(
                                "Unknown object found in the servers list. Only instances of String, org.restlet.data.Protocol and org.restlet.Server are allowed.");
            }
        }
    }

}
