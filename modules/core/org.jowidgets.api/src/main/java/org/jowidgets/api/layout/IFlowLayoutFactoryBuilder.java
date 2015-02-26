/*
 * Copyright (c) 2011, grossmann
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

package org.jowidgets.api.layout;

import org.jowidgets.common.types.Orientation;
import org.jowidgets.common.widgets.layout.ILayouter;

public interface IFlowLayoutFactoryBuilder {

	/**
	 * Sets the gap between the controls
	 * 
	 * The default gap is 4
	 * 
	 * @param gap The gap to set
	 * 
	 * @return This builder
	 */
	IFlowLayoutFactoryBuilder gap(int gap);

	/**
	 * Sets the orientation of the layout.
	 * 
	 * The default orientation is Orientation.HORIZONTAL
	 * 
	 * @param orientation The orientation to set, never null
	 * 
	 * @return This builder
	 */
	IFlowLayoutFactoryBuilder orientation(Orientation orientation);

	/**
	 * Sets the orientation to Orientation.VERTICAL
	 * 
	 * @return The builder
	 */
	IFlowLayoutFactoryBuilder vertical();

	/**
	 * Sets the orientation to Orientation.HORIZONTAL
	 * 
	 * @return The builder
	 */
	IFlowLayoutFactoryBuilder horizontal();

	/**
	 * Creates a new layout factory for 'FlowLayouts'
	 * 
	 * @return A new layout factory for 'FlowLayouts', never null
	 */
	ILayoutFactory<ILayouter> build();

}
