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

import org.jowidgets.validation.IValidator;

/**
 * Allows to convert from base value to a IUnitValue and vice versa.
 * 
 * The base value often can be used to be stored in a database e.g.
 * and the unit value may be used for representation in ui.
 * 
 * @param <BASE_VALUE_TYPE> The type of the base value
 * @param <UNIT_VALUE_TYPE> The value type of the unit values
 */
public interface IUnitConverter<BASE_VALUE_TYPE, UNIT_VALUE_TYPE> {

    /**
     * Converts a given {@link IUnitValue} to a base value
     * 
     * @param value The unit value to convert, may be null
     * 
     * @return The converted value, may be null
     */
    BASE_VALUE_TYPE toBaseValue(IUnitValue<UNIT_VALUE_TYPE> value);

    /**
     * Converts a given base value to a unit value.
     * 
     * The resulting unit will be chosen by the converter and may differ depending on the given base value.
     * 
     * @param value The value to convert, may be null
     * 
     * @return The converted value, may be null
     */
    IUnitValue<UNIT_VALUE_TYPE> toUnitValue(BASE_VALUE_TYPE value);

    /**
     * Gets a validator to validate IUnitValues
     * 
     * @return The validator, may be null if no validation is supported
     */
    IValidator<IUnitValue<UNIT_VALUE_TYPE>> getValidator();

}
