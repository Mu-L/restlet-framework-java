/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.ext.osgi.internal;

import java.util.logging.Logger;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.restlet.ext.osgi.ObapClientHelper;

/**
 * OSGi activator. It registers the installed bundles in order to cope with
 * futur calls made using the OBAP protocol.
 * 
 * @author Thierry Boileau
 * @See {@link ObapClientHelper}
 */
public class Activator implements BundleActivator {

    private static Logger logger = Logger.getLogger("org.restlet.ext.osgi");

    @Override
    public void start(BundleContext context) throws Exception {
        for (Bundle bundle : context.getBundles()) {
            if (!ObapClientHelper.register(bundle)) {
                logger.warning("OBAP client helper can't register this bundle: "
                        + bundle.getBundleId()
                        + " at location "
                        + bundle.getLocation());
            }
        }

        // Listen to installed bundles
        context.addBundleListener(new BundleListener() {
            public void bundleChanged(BundleEvent event) {
                switch (event.getType()) {
                case BundleEvent.INSTALLED:
                    if (!ObapClientHelper.register(event.getBundle())) {
                        logger.warning("OBAP client helper can't register this bundle: "
                                + event.getBundle().getBundleId()
                                + " at location "
                                + event.getBundle().getLocation());
                    }
                    break;
                }
            }
        });
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        ObapClientHelper.clear();
    }

}