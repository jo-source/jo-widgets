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

package org.jowidgets.spi.impl.swt.common;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jowidgets.common.application.IApplicationRunner;
import org.jowidgets.common.image.IImageRegistry;
import org.jowidgets.common.threads.IUiThreadAccessCommon;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.IComponentCommon;
import org.jowidgets.spi.IOptionalWidgetsFactorySpi;
import org.jowidgets.spi.IWidgetFactorySpi;
import org.jowidgets.spi.IWidgetsServiceProvider;
import org.jowidgets.spi.image.IImageHandleFactorySpi;
import org.jowidgets.spi.impl.swt.common.application.SwtApplicationRunnerFactory;
import org.jowidgets.spi.impl.swt.common.image.SwtImageHandleFactorySpi;
import org.jowidgets.spi.impl.swt.common.image.SwtImageRegistry;
import org.jowidgets.spi.impl.swt.common.threads.SwtUiThreadAccess;
import org.jowidgets.spi.impl.swt.common.util.PositionConvert;
import org.jowidgets.util.Assert;
import org.jowidgets.util.IFactory;

public class SwtWidgetsServiceProvider implements IWidgetsServiceProvider {

	private final Display display;
	private final SwtImageRegistry imageRegistry;
	private final SwtImageHandleFactorySpi imageHandleFactorySpi;
	private final SwtWidgetFactory widgetFactory;
	private final SwtOptionalWidgetsFactory optionalWidgetsFactory;
	private final IFactory<IApplicationRunner> applicationRunnerFactory;

	public SwtWidgetsServiceProvider() {
		this((Display) null);
	}

	public SwtWidgetsServiceProvider(final Display display) {
		this(display, new SwtApplicationRunnerFactory());
	}

	public SwtWidgetsServiceProvider(final IFactory<IApplicationRunner> applicationRunnerFactory) {
		this(null, applicationRunnerFactory);
	}

	public SwtWidgetsServiceProvider(final Display display, final IFactory<IApplicationRunner> applicationRunnerFactory) {
		Assert.paramNotNull(applicationRunnerFactory, "applicationRunnerFactory");
		this.display = display;
		this.applicationRunnerFactory = applicationRunnerFactory;
		this.imageRegistry = SwtImageRegistry.getInstance();
		this.imageHandleFactorySpi = new SwtImageHandleFactorySpi(imageRegistry);
		this.widgetFactory = new SwtWidgetFactory();
		this.optionalWidgetsFactory = new SwtOptionalWidgetsFactory();
	}

	@Override
	public IImageRegistry getImageRegistry() {
		return imageRegistry;
	}

	@Override
	public IImageHandleFactorySpi getImageHandleFactory() {
		return imageHandleFactorySpi;
	}

	@Override
	public IWidgetFactorySpi getWidgetFactory() {
		return widgetFactory;
	}

	@Override
	public IOptionalWidgetsFactorySpi getOptionalWidgetFactory() {
		return optionalWidgetsFactory;
	}

	@Override
	public IUiThreadAccessCommon createUiThreadAccess() {
		return new SwtUiThreadAccess(display);
	}

	@Override
	public IApplicationRunner createApplicationRunner() {
		return applicationRunnerFactory.create();
	}

	@Override
	public Object getActiveWindowUiReference() {
		return Display.getDefault().getActiveShell();
	}

	@Override
	public List<Object> getAllWindowsUiReference() {
		final List<Object> result = new LinkedList<Object>();
		final Display currentDisplay = Display.getDefault();
		for (final Shell shell : currentDisplay.getShells()) {
			result.add(shell);
		}
		return result;
	}

	@Override
	public Position toScreen(final Position localPosition, final IComponentCommon component) {
		final Control control;
		if (component.getUiReference() instanceof Control) {
			control = (Control) component.getUiReference();
		}
		else if (component.getUiReference() instanceof CTabItem) {
			control = ((CTabItem) component.getUiReference()).getControl();
		}
		else {
			throw new IllegalArgumentException("UiReference of component must be instance of '"
				+ Control.class.getName()
				+ "' or '"
				+ CTabItem.class.getName()
				+ "'");
		}

		return PositionConvert.convert(control.toDisplay(PositionConvert.convert(localPosition)));
	}

	@Override
	public Position toLocal(final Position screenPosition, final IComponentCommon component) {
		final Control control;
		if (component.getUiReference() instanceof Control) {
			control = (Control) component.getUiReference();
		}
		else if (component.getUiReference() instanceof CTabItem) {
			control = ((CTabItem) component.getUiReference()).getControl();
		}
		else {
			throw new IllegalArgumentException("UiReference of component must be instance of '"
				+ Control.class.getName()
				+ "' or '"
				+ CTabItem.class.getName()
				+ "'");
		}

		return PositionConvert.convert(control.toControl(PositionConvert.convert(screenPosition)));
	}

}
