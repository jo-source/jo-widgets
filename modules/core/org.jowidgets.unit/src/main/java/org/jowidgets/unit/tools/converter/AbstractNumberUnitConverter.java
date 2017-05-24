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

package org.jowidgets.unit.tools.converter;

import java.math.BigDecimal;

import org.jowidgets.unit.api.IUnit;
import org.jowidgets.unit.api.IUnitConverter;
import org.jowidgets.unit.api.IUnitProvider;
import org.jowidgets.unit.api.IUnitValue;
import org.jowidgets.unit.api.IUnitValueConverter;
import org.jowidgets.unit.tools.StaticUnitProvider;
import org.jowidgets.unit.tools.UnitValue;
import org.jowidgets.unit.tools.validator.NumberUnitValidator;
import org.jowidgets.util.Assert;
import org.jowidgets.validation.IValidationResult;
import org.jowidgets.validation.IValidator;
import org.jowidgets.validation.ValidationResult;

/**
 * A abstract {@link IUnitConverter} base implementation for numbers.
 * 
 * This implementation uses {@link BigDecimal} as internal representation for conversion.
 * 
 * Derived classes must provided conversions from base value type and unit value type to big decimal
 * and back.
 * 
 * Furthermore the derived classes must check if the internal calculated big decimals can be converted back to the
 * base value type and unit value type.
 *
 * @param <BASE_VALUE_TYPE> The type of the base value
 * @param <UNIT_VALUE_TYPE> The type of the {@link IUnitValue}'s value part
 */
public abstract class AbstractNumberUnitConverter<BASE_VALUE_TYPE extends Number, UNIT_VALUE_TYPE extends Number>
        implements IUnitValueConverter<BASE_VALUE_TYPE, UNIT_VALUE_TYPE> {

    private static final BigDecimal MAX_LONG = BigDecimal.valueOf(Long.MAX_VALUE);
    private static final BigDecimal MIN_LONG = BigDecimal.valueOf(Long.MIN_VALUE);

    private static final BigDecimal MAX_DOUBLE = BigDecimal.valueOf(Double.MAX_VALUE);
    private static final BigDecimal MIN_DOUBLE = MAX_DOUBLE.negate();

    private final IValidator<IUnitValue<UNIT_VALUE_TYPE>> validator;
    private final IUnitProvider<BASE_VALUE_TYPE> unitProvider;
    private final BASE_VALUE_TYPE unconvertibleSubstitude;

    /**
     * Creates a new instance {@link StaticUnitProvider} provider holding the given unit
     * 
     * @param defaultUnit The default unit to use for conversion, must not be null
     */
    public AbstractNumberUnitConverter(final IUnit defaultUnit) {
        this(new StaticUnitProvider<BASE_VALUE_TYPE>(defaultUnit));
    }

    /**
     * Creates a new instance with a given unit provider
     * 
     * @param defaultUnit The unit provider to use for conversion, must not be null
     */
    public AbstractNumberUnitConverter(final IUnitProvider<BASE_VALUE_TYPE> unitProvider) {
        this(unitProvider, null);
    }

    /**
     * Creates a new instance with a given unit provider
     * 
     * @param defaultUnit The unit provider to use for conversion, must not be null
     * @param unconvertibleSubstitude This value will be used if a base value it not convertible to the base value type because it
     *            is out of range. The substitute may be null
     */
    public AbstractNumberUnitConverter(
        final IUnitProvider<BASE_VALUE_TYPE> unitProvider,
        final BASE_VALUE_TYPE unconvertibleSubstitude) {
        Assert.paramNotNull(unitProvider, "unitProvider");
        this.unitProvider = unitProvider;
        this.unconvertibleSubstitude = unconvertibleSubstitude;
        this.validator = new UnitValidator();
    }

    /**
     * Converts the given base value into a big decimal
     * 
     * @param baseValue The base value, never null
     * 
     * @return The big decimal representation of the base value
     */
    protected abstract BigDecimal baseValueToBigDecimal(BASE_VALUE_TYPE baseValue);

    /**
     * Converts the given big decimal value into a base value
     * 
     * @param value The big decimal value, never null
     * 
     * @return The base value representation of the big decimal
     */
    protected abstract BASE_VALUE_TYPE bigDecimalToBaseValue(BigDecimal value);

    /**
     * Checks if the given big decimal is convertible to a base value.
     * 
     * The value is convertible even if the are rounding issues but is not convertible
     * if the given value is not in the base value range / interval
     * 
     * @param value The value to check, never null
     * 
     * @return True if convertible, false otherwise
     */
    protected abstract boolean isConvertibleToBaseValue(final BigDecimal value);

    /**
     * Converts the unit value value part into a big decimal
     * 
     * @param unitValue The unit value, never null
     * 
     * @return The big decimal representation of the unit value
     */
    protected abstract BigDecimal unitValueToBigDecimal(UNIT_VALUE_TYPE unitValue);

    /**
     * Converts the given big decimal value into a unit value
     * 
     * @param value The big decimal value, never null
     * 
     * @return The unit value representation of the big decimal
     */
    protected abstract UNIT_VALUE_TYPE bigDecimalToUnitValue(BigDecimal value);

    /**
     * Checks if the given big decimal is convertible to a unit value.
     * 
     * The value is convertible even if the are round issues but is not convertible
     * if the given value is not in the unit value range / interval
     * 
     * @param value The value to check, never null
     * 
     * @return True if convertible, false otherwise
     */
    protected abstract boolean isConvertibleToUnitValue(final BigDecimal value);

    @Override
    public IValidator<IUnitValue<UNIT_VALUE_TYPE>> getValidator() {
        return validator;
    }

    @Override
    public final BASE_VALUE_TYPE toBaseValue(final IUnitValue<UNIT_VALUE_TYPE> unitValue) {
        if (unitValue != null) {
            try {
                if (unitValue.getValue() != null) {
                    final BigDecimal baseValueDecimal = toBaseValueDecimal(unitValue);
                    if (isConvertibleToBaseValue(baseValueDecimal)) {
                        return bigDecimalToBaseValue(baseValueDecimal);
                    }
                    else {
                        return unconvertibleSubstitude;
                    }
                }
            }
            catch (final NumberFormatException e) {
                return unconvertibleSubstitude;
            }
        }
        return null;
    }

    private BigDecimal toBaseValueDecimal(final IUnitValue<UNIT_VALUE_TYPE> unitValue) {
        if (unitValue != null) {
            try {
                if (unitValue.getValue() != null) {
                    final BigDecimal valueDecimal = unitValueToBigDecimal(unitValue.getValue());
                    return valueDecimal.multiply(BigDecimal.valueOf(unitValue.getUnit().getConversionFactor()));
                }
            }
            catch (final NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    @Override
    public final IUnitValue<UNIT_VALUE_TYPE> toUnitValue(final BASE_VALUE_TYPE baseValue) {
        if (baseValue != null) {
            return toUnitValue(baseValue, unitProvider.getUnit(baseValue));
        }
        return null;
    }

    @Override
    public IUnitValue<UNIT_VALUE_TYPE> convert(final IUnitValue<UNIT_VALUE_TYPE> unitValue, final IUnit unit) {
        Assert.paramNotNull(unit, "unit");
        if (unitValue != null) {
            if (unit.equals(unitValue.getUnit())) {
                return unitValue;
            }
            else {
                final BASE_VALUE_TYPE baseValue = toBaseValue(unitValue);
                if (baseValue != null) {
                    return toUnitValue(baseValue, unit);
                }
            }
        }
        return null;
    }

    @Override
    public final IUnitValue<UNIT_VALUE_TYPE> toUnitValue(final BASE_VALUE_TYPE baseValue, final IUnit unit) {
        Assert.paramNotNull(unit, "unit");
        if (baseValue != null) {
            try {
                final BigDecimal baseValueDecimal = baseValueToBigDecimal(baseValue);
                final BigDecimal unitValueDecimal = baseValueDecimal.divide(BigDecimal.valueOf(unit.getConversionFactor()));
                if (isConvertibleToUnitValue(unitValueDecimal)) {
                    final UNIT_VALUE_TYPE unitValue = bigDecimalToUnitValue(unitValueDecimal);
                    if (unitValue != null) {
                        return new UnitValue<UNIT_VALUE_TYPE>(unitValue, unit);
                    }
                }
            }
            catch (final NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    protected static final boolean isInsideLongValuesInterval(final BigDecimal value) {
        if (MAX_LONG.compareTo(value) >= 0 && MIN_LONG.compareTo(value) <= 0) {
            return true;
        }
        else {
            return false;
        }
    }

    protected static final boolean isInsideDoubleValuesInterval(final BigDecimal value) {
        if (MAX_DOUBLE.compareTo(value) >= 0 && MIN_DOUBLE.compareTo(value) <= 0) {
            return true;
        }
        else {
            return false;
        }
    }

    final class UnitValidator implements IValidator<IUnitValue<UNIT_VALUE_TYPE>> {

        private final NumberUnitValidator<BASE_VALUE_TYPE, UNIT_VALUE_TYPE> validator;

        UnitValidator() {
            this.validator = new NumberUnitValidator<BASE_VALUE_TYPE, UNIT_VALUE_TYPE>(AbstractNumberUnitConverter.this);
        }

        @Override
        public IValidationResult validate(final IUnitValue<UNIT_VALUE_TYPE> unitValue) {
            if (unitValue != null && unitValue.getValue() != null) {
                if (!isInBaseValueRange(unitValue)) {
                    return ValidationResult.error(NumberUnitValidator.OUT_OF_RANGE_ERROR.get());
                }
            }
            return validator.validate(unitValue);
        }

        private boolean isInBaseValueRange(final IUnitValue<UNIT_VALUE_TYPE> unitValue) {
            final BigDecimal baseValueDecimal = toBaseValueDecimal(unitValue);
            return baseValueDecimal != null && isConvertibleToBaseValue(baseValueDecimal);
        }

    }

}
