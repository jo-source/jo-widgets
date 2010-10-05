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
 * ARE DISCLAIMED. IN NO EVENT SHALL jo-widgets.org BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jo.widgets.api.validation;

import java.util.LinkedList;
import java.util.List;

import org.jo.widgets.util.Assert;

public final class ValidationResult {

	private final List<ValidationMessage> errorMessages = new LinkedList<ValidationMessage>();

	private final List<ValidationMessage> warningMessages = new LinkedList<ValidationMessage>();

	private ValidationMessage okMessage;

	public ValidationResult(final ValidationMessage... validationMessages) {
		super();
		for (final ValidationMessage message : validationMessages) {
			addValidationMessage(message);
		}
	}

	public ValidationResult(final ValidationMessageType type, final String messageText) {
		this(new ValidationMessage(type, messageText));
	}

	public void addValidationResult(final ValidationResult result) {
		Assert.paramNotNull(result, "result"); //$NON-NLS-1$
		for (final ValidationMessage message : result.getValidationMessages()) {
			addValidationMessage(message);
		}
	}

	public void addValidationMessage(final ValidationMessage message) {
		Assert.paramNotNull(message, "message"); //$NON-NLS-1$
		if (ValidationMessageType.ERROR.equals(message.getType())) {
			errorMessages.add(message);
		}
		else if (ValidationMessageType.WARNING.equals(message.getType())) {
			warningMessages.add(message);
		}
	}

	public void addValidationError(final String messageText) {
		Assert.paramNotEmpty(messageText, "messageText"); //$NON-NLS-1$
		this.errorMessages.add(new ValidationMessage(ValidationMessageType.ERROR, messageText));
	}

	public void addValidationWarning(final String messageText) {
		Assert.paramNotEmpty(messageText, "messageText"); //$NON-NLS-1$
		this.warningMessages.add(new ValidationMessage(ValidationMessageType.WARNING, messageText));
	}

	public void setValidationOk(final String messageText) {
		Assert.paramNotNull(messageText, "messageText"); //$NON-NLS-1$
		okMessage = new ValidationMessage(ValidationMessageType.OK, messageText);
	}

	public List<ValidationMessage> getValidationMessages() {
		final List<ValidationMessage> result = getErrorMessages();
		result.addAll(getWarningMessages());
		return result;
	}

	public List<ValidationMessage> getErrorMessages() {
		return new LinkedList<ValidationMessage>(errorMessages);
	}

	public List<ValidationMessage> getWarningMessages() {
		return new LinkedList<ValidationMessage>(warningMessages);
	}

	public boolean isOk() {
		return ValidationMessageType.OK.equals(getWorstFirstMessage().getType());
	}

	public ValidationResult copyAndSetContext(final String context) {
		final ValidationResult result = new ValidationResult();
		for (final ValidationMessage message : getValidationMessages()) {
			result.addValidationMessage(new ValidationMessage(message.getType(), message.getMessageText(), context));
		}
		return result;
	}

	public ValidationMessage getWorstFirstMessage() {
		if (!errorMessages.isEmpty()) {
			return errorMessages.iterator().next();
		}
		else if (!warningMessages.isEmpty()) {
			return warningMessages.iterator().next();
		}
		else if (okMessage != null) {
			return okMessage;
		}
		else {
			return ValidationMessage.OK_MESSAGE;
		}
	}

}
