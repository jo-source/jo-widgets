/*
 * Copyright (c) 2010, Michael Grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jowidgets.impl.convert;

class DefaultYesNoConverterLong extends DefaultBooleanConverter {

	private static final String[] TRUE_STRINGS = new String[] {Messages.getString("DefaultYesNoConverterLong.yes"), //$NON-NLS-1$
			Messages.getString("DefaultYesNoConverterLong.yes_uppercase"), //$NON-NLS-1$
			Messages.getString("DefaultYesNoConverterLong.yes_lowercase")}; //$NON-NLS-1$
	private static final String[] FALSE_STRINGS = new String[] {Messages.getString("DefaultYesNoConverterLong.no"), //$NON-NLS-1$
			Messages.getString("DefaultYesNoConverterLong.no_uppercase"), //$NON-NLS-1$
			Messages.getString("DefaultYesNoConverterLong.no_lowercase")}; //$NON-NLS-1$
	private static final String MATCHING_REG_EXP = buildRegExp();

	public DefaultYesNoConverterLong() {
		super(TRUE_STRINGS, FALSE_STRINGS, MATCHING_REG_EXP);
	}

	private static String buildRegExp() {
		final StringBuilder result = new StringBuilder();
		result.append("^$"); //$NON-NLS-1$
		for (final String word : TRUE_STRINGS) {
			for (int i = 0; i < word.length(); i++) {
				result.append('|');
				result.append(word.substring(0, i + 1));
			}
		}
		for (final String word : FALSE_STRINGS) {
			for (int i = 0; i < word.length(); i++) {
				result.append('|');
				result.append(word.substring(0, i + 1));
			}
		}
		return result.toString();
	}
}
