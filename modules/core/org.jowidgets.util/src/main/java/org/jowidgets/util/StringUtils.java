/*
 * Copyright (c) 2012, grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 *   names of its contributors may be used to endorse or promote products
 *   derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL jo-widgets.org BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */

package org.jowidgets.util;

import java.util.Arrays;
import java.util.Collection;

public final class StringUtils {

    private StringUtils() {}

    public static String concatElementsSeparatedBy(final Object[] strings, final char separator) {
        Assert.paramNotNull(strings, "strings");
        return concatElementsSeparatedBy(Arrays.asList(strings), separator);
    }

    public static String concatElementsSeparatedBy(final Collection<?> strings, final char separator) {
        return concatElementsSeparatedBy(strings, separator + " ");
    }

    public static String concatElementsSeparatedByComma(final Collection<?> strings) {
        return concatElementsSeparatedBy(strings, ',');
    }

    public static String concatElementsSeparatedBy(final Collection<?> strings, final String separator) {
        return concatElementsSeparatedBy(strings, separator, true);
    }

    public static String concatElementsSeparatedBy(
        final Collection<?> strings,
        final String separator,
        final boolean excludeEmptyValues) {
        Assert.paramNotNull(strings, "strings");
        return concat(separator, excludeEmptyValues, strings.toArray());
    }

    /**
     * Concats the given objects with the separator to a new string.
     * 
     * Empty values will be excluded.
     * 
     * @param separator The separator to use
     * @param objects The objects to concat, the toString() method will be used to get the string for the objects
     * 
     * @return A new String with the concated elements
     */
    public static String concat(final String separator, final Object... objects) {
        return concat(separator, true, objects);
    }

    public static String concat(final String separator, final Iterable<?> objects) {
        return concat(separator, true, objects);
    }

    public static String concat(final String separator, final boolean excludeEmptyValues, final Object... objects) {
        return concat(separator, excludeEmptyValues, Arrays.asList(objects));
    }

    /**
     * Concats the given objects with the separator to a new string
     * 
     * @param separator The separator to use
     * @param excludeEmptyValues If true, empty values will be excluded, otherwise empty values will be replaced by EmptyString
     * @param objects The objects to concat, the toString() method will be used to get the string for the objects
     * 
     * @return A new String with the concated elements
     */
    public static String concat(final String separator, final boolean excludeEmptyValues, final Iterable<?> objects) {
        Assert.paramNotNull(separator, "separator");
        Assert.paramNotNull(objects, "objects");
        final StringBuilder result = new StringBuilder();
        int added = 0;
        for (final Object label : objects) {
            if (!EmptyCheck.isEmpty(label)) {
                result.append(label.toString() + separator);
                added++;
            }
            else if (!excludeEmptyValues) {
                result.append("" + separator);
            }
        }
        if (added > 0) {
            result.replace(result.length() - separator.length(), result.length(), "");
        }
        return result.toString();
    }

    public static String truncateToLength(final String string, final int length) {
        Assert.paramInBounds(Integer.MAX_VALUE, length, "length");
        if (EmptyCheck.isEmpty(string)) {
            return string;
        }
        if (string.length() <= length) {
            return string;
        }
        else {
            return string.substring(0, length - 4) + " ...";
        }
    }

    /**
     * Counts how often the char 'toFind' is contained in the string 'toSearchIn'
     * 
     * @param toSearchIn The string to search in
     * @param toFind The string to find
     * 
     * @return The number of matches between 0 and toSearchIn.length
     */
    public static int countMatches(final String toSearchIn, final char toFind) {
        return countMatches(toSearchIn, String.valueOf(toFind));
    }

    /**
     * Counts how often the string 'toFind' is contained in the string 'toSearchIn'
     * 
     * @param toSearchIn The string to search in
     * @param toFind The string to find
     * 
     * @return The number of matches between 0 and toSearchIn.length
     */
    public static int countMatches(final String toSearchIn, final String toFind) {
        if (EmptyCheck.isEmpty(toSearchIn) || EmptyCheck.isEmpty(toFind)) {
            return 0;
        }
        final int stringToFindLength = toFind.length();
        int result = 0;
        int index = 0;
        while ((index = toSearchIn.indexOf(toFind, index)) != -1) {
            result++;
            index = index + stringToFindLength;
        }
        return result;
    }

    public static String loop(final String tanga, final int count) {
        if (tanga == null) {
            return null;
        }
        else if (tanga.isEmpty()) {
            return tanga;
        }
        else {
            final StringBuilder result = new StringBuilder();
            for (int i = 0; i < count; i++) {
                result.append(tanga);
            }
            return result.toString();
        }

    }
}
