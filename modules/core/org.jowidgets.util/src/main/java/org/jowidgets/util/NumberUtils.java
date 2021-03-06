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

public final class NumberUtils {

    private NumberUtils() {}

    public static double round(final double value, final int decimalPlaces) {
        Assert.paramInBounds(10, decimalPlaces, "decimalPlaces");
        final double factor = Math.pow(10.0d, decimalPlaces);
        return Math.round(value * factor) / factor;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <NUMBER_TYPE extends Number> int compareTo(final NUMBER_TYPE n1, final NUMBER_TYPE n2) {
        if (n1 == null && n2 == null) {
            return 0;
        }
        else if (n1 == null) {// only one of them is null
            return -1;
        }
        else if (n2 == null) {// only one of them is null
            return 1;
        }
        else {
            if (!(n1 instanceof Comparable<?>) || !(n2 instanceof Comparable<?>)) {
                throw new IllegalArgumentException("The given numbers must be comparable");
            }
            return ((Comparable) n1).compareTo(n2);
        }
    }

    public static <NUMBER_TYPE extends Number> NUMBER_TYPE min(final NUMBER_TYPE n1, final NUMBER_TYPE n2) {
        if (n1 == null && n2 == null) {
            return null;
        }
        else if (n1 == null) {// only one of them is null
            return n2;
        }
        else if (n2 == null) {// only one of them is null
            return n1;
        }
        final int compared = compareTo(n1, n2);
        if (compared < 0) {
            return n1;
        }
        else if (compared > 0) {
            return n2;
        }
        else {
            return n1;
        }
    }

    public static <NUMBER_TYPE extends Number> NUMBER_TYPE max(final NUMBER_TYPE n1, final NUMBER_TYPE n2) {
        if (n1 == null && n2 == null) {
            return null;
        }
        else if (n1 == null) {// only one of them is null
            return n2;
        }
        else if (n2 == null) {// only one of them is null
            return n1;
        }
        final int compared = compareTo(n1, n2);
        if (compared > 0) {
            return n1;
        }
        else if (compared < 0) {
            return n2;
        }
        else {
            return n1;
        }
    }
}
