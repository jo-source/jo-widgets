/*
 * Copyright (c) 2013, Michael Grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jowidgets.api.widgets.blueprint.builder;

import org.jowidgets.api.model.levelmeter.ILevelMeterModel;
import org.jowidgets.common.color.IColorConstant;

public interface ILevelMeterSetupBuilder<INSTANCE_TYPE extends ILevelMeterSetupBuilder<?>> extends
		IComponentSetupBuilder<INSTANCE_TYPE> {

	INSTANCE_TYPE setModel(ILevelMeterModel model);

	/**
	 * Sets the lettering visible or invisible
	 * 
	 * @param visible visibility
	 * @return This builder
	 */
	INSTANCE_TYPE setLetteringVisible(boolean visible);

	/**
	 * Sets the size of the gaps between the colored boxes
	 * 
	 * @param gapSize size of the gaps (in pixels)
	 * @return This builder
	 */
	INSTANCE_TYPE setGapSize(int gapSize);

	/**
	 * Sets the size of the small colored boxes indicating the level
	 * 
	 * @param boxSize size of the boxes (in pixels)
	 * @return This builder
	 */
	INSTANCE_TYPE setBoxSize(int boxSize);

	/**
	 * Sets the foreground color of the low peak range (from 0.0 to highPeakTreshold)
	 * 
	 * @param color The foreground color
	 * @return This builder
	 */
	INSTANCE_TYPE setLowPeakColor(IColorConstant color);

	/**
	 * Sets the foreground color of the high peak range (from highPeakThreshold to clipPeakThreshold)
	 * 
	 * @param color The foreground color
	 * @return This builder
	 */
	INSTANCE_TYPE setHighPeakColor(IColorConstant color);

	/**
	 * Sets the foreground color of the low peak range (from clipPeakThreshold to 1.0)
	 * 
	 * @param color The foreground color
	 * @return This builder
	 */
	INSTANCE_TYPE setClipPeakColor(IColorConstant color);

	/**
	 * Sets the color for boxes that are not filled
	 * 
	 * @param color The base box color
	 * @return This builder
	 */
	INSTANCE_TYPE setBaseBoxColor(IColorConstant color);

	/**
	 * Sets the threshold that defines a high peak
	 * 
	 * @param threshold The threshold to set (0.0 <= highPeakThreshold < clipPeakThreshold)
	 * @return This builder
	 */
	INSTANCE_TYPE setHighPeakThreshold(double threshold);

	/**
	 * Sets the threshold that defines a clip peak
	 * 
	 * @param threshold The threshold to set (highPeakThreshold <= clipPeakThreshold <= 1.0)
	 * @return This builder
	 */
	INSTANCE_TYPE setClipPeakThreshold(double threshold);

}
