/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.client.engine.util;

import java.util.List;

import org.restlet.client.data.Header;
import org.restlet.client.util.Series;

/**
 * Header series.
 * 
 * @author Thierry Boileau
 */
public class HeaderSeries extends Series<Header> {

    /**
     * Constructor.
     */
    public HeaderSeries() {
        super(Header.class);
    }

    /**
     * Constructor.
     * 
     * @param delegate
     *            The delegate list.
     */
    public HeaderSeries(List<Header> delegate) {
        super(Header.class, delegate);
    }

    @Override
    public Header createEntry(String name, String value) {
        return new Header(name, value);
    }

	@Override
	public Series<Header> createSeries(List<Header> delegate) {
		return new HeaderSeries(delegate);
	}

}
