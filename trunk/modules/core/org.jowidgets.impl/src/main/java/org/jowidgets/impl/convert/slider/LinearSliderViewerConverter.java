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
import org.jowidgets.util.IConverter;

final class LinearSliderViewerConverter<VALUE_TYPE extends Number> implements ISliderViewerConverter<VALUE_TYPE> {

    private final VALUE_TYPE minValue;
    private final VALUE_TYPE maxValue;

    private final IConverter<Double, VALUE_TYPE> converter;

    private final double scale;
    private final double size;

    @SuppressWarnings("unchecked")
    LinearSliderViewerConverter(final VALUE_TYPE minValue, final VALUE_TYPE maxValue) {
        Assert.paramNotNull(maxValue, "maxValue");

        if (minValue != null) {
            Assert.paramLess(minValue.doubleValue(), maxValue.doubleValue(), "minValue", "maxValue");
        }
        else {
            Assert.paramGreater(maxValue.doubleValue(), 0.0d, "maxValue", "minValue(default=0)");
        }

        this.minValue = minValue;
        this.maxValue = maxValue;

        final Class<?> type = maxValue.getClass();

        if (Double.class.isAssignableFrom(type)) {
            converter = (IConverter<Double, VALUE_TYPE>) new IConverter<Double, Double>() {
                @Override
                public Double convert(final Double source) {
                    return source;
                }
            };
        }
        else if (Float.class.isAssignableFrom(type)) {
            converter = (IConverter<Double, VALUE_TYPE>) new IConverter<Double, Float>() {
                @Override
                public Float convert(final Double source) {
                    return Float.valueOf(source.floatValue());
                }
            };
        }
        else if (Integer.class.isAssignableFrom(type)) {
            converter = (IConverter<Double, VALUE_TYPE>) new IConverter<Double, Integer>() {
                @Override
                public Integer convert(final Double source) {
                    return Integer.valueOf(source.intValue());
                }
            };
        }
        else if (Long.class.isAssignableFrom(type)) {
            converter = (IConverter<Double, VALUE_TYPE>) new IConverter<Double, Long>() {
                @Override
                public Long convert(final Double source) {
                    return Long.valueOf(source.longValue());
                }
            };
        }
        else {
            throw new IllegalArgumentException("Only Double, Float, Integer, Long is supported. Feel free to support more types");
        }

        Assert.paramLessOrEqual(getMin(), getMax(), "minValue", "maxValue");

        this.size = getMax() - getMin();
        this.scale = 1.0d / size;
    }

    @Override
    public int getSliderValue(final int sliderMin, final int sliderMax, final VALUE_TYPE modelValue) {
        if (modelValue != null) {
            return (int) (sliderMin + ((sliderMax - sliderMin) * (scale * (modelValue.doubleValue() - getMin()))));
        }
        else {
            return sliderMin;
        }
    }

    @Override
    public VALUE_TYPE getModelValue(final int sliderMin, final int sliderMax, final int sliderValue) {
        return createValue(getMin() + size * (((1.0d / (sliderMax - sliderMin)) * (sliderValue - sliderMin))));
    }

    private VALUE_TYPE createValue(final double value) {
        return converter.convert(value);
    }

    private double getMin() {
        return minValue != null ? minValue.doubleValue() : 0.0d;
    }

    private double getMax() {
        return maxValue.doubleValue();
    }

}
