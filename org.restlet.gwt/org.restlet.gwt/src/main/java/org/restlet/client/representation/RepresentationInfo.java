/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.client.representation;

import java.util.Date;

import org.restlet.client.data.MediaType;
import org.restlet.client.data.Tag;
import org.restlet.client.engine.util.DateUtils;

/**
 * Information about a representation. Those metadata don't belong to the parent
 * {@link Variant} class, however they are important for conditional method
 * processing. The advantage over the complete {@link Representation} class is
 * that it is much lighter to create.
 * 
 * @see <a href=
 *      "http://roy.gbiv.com/pubs/dissertation/rest_arch_style.htm#sec_5_2_1_2"
 *      >Source dissertation</a>
 * @author Jerome Louvel
 */
public class RepresentationInfo extends Variant {

    /** The modification date. */
    private volatile Date modificationDate;

    /** The tag. */
    private volatile Tag tag;

    /**
     * Default constructor.
     */
    public RepresentationInfo() {
        this(null);
    }

    /**
     * Constructor.
     * 
     * @param mediaType
     *            The media type.
     */
    public RepresentationInfo(MediaType mediaType) {
        this(mediaType, null, null);
    }

    /**
     * Constructor.
     * 
     * @param mediaType
     *            The media type.
     * @param modificationDate
     *            The modification date.
     */
    public RepresentationInfo(MediaType mediaType, Date modificationDate) {
        this(mediaType, modificationDate, null);
    }

    /**
     * Constructor.
     * 
     * @param mediaType
     *            The media type.
     * @param modificationDate
     *            The modification date.
     * @param tag
     *            The tag.
     */
    public RepresentationInfo(MediaType mediaType, Date modificationDate,
            Tag tag) {
        super(mediaType);
        this.modificationDate = modificationDate;
        this.tag = tag;
    }

    /**
     * Constructor.
     * 
     * @param mediaType
     *            The media type.
     * @param tag
     *            The tag.
     */
    public RepresentationInfo(MediaType mediaType, Tag tag) {
        this(mediaType, null, tag);
    }

    /**
     * Constructor from a variant.
     * 
     * @param variant
     *            The variant to copy.
     * @param modificationDate
     *            The modification date.
     */
    public RepresentationInfo(Variant variant, Date modificationDate) {
        this(variant, modificationDate, null);
    }

    /**
     * Constructor from a variant.
     * 
     * @param variant
     *            The variant to copy.
     * @param modificationDate
     *            The modification date.
     * @param tag
     *            The tag.
     */
    public RepresentationInfo(Variant variant, Date modificationDate, Tag tag) {
        setCharacterSet(variant.getCharacterSet());
        setEncodings(variant.getEncodings());
        setLocationRef(variant.getLocationRef());
        setLanguages(variant.getLanguages());
        setMediaType(variant.getMediaType());
        setModificationDate(modificationDate);
        setTag(tag);
    }

    /**
     * Constructor from a variant.
     * 
     * @param variant
     *            The variant to copy.
     * @param tag
     *            The tag.
     */
    public RepresentationInfo(Variant variant, Tag tag) {
        this(variant, null, tag);
    }

    /**
     * Returns the last date when this representation was modified. If this
     * information is not known, returns null.<br>
     * <br>
     * Note that when used with HTTP connectors, this property maps to the
     * "Last-Modified" header.
     * 
     * @return The modification date.
     */
    public Date getModificationDate() {
        return this.modificationDate;
    }

    /**
     * Returns the tag.<br>
     * <br>
     * Note that when used with HTTP connectors, this property maps to the
     * "ETag" header.
     * 
     * @return The tag.
     */
    public Tag getTag() {
        return this.tag;
    }

    /**
     * Sets the last date when this representation was modified. If this
     * information is not known, pass null.<br>
     * <br>
     * Note that when used with HTTP connectors, this property maps to the
     * "Last-Modified" header.
     * 
     * @param modificationDate
     *            The modification date.
     */
    public void setModificationDate(Date modificationDate) {
        this.modificationDate = DateUtils.unmodifiable(modificationDate);
    }

    /**
     * Sets the tag.<br>
     * <br>
     * Note that when used with HTTP connectors, this property maps to the
     * "ETag" header.
     * 
     * @param tag
     *            The tag.
     */
    public void setTag(Tag tag) {
        this.tag = tag;
    }

}
