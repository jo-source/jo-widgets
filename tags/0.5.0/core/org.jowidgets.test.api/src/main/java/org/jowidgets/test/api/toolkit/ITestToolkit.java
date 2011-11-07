/*
 * Copyright (c) 2011, Lukas Gross
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

package org.jowidgets.test.api.toolkit;

import java.util.List;

import org.jowidgets.api.command.IActionBuilderFactory;
import org.jowidgets.api.convert.IConverterProvider;
import org.jowidgets.api.model.IModelFactoryProvider;
import org.jowidgets.api.threads.IUiThreadAccess;
import org.jowidgets.api.toolkit.IMessagePane;
import org.jowidgets.api.toolkit.IQuestionPane;
import org.jowidgets.api.toolkit.IWidgetWrapperFactory;
import org.jowidgets.api.utils.IWidgetUtils;
import org.jowidgets.api.widgets.IComponent;
import org.jowidgets.api.widgets.IWindow;
import org.jowidgets.api.widgets.content.IInputContentCreatorFactory;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.application.IApplicationRunner;
import org.jowidgets.common.image.IImageRegistry;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.test.api.widgets.IFrameUi;
import org.jowidgets.test.api.widgets.blueprint.factory.IBasicSimpleTestBluePrintFactory;
import org.jowidgets.test.api.widgets.descriptor.IFrameDescriptorUi;

public interface ITestToolkit {

	IBasicSimpleTestBluePrintFactory getBluePrintFactory();

	IGenericWidgetFactory getWidgetFactory();

	IImageRegistry getImageRegistry();

	IMessagePane getMessagePane();

	IQuestionPane getQuestionPane();

	IWidgetWrapperFactory getWidgetWrapperFactory();

	IInputContentCreatorFactory getInputContentCreatorFactory();

	IConverterProvider getConverterProvider();

	IActionBuilderFactory getActionBuilderFactory();

	IModelFactoryProvider getModelFactoryProvider();

	IApplicationRunner getApplicationRunner();

	IUiThreadAccess getUiThreadAccess();

	IWidgetUtils getWidgetUtils();

	IWindow getActiveWindow();

	List<IWindow> getAllWindows();

	IFrameUi createRootFrame(IFrameDescriptorUi descriptor);

	/**
	 * Creates an root frame for an application lifecycle. When the rootFrame will be
	 * closed, the lifecycle will be finished.
	 * 
	 * @param descriptor The frame descriptor
	 * @param lifecycle The lifecycle of the current application
	 * @return the created frame
	 */
	IFrameUi createRootFrame(IFrameDescriptorUi descriptor, IApplicationLifecycle lifecycle);

	/**
	 * Transforms a local component position to a screen position
	 * 
	 * @param localPosition Local position relative to the component
	 * @param component The component
	 * @return screen position
	 */
	Position toScreen(final Position localPosition, final IComponent component);

	/**
	 * Transforms a screen position to a local component position
	 * 
	 * @param screenPosition Screen position
	 * @param component The component
	 * @return local position relative to the component
	 */
	Position toLocal(final Position screenPosition, final IComponent component);
}
