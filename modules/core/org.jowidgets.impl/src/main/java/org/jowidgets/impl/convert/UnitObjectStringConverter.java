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

package org.jowidgets.impl.convert;

import org.jowidgets.api.convert.IObjectStringConverter;
import org.jowidgets.tools.converter.AbstractObjectStringConverter;
import org.jowidgets.unit.api.IUnitConverter;
import org.jowidgets.unit.api.IUnitValue;
import org.jowidgets.util.Assert;

final class UnitObjectStringConverter<BASE_VALUE_TYPE, UNIT_VALUE_TYPE> extends AbstractObjectStringConverter<BASE_VALUE_TYPE> implements
		IObjectStringConverter<BASE_VALUE_TYPE> {

	private final IUnitConverter<BASE_VALUE_TYPE, UNIT_VALUE_TYPE> unitConverter;
	private final IObjectStringConverter<UNIT_VALUE_TYPE> unitValueConverter;

	@SuppressWarnings("unchecked")
	UnitObjectStringConverter(
		final IUnitConverter<BASE_VALUE_TYPE, UNIT_VALUE_TYPE> unitConverter,
		final IObjectStringConverter<? extends UNIT_VALUE_TYPE> unitValueConverter) {
		Assert.paramNotNull(unitConverter, "unitConverter");
		this.unitConverter = unitConverter;
		this.unitValueConverter = (IObjectStringConverter<UNIT_VALUE_TYPE>) unitValueConverter;
	}

	@Override
	public String convertToString(final BASE_VALUE_TYPE value) {
		if (value != null) {
			final IUnitValue<UNIT_VALUE_TYPE> unitValue = unitConverter.toUnitValue(value);
			if (unitValue != null && unitValue.getValue() != null) {
				return unitValueConverter.convertToString(unitValue.getValue()) + " " + unitValue.getUnit().getAbbreviation();
			}
		}
		return null;
	}

}
