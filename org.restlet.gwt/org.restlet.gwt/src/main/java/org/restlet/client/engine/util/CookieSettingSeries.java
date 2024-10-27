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

import org.restlet.client.data.CookieSetting;
import org.restlet.client.util.Series;

/**
 * Cookie setting series.
 * 
 * @author Jerome Louvel
 */
public class CookieSettingSeries extends Series<CookieSetting> {
    /**
     * Constructor.
     */
    public CookieSettingSeries() {
        super(CookieSetting.class);
    }

    /**
     * Constructor.
     * 
     * @param delegate
     *            The delegate list.
     */
    public CookieSettingSeries(List<CookieSetting> delegate) {
        super(CookieSetting.class, delegate);
    }

    @Override
    public CookieSetting createEntry(String name, String value) {
        return new CookieSetting(name, value);
    }

    @Override
	public Series<CookieSetting> createSeries(List<CookieSetting> delegate) {
		return new CookieSettingSeries(delegate);
	}

}
