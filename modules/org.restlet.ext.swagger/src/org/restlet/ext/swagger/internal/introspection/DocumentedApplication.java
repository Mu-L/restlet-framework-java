/**
 * Copyright 2005-2024 Qlik
 *
 * The contents of this file are subject to the terms of one of the following
 * open source licenses: Apache 2.0 or or EPL 1.0 (the "Licenses"). You can
 * select the license that you prefer but you may not use this file except in
 * compliance with one of these Licenses.
 *
 * You can obtain a copy of the Apache 2.0 license at
 * http://www.opensource.org/licenses/apache-2.0
 *
 * You can obtain a copy of the EPL 1.0 license at
 * http://www.opensource.org/licenses/eclipse-1.0
 *
 * See the Licenses for the specific language governing permissions and
 * limitations under the Licenses.
 *
 * Alternatively, you can obtain a royalty free commercial license with less
 * limitations, transferable or non-transferable, directly at
 * https://restlet.talend.com
 *
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.ext.platform.internal.introspection;

import java.util.Map;

import org.restlet.ext.platform.internal.model.Section;

/**
 * Completes an application for documentation purpose.
 *
 * @author Cyprien Quilici
 * @deprecated Will be removed in 2.5 release.
 */
@Deprecated
public interface DocumentedApplication {

    /**
     * The sections of the Web API.
     * The key of the map is the section name and the value is the optional description.
     *
     * @return The sections of the Web API.
     */
    Map<String, Section> getSections();

}
