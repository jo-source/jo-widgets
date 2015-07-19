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

package org.jowidgets.api.convert;

public interface ILinearSliderConverterBuilder<VALUE_TYPE extends Number> {

    /**
     * Sets the minimal model value
     * 
     * @param minValue The minimal model value
     * 
     * @return This builder
     */
    ILinearSliderConverterBuilder<VALUE_TYPE> setMinValue(VALUE_TYPE minValue);

    /**
     * Sets the maximal model value
     * 
     * @param maxValue The maximal model value
     * 
     * @return This builder
     */
    ILinearSliderConverterBuilder<VALUE_TYPE> setMaxValue(VALUE_TYPE maxValue);

    /**
     * Sets a pivot value. With that, the slider can be separated into two separate zones.
     * Example: Modeling a scale factor for magnifying and demagnifying with factors from 1/10 to 10x.
     * Use min=0.1d, max=10.0d, pivot=(0.5d, 1.0d). Then the lower part of the slider has the (linear)
     * range from 0.1 to 1.0 and the upper part has the range from 1.0d to 10.0d.
     * 
     * @param ratio The ration of the pivot where 0.0d is at the sliders min position, 0.5d is in the middle and 1.0d is at the
     *            max position
     * @param value The value at the ratio position. This value must be between min and max
     * 
     * @return This builder
     */
    ILinearSliderConverterBuilder<VALUE_TYPE> setPivotValue(double ratio, VALUE_TYPE value);

    /**
     * Sets a pivot value with ratio 0.5d
     * 
     * @param value The value at the ratio position. This value must be between min and max
     * 
     * @return This builder
     */
    ILinearSliderConverterBuilder<VALUE_TYPE> setPivotValue(VALUE_TYPE value);

    /**
     * @return A newly created converter
     */
    ISliderViewerConverter<VALUE_TYPE> build();

}
