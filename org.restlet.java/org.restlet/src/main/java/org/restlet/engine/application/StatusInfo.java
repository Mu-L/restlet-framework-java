/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.engine.application;

import java.io.Serializable;

import org.restlet.data.Status;

/**
 * Representation of a {@link Status}.
 * 
 * @author Manuel Boillod
 */
public class StatusInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	/** The specification code. */
	private volatile int code;

	/** The email address of the administrator to contact in case of error. */
	private volatile String contactEmail;

	/** The longer description. */
	private volatile String description;

	/** The home URI to propose in case of error. */
	private volatile String homeRef;

	/** The short reason phrase. */
	private volatile String reasonPhrase;

	/** The URI of the specification describing the method. */
	private volatile String uri;

	/**
	 * Empty Constructor
	 */
	public StatusInfo() {
	}

	/**
	 * Constructor.
	 * 
	 * @param code         The specification code.
	 * @param description  The longer description.
	 * @param reasonPhrase The short reason phrase.
	 */
	public StatusInfo(int code, String description, String reasonPhrase) {
		this(code, description, reasonPhrase, null, null, null);
	}

	/**
	 * Constructor.
	 * 
	 * @param code         The specification code.
	 * @param description  The longer description.
	 * @param reasonPhrase The short reason phrase.
	 * @param uri          The URI of the specification describing the method.
	 * @param contactEmail The email address of the administrator to contact in case
	 *                     of error.
	 * @param homeRef      The home URI to propose in case of error.
	 */
	public StatusInfo(int code, String description, String reasonPhrase, String uri, String contactEmail,
			String homeRef) {
		super();
		this.code = code;
		this.description = description;
		this.reasonPhrase = reasonPhrase;
		this.uri = uri;
		this.contactEmail = contactEmail;
		this.homeRef = homeRef;
	}

	/**
	 * Constructor.
	 * 
	 * @param status The represented status.
	 */
	public StatusInfo(Status status) {
		this(status, null, null);
	}

	/**
	 * Constructor.
	 * 
	 * @param status       The represented status.
	 * @param contactEmail The email address of the administrator to contact in case
	 *                     of error.
	 * @param homeRef      The home URI to propose in case of error.
	 */
	public StatusInfo(Status status, String contactEmail, String homeRef) {
		this(status.getCode(), status.getDescription(), status.getReasonPhrase(), status.getUri(), contactEmail,
				homeRef);
	}

	/**
	 * Returns the code of the status.
	 * 
	 * @return The code of the status.
	 */
	public int getCode() {
		return code;
	}

	/**
	 * Returns the email address of the administrator to contact in case of error.
	 * 
	 * @return The email address.
	 */
	public String getContactEmail() {
		return contactEmail;
	}

	/**
	 * Returns the description of the status.
	 * 
	 * @return The description of the status.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns the home URI to propose in case of error.
	 * 
	 * @return The home URI.
	 */
	public String getHomeRef() {
		return homeRef;
	}

	/**
	 * Returns the short description of the status.
	 * 
	 * @return The short description of the status.
	 */
	public String getReasonPhrase() {
		return reasonPhrase;
	}

	/**
	 * Returns the URI of the specification describing the status.
	 * 
	 * @return The URI of the specification describing the status.
	 */
	public String getUri() {
		return this.uri;
	}

	/**
	 * Sets the code of the status.
	 * 
	 * @param code The code of the status.
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * Sets the email address of the administrator to contact in case of error.
	 * 
	 * @param email The email address.
	 */
	public void setContactEmail(String email) {
		this.contactEmail = email;
	}

	/**
	 * Sets the description of the status.
	 * 
	 * @param description The description of the status.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Sets the home URI to propose in case of error.
	 * 
	 * @param homeRef The home URI.
	 */
	public void setHomeRef(String homeRef) {
		this.homeRef = homeRef;
	}

	/**
	 * Sets the short description of the status.
	 * 
	 * @param reasonPhrase The short description of the status.
	 */
	public void setReasonPhrase(String reasonPhrase) {
		this.reasonPhrase = reasonPhrase;
	}
}
