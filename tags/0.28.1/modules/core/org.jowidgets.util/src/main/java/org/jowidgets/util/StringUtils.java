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

	public static String concatElementsSeparatedBy(final String[] strings, final char separator) {
		Assert.paramNotNull(strings, "strings");
		return concatElementsSeparatedBy(Arrays.asList(strings), separator);
	}

	public static String concatElementsSeparatedBy(final Collection<String> strings, final char separator) {
		final StringBuilder result = new StringBuilder();
		for (final String label : strings) {
			result.append(label + separator + " ");
		}
		if (strings.size() > 0) {
			result.replace(result.length() - 2, result.length(), "");
		}
		return result.toString();
	}

	public static String concatElementsSeparatedByComma(final Collection<String> strings) {
		return concatElementsSeparatedBy(strings, ',');
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
}
