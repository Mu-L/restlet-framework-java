/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Date;
import java.util.List;

import org.restlet.data.CharacterSet;
import org.restlet.data.Disposition;
import org.restlet.data.Encoding;
import org.restlet.data.Language;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.Tag;
import org.restlet.representation.Representation;

/**
 * Representation wrapper. Useful for application developer who need to enrich
 * the representation with application related properties and behavior.
 * 
 * @see <a href="http://c2.com/cgi/wiki?DecoratorPattern">The decorator (aka
 *      wrapper) pattern</a>
 * @author Jerome Louvel
 */
public class WrapperRepresentation extends Representation {
	/** The wrapped representation. */
	private final Representation wrappedRepresentation;

	/**
	 * Constructor.
	 * 
	 * @param wrappedRepresentation The wrapped representation.
	 */
	public WrapperRepresentation(Representation wrappedRepresentation) {
		this.wrappedRepresentation = wrappedRepresentation;
	}

	@Override
	public long exhaust() throws IOException {
		return getWrappedRepresentation().exhaust();
	}

	@Override
	public long getAvailableSize() {
		return getWrappedRepresentation().getAvailableSize();
	}

	@Override
	@Deprecated
	public java.nio.channels.ReadableByteChannel getChannel() throws IOException {
		return getWrappedRepresentation().getChannel();
	}

	@Override
	public CharacterSet getCharacterSet() {
		return getWrappedRepresentation().getCharacterSet();
	}

	@Override
	public org.restlet.data.Digest getDigest() {
		return getWrappedRepresentation().getDigest();
	}

	@Override
	public Disposition getDisposition() {
		return getWrappedRepresentation().getDisposition();
	}

	@Override
	public List<Encoding> getEncodings() {
		return getWrappedRepresentation().getEncodings();
	}

	@Override
	public Date getExpirationDate() {
		return getWrappedRepresentation().getExpirationDate();
	}

	@Override
	public List<Language> getLanguages() {
		return getWrappedRepresentation().getLanguages();
	}

	@Override
	public Reference getLocationRef() {
		return getWrappedRepresentation().getLocationRef();
	}

	@Override
	public MediaType getMediaType() {
		return getWrappedRepresentation().getMediaType();
	}

	@Override
	public Date getModificationDate() {
		return getWrappedRepresentation().getModificationDate();
	}

	@Override
	public org.restlet.data.Range getRange() {
		return getWrappedRepresentation().getRange();
	}

	@Override
	public Reader getReader() throws IOException {
		return getWrappedRepresentation().getReader();
	}

	@Override
	public long getSize() {
		return getWrappedRepresentation().getSize();
	}

	@Override
	public InputStream getStream() throws IOException {
		return getWrappedRepresentation().getStream();
	}

	@Override
	public Tag getTag() {
		return getWrappedRepresentation().getTag();
	}

	@Override
	public String getText() throws IOException {
		return getWrappedRepresentation().getText();
	}

	/**
	 * Returns the wrapped representation.
	 * 
	 * @return The wrapped representation.
	 */
	public Representation getWrappedRepresentation() {
		return this.wrappedRepresentation;
	}

	@Override
	public boolean isAvailable() {
		return getWrappedRepresentation().isAvailable();
	}

	@Override
	@Deprecated
	public boolean isSelectable() {
		return getWrappedRepresentation().isSelectable();
	}

	@Override
	public boolean isTransient() {
		return getWrappedRepresentation().isTransient();
	}

	@Override
	@Deprecated
	public org.restlet.util.SelectionRegistration getRegistration() throws IOException {
		return getWrappedRepresentation().getRegistration();
	}

	@Override
	public void release() {
		getWrappedRepresentation().release();
	}

	@Override
	public void setAvailable(boolean isAvailable) {
		getWrappedRepresentation().setAvailable(isAvailable);
	}

	@Override
	public void setCharacterSet(CharacterSet characterSet) {
		getWrappedRepresentation().setCharacterSet(characterSet);
	}

	@Override
	public void setDigest(org.restlet.data.Digest digest) {
		getWrappedRepresentation().setDigest(digest);
	}

	@Override
	public void setDisposition(Disposition disposition) {
		getWrappedRepresentation().setDisposition(disposition);
	}

	@Override
	public void setEncodings(List<Encoding> encodings) {
		getWrappedRepresentation().setEncodings(encodings);
	}

	@Override
	public void setExpirationDate(Date expirationDate) {
		getWrappedRepresentation().setExpirationDate(expirationDate);
	}

	@Override
	public void setLanguages(List<Language> languages) {
		getWrappedRepresentation().setLanguages(languages);
	}

	@Override
	public void setLocationRef(Reference location) {
		getWrappedRepresentation().setLocationRef(location);
	}

	@Override
	public void setLocationRef(String locationUri) {
		getWrappedRepresentation().setLocationRef(locationUri);
	}

	@Override
	public void setMediaType(MediaType mediaType) {
		getWrappedRepresentation().setMediaType(mediaType);
	}

	@Override
	public void setModificationDate(Date modificationDate) {
		getWrappedRepresentation().setModificationDate(modificationDate);
	}

	@Override
	public void setRange(org.restlet.data.Range range) {
		getWrappedRepresentation().setRange(range);
	}

	@Override
	public void setSize(long expectedSize) {
		getWrappedRepresentation().setSize(expectedSize);
	}

	@Override
	public void setTag(Tag tag) {
		getWrappedRepresentation().setTag(tag);
	}

	@Override
	public void setTransient(boolean isTransient) {
		getWrappedRepresentation().setTransient(isTransient);
	}

	@Override
	public void write(java.io.OutputStream outputStream) throws IOException {
		getWrappedRepresentation().write(outputStream);
	}

	@Override
	public void write(java.io.Writer writer) throws IOException {
		getWrappedRepresentation().write(writer);
	}

	@Override
	@Deprecated
	public void write(java.nio.channels.WritableByteChannel writableChannel) throws IOException {
		getWrappedRepresentation().write(writableChannel);
	}
}
