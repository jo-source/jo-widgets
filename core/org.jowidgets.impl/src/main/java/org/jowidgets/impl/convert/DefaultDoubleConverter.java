/*
 * Copyright (c) 2011, Nikolaus Moll
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

import java.text.DecimalFormat;
import java.text.ParsePosition;

import org.jowidgets.api.convert.IConverter;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.tools.converter.AbstractConverter;
import org.jowidgets.validation.IValidationResult;
import org.jowidgets.validation.IValidator;
import org.jowidgets.validation.ValidationResult;

class DefaultDoubleConverter extends AbstractConverter<Double> implements IConverter<Double> {

	private final DecimalFormat decimalFormat;
	private final String formatHint;

	DefaultDoubleConverter(final DecimalFormat decimalFormat, final String formatHint) {
		this.decimalFormat = decimalFormat;
		this.formatHint = formatHint;
	}

	@Override
	public Double convertToObject(final String string) {
		try {
			final ParsePosition pos = new ParsePosition(0);
			final Number result = decimalFormat.parse(string, pos);
			if (result == null || pos.getIndex() < string.length()) {
				return null;
			}
			return result.doubleValue();
		}
		catch (final NumberFormatException e) {
			return null;
		}
	}

	@Override
	public String convertToString(final Double value) {
		if (value != null) {
			return decimalFormat.format(value);
		}
		return null;
	}

	@Override
	public IValidator<String> getStringValidator() {
		return new IValidator<String>() {
			@Override
			public IValidationResult validate(final String input) {
				if (input != null && !input.trim().isEmpty()) {
					final ParsePosition pos = new ParsePosition(0);
					decimalFormat.parse(input, pos);
					if (pos.getIndex() < input.length()) {
						if (formatHint != null) {
							return ValidationResult.error(Toolkit.getMessageReplacer().replace(
									"Must have the format '%1'",
									formatHint));
						}
						else {
							return ValidationResult.error("Is not a valid date or time");
						}
					}
				}
				return ValidationResult.ok();
			}
		};
	}

	@Override
	public String getAcceptingRegExp() {
		final String decimalSeparatorRegEx;
		if (isSpecialChar(decimalFormat.getDecimalFormatSymbols().getDecimalSeparator())) {
			decimalSeparatorRegEx = "\\" + decimalFormat.getDecimalFormatSymbols().getDecimalSeparator();
		}
		else {
			decimalSeparatorRegEx = String.valueOf(decimalFormat.getDecimalFormatSymbols().getDecimalSeparator());
		}
		return "-?([0-9]*([0-9]" + decimalSeparatorRegEx + "[0-9]*)?)";
	}

	// TODO NM add more special chars... externalize method?
	private boolean isSpecialChar(final char c) {
		if (c == '.') {
			return true;
		}
		return false;
	}
}
