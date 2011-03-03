/*
 * Copyright (c) 2010, Michael Grossmann
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
package org.jowidgets.common.widgets.builder;

import org.jowidgets.common.types.Border;
import org.jowidgets.common.types.Orientation;
import org.jowidgets.common.types.SplitResizePolicy;
import org.jowidgets.common.widgets.builder.convenience.ISplitCompositeSetupConvenience;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;

public interface ISplitCompositeSetupBuilderCommon<INSTANCE_TYPE extends ISplitCompositeSetupBuilderCommon<?>> extends
		IComponentSetupBuilderCommon<INSTANCE_TYPE>,
		ISplitCompositeSetupConvenience<INSTANCE_TYPE> {

	INSTANCE_TYPE setOrientation(final Orientation orientation);

	INSTANCE_TYPE setFirstBorder(Border border);

	INSTANCE_TYPE setSecondBorder(Border border);

	INSTANCE_TYPE setFirstLayout(ILayoutDescriptor layoutDescriptor);

	INSTANCE_TYPE setSecondLayout(ILayoutDescriptor layoutDescriptor);

	INSTANCE_TYPE setResizePolicy(SplitResizePolicy resizePolicy);

	/**
	 * Sets the dividers size
	 * 
	 * @param size The size in pixel
	 * @return An instance of this builder
	 */
	INSTANCE_TYPE setDividerSize(int size);

	/**
	 * Sets the weight of the divider.
	 * 
	 * @param weight The weight to set. Value must be between 0.0 and 1.0.
	 * @return An instance of this builder
	 */
	INSTANCE_TYPE setWeight(double weight);

}
