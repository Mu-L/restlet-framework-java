/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.client.engine.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Date manipulation utilities.
 * 
 * @author Jerome Louvel
 */
public final class DateUtils {

    /**
     * Obsoleted HTTP date format (ANSI C asctime() format). Pattern:
     * "EEE MMM dd HH:mm:ss yyyy".
     */
    public static final List<String> FORMAT_ASC_TIME = unmodifiableList("EEE MMM dd HH:mm:ss yyyy");

    /**
     * Obsoleted HTTP date format (RFC 1036). Pattern:
     * "EEEE, dd-MMM-yy HH:mm:ss zzz".
     */
    public static final List<String> FORMAT_RFC_1036 = unmodifiableList("EEEE, dd-MMM-yy HH:mm:ss zzz");

    /**
     * Preferred HTTP date format (RFC 1123). Pattern:
     * "EEE, dd MMM yyyy HH:mm:ss zzz".
     */
    public static final List<String> FORMAT_RFC_1123 = unmodifiableList("EEE, dd MMM yyyy HH:mm:ss zzz");

    /** W3C date format (RFC 3339). Pattern: "yyyy-MM-dd'T'HH:mm:ssz". */
    public static final List<String> FORMAT_RFC_3339 = unmodifiableList("yyyy-MM-dd'T'HH:mm:ssz");

    /** AWS date format (ISO 8601). Pattern: "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'". */
    public static final List<String> FORMAT_ISO_8601 = unmodifiableList("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    /**
     * Common date format (RFC 822). Patterns: "EEE, dd MMM yy HH:mm:ss z" or
     * "EEE, dd MMM yy HH:mm z", "dd MMM yy HH:mm:ss z" or "dd MMM yy HH:mm z".
     */
    public static final List<String> FORMAT_RFC_822 = unmodifiableList(
            "EEE, dd MMM yy HH:mm:ss z", "EEE, dd MMM yy HH:mm z",
            "dd MMM yy HH:mm:ss z", "dd MMM yy HH:mm z");


     private static final com.google.gwt.i18n.client.TimeZone TIMEZONE_GMT =
     com.google.gwt.i18n.client.TimeZone.createTimeZone(0);
    /**
     * Compares two date with a precision of one second.
     * 
     * @param baseDate
     *            The base date
     * @param afterDate
     *            The date supposed to be after.
     * @return True if the afterDate is indeed after the baseDate.
     */
    public static boolean after(final Date baseDate, final Date afterDate) {
        if ((baseDate == null) || (afterDate == null)) {
            throw new IllegalArgumentException(
                    "Can't compare the dates, at least one of them is null");
        }

        long baseTime = baseDate.getTime() / 1000;
        long afterTime = afterDate.getTime() / 1000;
        return baseTime < afterTime;
    }

    /**
     * Compares two date with a precision of one second.
     * 
     * @param baseDate
     *            The base date
     * @param beforeDate
     *            The date supposed to be before.
     * @return True if the beforeDate is indeed before the baseDate.
     */
    public static boolean before(final Date baseDate, final Date beforeDate) {
        if ((baseDate == null) || (beforeDate == null)) {
            throw new IllegalArgumentException(
                    "Can't compare the dates, at least one of them is null");
        }

        long baseTime = baseDate.getTime() / 1000;
        long beforeTime = beforeDate.getTime() / 1000;
        return beforeTime < baseTime;
    }

    /**
     * Compares two date with a precision of one second.
     * 
     * @param baseDate
     *            The base date
     * @param otherDate
     *            The other date supposed to be equals.
     * @return True if both dates are equals.
     */
    public static boolean equals(final Date baseDate, final Date otherDate) {
        if ((baseDate == null) || (otherDate == null)) {
            throw new IllegalArgumentException(
                    "Can't compare the dates, at least one of them is null");
        }

        long baseTime = baseDate.getTime() / 1000;
        long otherTime = otherDate.getTime() / 1000;
        return otherTime == baseTime;
    }

    /**
     * Formats a Date in the default HTTP format (RFC 1123).
     * 
     * @param date
     *            The date to format.
     * @return The formatted date.
     */
    public static String format(final Date date) {
        return format(date, DateUtils.FORMAT_RFC_1123.get(0));
    }

    /**
     * Formats a Date according to the first format in the array.
     * 
     * @param date
     *            The date to format.
     * @param formats
     *            The array of date formats to use.
     * @return The formatted date.
     */
    public static String format(final Date date, final List<String> formats) {
        return format(date, formats != null ? formats.get(0) : null);
    }

    /**
     * Formats a Date according to the given format.
     * 
     * @param date
     *            The date to format.
     * @param format
     *            The date format to use.
     * @return The formatted date.
     */
    public static String format(final Date date, final String format) {
        if (date == null) {
            throw new IllegalArgumentException("Date is null");
        }

        /*
         * GWT difference: DateTimeFormat parser is not passed a Locale in the
         * same way as SimpleDateFormat. It derives locale information from the
         * GWT application's locale.
         * 
         * Default timezone is GMT unless specified via a GMT:hhmm, GMT:+hhmm,
         * or GMT:-hhmm string.
         */
         final com.google.gwt.i18n.client.DateTimeFormat formatter =
         com.google.gwt.i18n.client.DateTimeFormat.getFormat(format);
         return formatter.format(date, TIMEZONE_GMT);
    }

    /**
     * Parses a formatted date into a Date object using the default HTTP format
     * (RFC 1123).
     * 
     * @param date
     *            The date to parse.
     * @return The parsed date.
     */
    public static Date parse(String date) {
        return parse(date, FORMAT_RFC_1123);
    }

    /**
     * Parses a formatted date into a Date object.
     * 
     * @param date
     *            The date to parse.
     * @param formats
     *            The date formats to use sorted by completeness.
     * @return The parsed date.
     */
    public static Date parse(String date, List<String> formats) {
        if (date == null) {
            throw new IllegalArgumentException("Date is null");
        }

        Date result = null;

        String format = null;
        int formatsSize = formats.size();

        for (int i = 0; (result == null) && (i < formatsSize); i++) {
            format = formats.get(i);
            /*
             * GWT difference: DateTimeFormat parser is not passed a Locale in
             * the same way as SimpleDateFormat. It derives locale information
             * from the GWT application's locale.
             * 
             * Default timezone is GMT unless specified via a GMT:hhmm,
             * GMT:+hhmm, or GMT:-hhmm string.
             */
             final com.google.gwt.i18n.client.DateTimeFormat parser =
             com.google.gwt.i18n.client.DateTimeFormat.getFormat(format);
            try {
                result = parser.parse(date);
            } catch (Exception e) {
                // Ignores error as the next format may work better
            }
        }

        return result;
    }

    /**
     * Returns an immutable version of a given date.
     * 
     * @param date
     *            The modifiable date.
     * @return An immutable version of a given date.
     */
    public static Date unmodifiable(Date date) {
        return (date == null) ? null : new ImmutableDate(date);
    }

    /**
     * Helper method to help initialize this class by providing unmodifiable
     * lists based on arrays.
     * 
     * @param <T>
     *            Any valid java object
     * @param array
     *            to be converted into an unmodifiable list
     * @return unmodifiable list based on the provided array
     */
    @SafeVarargs
    private static <T> List<T> unmodifiableList(final T... array) {
        return Collections.unmodifiableList(Arrays.asList(array));
    }

    /**
     * Private constructor to ensure that the class acts as a true utility class
     * i.e. it isn't instantiable and extensible.
     */
    private DateUtils() {

    }

}
