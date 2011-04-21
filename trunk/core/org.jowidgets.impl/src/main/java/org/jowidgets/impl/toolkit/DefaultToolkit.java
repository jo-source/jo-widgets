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

package org.jowidgets.impl.toolkit;

import java.util.List;

import org.jowidgets.api.command.IActionBuilderFactory;
import org.jowidgets.api.convert.IConverterProvider;
import org.jowidgets.api.image.Icons;
import org.jowidgets.api.image.IconsSmall;
import org.jowidgets.api.layout.ILayoutFactoryProvider;
import org.jowidgets.api.model.IModelFactoryProvider;
import org.jowidgets.api.threads.IUiThreadAccess;
import org.jowidgets.api.toolkit.IMessagePane;
import org.jowidgets.api.toolkit.IQuestionPane;
import org.jowidgets.api.toolkit.ISupportedWidgets;
import org.jowidgets.api.toolkit.IToolkit;
import org.jowidgets.api.toolkit.IWidgetWrapperFactory;
import org.jowidgets.api.utils.IWidgetUtils;
import org.jowidgets.api.widgets.IComponent;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IWindow;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.api.widgets.content.IInputContentCreatorFactory;
import org.jowidgets.api.widgets.descriptor.IFrameDescriptor;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.application.IApplicationRunner;
import org.jowidgets.common.image.IImageRegistry;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.impl.application.ApplicationRunner;
import org.jowidgets.impl.command.ActionBuilderFactory;
import org.jowidgets.impl.content.InputContentCreatorFactory;
import org.jowidgets.impl.convert.DefaultConverterProvider;
import org.jowidgets.impl.image.DefaultIconsRegisterService;
import org.jowidgets.impl.layout.LayoutFactoryProvider;
import org.jowidgets.impl.model.ModelFactoryProvider;
import org.jowidgets.impl.threads.UiThreadAccess;
import org.jowidgets.impl.utils.WidgetUtils;
import org.jowidgets.impl.widgets.composed.blueprint.BluePrintFactory;
import org.jowidgets.impl.widgets.composed.factory.GenericWidgetFactory;
import org.jowidgets.spi.IWidgetsServiceProvider;
import org.jowidgets.spi.image.IImageHandleFactorySpi;
import org.jowidgets.tools.controler.WindowAdapter;
import org.jowidgets.util.Assert;

public class DefaultToolkit implements IToolkit {

	private final IWidgetsServiceProvider widgetsServiceProvider;
	private final IGenericWidgetFactory genericWidgetFactory;
	private final ILayoutFactoryProvider layoutFactoryProvider;
	private final IWidgetWrapperFactory widgetWrapperFactory;
	private final IBluePrintFactory bluePrintFactory;
	private ISupportedWidgets supportedWidgets;
	private final IActionBuilderFactory actionBuilderFactory;
	private final IModelFactoryProvider modelFactoryProvider;
	private final IConverterProvider converterProvider;
	private final IInputContentCreatorFactory inputContentCreatorFactory;
	private final WindowProvider windowProvider;
	private final IMessagePane messagePane;
	private final IQuestionPane questionPane;
	private final IWidgetUtils widgetUtils;

	private IUiThreadAccess uiThreadAccess;
	private IApplicationRunner applicationRunner;

	public DefaultToolkit(final IWidgetsServiceProvider toolkitSpi) {
		Assert.paramNotNull(toolkitSpi, "toolkitSpi");
		this.widgetsServiceProvider = toolkitSpi;
		this.genericWidgetFactory = new GenericWidgetFactory(toolkitSpi);
		this.widgetWrapperFactory = new DefaultWidgetWrapperFactory(genericWidgetFactory, toolkitSpi.getWidgetFactory());
		this.layoutFactoryProvider = new LayoutFactoryProvider();
		this.bluePrintFactory = new BluePrintFactory();
		this.actionBuilderFactory = new ActionBuilderFactory();
		this.modelFactoryProvider = new ModelFactoryProvider();
		this.converterProvider = new DefaultConverterProvider();
		this.inputContentCreatorFactory = new InputContentCreatorFactory();
		this.windowProvider = new WindowProvider(genericWidgetFactory, toolkitSpi);
		this.messagePane = new DefaultMessagePane(genericWidgetFactory, bluePrintFactory, windowProvider);
		this.questionPane = new DefaultQuestionPane(genericWidgetFactory, bluePrintFactory, windowProvider);
		this.widgetUtils = new WidgetUtils();

		final IImageRegistry imageRegistry = toolkitSpi.getImageRegistry();
		final IImageHandleFactorySpi imageHandleFactory = toolkitSpi.getImageHandleFactory();

		final DefaultIconsRegisterService registerService = new DefaultIconsRegisterService(imageRegistry, imageHandleFactory);
		registerService.registerImages();

		imageRegistry.registerImageConstant(Icons.INFO, imageHandleFactory.infoIcon());
		imageRegistry.registerImageConstant(Icons.QUESTION, imageHandleFactory.questionIcon());
		imageRegistry.registerImageConstant(Icons.WARNING, imageHandleFactory.warningIcon());
		imageRegistry.registerImageConstant(Icons.ERROR, imageHandleFactory.errorIcon());

		imageRegistry.registerImageConstant(IconsSmall.INFO, imageHandleFactory.createImageHandle(Icons.INFO, 16, 16));
		imageRegistry.registerImageConstant(IconsSmall.QUESTION, imageHandleFactory.createImageHandle(Icons.QUESTION, 16, 16));
		imageRegistry.registerImageConstant(IconsSmall.WARNING, imageHandleFactory.createImageHandle(Icons.WARNING, 16, 16));
		imageRegistry.registerImageConstant(IconsSmall.ERROR, imageHandleFactory.createImageHandle(Icons.ERROR, 16, 16));
	}

	@Override
	public IImageRegistry getImageRegistry() {
		return widgetsServiceProvider.getImageRegistry();
	}

	@Override
	public IGenericWidgetFactory getWidgetFactory() {
		return genericWidgetFactory;
	}

	@Override
	public IWidgetWrapperFactory getWidgetWrapperFactory() {
		return widgetWrapperFactory;
	}

	@Override
	public ILayoutFactoryProvider getLayoutFactoryProvider() {
		return layoutFactoryProvider;
	}

	@Override
	public IBluePrintFactory getBluePrintFactory() {
		return bluePrintFactory;
	}

	@Override
	public ISupportedWidgets getSupportedWidgets() {
		if (supportedWidgets == null) {
			supportedWidgets = new SupportedWidgets(widgetsServiceProvider.getOptionalWidgetFactory());
		}
		return supportedWidgets;
	}

	@Override
	public IActionBuilderFactory getActionBuilderFactory() {
		return actionBuilderFactory;
	}

	@Override
	public IModelFactoryProvider getModelFactoryProvider() {
		return modelFactoryProvider;
	}

	@Override
	public IInputContentCreatorFactory getInputContentCreatorFactory() {
		return inputContentCreatorFactory;
	}

	@Override
	public IConverterProvider getConverterProvider() {
		return converterProvider;
	}

	@Override
	public synchronized IUiThreadAccess getUiThreadAccess() {
		if (uiThreadAccess == null) {
			uiThreadAccess = new UiThreadAccess(widgetsServiceProvider);
		}
		return uiThreadAccess;
	}

	@Override
	public synchronized IApplicationRunner getApplicationRunner() {
		if (applicationRunner == null) {
			applicationRunner = new ApplicationRunner(widgetsServiceProvider.createApplicationRunner());
		}
		return applicationRunner;
	}

	@Override
	public IWindow getActiveWindow() {
		return windowProvider.getActiveWindow();
	}

	@Override
	public List<IWindow> getAllWindows() {
		return windowProvider.getAllWindows();
	}

	@Override
	public IMessagePane getMessagePane() {
		return messagePane;
	}

	@Override
	public IQuestionPane getQuestionPane() {
		return questionPane;
	}

	@Override
	public IWidgetUtils getWidgetUtils() {
		return widgetUtils;
	}

	@Override
	public IFrame createRootFrame(final IFrameDescriptor setup) {
		return genericWidgetFactory.create(setup);
	}

	@Override
	public IFrame createRootFrame(final IFrameDescriptor descriptor, final IApplicationLifecycle lifecycle) {
		final IFrame result = genericWidgetFactory.create(descriptor);
		result.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosed() {
				lifecycle.finish();
			}

		});
		return result;
	}

	@Override
	public Position toScreen(final Position localPosition, final IComponent component) {
		return widgetsServiceProvider.toScreen(localPosition, component);
	}

	@Override
	public Position toLocal(final Position screenPosition, final IComponent component) {
		return widgetsServiceProvider.toLocal(screenPosition, component);
	}

}
