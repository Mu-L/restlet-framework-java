/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.resource;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.restlet.engine.connector.Method;
import org.restlet.service.MetadataService;

/**
 * Annotation for methods that describe a resource. Its semantics is equivalent
 * to an HTTP OPTIONS method.<br>
 * <br>
 * Example:
 * 
 * <pre>
 * &#064;Options
 * public ApplicationInfo describe();
 * 
 * &#064;Options(&quot;wadl|html&quot;)
 * public Representation describe();
 * 
 * &#064;Options(&quot;wadl?param=val&quot;)
 * public Representation describeWithParam();
 * 
 * &#064;Options(&quot;wadl?param&quot;)
 * public Representation describeWithParam();
 * 
 * &#064;Options(&quot;?param&quot;)
 * public Representation describeWithParam();
 * </pre>
 * 
 * @author Jerome Louvel
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Method("OPTIONS")
public @interface Options {

	/**
	 * Specifies the media type extension of the response entity. If several media
	 * types are supported, their extension can be specified separated by "|"
	 * characters. Note that this isn't the full MIME type value, just the extension
	 * name declared in {@link MetadataService}. For a list of all predefined
	 * extensions, please check {@link MetadataService#addCommonExtensions()}. New
	 * extension can be registered using
	 * {@link MetadataService#addExtension(String, org.restlet.data.Metadata)}
	 * method.
	 * 
	 * @return The result media types.
	 */
	String value() default "";

}
