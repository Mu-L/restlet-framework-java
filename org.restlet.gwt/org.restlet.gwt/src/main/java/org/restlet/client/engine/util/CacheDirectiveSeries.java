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

import org.restlet.client.data.CacheDirective;
import org.restlet.client.util.Series;

/**
 * CacheDirective series.
 * 
 * @author Thierry Boileau
 */
public class CacheDirectiveSeries extends Series<CacheDirective> {

    /**
     * Constructor.
     */
    public CacheDirectiveSeries() {
        super(CacheDirective.class);
    }

    /**
     * Constructor.
     * 
     * @param delegate
     *            The delegate list.
     */
    public CacheDirectiveSeries(List<CacheDirective> delegate) {
        super(CacheDirective.class, delegate);
    }

    @Override
    public CacheDirective createEntry(String name, String value) {
        return new CacheDirective(name, value);
    }
    
    @Override
	public Series<CacheDirective> createSeries(List<CacheDirective> delegate) {
		return new CacheDirectiveSeries(delegate);
	}

}
