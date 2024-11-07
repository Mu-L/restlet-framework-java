/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.data;

import java.io.IOException;
import java.util.List;

import org.restlet.engine.util.FormUtils;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.util.Series;

/**
 * Form which is a specialized modifiable list of parameters.
 * 
 * @author Jerome Louvel
 */
public class Form extends Series<Parameter> {
	/**
	 * Empty constructor.
	 */
	public Form() {
		super(Parameter.class);
	}

	/**
	 * Constructor.
	 * 
	 * @param initialCapacity The initial list capacity.
	 */
	public Form(int initialCapacity) {
		super(Parameter.class, initialCapacity);
	}

	/**
	 * Constructor.
	 * 
	 * @param delegate The delegate list.
	 */
	public Form(List<Parameter> delegate) {
		super(Parameter.class, delegate);
	}

	/**
	 * Constructor.
	 * 
	 * @param webForm The URL encoded Web form.
	 */
	public Form(Representation webForm) {
		this(webForm, true);
	}

	/**
	 * Constructor.
	 * 
	 * @param webForm The URL encoded Web form.
	 */
	public Form(Representation webForm, boolean decode) {
		this();
		FormUtils.parse(this, webForm, decode);
	}

	/**
	 * Constructor. Uses UTF-8 as the character set for encoding non-ASCII
	 * characters.
	 * 
	 * @param queryString The Web form parameters as a string.
	 */
	public Form(String queryString) {
		this(queryString, true);
	}

	/**
	 * Constructor. Uses UTF-8 as the character set for encoding non-ASCII
	 * characters.
	 * 
	 * @param queryString The Web form parameters as a string.
	 * @param decode      Indicates if the names and values should be automatically
	 *                    decoded.
	 */
	public Form(String queryString, boolean decode) {
		this(queryString, CharacterSet.UTF_8, decode);
	}

	/**
	 * Constructor. Uses UTF-8 as the character set for encoding non-ASCII
	 * characters.
	 * 
	 * @param parametersString The parameters string to parse.
	 * @param separator        The separator character to append between parameters.
	 */
	public Form(String parametersString, char separator) {
		this(parametersString, separator, true);
	}

	/**
	 * Constructor. Uses UTF-8 as the character set for encoding non-ASCII
	 * characters.
	 * 
	 * @param parametersString The parameters string to parse.
	 * @param separator        The separator character to append between parameters.
	 * @param decode           Indicates if the names and values should be
	 *                         automatically decoded.
	 */
	public Form(String parametersString, char separator, boolean decode) {
		this(parametersString, CharacterSet.UTF_8, separator, decode);
	}

	/**
	 * Constructor.
	 * 
	 * @param queryString  The Web form parameters as a string.
	 * @param characterSet The supported character encoding.
	 */
	public Form(String queryString, CharacterSet characterSet) {
		this(queryString, characterSet, true);
	}

	/**
	 * Constructor.
	 * 
	 * @param queryString  The Web form parameters as a string.
	 * @param characterSet The supported character encoding.
	 * @param decode       Indicates if the names and values should be automatically
	 *                     decoded.
	 */
	public Form(String queryString, CharacterSet characterSet, boolean decode) {
		this(queryString, characterSet, '&', decode);
	}

	/**
	 * Constructor.
	 * 
	 * @param parametersString The parameters string to parse.
	 * @param characterSet     The supported character encoding.
	 * @param separator        The separator character to append between parameters.
	 */
	public Form(String parametersString, CharacterSet characterSet, char separator) {
		this(parametersString, characterSet, separator, true);
	}

	/**
	 * Constructor.
	 * 
	 * @param parametersString The parameters string to parse.
	 * @param characterSet     The supported character encoding.
	 * @param separator        The separator character to append between parameters.
	 * @param decode           Indicates if the names and values should be
	 *                         automatically decoded.
	 */
	public Form(String parametersString, CharacterSet characterSet, char separator, boolean decode) {
		this();
		FormUtils.parse(this, parametersString, characterSet, decode, separator);
	}

	@Override
	public Parameter createEntry(String name, String value) {
		return new Parameter(name, value);
	}

	/**
	 * Encodes the form using the standard URI encoding mechanism and the UTF-8
	 * character set.
	 * 
	 * @return The encoded form.
	 * @throws IOException
	 */
	public String encode() throws IOException {
		return encode(CharacterSet.UTF_8);
	}

	/**
	 * URL encodes the form. The '&amp;' character is used as a separator.
	 * 
	 * @param characterSet The supported character encoding.
	 * @return The encoded form.
	 * @throws IOException
	 */
	public String encode(CharacterSet characterSet) throws IOException {
		return encode(characterSet, '&');
	}

	/**
	 * URL encodes the form.
	 * 
	 * @param characterSet The supported character encoding.
	 * @param separator    The separator character to append between parameters.
	 * @return The encoded form.
	 * @throws IOException
	 */
	public String encode(CharacterSet characterSet, char separator) throws IOException {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < size(); i++) {
			if (i > 0) {
				sb.append(separator);
			}

			get(i).encode(sb, characterSet);
		}

		return sb.toString();
	}

	/**
	 * Formats the form as a matrix path string. Uses UTF-8 as the character set for
	 * encoding non-ASCII characters.
	 * 
	 * @return The form as a matrix string.
	 * @see <a href="http://www.w3.org/DesignIssues/MatrixURIs.html">Matrix URIs by
	 *      Tim Berners Lee</a>
	 */
	public String getMatrixString() {
		return getMatrixString(CharacterSet.UTF_8);
	}

	/**
	 * Formats the form as a query string.
	 * 
	 * @param characterSet The supported character encoding.
	 * @return The form as a matrix string.
	 * @see <a href="http://www.w3.org/DesignIssues/MatrixURIs.html">Matrix URIs by
	 *      Tim Berners Lee</a>
	 */
	public String getMatrixString(CharacterSet characterSet) {
		try {
			return encode(characterSet, ';');
		} catch (IOException ioe) {
			return null;
		}
	}

	/**
	 * Formats the form as a query string. Uses UTF-8 as the character set for
	 * encoding non-ASCII characters.
	 * 
	 * @return The form as a query string.
	 */
	public String getQueryString() {
		return getQueryString(CharacterSet.UTF_8);
	}

	/**
	 * Formats the form as a query string.
	 * 
	 * @param characterSet The supported character encoding.
	 * @return The form as a query string.
	 */
	public String getQueryString(CharacterSet characterSet) {
		try {
			return encode(characterSet);
		} catch (IOException ioe) {
			return null;
		}
	}

	/**
	 * Returns the form as a Web representation (MediaType.APPLICATION_WWW_FORM).
	 * Uses UTF-8 as the character set for encoding non-ASCII characters.
	 * 
	 * @return The form as a Web representation.
	 */
	public Representation getWebRepresentation() {
		return getWebRepresentation(CharacterSet.UTF_8);
	}

	/**
	 * Returns the form as a Web representation (MediaType.APPLICATION_WWW_FORM).
	 * 
	 * @param characterSet The supported character encoding.
	 * @return The form as a Web representation.
	 */
	public Representation getWebRepresentation(CharacterSet characterSet) {
		return new StringRepresentation(getQueryString(characterSet), MediaType.APPLICATION_WWW_FORM, null,
				characterSet);
	}

}