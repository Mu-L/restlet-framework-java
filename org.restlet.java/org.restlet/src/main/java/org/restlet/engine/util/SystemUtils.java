/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.engine.util;

/**
 * System utilities.
 * 
 * @author Jerome Louvel
 */
public class SystemUtils {

	/**
	 * Parses the "java.version" system property and returns the first digit of the
	 * version number of the Java Runtime Environment (e.g. "1" for "1.3.0").
	 * 
	 * @see <a href="http://java.sun.com/j2se/versioning_naming.html">Official Java
	 *      versioning</a>
	 * @return The major version number of the Java Runtime Environment.
	 */
	public static int getJavaMajorVersion() {
		int result;
		final String javaVersion = System.getProperty("java.version");
		try {
			result = Integer.parseInt(javaVersion.substring(0, javaVersion.indexOf(".")));
		} catch (Exception e) {
			result = 0;
		}

		return result;
	}

	/**
	 * Parses the "java.version" system property and returns the second digit of the
	 * version number of the Java Runtime Environment (e.g. "3" for "1.3.0").
	 * 
	 * @see <a href="http://java.sun.com/j2se/versioning_naming.html">Official Java
	 *      versioning</a>
	 * @return The minor version number of the Java Runtime Environment.
	 */
	public static int getJavaMinorVersion() {
		int result;
		final String javaVersion = System.getProperty("java.version");
		try {
			result = Integer.parseInt(javaVersion.split("\\.")[1]);
		} catch (Exception e) {
			result = 0;
		}

		return result;
	}

	/**
	 * Parses the "java.version" system property and returns the update release
	 * number of the Java Runtime Environment (e.g. "10" for "1.3.0_10").
	 * 
	 * @see <a href="http://java.sun.com/j2se/versioning_naming.html">Official Java
	 *      versioning</a>
	 * @return The release number of the Java Runtime Environment or 0 if it does
	 *         not exist.
	 */
	public static int getJavaUpdateVersion() {
		int result;
		final String javaVersion = System.getProperty("java.version");
		try {
			result = Integer.parseInt(javaVersion.substring(javaVersion.indexOf('_') + 1));
		} catch (Exception e) {
			result = 0;
		}

		return result;
	}

	/**
	 * Computes the hash code of a set of objects. Follows the algorithm specified
	 * in List.hasCode().
	 * 
	 * @param objects the objects to compute the hashCode
	 * 
	 * @return The hash code of a set of objects.
	 */
	public static int hashCode(Object... objects) {
		int result = 17;

		if (objects != null) {
			for (final Object obj : objects) {
				result = 31 * result + (obj == null ? 0 : obj.hashCode());
			}
		}

		return result;
	}

	/**
	 * Indicates if the current operating system is in the Windows family.
	 * 
	 * @return True if the current operating system is in the Windows family.
	 */
	public static boolean isWindows() {
		return System.getProperty("os.name").toLowerCase().contains("win");
	}

	/**
	 * Private constructor to ensure that the class acts as a true utility class
	 * i.e. it isn't instantiable and extensible.
	 */
	private SystemUtils() {
	}

}
