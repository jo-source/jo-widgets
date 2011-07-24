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

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

final class ValidationResultImpl implements IValidationResult, Serializable {

	private static final long serialVersionUID = -6306348636538693857L;

	private final IValidationResult inheritedResult;
	private final IValidationResult newResult;
	private final IValidationMessage newMessage;
	private final String newContext;
	private final IValidationMessage worstFirst;

	private List<IValidationMessage> messages;
	private List<IValidationMessage> errors;
	private List<IValidationMessage> infoErrors;
	private List<IValidationMessage> warnings;
	private List<IValidationMessage> infos;

	ValidationResultImpl() {
		this(null, null, null, null);
	}

	private ValidationResultImpl(
		final IValidationResult inheritedResult,
		final IValidationResult newResult,
		final IValidationMessage newMessage,
		final String newContext) {

		this.inheritedResult = inheritedResult;
		this.newResult = newResult;
		this.newMessage = newMessage;
		this.newContext = newContext;

		final IValidationMessage firstWorst = inheritedResult != null ? inheritedResult.getWorstFirst() : ValidationMessage.ok();
		final IValidationMessage secondWorst = newResult != null ? newResult.getWorstFirst() : ValidationMessage.ok();
		final IValidationMessage thirdWorst = newMessage != null ? newMessage : ValidationMessage.ok();

		IValidationMessage worst = firstWorst;
		if (secondWorst.worse(worst)) {
			worst = secondWorst;
		}
		if (thirdWorst.worse(worst)) {
			worst = thirdWorst;
		}
		this.worstFirst = getMessage(worst, newContext);
	}

	@Override
	public List<IValidationMessage> getAll() {
		if (messages == null) {
			initializeLazy();
		}
		return messages;
	}

	@Override
	public List<IValidationMessage> getErrors() {
		if (errors == null) {
			initializeLazy();
		}
		return errors;
	}

	@Override
	public List<IValidationMessage> getInfoErrors() {
		if (infoErrors == null) {
			initializeLazy();
		}
		return infoErrors;
	}

	@Override
	public List<IValidationMessage> getWarnings() {
		if (warnings == null) {
			initializeLazy();
		}
		return warnings;
	}

	@Override
	public List<IValidationMessage> getInfos() {
		if (infos == null) {
			initializeLazy();
		}
		return infos;
	}

	@Override
	public IValidationMessage getWorstFirst() {
		return worstFirst;
	}

	@Override
	public boolean isValid() {
		return worstFirst.getType().isValid();
	}

	@Override
	public IValidationResult withMessage(final IValidationMessage message) {
		Assert.paramNotNull(message, "messages");
		return new ValidationResultImpl(this, null, message, null);
	}

	@Override
	public IValidationResult withContext(final String context) {
		if (NullCompatibleEquivalence.equals(context, newContext)) {
			return this;
		}
		else {
			return new ValidationResultImpl(this, null, null, context);
		}
	}

	@Override
	public IValidationResult withResult(final IValidationResult result) {
		return new ValidationResultImpl(this, result, null, null);
	}

	@Override
	public IValidationResult withError(final String message) {
		return withMessage(ValidationMessage.error(message));
	}

	@Override
	public IValidationResult withInfoError(final String message) {
		return withMessage(ValidationMessage.infoError(message));
	}

	@Override
	public IValidationResult withWarning(final String message) {
		return withMessage(ValidationMessage.warning(message));
	}

	@Override
	public IValidationResult withInfo(final String message) {
		return withMessage(ValidationMessage.info(message));
	}

	@Override
	public IValidationResult withError(final String context, final String message) {
		return withMessage(ValidationMessage.error(context, message));
	}

	@Override
	public IValidationResult withInfoError(final String context, final String message) {
		return withMessage(ValidationMessage.infoError(context, message));
	}

	@Override
	public IValidationResult withWarning(final String context, final String message) {
		return withMessage(ValidationMessage.warning(context, message));
	}

	@Override
	public IValidationResult withInfo(final String context, final String message) {
		return withMessage(ValidationMessage.info(context, message));
	}

	@Override
	public String toString() {
		return "ValidationResultImpl [worstFirst=" + worstFirst + "]";
	}

	private void initializeLazy() {
		final List<IValidationMessage> messagesMutable = new LinkedList<IValidationMessage>();
		final List<IValidationMessage> errorsMutable = new LinkedList<IValidationMessage>();
		final List<IValidationMessage> infoErrorsMutable = new LinkedList<IValidationMessage>();
		final List<IValidationMessage> warningsMutable = new LinkedList<IValidationMessage>();
		final List<IValidationMessage> infosMutable = new LinkedList<IValidationMessage>();

		if (inheritedResult != null) {
			addMessages(
					inheritedResult.getAll(),
					messagesMutable,
					errorsMutable,
					infoErrorsMutable,
					warningsMutable,
					infosMutable);
		}

		if (newResult != null) {
			addMessages(newResult.getAll(), messagesMutable, errorsMutable, infoErrorsMutable, warningsMutable, infosMutable);
		}

		if (newMessage != null) {
			addMessage(newMessage, messagesMutable, errorsMutable, infoErrorsMutable, warningsMutable, infosMutable);
		}

		messages = Collections.unmodifiableList(messagesMutable);
		errors = Collections.unmodifiableList(errorsMutable);
		infoErrors = Collections.unmodifiableList(infoErrorsMutable);
		warnings = Collections.unmodifiableList(warningsMutable);
		infos = Collections.unmodifiableList(infosMutable);
	}

	private void addMessages(
		final List<IValidationMessage> source,
		final List<IValidationMessage> messages,
		final List<IValidationMessage> errors,
		final List<IValidationMessage> infoErrors,
		final List<IValidationMessage> warnings,
		final List<IValidationMessage> infos) {
		for (final IValidationMessage message : source) {
			addMessage(message, messages, errors, infoErrors, warnings, infos);
		}
	}

	private void addMessage(
		IValidationMessage message,
		final List<IValidationMessage> messages,
		final List<IValidationMessage> errors,
		final List<IValidationMessage> infoErrors,
		final List<IValidationMessage> warnings,
		final List<IValidationMessage> infos) {

		message = getMessage(message, newContext);
		if (MessageType.ERROR == message.getType()) {
			messages.add(message);
			errors.add(message);
		}
		else if (MessageType.INFO_ERROR == message.getType()) {
			messages.add(message);
			infoErrors.add(message);
		}
		else if (MessageType.WARNING == message.getType()) {
			messages.add(message);
			warnings.add(message);
		}
		else if (MessageType.INFO == message.getType()) {
			messages.add(message);
			infos.add(message);
		}
	}

	private IValidationMessage getMessage(final IValidationMessage original, final String newContext) {
		if (newContext != null) {
			return original.withContext(newContext);
		}
		else {
			return original;
		}
	}

}
