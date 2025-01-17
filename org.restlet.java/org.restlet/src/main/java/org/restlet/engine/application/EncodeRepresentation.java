/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.engine.application;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.restlet.data.Disposition;
import org.restlet.data.Encoding;
import org.restlet.engine.io.IoUtils;
import org.restlet.representation.Representation;
import org.restlet.util.WrapperList;
import org.restlet.util.WrapperRepresentation;

/**
 * Content that encodes a wrapped content. Allows to apply only one encoding.
 * 
 * @author Jerome Louvel
 */
public class EncodeRepresentation extends WrapperRepresentation {
	/**
	 * Returns the list of supported encodings.
	 * 
	 * @return The list of supported encodings.
	 */
	public static List<Encoding> getSupportedEncodings() {
		return Arrays.<Encoding>asList(Encoding.GZIP, Encoding.DEFLATE, Encoding.DEFLATE_NOWRAP, Encoding.ZIP,
				Encoding.IDENTITY);
	}

	/** Indicates if the encoding can happen. */
	private volatile boolean canEncode;

	/** The encoding to apply. */
	private volatile Encoding encoding;

	/** The applied encodings. */
	private volatile List<Encoding> encodings;

	/**
	 * Constructor.
	 * 
	 * @param encoding              Encoder algorithm.
	 * @param wrappedRepresentation The wrapped representation.
	 */
	public EncodeRepresentation(Encoding encoding, Representation wrappedRepresentation) {
		super(wrappedRepresentation);
		this.canEncode = getSupportedEncodings().contains(encoding);
		this.encodings = null;
		this.encoding = encoding;
	}

	/**
	 * Indicates if the encoding can happen.
	 * 
	 * @return True if the encoding can happen.
	 */
	public boolean canEncode() {
		return this.canEncode;
	}

	/**
	 * Returns the available size in bytes of the encoded representation if known,
	 * UNKNOWN_SIZE (-1) otherwise.
	 * 
	 * @return The available size in bytes if known, UNKNOWN_SIZE (-1) otherwise.
	 */
	@Override
	public long getAvailableSize() {
		long result = UNKNOWN_SIZE;

		if (canEncode()) {
			if (this.encoding.equals(Encoding.IDENTITY)) {
				result = getWrappedRepresentation().getAvailableSize();
			}
		} else {
			result = getWrappedRepresentation().getAvailableSize();
		}

		return result;
	}

	/**
	 * Returns a readable byte channel. If it is supported by a file a read-only
	 * instance of FileChannel is returned.
	 * 
	 * @return A readable byte channel.
	 * @deprecated NIO will be removed in next major release.
	 */
	@Deprecated
	@Override
	public ReadableByteChannel getChannel() throws IOException {
		if (canEncode()) {
			return IoUtils.getChannel(this);
		} else {
			return getWrappedRepresentation().getChannel();
		}
	}

	/**
	 * Returns the applied encodings.
	 * 
	 * @return The applied encodings.
	 */
	@Override
	public List<Encoding> getEncodings() {
		if (this.encodings == null) {
			this.encodings = new WrapperList<Encoding>() {

				@Override
				public boolean add(Encoding element) {
					if (element == null) {
						throw new IllegalArgumentException("Cannot add a null encoding.");
					}

					return super.add(element);
				}

				@Override
				public void add(int index, Encoding element) {
					if (element == null) {
						throw new IllegalArgumentException("Cannot add a null encoding.");
					}

					super.add(index, element);
				}

				@Override
				public boolean addAll(Collection<? extends Encoding> elements) {
					boolean addNull = (elements == null);
					if (!addNull) {
						for (final Iterator<? extends Encoding> iterator = elements.iterator(); !addNull
								&& iterator.hasNext();) {
							addNull = (iterator.next() == null);
						}
					}
					if (addNull) {
						throw new IllegalArgumentException("Cannot add a null encoding.");
					}

					return super.addAll(elements);
				}

				@Override
				public boolean addAll(int index, Collection<? extends Encoding> elements) {
					boolean addNull = (elements == null);
					if (!addNull) {
						for (final Iterator<? extends Encoding> iterator = elements.iterator(); !addNull
								&& iterator.hasNext();) {
							addNull = (iterator.next() == null);
						}
					}
					if (addNull) {
						throw new IllegalArgumentException("Cannot add a null encoding.");
					}

					return super.addAll(index, elements);
				}
			};
			this.encodings.addAll(getWrappedRepresentation().getEncodings());
			if (canEncode()) {
				this.encodings.add(this.encoding);
			}
		}
		return this.encodings;
	}

	@Override
	public Reader getReader() throws IOException {
		if (canEncode()) {
			return IoUtils.getReader(getStream(), getCharacterSet());
		} else {
			return getWrappedRepresentation().getReader();
		}
	}

	/**
	 * Returns the size in bytes of the encoded representation if known,
	 * UNKNOWN_SIZE (-1) otherwise.
	 * 
	 * @return The size in bytes if known, UNKNOWN_SIZE (-1) otherwise.
	 */
	@Override
	public long getSize() {
		long result = UNKNOWN_SIZE;

		if (canEncode()) {
			if (this.encoding.equals(Encoding.IDENTITY)) {
				result = getWrappedRepresentation().getSize();
			}
		} else {
			result = getWrappedRepresentation().getSize();
		}

		return result;
	}

	@Override
	public InputStream getStream() throws IOException {
		if (canEncode()) {
			return IoUtils.getStream(this);
		} else {
			return getWrappedRepresentation().getStream();
		}
	}

	@Override
	public String getText() throws IOException {
		if (canEncode()) {
			return IoUtils.toString(getStream(), getCharacterSet());
		} else {
			return getWrappedRepresentation().getText();
		}
	}

	@Override
	public void write(OutputStream outputStream) throws IOException {
		if (canEncode()) {
			DeflaterOutputStream encoderOutputStream = null;

			if (this.encoding.equals(Encoding.GZIP)) {
				encoderOutputStream = new GZIPOutputStream(outputStream);
			} else if (this.encoding.equals(Encoding.DEFLATE)) {
				encoderOutputStream = new DeflaterOutputStream(outputStream);
			} else if (this.encoding.equals(Encoding.DEFLATE_NOWRAP)) {
				encoderOutputStream = new DeflaterOutputStream(outputStream,
						new Deflater(Deflater.DEFAULT_COMPRESSION, true));
			} else if (this.encoding.equals(Encoding.ZIP)) {
				final ZipOutputStream stream = new ZipOutputStream(outputStream);
				String name = "entry";

				if (getWrappedRepresentation().getDisposition() != null) {
					name = getWrappedRepresentation().getDisposition().getParameters()
							.getFirstValue(Disposition.NAME_FILENAME, true, name);
				}

				stream.putNextEntry(new ZipEntry(name));
				encoderOutputStream = stream;
			} else if (this.encoding.equals(Encoding.IDENTITY)) {
				// Encoder unnecessary for identity encoding
			}

			if (encoderOutputStream != null) {
				getWrappedRepresentation().write(encoderOutputStream);
				encoderOutputStream.flush();
				encoderOutputStream.finish();
			} else {
				getWrappedRepresentation().write(outputStream);
			}
		} else {
			getWrappedRepresentation().write(outputStream);
		}
	}

	@Deprecated
	@Override
	public void write(WritableByteChannel writableChannel) throws IOException {
		if (canEncode()) {
			OutputStream os = IoUtils.getStream(writableChannel);
			write(os);
			os.flush();
		} else {
			getWrappedRepresentation().write(writableChannel);
		}
	}

	@Override
	public void write(java.io.Writer writer) throws IOException {
		if (canEncode()) {
			OutputStream os = IoUtils.getStream(writer, getCharacterSet());
			write(os);
			os.flush();
		} else {
			getWrappedRepresentation().write(writer);
		}
	}

}
