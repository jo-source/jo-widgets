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

import org.jowidgets.api.convert.IConverterProvider;
import org.jowidgets.api.utils.IWidgetUtils;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.api.widgets.descriptor.IFrameDescriptor;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.application.IApplicationRunner;
import org.jowidgets.common.image.IImageRegistry;
import org.jowidgets.common.threads.IUiThreadAccess;
import org.jowidgets.common.widgets.IWindowCommon;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;

public interface IToolkit {

	IImageRegistry getImageRegistry();

	IMessagePane getMessagePane();

	IQuestionPane getQuestionPane();

	IGenericWidgetFactory getWidgetFactory();

	IWidgetWrapperFactory getWidgetWrapperFactory();

	IBluePrintFactory getBluePrintFactory();

	IConverterProvider getConverterProvider();

	IApplicationRunner getApplicationRunner();

	IUiThreadAccess getUiThreadAccess();

	IWidgetUtils getWidgetUtils();

	IWindowCommon getActiveWindow();

	IFrame createRootFrame(IFrameDescriptor descriptor);

	/**
	 * Creates an root frame for an application lifecycle. When the rootFrame will be
	 * closed, the lifecycle will be finished.
	 * 
	 * @param descriptor The frame descriptor
	 * @param lifecycle The lifecycle of the current application
	 * @return the created frame
	 */
	IFrame createRootFrame(IFrameDescriptor descriptor, IApplicationLifecycle lifecycle);

}
