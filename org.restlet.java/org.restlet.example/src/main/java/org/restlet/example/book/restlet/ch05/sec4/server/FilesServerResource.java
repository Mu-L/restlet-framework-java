/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.example.book.restlet.ch05.sec4.server;

import java.io.File;
import java.io.FilenameFilter;
import java.security.AccessControlException;
import java.security.PrivilegedAction;

import org.restlet.data.Status;
import org.restlet.ext.jaas.JaasUtils;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

/**
 * Using JVM security manager.
 * 
 * @author Bruno Harbulot (bruno/distributedmatter.net)
 */
public class FilesServerResource extends ServerResource {

    @Get("txt")
    public Representation retrieve() throws ResourceException {
        StringBuilder result = null;

        // The action requiring the CFO role to run
        PrivilegedAction<StringBuilder> action = new PrivilegedAction<StringBuilder>() {
            public StringBuilder run() {
                File dir = new File(System.getProperty("user.home"));
                String[] filenames = dir.list(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return !name.startsWith(".");
                    }
                });

                StringBuilder sb = new StringBuilder(
                        "Files in the home directory: \n\n");
                for (String filename : filenames) {
                    sb.append(filename);
                    sb.append("\n");
                }
                return sb;
            }
        };

        // Invoking the privileged action only if CFO role granted to
        // authenticated user
        try {
            result = JaasUtils.doAsPriviledged(getRequest().getClientInfo(),
                    action);
        } catch (AccessControlException ace) {
            setStatus(Status.CLIENT_ERROR_FORBIDDEN);
        }

        // Returning home dir files listing
        return (result == null) ? null : new StringRepresentation(result);
    }
}
