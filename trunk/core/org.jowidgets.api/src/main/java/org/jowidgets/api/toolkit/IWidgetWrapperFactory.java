/*
 * Copyright (c) 2010, grossmann
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

package org.jowidgets.api.toolkit;

import org.jowidgets.common.widgets.IContainerWidgetCommon;
import org.jowidgets.common.widgets.IFrameWidgetCommon;

public interface IWidgetWrapperFactory {

	/**
	 * Tests if the ui reference could be converted / wrapped to an FrameWidget
	 * 
	 * @param uiReference
	 * @return True if convertible, false otherwise
	 */
	boolean isConvertibleToFrame(final Object uiReference);

	/**
	 * Creates a FrameWidget from an ui-platform specific ui-reference
	 * 
	 * @param uiReference The ui-platform specific object that could be wrapped to an
	 *            FrameWidget (e.g. Window for Swing, Shell for swt).
	 * @return The created FrameWidget
	 * @throws IllegalArgumentException If the ui-reference could not be wrapped to an FrameWidget.
	 */
	IFrameWidgetCommon createFrameWidget(final Object uiReference);

	/**
	 * Tests if the ui reference could be converted / wrapped to an ContainerWidget
	 * 
	 * @param uiReference
	 * @return True if convertible, false otherwise
	 */
	boolean isConvertibleToContainer(final Object uiReference);

	/**
	 * Creates a ContainerWidget from an ui-platform specific ui-reference
	 * 
	 * @param uiReference The ui-platform specific object that could be wrapped to an
	 *            ContainerWidget (e.g. Container for Swing, Composite for swt).
	 * @return The created ContainerWidget
	 * @throws IllegalArgumentException If the ui-reference could not be wrapped to an ContainerWidget.
	 */
	IContainerWidgetCommon createContainerWidget(final Object uiReference);

}
