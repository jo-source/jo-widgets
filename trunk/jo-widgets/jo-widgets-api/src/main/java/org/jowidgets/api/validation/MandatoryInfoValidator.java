/*
 * Copyright (c) 2010 PLATH Group(R), Germany. All rights reserved.
 * Creation date: 21.07.2010
 */
package org.jowidgets.api.validation;

import org.jowidgets.util.Assert;

// TODO Add use ERROR_INFO message type (must be implemented)
public class MandatoryInfoValidator<VALIDATION_INPUT_TYPE> implements IValidator<VALIDATION_INPUT_TYPE> {

	private final ValidationResult infoResult;

	public MandatoryInfoValidator(final String messageText) {
		Assert.paramNotEmpty(messageText, "controlName");
		this.infoResult = new ValidationResult(ValidationMessageType.ERROR, messageText);
	}

	@Override
	public ValidationResult validate(final VALIDATION_INPUT_TYPE validationInput) {
		if (validationInput == null) {
			return infoResult;
		}
		else if (validationInput instanceof String && ((String) validationInput).trim().isEmpty()) {
			return infoResult;
		}
		return new ValidationResult();
	}

}
