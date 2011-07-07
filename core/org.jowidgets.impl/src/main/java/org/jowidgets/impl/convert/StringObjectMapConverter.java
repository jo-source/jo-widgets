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

import java.util.HashMap;
import java.util.Map;

import org.jowidgets.tools.converter.AbstractStringObjectConverter;
import org.jowidgets.util.Assert;
import org.jowidgets.validation.IValidationResult;
import org.jowidgets.validation.IValidator;
import org.jowidgets.validation.ValidationResult;

public class StringObjectMapConverter<OBJECT_TYPE> extends AbstractStringObjectConverter<OBJECT_TYPE> {

	private final Map<String, OBJECT_TYPE> stringToObject;
	private final IValidator<String> stringValidator;

	public StringObjectMapConverter(final Map<String, ? extends OBJECT_TYPE> stringToObject, final String hint) {
		super();
		Assert.paramNotNull(stringToObject, "stringToObject");
		this.stringToObject = new HashMap<String, OBJECT_TYPE>(stringToObject);
		this.stringValidator = new IValidator<String>() {

			@Override
			public IValidationResult validate(final String input) {
				if (input != null && !input.isEmpty() && convertToObject(input) == null) {
					if (hint != null && !hint.isEmpty()) {
						return ValidationResult.error(hint);
					}
					else {
						return ValidationResult.error("Input not valid");
					}
				}
				return ValidationResult.ok();
			}
		};
	}

	@Override
	public OBJECT_TYPE convertToObject(final String string) {
		return stringToObject.get(string);
	}

	@Override
	public IValidator<String> getStringValidator() {
		return stringValidator;
	}

}
