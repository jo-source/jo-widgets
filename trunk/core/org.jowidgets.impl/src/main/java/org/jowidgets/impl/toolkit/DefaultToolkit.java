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

import org.jowidgets.api.convert.IConverterProvider;
import org.jowidgets.api.image.Icons;
import org.jowidgets.api.toolkit.IMessagePane;
import org.jowidgets.api.toolkit.IQuestionPane;
import org.jowidgets.api.toolkit.IToolkit;
import org.jowidgets.api.toolkit.IWidgetWrapperFactory;
import org.jowidgets.api.utils.IWidgetUtils;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.application.IApplicationRunner;
import org.jowidgets.common.image.IImageRegistry;
import org.jowidgets.common.threads.IUiThreadAccess;
import org.jowidgets.common.widgets.IWindowWidgetCommon;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.impl.application.ApplicationRunner;
import org.jowidgets.impl.convert.DefaultConverterProvider;
import org.jowidgets.impl.image.DefaultIconsRegisterService;
import org.jowidgets.impl.utils.WidgetUtils;
import org.jowidgets.impl.widgets.composed.blueprint.BluePrintFactory;
import org.jowidgets.impl.widgets.composed.factory.GenericWidgetFactory;
import org.jowidgets.spi.IWidgetsServiceProvider;
import org.jowidgets.spi.image.IImageHandleFactorySpi;
import org.jowidgets.util.Assert;

public class DefaultToolkit implements IToolkit {

	private final IWidgetsServiceProvider widgetsServiceProvider;
	private final IGenericWidgetFactory genericWidgetFactory;
	private final IWidgetWrapperFactory widgetWrapperFactory;
	private final IBluePrintFactory bluePrintFactory;
	private final IConverterProvider converterProvider;
	private final ActiveWindowProvider activeWindowProvider;
	private final IMessagePane messagePane;
	private final IQuestionPane questionPane;
	private final IWidgetUtils widgetUtils;

	private IUiThreadAccess uiThreadAccess;
	private IApplicationRunner applicationRunner;

	public DefaultToolkit(final IWidgetsServiceProvider toolkitSpi) {
		Assert.paramNotNull(toolkitSpi, "toolkitSpi");
		this.widgetsServiceProvider = toolkitSpi;
		this.genericWidgetFactory = new GenericWidgetFactory(toolkitSpi.getWidgetFactory());
		this.widgetWrapperFactory = new DefaultWidgetWrapperFactory(genericWidgetFactory, toolkitSpi.getWidgetFactory());
		this.bluePrintFactory = new BluePrintFactory();
		this.converterProvider = new DefaultConverterProvider();
		this.activeWindowProvider = new ActiveWindowProvider(genericWidgetFactory, toolkitSpi);
		this.messagePane = new DefaultMessagePane(genericWidgetFactory, bluePrintFactory, activeWindowProvider);
		this.questionPane = new DefaultQuestionPane(genericWidgetFactory, bluePrintFactory, activeWindowProvider);
		this.widgetUtils = new WidgetUtils();

		final IImageRegistry imageRegistry = toolkitSpi.getImageRegistry();
		final IImageHandleFactorySpi imageHandleFactory = toolkitSpi.getImageHandleFactory();

		final DefaultIconsRegisterService registerService = new DefaultIconsRegisterService(imageRegistry, imageHandleFactory);
		registerService.registerImages();

		imageRegistry.registerImageConstant(Icons.INFO, imageHandleFactory.infoIcon());
		imageRegistry.registerImageConstant(Icons.QUESTION, imageHandleFactory.questionIcon());
		imageRegistry.registerImageConstant(Icons.WARNING, imageHandleFactory.warningIcon());
		imageRegistry.registerImageConstant(Icons.ERROR, imageHandleFactory.errorIcon());
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
	public IBluePrintFactory getBluePrintFactory() {
		return bluePrintFactory;
	}

	@Override
	public IConverterProvider getConverterProvider() {
		return converterProvider;
	}

	@Override
	public IUiThreadAccess getUiThreadAccess() {
		if (uiThreadAccess == null) {
			uiThreadAccess = widgetsServiceProvider.createUiThreadAccess();
		}
		return uiThreadAccess;
	}

	@Override
	public IApplicationRunner getApplicationRunner() {
		if (applicationRunner == null) {
			applicationRunner = new ApplicationRunner(widgetsServiceProvider.createApplicationRunner());
		}
		return applicationRunner;
	}

	@Override
	public IWindowWidgetCommon getActiveWindow() {
		return activeWindowProvider.getActiveWindow();
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

}
