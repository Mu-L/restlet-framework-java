/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.engine.io;

import java.io.IOException;
import java.nio.channels.Selector;
import java.util.EmptyStackException;
import java.util.Stack;

/**
 * Factory used to dispatch/share <code>Selector</code>.
 * 
 * @author Jean-Francois Arcand
 */
public class SelectorFactory {
	/** The maximum number of <code>Selector</code> to create. */
	public static final int MAX_SELECTORS = 20;

	/** The number of attempts to find an available selector. */
	public static final int MAX_ATTEMPTS = 2;

	/** Cache of <code>Selector</code>. */
	private static final Stack<Selector> SELECTORS = new Stack<Selector>();

	/** The timeout before we exit. */
	public static final long TIMEOUT = 5000;

	/** Creates the <code>Selector</code>. */
	static {
		try {
			for (int i = 0; i < MAX_SELECTORS; i++) {
				SELECTORS.add(Selector.open());
			}
		} catch (IOException ex) {
			// do nothing.
		}
	}

	/**
	 * Get an exclusive <code>Selector</code>.
	 * 
	 * @return An exclusive <code>Selector</code>.
	 */
	public final static Selector getSelector() {
		synchronized (SELECTORS) {
			Selector selector = null;

			try {
				if (SELECTORS.size() != 0) {
					selector = SELECTORS.pop();
				}
			} catch (EmptyStackException ex) {
			}

			int attempts = 0;
			try {
				while ((selector == null) && (attempts < MAX_ATTEMPTS)) {
					SELECTORS.wait(TIMEOUT);

					try {
						if (SELECTORS.size() != 0) {
							selector = SELECTORS.pop();
						}
					} catch (EmptyStackException ex) {
						break;
					}

					attempts++;
				}
			} catch (InterruptedException ex) {
			}

			return selector;
		}
	}

	/**
	 * Returns the <code>Selector</code> to the cache.
	 * 
	 * @param selector The <code>Selector</code> to return.
	 */
	public final static void returnSelector(Selector selector) {
		synchronized (SELECTORS) {
			SELECTORS.push(selector);
			if (SELECTORS.size() == 1) {
				SELECTORS.notify();
			}
		}
	}
}
