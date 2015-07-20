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

package org.jowidgets.unit.tools;

import java.io.Serializable;

import org.jowidgets.unit.api.IUnit;
import org.jowidgets.unit.api.IUnitValue;
import org.jowidgets.util.Assert;

public final class UnitValue<VALUE_TYPE> implements IUnitValue<VALUE_TYPE>, Serializable {

    private static final long serialVersionUID = 2535546200656003911L;

    private final VALUE_TYPE value;
    private final IUnit unit;

    public UnitValue(final VALUE_TYPE value, final IUnit unit) {
        Assert.paramNotNull(value, "value");
        Assert.paramNotNull(unit, "unit");

        this.value = value;
        this.unit = unit;
    }

    @Override
    public VALUE_TYPE getValue() {
        return value;
    }

    @Override
    public IUnit getUnit() {
        return unit;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((unit == null) ? 0 : unit.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof UnitValue)) {
            return false;
        }
        final IUnitValue<?> other = (IUnitValue<?>) obj;
        if (unit == null) {
            if (other.getUnit() != null) {
                return false;
            }
        }
        else if (!unit.equals(other.getUnit())) {
            return false;
        }
        if (value == null) {
            if (other.getValue() != null) {
                return false;
            }
        }
        else if (!value.equals(other.getValue())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UnitValue [value=" + value + ", unit=" + unit + "]";
    }

}
