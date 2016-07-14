/*
 * Copyright (c) 2011 innoSysTec (R) GmbH, Germany. All rights reserved.
 * Original Author: grossmann, bentele
 * Creation Date: 12.7.2016
 */

package org.jowidgets.tools.unit;

import org.jowidgets.api.convert.IConverter;
import org.jowidgets.unit.api.IUnit;
import org.jowidgets.unit.api.IUnitProvider;
import org.jowidgets.unit.api.IUnitSet;
import org.jowidgets.unit.tools.converter.LongDoubleUnitConverter;
import org.jowidgets.util.Assert;

public class AutoUnitProviderLongDouble implements IUnitProvider<Long> {

    private final IUnitSet unitSet;
    private final IUnit defaultUnit;
    private final double minValueForUnit;
    private final IConverter<Double> doubleConverter;

    /**
     * Creates a new instance
     * 
     * @param unitSet The unit set to use
     * @param defaultUnit The default unit
     * @param minValueForUnit The minimal value to accept for a unit
     * @param doubleConverter The converter that will be used for double converting. If set, the converted double value will be
     *            checked with this converter for loss by rounding. If so, the next lower unit will be used. If set to null, no
     *            rounding check will be made.
     */
    public AutoUnitProviderLongDouble(
        final IUnitSet unitSet,
        final IUnit defaultUnit,
        final double minValueForUnit,
        final IConverter<Double> doubleConverter) {
        Assert.paramNotNull(unitSet, "unitSet");
        Assert.paramNotNull(defaultUnit, "defaultUnit");
        this.unitSet = unitSet;
        this.defaultUnit = defaultUnit;
        this.minValueForUnit = minValueForUnit;
        this.doubleConverter = doubleConverter;
    }

    @Override
    public final IUnit getUnit(final Long value) {
        if (value != null) {
            for (int i = unitSet.size() - 1; i >= 0; i--) {
                final IUnit unit = unitSet.get(i);
                if (isUnitAppropriateForValue(unit, value.longValue())) {
                    return unit;
                }
            }
        }
        return defaultUnit;
    }

    private boolean isUnitAppropriateForValue(final IUnit unit, final long value) {
        final double convertedForUnit = convertToDoubleForUnit(value, unit);
        if (convertedForUnit < minValueForUnit) {
            return false;
        }
        else if (doubleConverter != null) {
            final String convertedString = doubleConverter.convertToString(Double.valueOf(convertedForUnit));
            final Double convertedFromString = doubleConverter.convertToObject(convertedString);
            return Double.valueOf(convertedForUnit).equals(convertedFromString);
        }
        else {
            return true;
        }
    }

    private double convertToDoubleForUnit(final long value, final IUnit unit) {
        return new LongDoubleUnitConverter(unit).toUnitValue(Long.valueOf(value)).getValue().doubleValue();
    }

}
