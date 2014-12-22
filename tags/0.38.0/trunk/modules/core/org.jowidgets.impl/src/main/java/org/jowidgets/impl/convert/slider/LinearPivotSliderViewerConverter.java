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

import org.jowidgets.api.convert.ISliderViewerConverter;
import org.jowidgets.util.Assert;

final class LinearPivotSliderViewerConverter<VALUE_TYPE extends Number> implements ISliderViewerConverter<VALUE_TYPE> {

	private final VALUE_TYPE pivot;
	private final double ratio;
	private final LinearSliderViewerConverter<VALUE_TYPE> lowerConverter;
	private final LinearSliderViewerConverter<VALUE_TYPE> upperConverter;

	LinearPivotSliderViewerConverter(
		final VALUE_TYPE minValue,
		final VALUE_TYPE maxValue,
		final VALUE_TYPE pivot,
		final double ratio) {

		Assert.paramNotNull(maxValue, "maxValue");
		Assert.paramNotNull(pivot, "pivot");

		Assert.paramInBounds(0.0d, 1.0d, ratio, "ratio");

		if (minValue != null) {
			Assert.paramLess(minValue.doubleValue(), maxValue.doubleValue(), "minValue", "maxValue");
			Assert.paramInBounds(minValue.doubleValue(), maxValue.doubleValue(), pivot.doubleValue(), "pivot");
		}
		else {
			Assert.paramInBounds(0.0d, maxValue.doubleValue(), pivot.doubleValue(), "pivot");
			Assert.paramGreater(maxValue.doubleValue(), 0.0d, "maxValue", "minValue(default=0)");
		}

		this.pivot = pivot;
		this.ratio = ratio;

		this.lowerConverter = new LinearSliderViewerConverter<VALUE_TYPE>(minValue, pivot);
		this.upperConverter = new LinearSliderViewerConverter<VALUE_TYPE>(pivot, maxValue);
	}

	@Override
	public VALUE_TYPE getModelValue(final int sliderMin, final int sliderMax, final int sliderValue) {
		final int sliderPivot = (int) (sliderMin + ((sliderMax - sliderMin)) * ratio);
		if (sliderValue < sliderPivot) {
			return lowerConverter.getModelValue(sliderMin, sliderPivot, sliderValue);
		}
		else {
			return upperConverter.getModelValue(sliderPivot, sliderMax, sliderValue);
		}
	}

	@Override
	public int getSliderValue(final int sliderMin, final int sliderMax, final VALUE_TYPE modelValue) {
		if (modelValue != null) {
			final int sliderPivot = (int) (sliderMin + ((sliderMax - sliderMin)) * ratio);
			if (modelValue.doubleValue() < pivot.doubleValue()) {
				return lowerConverter.getSliderValue(sliderMin, sliderPivot, modelValue);
			}
			else {
				return upperConverter.getSliderValue(sliderPivot, sliderMax, modelValue);
			}
		}
		else {
			return sliderMin;
		}
	}
}
