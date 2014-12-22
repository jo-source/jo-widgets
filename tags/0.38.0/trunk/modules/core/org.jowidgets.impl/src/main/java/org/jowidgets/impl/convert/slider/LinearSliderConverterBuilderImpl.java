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

package org.jowidgets.impl.convert.slider;

import org.jowidgets.api.convert.ILinearSliderConverterBuilder;
import org.jowidgets.api.convert.ISliderViewerConverter;
import org.jowidgets.util.Assert;

final class LinearSliderConverterBuilderImpl<VALUE_TYPE extends Number> implements ILinearSliderConverterBuilder<VALUE_TYPE> {

	private VALUE_TYPE minValue;
	private VALUE_TYPE maxValue;

	private double pivotRatio;
	private VALUE_TYPE pivot;

	@Override
	public ILinearSliderConverterBuilder<VALUE_TYPE> setMinValue(final VALUE_TYPE minValue) {
		this.minValue = minValue;
		return this;
	}

	@Override
	public ILinearSliderConverterBuilder<VALUE_TYPE> setMaxValue(final VALUE_TYPE maxValue) {
		Assert.paramNotNull(maxValue, "maxValue");
		this.maxValue = maxValue;
		return this;
	}

	@Override
	public ILinearSliderConverterBuilder<VALUE_TYPE> setPivotValue(final double ratio, final VALUE_TYPE value) {
		this.pivot = value;
		this.pivotRatio = ratio;
		return this;
	}

	@Override
	public ILinearSliderConverterBuilder<VALUE_TYPE> setPivotValue(final VALUE_TYPE value) {
		return setPivotValue(0.5d, value);
	}

	@Override
	public ISliderViewerConverter<VALUE_TYPE> build() {
		if (pivot == null) {
			return new LinearSliderViewerConverter<VALUE_TYPE>(minValue, maxValue);
		}
		else {
			return new LinearPivotSliderViewerConverter<VALUE_TYPE>(minValue, maxValue, pivot, pivotRatio);
		}
	}

}
