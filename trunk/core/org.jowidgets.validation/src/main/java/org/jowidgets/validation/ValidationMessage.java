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

package org.jowidgets.validation;

public final class ValidationMessage {

	private static final IValidationMessage OK = create(MessageType.OK, null, "");

	private ValidationMessage() {}

	public static IValidationMessage ok() {
		return OK;
	}

	public static IValidationMessage create(final MessageType type, final String context, final String text) {
		Assert.paramNotNull(type, "type");
		return new ValidationMessageImpl(type, context, text);
	}

	public static IValidationMessage create(final MessageType type, final String text) {
		return create(type, null, text);
	}

	public static IValidationMessage error(final String context, final String text) {
		return create(MessageType.ERROR, context, text);
	}

	public static IValidationMessage error(final String message) {
		return error(null, message);
	}

	public static IValidationMessage infoError(final String context, final String text) {
		return create(MessageType.INFO_ERROR, context, text);
	}

	public static IValidationMessage infoError(final String text) {
		return infoError(null, text);
	}

	public static IValidationMessage warning(final String context, final String text) {
		return create(MessageType.WARNING, context, text);
	}

	public static IValidationMessage warning(final String text) {
		return warning(null, text);
	}

}
