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

package org.jowidgets.api.convert.impl;

import org.jowidgets.api.convert.IConverter;
import org.jowidgets.api.convert.IObjectStringConverter;
import org.jowidgets.api.convert.IStringObjectConverter;
import org.jowidgets.api.validation.ValidationMessage;
import org.jowidgets.api.validation.ValidationResult;
import org.jowidgets.util.Assert;

public class Converter<TYPE> implements IConverter<TYPE> {

	private final IObjectStringConverter<TYPE> objectStringConverter;
	private final IStringObjectConverter<TYPE> stringObjectConverter;

	public Converter(
		final IObjectStringConverter<TYPE> objectStringConverter,
		final IStringObjectConverter<TYPE> stringObjectConverter) {
		super();
		Assert.paramNotNull(objectStringConverter, "objectStringConverter");
		Assert.paramNotNull(stringObjectConverter, "stringObjectConverter");
		this.objectStringConverter = objectStringConverter;
		this.stringObjectConverter = stringObjectConverter;
	}

	@Override
	public TYPE convertToObject(final String string) {
		return stringObjectConverter.convertToObject(string);
	}

	@Override
	public ValidationMessage isCompletableToValid(final String string) {
		return stringObjectConverter.isCompletableToValid(string);
	}

	@Override
	public ValidationResult validate(final String validationInput) {
		return stringObjectConverter.validate(validationInput);
	}

	@Override
	public String convertToString(final TYPE value) {
		return objectStringConverter.convertToString(value);
	}

	@Override
	public String getDescription(final TYPE value) {
		return objectStringConverter.getDescription(value);
	}

}
