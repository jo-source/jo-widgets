/*
 * Copyright (c) 2011, grossmann
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

package org.jowidgets.tools.converter;

import java.util.Map;

import org.jowidgets.api.convert.IConverter;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.common.mask.ITextMask;
import org.jowidgets.common.verify.IInputVerifier;
import org.jowidgets.validation.IValidator;

public final class MapConverter<OBJECT_TYPE> implements IConverter<OBJECT_TYPE> {

	private final IConverter<OBJECT_TYPE> converter;

	public MapConverter(
		final Map<? extends OBJECT_TYPE, String> objectToString,
		final Map<String, ? extends OBJECT_TYPE> stringToObject,
		final String hint) {
		this.converter = Toolkit.getConverterProvider().mapConverter(objectToString, stringToObject, hint);
	}

	@Override
	public String convertToString(final OBJECT_TYPE value) {
		return converter.convertToString(value);
	}

	@Override
	public OBJECT_TYPE convertToObject(final String string) {
		return converter.convertToObject(string);
	}

	@Override
	public String getDescription(final OBJECT_TYPE value) {
		return converter.getDescription(value);
	}

	@Override
	public IValidator<String> getStringValidator() {
		return converter.getStringValidator();
	}

	@Override
	public IInputVerifier getInputVerifier() {
		return converter.getInputVerifier();
	}

	@Override
	public String getAcceptingRegExp() {
		return converter.getAcceptingRegExp();
	}

	@Override
	public ITextMask getMask() {
		return converter.getMask();
	}

}
