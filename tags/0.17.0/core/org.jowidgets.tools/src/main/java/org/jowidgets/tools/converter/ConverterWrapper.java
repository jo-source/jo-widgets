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

package org.jowidgets.tools.converter;

import org.jowidgets.api.convert.IConverter;
import org.jowidgets.common.mask.ITextMask;
import org.jowidgets.common.verify.IInputVerifier;
import org.jowidgets.util.Assert;
import org.jowidgets.validation.IValidator;

public class ConverterWrapper<FROM_TYPE> implements IConverter<FROM_TYPE> {

	private final IConverter<FROM_TYPE> original;

	public ConverterWrapper(final IConverter<FROM_TYPE> original) {
		Assert.paramNotNull(original, "original");
		this.original = original;
	}

	@Override
	public String convertToString(final FROM_TYPE value) {
		return original.convertToString(value);
	}

	@Override
	public FROM_TYPE convertToObject(final String string) {
		return original.convertToObject(string);
	}

	@Override
	public String getDescription(final FROM_TYPE value) {
		return original.getDescription(value);
	}

	@Override
	public IValidator<String> getStringValidator() {
		return original.getStringValidator();
	}

	@Override
	public IInputVerifier getInputVerifier() {
		return original.getInputVerifier();
	}

	@Override
	public String getAcceptingRegExp() {
		return original.getAcceptingRegExp();
	}

	@Override
	public ITextMask getMask() {
		return original.getMask();
	}

}
