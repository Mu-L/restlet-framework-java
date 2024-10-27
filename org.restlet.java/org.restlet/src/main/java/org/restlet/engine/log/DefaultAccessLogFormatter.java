/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.engine.log;

import java.util.logging.Handler;

import org.restlet.engine.Engine;

/**
 * Access log record formatter which writes a header describing the default log
 * format.
 * 
 * @author Jerome Louvel
 */
public class DefaultAccessLogFormatter extends AccessLogFormatter {

	@Override
	public String getHead(Handler h) {
		final StringBuilder sb = new StringBuilder();
		sb.append("#Software: Restlet Framework ").append(Engine.VERSION).append('\n');
		sb.append("#Version: 1.0\n");
		sb.append("#Date: ");
		final long currentTime = System.currentTimeMillis();
		sb.append(String.format("%tF", currentTime));
		sb.append(' ');
		sb.append(String.format("%tT", currentTime));
		sb.append('\n');
		sb.append("#Fields: ");
		sb.append("date time c-ip cs-username s-ip s-port cs-method ");
		sb.append("cs-uri-stem cs-uri-query sc-status sc-bytes cs-bytes ");
		sb.append("time-taken cs-host cs(User-Agent) cs(Referrer)\n");
		return sb.toString();
	}

}
