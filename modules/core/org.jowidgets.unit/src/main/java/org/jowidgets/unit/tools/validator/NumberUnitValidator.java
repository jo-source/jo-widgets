/*
 * Copyright (c) 2014, grossmann
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

package org.jowidgets.unit.tools.validator;

import org.jowidgets.i18n.api.IMessage;
import org.jowidgets.unit.api.IUnitValue;
import org.jowidgets.unit.api.IUnitValueConverter;
import org.jowidgets.util.Assert;
import org.jowidgets.util.NullCompatibleEquivalence;
import org.jowidgets.validation.IValidationResult;
import org.jowidgets.validation.IValidator;
import org.jowidgets.validation.ValidationResult;

public final class NumberUnitValidator<BASE_VALUE_TYPE extends Number, UNIT_VALUE_TYPE extends Number>
        implements IValidator<IUnitValue<UNIT_VALUE_TYPE>> {

    public static final IMessage OUT_OF_RANGE_ERROR = Messages.getMessage("NumberUnitValidator.outOfRange");
    private static final IMessage BIJECTIVITY_CHECK_WARNING = Messages.getMessage("NumberUnitValidator.bijectivityCheckFailed");

    private final IUnitValueConverter<BASE_VALUE_TYPE, UNIT_VALUE_TYPE> converter;

    public NumberUnitValidator(final IUnitValueConverter<BASE_VALUE_TYPE, UNIT_VALUE_TYPE> converter) {
        Assert.paramNotNull(converter, "converter");
        this.converter = converter;
    }

    @Override
    public IValidationResult validate(final IUnitValue<UNIT_VALUE_TYPE> unitValue) {
        if (unitValue != null && unitValue.getValue() != null) {
            final BASE_VALUE_TYPE baseValue = converter.toBaseValue(unitValue);
            if (baseValue == null) {
                return ValidationResult.error(OUT_OF_RANGE_ERROR.get());
            }

            final IUnitValue<UNIT_VALUE_TYPE> bakUnitValue = converter.toUnitValue(baseValue, unitValue.getUnit());
            if (!NullCompatibleEquivalence.equals(bakUnitValue, unitValue)) {
                return ValidationResult.warning(BIJECTIVITY_CHECK_WARNING.get());
            }
        }
        return ValidationResult.ok();
    }

}
