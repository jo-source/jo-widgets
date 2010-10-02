/*
 * Copyright (c) 2010, Michael Grossmann
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *   * Neither the name of the jo-widgets.org nor the
 *     names of its contributors may be used to endorse or promote products
 *     derived from this software without specific prior written permission.
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
package org.jo.widgets.api.convert.impl.internal;

import org.jo.widgets.api.convert.IConverter;
import org.jo.widgets.api.convert.impl.AbstractObjectStringConverter;
import org.jo.widgets.api.validation.OkMessage;
import org.jo.widgets.api.validation.ValidationMessage;
import org.jo.widgets.api.validation.ValidationMessageType;
import org.jo.widgets.api.validation.ValidationResult;

public class DefaultLongConverter extends AbstractObjectStringConverter<Long>
		implements IConverter<Long> {

	private static final String NO_VALID_INTEGER_MESSAGE = "is no valid integer number";

	@Override
	public Long convertToObject(final String string) {
		try {
			// TODO define business logic and parse (less tolerant) with
			// consideration of i18n
			return Long.valueOf(Long.parseLong(string));
		} catch (final NumberFormatException e) {
			return null;
		}
	}

	@Override
	public String convertToString(final Long value) {
		return value.toString();
	}

	@Override
	public ValidationResult validate(final String text) {
		if (text != null && !text.isEmpty()) {
			if (convertToObject(text) == null) {
				return new ValidationResult(ValidationMessageType.ERROR,
						NO_VALID_INTEGER_MESSAGE);
			}
		}
		return new ValidationResult();
	}

	@Override
	public ValidationMessage isCompletableToValid(final String string) {
		if (string != null) {
			if (!string.matches("-?[0-9]*")) {
				return new ValidationMessage(ValidationMessageType.ERROR, "'"
						+ string + "' could not be completed to a valid number");
			}
		}
		return OkMessage.getInstance();
	}

}
