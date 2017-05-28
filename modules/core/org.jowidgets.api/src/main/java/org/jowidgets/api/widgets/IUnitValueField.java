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

package org.jowidgets.api.widgets;

import org.jowidgets.common.widgets.ISelectable;
import org.jowidgets.unit.api.IUnit;
import org.jowidgets.unit.api.IUnitValue;

public interface IUnitValueField<BASE_VALUE_TYPE, UNIT_VALUE_TYPE> extends IInputControl<BASE_VALUE_TYPE>, ISelectable {

    /**
     * Gets the current unit value, may be null
     * 
     * @return The current unit value
     */
    IUnitValue<UNIT_VALUE_TYPE> getUnitValue();

    /**
     * Sets a new unit value
     * 
     * @param unitValue The value to set, may be null
     */
    void setUnitValue(IUnitValue<UNIT_VALUE_TYPE> unitValue);

    /**
     * Gets the text from the underlying text field
     * 
     * @return The text of the underlying text field, may be null
     */
    String getText();

    /**
     * Sets the text into the underlying text field
     * 
     * @param text The text to set, may be null
     */
    void setText(String text);

    /**
     * Gets the currently selected unit
     * 
     * @return The selected unit, may be null
     */
    IUnit getUnit();

    /**
     * Sets the unit
     * 
     * @param unit The unit to set, may be null
     */
    void setUnit(IUnit unit);

}
