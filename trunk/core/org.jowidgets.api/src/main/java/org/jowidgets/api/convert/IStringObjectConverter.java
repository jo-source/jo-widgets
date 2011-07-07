/*
 * Copyright (c) 2010, Michael Grossmann
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
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jowidgets.api.convert;

import org.jowidgets.common.mask.ITextMask;
import org.jowidgets.common.verify.IInputVerifier;
import org.jowidgets.validation.IValidator;

public interface IStringObjectConverter<OBJECT_TYPE> {

	/**
	 * Converts an string input to an object.
	 * 
	 * @param string The string to convert.
	 * @return The object that is represented by the string or null if:
	 *         1. The string input is not convertible to the OBJECT_TYPE
	 *         2. The string represents null
	 */
	OBJECT_TYPE convertToObject(String string);

	/**
	 * Gets a validator that will be used to validate the string input.
	 * 
	 * @return A validator or null
	 */
	IValidator<String> getStringValidator();

	/**
	 * Gets an input verifier that verifies the input.
	 * 
	 * REMARK: Implementors must not assume that the verifier will be used on all platforms,
	 * so the validator implementation must consider inputs that won't be verified
	 * 
	 * REMARK: Implementors should prefer to use regular expressions instead of {@link IInputVerifier}'s
	 * if possible because SPI implementations that use 'AJAX' could implement client side verification easier.
	 * 
	 * @return A verifier or null
	 */
	IInputVerifier getInputVerifier();

	/**
	 * Gets the regular expression that describes the valid inputs.
	 * 
	 * REMARK: Implementors should prefer to use regular expressions instead of {@link IInputVerifier}'s
	 * if possible because SPI implementations that uses e.g 'AJAX' could implement client side verification easier.
	 * 
	 * REMARK: Implementors must not assume that the regular expression will be used on all platforms
	 * 
	 * @return A regular expression or null
	 */
	String getAcceptingRegExp();

	/**
	 * Gets a mask if the input should be masked.
	 * 
	 * @return A text mask or null if the input is not masked
	 */
	ITextMask getMask();

}
