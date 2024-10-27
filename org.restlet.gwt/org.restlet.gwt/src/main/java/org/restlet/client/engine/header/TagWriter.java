/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.client.engine.header;

import java.util.List;

import org.restlet.client.data.Tag;

/**
 * Tag header writer.
 * 
 * @author Jerome Louvel
 */
public class TagWriter extends HeaderWriter<Tag> {

    /**
     * Writes a list of tags.
     * 
     * @param tags
     *            The tags to write.
     * @return This writer.
     */
    public static String write(List<Tag> tags) {
        return new TagWriter().append(tags).toString();
    }

    /**
     * Writes a tag.
     * 
     * @param tag
     *            The tag to write.
     * @return This writer.
     */
    public static String write(Tag tag) {
        return tag.format();
    }

    @Override
    public HeaderWriter<Tag> append(Tag tag) {
        return append(write(tag));
    }

}
