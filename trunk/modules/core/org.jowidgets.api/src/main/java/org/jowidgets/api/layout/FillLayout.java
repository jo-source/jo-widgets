/*
 * Copyright (c) 2015, grossmann
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

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.common.widgets.layout.ILayouter;

/**
 * Accessor for 'FillLayout'
 * 
 * 'FillLayout' allows only one visible control in the container when calculation is done.
 * If more than one control is visible, it will be ignored for the layouting.
 * 
 * layout(): The size of the visible control is set to the containers clientAreaSize
 * 
 * getPreferredSize(): Returns the preferred size of the visible control
 * 
 * getMinSize(): Returns the min size of the visible control
 * 
 * getMaxSize(): returns new Dimension(Short.MAX_VALUE, Short.MAX_VALUE)
 */
public final class FillLayout {

	private FillLayout() {}

	/**
	 * Gets a layout factory for an 'FillLayout'
	 * 
	 * @return A layout factory that produces 'FillLayout'
	 */
	public static ILayoutFactory<ILayouter> get() {
		return Toolkit.getLayoutFactoryProvider().fillLayout();
	}

	/**
	 * Gets a builder for an layout factory of an 'FillLayout'
	 * 
	 * @return A builder for an layout factory of an 'FillLayout'
	 */
	public static IFillLayoutFactoryBuilder builder() {
		return Toolkit.getLayoutFactoryProvider().fillLayoutBuilder();
	}

}
