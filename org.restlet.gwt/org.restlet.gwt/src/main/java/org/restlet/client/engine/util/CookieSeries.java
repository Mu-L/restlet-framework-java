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

import org.restlet.client.data.Cookie;
import org.restlet.client.util.Series;

/**
 * Cookie series.
 * 
 * @author Jerome Louvel
 */
public class CookieSeries extends Series<Cookie> {

    /**
     * Constructor.
     */
    public CookieSeries() {
        super(Cookie.class);
    }

    /**
     * Constructor.
     * 
     * @param delegate
     *            The delegate list.
     */
    public CookieSeries(List<Cookie> delegate) {
        super(Cookie.class, delegate);
    }

    @Override
    public Cookie createEntry(String name, String value) {
        return new Cookie(name, value);
    }
    
    @Override
	public Series<Cookie> createSeries(List<Cookie> delegate) {
		return new CookieSeries(delegate);
	}

}
