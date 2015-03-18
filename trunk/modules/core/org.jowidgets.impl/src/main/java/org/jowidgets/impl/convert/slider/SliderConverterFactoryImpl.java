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
import org.jowidgets.api.convert.ISliderConverterFactory;
import org.jowidgets.api.convert.ISliderViewerConverter;

public final class SliderConverterFactoryImpl implements ISliderConverterFactory {

    private static final SliderConverterFactoryImpl INSTANCE = new SliderConverterFactoryImpl();

    private SliderConverterFactoryImpl() {}

    public static ISliderConverterFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public <VALUE_TYPE extends Number> ILinearSliderConverterBuilder<VALUE_TYPE> linearConverterBuilder() {
        return new LinearSliderConverterBuilderImpl<VALUE_TYPE>();
    }

    @Override
    public <VALUE_TYPE extends Number> ISliderViewerConverter<VALUE_TYPE> linearConverter(final VALUE_TYPE max) {
        final ILinearSliderConverterBuilder<VALUE_TYPE> result = linearConverterBuilder();
        return result.setMaxValue(max).build();
    }

    @Override
    public <VALUE_TYPE extends Number> ISliderViewerConverter<VALUE_TYPE> linearConverter(
        final VALUE_TYPE min,
        final VALUE_TYPE max) {
        final ILinearSliderConverterBuilder<VALUE_TYPE> result = linearConverterBuilder();
        return result.setMinValue(min).setMaxValue(max).build();
    }

    @Override
    public ISliderViewerConverter<Double> linearConverter() {
        return linearConverter(1.0d);
    }

    @Override
    public ISliderViewerConverter<Double> linearConverter(final double max) {
        return linearConverter(Double.valueOf(max));
    }

    @Override
    public ISliderViewerConverter<Double> linearConverter(final double min, final double max) {
        return linearConverter(Double.valueOf(min), Double.valueOf(max));
    }

    @Override
    public ISliderViewerConverter<Float> linearConverter(final float max) {
        return linearConverter(Float.valueOf(max));
    }

    @Override
    public ISliderViewerConverter<Float> linearConverter(final float min, final float max) {
        return linearConverter(Float.valueOf(min), Float.valueOf(max));
    }

    @Override
    public ISliderViewerConverter<Integer> linearConverter(final int max) {
        return linearConverter(Integer.valueOf(max));
    }

    @Override
    public ISliderViewerConverter<Integer> linearConverter(final int min, final int max) {
        return linearConverter(Integer.valueOf(min), Integer.valueOf(max));
    }

    @Override
    public ISliderViewerConverter<Long> linearConverter(final long max) {
        return linearConverter(Long.valueOf(max));
    }

    @Override
    public ISliderViewerConverter<Long> linearConverter(final long min, final long max) {
        return linearConverter(Long.valueOf(min), Long.valueOf(max));
    }

}
