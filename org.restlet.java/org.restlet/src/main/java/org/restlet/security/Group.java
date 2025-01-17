/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.security;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Group that contains member groups and users.
 * 
 * @author Jerome Louvel
 */
public class Group {

	/** The description. */
	private volatile String description;

	/**
	 * Indicates if the roles of the parent group should be inherited. Those roles
	 * indirectly cover the granted or denied permissions.
	 */
	private volatile boolean inheritingRoles;

	/** The modifiable list of child groups. */
	private final List<Group> memberGroups;

	/** The modifiable list of members user references. */
	private final List<User> memberUsers;

	/** The display name. */
	private volatile String name;

	/**
	 * Default constructor. Note that roles are inherited by default.
	 */
	public Group() {
		this(null, null);
	}

	/**
	 * Constructor. Note that roles are inherited by default.
	 * 
	 * @param name        The display name.
	 * @param description The description.
	 */
	public Group(String name, String description) {
		this(name, description, true);
	}

	/**
	 * Constructor.
	 * 
	 * @param name            The display name.
	 * @param description     The description.
	 * @param inheritingRoles Indicates if the roles of the parent group should be
	 *                        inherited.
	 */
	public Group(String name, String description, boolean inheritingRoles) {
		this.name = name;
		this.description = description;
		this.inheritingRoles = inheritingRoles;
		this.memberGroups = new CopyOnWriteArrayList<Group>();
		this.memberUsers = new CopyOnWriteArrayList<User>();
	}

	/**
	 * Returns the description.
	 * 
	 * @return The description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Returns the modifiable list of member groups.
	 * 
	 * @return The modifiable list of member groups.
	 */
	public List<Group> getMemberGroups() {
		return memberGroups;
	}

	public List<User> getMemberUsers() {
		return memberUsers;
	}

	/**
	 * Returns the display name.
	 * 
	 * @return The display name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Indicates if the roles of the parent group should be inherited. Those roles
	 * indirectly cover the granted or denied permissions.
	 * 
	 * @return True if the roles of the parent group should be inherited.
	 */
	public boolean isInheritingRoles() {
		return inheritingRoles;
	}

	/**
	 * Sets the description.
	 * 
	 * @param description The description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Indicates if the roles of the parent group should be inherited. Those roles
	 * indirectly cover the granted or denied permissions.
	 * 
	 * @param inheritingRoles True if the roles of the parent group should be
	 *                        inherited.
	 */
	public void setInheritingRoles(boolean inheritingRoles) {
		this.inheritingRoles = inheritingRoles;
	}

	/**
	 * Sets the modifiable list of member groups. This method clears the current
	 * list and adds all entries in the parameter list.
	 * 
	 * @param memberGroups A list of member groups.
	 */
	public void setMemberGroups(List<Group> memberGroups) {
		synchronized (getMemberGroups()) {
			if (memberGroups != getMemberGroups()) {
				getMemberGroups().clear();

				if (memberGroups != null) {
					getMemberGroups().addAll(memberGroups);
				}
			}
		}
	}

	/**
	 * Sets the modifiable list of member user references. This method clears the
	 * current list and adds all entries in the parameter list.
	 * 
	 * @param memberUsers A list of member user references.
	 */
	public void setMemberUsers(List<User> memberUsers) {
		synchronized (getMemberUsers()) {
			if (memberUsers != getMemberUsers()) {
				getMemberUsers().clear();

				if (memberUsers != null) {
					getMemberUsers().addAll(memberUsers);
				}
			}
		}
	}

	/**
	 * Sets the display name.
	 * 
	 * @param name The display name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return getName();
	}

}
