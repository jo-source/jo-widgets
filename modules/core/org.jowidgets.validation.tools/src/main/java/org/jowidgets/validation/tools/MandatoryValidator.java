/*
 * Copyright (c) 2014, Michael Grossmann
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
package org.jowidgets.validation.tools;

import java.io.Serializable;
import java.util.Collection;

import org.jowidgets.i18n.api.IMessage;
import org.jowidgets.util.Assert;
import org.jowidgets.validation.IValidationResult;
import org.jowidgets.validation.IValidator;
import org.jowidgets.validation.ValidationResult;

public final class MandatoryValidator<VALIDATION_INPUT_TYPE> implements IValidator<VALIDATION_INPUT_TYPE>, Serializable {

    private static final long serialVersionUID = 8029380657803427107L;

    private static final IMessage MUST_NOT_BE_EMPTY = Messages.getMessage("MandatoryValidator.must_not_be_empty");
    private static final IMessage PLEASE_INSERT_VALUE = Messages.getMessage("MandatoryValidator.please_insert_value");

    private final IValidationResult result;

    public enum MandatoryMessageType {
        ERROR,
        INFO_ERROR;
    }

    public MandatoryValidator(final MandatoryMessageType messageType) {
        this(getValidationResult(messageType));
    }

    public MandatoryValidator() {
        this(MUST_NOT_BE_EMPTY.get());
    }

    public MandatoryValidator(final String messageText) {
        this(ValidationResult.error(messageText));
    }

    public MandatoryValidator(final IValidationResult errorResult) {
        Assert.paramNotNull(errorResult, "errorResult");
        this.result = errorResult;
    }

    private static IValidationResult getValidationResult(final MandatoryMessageType messageType) {
        Assert.paramNotNull(messageType, "messageType");
        if (MandatoryMessageType.ERROR.equals(messageType)) {
            return ValidationResult.error(MUST_NOT_BE_EMPTY.get());
        }
        else if (MandatoryMessageType.INFO_ERROR.equals(messageType)) {
            return ValidationResult.infoError(PLEASE_INSERT_VALUE.get());
        }
        else {
            throw new IllegalArgumentException("MandatoryMessageType '" + messageType + "' is not known");
        }
    }

    @Override
    public IValidationResult validate(final VALIDATION_INPUT_TYPE validationInput) {
        if (validationInput == null) {
            return result;
        }
        else if (validationInput instanceof String && ((String) validationInput).trim().isEmpty()) {
            return result;
        }
        else if (validationInput instanceof Collection<?> && ((Collection<?>) validationInput).isEmpty()) {
            return result;
        }
        return ValidationResult.ok();
    }

}
