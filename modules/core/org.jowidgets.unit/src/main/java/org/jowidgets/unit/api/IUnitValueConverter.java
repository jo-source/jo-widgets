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

package org.jowidgets.unit.api;

/**
 * Allows to converts base values to a defined {@link IUnit} or to convert a {@link IUnitValue} to a {@link IUnitValue}
 * with different {@link IUnit}.
 * 
 * @param <BASE_VALUE_TYPE> The type of the base value
 * @param <UNIT_VALUE_TYPE> The value type of the unit values
 */
public interface IUnitValueConverter<BASE_VALUE_TYPE, UNIT_VALUE_TYPE> extends IUnitConverter<BASE_VALUE_TYPE, UNIT_VALUE_TYPE> {

    /**
     * Converts a base value to a unit value with a given unit
     * 
     * @param baseValue The base value, may be null
     * @param resultUnit The unit of the result, must not be null
     * 
     * @return The converted value, may be null
     */
    IUnitValue<UNIT_VALUE_TYPE> toUnitValue(BASE_VALUE_TYPE baseValue, IUnit resultUnit);

    /**
     * Converts a unit value to a a unit value with a given unit.
     * 
     * If the unit of the given value is equal with the expected result value, the given value
     * will be returned without any conversion
     * 
     * @param unitValue The source value, may be null
     * @param resultUnit The unit of the result, must not be null
     *
     * @return The converted value, may be null
     */
    IUnitValue<UNIT_VALUE_TYPE> convert(IUnitValue<UNIT_VALUE_TYPE> unitValue, IUnit resultUnit);

}
