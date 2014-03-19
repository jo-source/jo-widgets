/*
 * Copyright (c) 2012, waheckma
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

package org.jowidgets.api.test;

import java.util.Locale;
import java.util.regex.Pattern;

import org.jowidgets.api.toolkit.Toolkit;
import org.junit.Assert;
import org.junit.Test;

public class AcceptingRegExpTest {

	@Test
	public void createFloatTest() {

		// Get the regex of the doubleNumber converter
		final Locale defaultLocale = Locale.getDefault();
		Locale.setDefault(Locale.GERMAN);
		final String regex = Toolkit.getConverterProvider().doubleNumber().getAcceptingRegExp();
		Locale.setDefault(defaultLocale);

		// All cases which have to be true
		final String[] caseTrue = {"123165466554,55", "132.123.123.123,123", "1,234", "-1234567,55", "-12.345.674,54", "-12,345"};

		// All cases wich have to be false
		final String[] caseFalse = {"abcdefg", "-abd", "1.33.23,455", "1,434,343", "1.535.22,565", ",00", "-,88"};

		// Test all true cases
		for (int i = 0; i <= caseTrue.length - 1; i++) {
			Assert.assertTrue(matchString(regex, caseTrue[i]));
			Assert.assertTrue(entryStringPossible(regex, caseTrue[i]));
		}

		// Test all false cases
		for (int i = 0; i <= caseFalse.length - 1; i++) {
			Assert.assertFalse(matchString(regex, caseFalse[i]));
			Assert.assertFalse(entryStringPossible(regex, caseFalse[i]));
		}

	}

	public boolean matchString(final String regex, final String str) {
		final boolean matches = Pattern.matches(regex, str);
		return matches;
	}

	public boolean entryStringPossible(final String regex, final String str) {
		boolean test = true;

		String x = "";

		for (int i = 0; i <= str.length() - 1; i++) {
			x += str.subSequence(i, i + 1);
			if (!matchString(regex, x)) {
				test = false;
			}
		}
		return test;
	}

}
