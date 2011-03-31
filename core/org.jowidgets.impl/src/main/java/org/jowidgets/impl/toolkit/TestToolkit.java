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

package org.jowidgets.impl.toolkit;

import java.util.List;

import org.jowidgets.api.command.IActionBuilderFactory;
import org.jowidgets.api.convert.IConverterProvider;
import org.jowidgets.api.model.IModelFactoryProvider;
import org.jowidgets.api.threads.IUiThreadAccess;
import org.jowidgets.api.toolkit.IMessagePane;
import org.jowidgets.api.toolkit.IQuestionPane;
import org.jowidgets.api.toolkit.IWidgetWrapperFactory;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.utils.IWidgetUtils;
import org.jowidgets.api.widgets.IComponent;
import org.jowidgets.api.widgets.IWindow;
import org.jowidgets.api.widgets.content.IInputContentCreatorFactory;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.application.IApplicationRunner;
import org.jowidgets.common.image.IImageRegistry;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.impl.widgets.basic.blueprint.BasicSimpleTestBluePrintFactory;
import org.jowidgets.test.api.toolkit.ITestToolkit;
import org.jowidgets.test.api.widgets.IFrameUi;
import org.jowidgets.test.api.widgets.blueprint.factory.IBasicSimpleTestBluePrintFactory;
import org.jowidgets.test.api.widgets.descriptor.IFrameDescriptorUi;
import org.jowidgets.tools.controler.WindowAdapter;

public final class TestToolkit implements ITestToolkit {

	private final IBasicSimpleTestBluePrintFactory bpf;

	public TestToolkit() {
		bpf = new BasicSimpleTestBluePrintFactory();
	}

	@Override
	public IBasicSimpleTestBluePrintFactory getBluePrintFactory() {
		return bpf;
	}

	@Override
	public IGenericWidgetFactory getWidgetFactory() {
		return Toolkit.getWidgetFactory();
	}

	@Override
	public IImageRegistry getImageRegistry() {
		return Toolkit.getImageRegistry();
	}

	@Override
	public IMessagePane getMessagePane() {
		return Toolkit.getMessagePane();
	}

	@Override
	public IQuestionPane getQuestionPane() {
		return Toolkit.getQuestionPane();
	}

	@Override
	public IWidgetWrapperFactory getWidgetWrapperFactory() {
		return Toolkit.getWidgetWrapperFactory();
	}

	@Override
	public IInputContentCreatorFactory getInputContentCreatorFactory() {
		return Toolkit.getInputContentCreatorFactory();
	}

	@Override
	public IConverterProvider getConverterProvider() {
		return Toolkit.getConverterProvider();
	}

	@Override
	public IActionBuilderFactory getActionBuilderFactory() {
		return Toolkit.getActionBuilderFactory();
	}

	@Override
	public IModelFactoryProvider getModelFactoryProvider() {
		return Toolkit.getModelFactoryProvider();
	}

	@Override
	public IApplicationRunner getApplicationRunner() {
		return Toolkit.getApplicationRunner();
	}

	@Override
	public IUiThreadAccess getUiThreadAccess() {
		return Toolkit.getUiThreadAccess();
	}

	@Override
	public IWidgetUtils getWidgetUtils() {
		return Toolkit.getWidgetUtils();
	}

	@Override
	public IWindow getActiveWindow() {
		return Toolkit.getActiveWindow();
	}

	@Override
	public List<IWindow> getAllWindows() {
		return Toolkit.getAllWindows();
	}

	@Override
	public Position toScreen(final Position localPosition, final IComponent component) {
		return Toolkit.toScreen(localPosition, component);
	}

	@Override
	public Position toLocal(final Position screenPosition, final IComponent component) {
		return Toolkit.toLocal(screenPosition, component);
	}

	@Override
	public IFrameUi createRootFrame(final IFrameDescriptorUi descriptor) {
		return Toolkit.getWidgetFactory().create(descriptor);
	}

	@Override
	public IFrameUi createRootFrame(final IFrameDescriptorUi descriptor, final IApplicationLifecycle lifecycle) {
		final IFrameUi result = Toolkit.getWidgetFactory().create(descriptor);
		result.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosed() {
				lifecycle.finish();
			}

		});
		return result;
	}
}
