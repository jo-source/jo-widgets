/*
 * Copyright (c) 2017, grossmann
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

public final class LongDoubleUnitConverter extends AbstractNumberUnitConverter<Long, Double>
        implements IUnitConverter<Long, Double> {

    /**
     * Creates a new instance with static unit provider holding the given unit
     * 
     * @param defaultUnit The default unit to use for conversion, must not be null
     */
    public LongDoubleUnitConverter(final IUnit defaultUnit) {
        super(defaultUnit);
    }

    /**
     * Creates a new instance with a given unit provider
     * 
     * @param defaultUnit The unit provider to use for conversion, must not be null
     */
    public LongDoubleUnitConverter(final IUnitProvider<Long> unitProvider) {
        super(unitProvider);
    }

    /**
     * Creates a new instance with a given unit provider
     * 
     * @param defaultUnit The unit provider to use for conversion, must not be null
     * @param unconvertibleSubstitude This value will be used if a base value it not convertible to the base value type because it
     *            is out of range. The substitute may be null
     */
    public LongDoubleUnitConverter(final IUnitProvider<Long> unitProvider, final Long unconvertibleSubstitude) {
        super(unitProvider, unconvertibleSubstitude);
    }

    @Override
    protected BigDecimal baseValueToBigDecimal(final Long baseValue) {
        return BigDecimal.valueOf(baseValue);
    }

    @Override
    protected Long bigDecimalToBaseValue(final BigDecimal value) {
        return value.longValue();
    }

    @Override
    protected boolean isConvertibleToBaseValue(final BigDecimal value) {
        return isInsideLongValuesInterval(value);
    }

    @Override
    protected BigDecimal unitValueToBigDecimal(final Double unitValue) {
        return BigDecimal.valueOf(unitValue);
    }

    @Override
    protected Double bigDecimalToUnitValue(final BigDecimal value) {
        return value.doubleValue();
    }

    @Override
    protected boolean isConvertibleToUnitValue(final BigDecimal value) {
        return isInsideDoubleValuesInterval(value);
    }

}
