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
import org.jowidgets.unit.tools.StaticUnitProvider;
import org.jowidgets.unit.tools.UnitValue;
import org.jowidgets.util.Assert;

public final class DoubleUnitConverter extends AbstractNumberUnitConverter<Double, Double>
        implements IUnitConverter<Double, Double> {

    private final IUnitProvider<Double> unitProvider;

    public DoubleUnitConverter(final IUnit defaultUnit) {
        this(new StaticUnitProvider<Double>(defaultUnit));
    }

    public DoubleUnitConverter(final IUnitProvider<Double> unitProvider) {
        Assert.paramNotNull(unitProvider, "name");
        this.unitProvider = unitProvider;
    }

    //    @Override
    //    public Double toBaseValue(final IUnitValue<Double> unitValue) {
    //        if (unitValue != null) {
    //            return Double.valueOf(unitValue.getValue() * unitValue.getUnit().getConversionFactor());
    //        }
    //        return null;
    //    }
    //
    //    @Override
    //    public IUnitValue<Double> toUnitValue(final Double baseValue) {
    //        if (baseValue != null) {
    //            final IUnit unit = unitProvider.getUnit(baseValue);
    //            final Double unitValue = baseValue.doubleValue() / unit.getConversionFactor();
    //            return new UnitValue<Double>(unitValue, unit);
    //        }
    //        return null;
    //    }

    @Override
    public Double toBaseValue(final IUnitValue<Double> unitValue) {
        if (unitValue != null) {
            final BigDecimal value = BigDecimal.valueOf(unitValue.getValue());
            final BigDecimal baseValue = value.multiply(BigDecimal.valueOf(unitValue.getUnit().getConversionFactor()));
            return Double.valueOf(baseValue.doubleValue());
        }
        return null;
    }

    @Override
    public IUnitValue<Double> toUnitValue(final Double baseValue) {
        if (baseValue != null) {
            final IUnit unit = unitProvider.getUnit(baseValue);
            final BigDecimal baseValueDecimal = BigDecimal.valueOf(baseValue);
            final BigDecimal unitValueDecimal = baseValueDecimal.divide(BigDecimal.valueOf(unit.getConversionFactor()));
            final Double unitValue = unitValueDecimal.doubleValue();
            return new UnitValue<Double>(unitValue, unit);
        }
        return null;
    }

}
