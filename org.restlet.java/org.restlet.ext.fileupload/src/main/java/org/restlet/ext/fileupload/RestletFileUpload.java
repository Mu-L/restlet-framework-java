/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.ext.fileupload;

import java.io.IOException;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.restlet.Request;
import org.restlet.data.Method;
import org.restlet.representation.Representation;

/**
 * High level API for processing file uploads. This class handles multiple files
 * per single HTML widget, sent using the "multipart/mixed" encoding type, as
 * specified by RFC 1867. Use {@link #parseRequest(Request)} method to acquire a
 * list of FileItems associated with a given HTML widget.<br>
 * <br>
 * How the data for individual parts is stored is determined by the factory used
 * to create them; a given part may be in memory, on disk, or somewhere
 * else.<br>
 * <br>
 * In addition, it is possible to use <a
 * href="https://commons.apache.org/proper/commons-fileupload/streaming.html>
 * FileUpload's streaming API</a> to prevent the intermediary storage step. For
 * this, use the
 * {@link #getItemIterator(org.apache.commons.fileupload.RequestContext)}
 * method.
 * 
 * @author Jerome Louvel
 * @deprecated Will be removed in next minor release in favor or Jetty extension.
 */
@Deprecated
public class RestletFileUpload extends FileUpload {
	/**
	 * Determines if the request has multipart content.
	 *
	 * @param request The request to be assessed.
	 *
	 * @return True if the request is multipart.
	 */
	public static final boolean isMultipartContent(Request request) {
		if (request.getMethod().equals(Method.POST)) {
			return FileUploadBase.isMultipartContent(new RepresentationContext(request.getEntity()));
		} else {
			return false;
		}
	}

	/**
	 * Constructs an uninitialized instance of this class. A factory must be
	 * configured, using <code>setFileItemFactory()</code>, before attempting to
	 * parse request entity.
	 * 
	 * @see RestletFileUpload#RestletFileUpload(FileItemFactory)
	 */
	public RestletFileUpload() {
		super();
	}

	/**
	 * Constructs an instance of this class which uses the supplied factory to
	 * create <code>FileItem</code> instances.
	 * 
	 * @see RestletFileUpload#RestletFileUpload()
	 */
	public RestletFileUpload(FileItemFactory fileItemFactory) {
		super(fileItemFactory);
	}

	/**
	 * Processes an <a href="http://www.ietf.org/rfc/rfc1867.txt">RFC 1867</a>
	 * compliant <code>multipart/form-data</code> input representation. Note that
	 * this will not result in the writing of the parts on the disk but will instead
	 * allow you to use stream access.
	 * 
	 * @param multipartForm The input representation.
	 * @return An iterator to instances of FileItemStream parsed from the request.
	 * @throws FileUploadException
	 * @throws IOException
	 * @see <a href="http://commons.apache.org/fileupload/streaming.html">FileUpload
	 *      Streaming API</a>
	 */
	public FileItemIterator getItemIterator(Representation multipartForm) throws FileUploadException, IOException {
		return getItemIterator(new RepresentationContext(multipartForm));
	}

	/**
	 * Processes an <a href="http://www.ietf.org/rfc/rfc1867.txt">RFC 1867</a>
	 * compliant <code>multipart/form-data</code> input representation. Note that
	 * this will result in the writing of the parts on the disk.
	 * 
	 * @param multipartForm The multipart representation to be parsed.
	 * @return A list of <code>FileItem</code> instances parsed, in the order that
	 *         they were transmitted.
	 * @throws FileUploadException if there are problems reading/parsing the request
	 *                             or storing files.
	 */
	public List<FileItem> parseRepresentation(Representation multipartForm) throws FileUploadException {
		return parseRequest(new RepresentationContext(multipartForm));
	}

	/**
	 * Processes an <a href="http://www.ietf.org/rfc/rfc1867.txt">RFC 1867</a>
	 * compliant <code>multipart/form-data</code> input representation. Note that
	 * this will result in the writing of the parts on the disk.
	 * 
	 * @param request The request containing the entity to be parsed.
	 * @return A list of <code>FileItem</code> instances parsed, in the order that
	 *         they were transmitted.
	 * @throws FileUploadException if there are problems reading/parsing the request
	 *                             or storing files.
	 */
	public List<FileItem> parseRequest(Request request) throws FileUploadException {
		return parseRequest(new RepresentationContext(request.getEntity()));
	}

}
