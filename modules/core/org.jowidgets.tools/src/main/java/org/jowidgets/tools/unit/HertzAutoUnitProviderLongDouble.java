/*
 * Copyright (c) 2016, grossmann, bentele
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

package org.jowidgets.tools.unit;

import org.jowidgets.api.convert.IConverter;
import org.jowidgets.unit.api.IUnit;
import org.jowidgets.unit.tools.units.HertzUnitSet;

public final class HertzAutoUnitProviderLongDouble extends AutoUnitProviderLongDouble {

    /**
     * Creates a new instance
     * 
     * @param defaultUnit The default unit
     * @param doubleConverter The converter that will be used for double converting. If set, the converted double value will be
     *            checked with this converter for loss by rounding. If so, the next lower unit will be used. If set to null, no
     *            rounding check will be made.
     */
    public HertzAutoUnitProviderLongDouble(final IUnit defaultUnit, final IConverter<Double> doubleConverter) {
        this(defaultUnit, 1.0d, doubleConverter);
    }

    /**
     * Creates a new instance
     * 
     * @param defaultUnit The default unit
     * @param minValueForUnit The minimal value to accept for a unit
     * @param doubleConverter The converter that will be used for double converting. If set, the converted double value will be
     *            checked with this converter for loss by rounding. If so, the next lower unit will be used. If set to null, no
     *            rounding check will be made.
     */
    public HertzAutoUnitProviderLongDouble(
        final IUnit defaultUnit,
        final double minValueForUnit,
        final IConverter<Double> doubleConverter) {
        super(HertzUnitSet.instance(), defaultUnit, minValueForUnit, doubleConverter);
    }

}
