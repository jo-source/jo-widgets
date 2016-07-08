/*
 * Copyright (c) 2016, bentele, grossmann
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

package org.jowidgets.unit.tools;

import org.jowidgets.unit.api.IUnit;
import org.jowidgets.unit.api.IUnitProvider;
import org.jowidgets.unit.api.IUnitSet;
import org.jowidgets.unit.tools.converter.LongDoubleUnitConverter;
import org.jowidgets.util.Assert;

public class AutoUnitProviderLongDouble implements IUnitProvider<Long> {

    private final IUnitSet unitSet;
    private final IUnit defaultUnit;

    /**
     * Creates a new instance for a given unit set and default unit
     * 
     * @param unitSet The unit set to use
     * @param defaultUnit The default unit
     */
    public AutoUnitProviderLongDouble(final IUnitSet unitSet, final IUnit defaultUnit) {
        Assert.paramNotNull(unitSet, "unitSet");
        Assert.paramNotNull(defaultUnit, "defaultUnit");
        this.unitSet = unitSet;
        this.defaultUnit = defaultUnit;
    }

    @Override
    public final IUnit getUnit(final Long value) {
        if (value != null) {
            for (int i = unitSet.size() - 1; i >= 0; i--) {
                final IUnit unit = unitSet.get(i);
                if (convertToDouble(value, unit) >= 1.0d) {
                    return unit;
                }
            }
        }
        return defaultUnit;
    }

    private double convertToDouble(final long value, final IUnit unit) {
        return new LongDoubleUnitConverter(unit).toUnitValue(value).getValue().doubleValue();
    }

}
