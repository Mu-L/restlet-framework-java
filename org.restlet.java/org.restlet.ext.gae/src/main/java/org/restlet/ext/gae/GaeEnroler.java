/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.ext.gae;

import org.restlet.Application;
import org.restlet.data.ClientInfo;
import org.restlet.security.Enroler;
import org.restlet.security.Role;

import com.google.appengine.api.users.UserServiceFactory;

/**
 * Enroler that adds a Restlet {@link Role} object to the request's
 * {@link ClientInfo} if the GAE API reports that the user is an administrator.
 * 
 * @author Matt Kennedy
 */
public class GaeEnroler implements Enroler {

    /** The Administrator role. */
    private Role adminRole;

    /**
     * Constructor.
     * 
     * @param application
     *            The parent application.
     * @param adminRoleName
     *            The name of the administrator role.
     */
    public GaeEnroler(Application application, String adminRoleName) {
        this(Role.get(application, adminRoleName,
                "Administrator of the current application."));
    }

    /**
     * Constructor.
     * 
     * @param application
     *            The parent application.
     * @param adminRoleName
     *            The name of the administrator role.
     * @param adminRoleDescription
     *            The description of the administrator role.
     */
    public GaeEnroler(Application application, String adminRoleName,
            String adminRoleDescription) {
        this(Role.get(application, adminRoleName, adminRoleDescription));
    }

    /**
     * Constructor.
     * 
     * @param adminRole
     *            The administrator role.
     */
    public GaeEnroler(Role adminRole) {
        setAdminRole(adminRole);
    }

    /**
     * Adds admin role object if user is an administrator according to Google
     * App Engine UserService.
     * 
     * @see org.restlet.security.Enroler#enrole(org.restlet.data.ClientInfo)
     */
    public void enrole(ClientInfo info) {
        if (UserServiceFactory.getUserService().isUserAdmin()
                && getAdminRole() != null) {
            info.getRoles().add(getAdminRole());
        }
    }

    /**
     * Returns the administrator role.
     * 
     * @return The administrator role.
     */
    public Role getAdminRole() {
        return adminRole;
    }

    /**
     * Sets the administrator role.
     * 
     * @param adminRole
     *            The administrator role.
     */
    public void setAdminRole(Role adminRole) {
        this.adminRole = adminRole;
    }

}
