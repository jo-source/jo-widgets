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

package org.jowidgets.impl.swt;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jowidgets.common.application.IApplicationRunner;
import org.jowidgets.common.image.IImageRegistry;
import org.jowidgets.common.threads.IUiThreadAccessCommon;
import org.jowidgets.impl.swt.application.SwtApplicationRunner;
import org.jowidgets.impl.swt.image.SwtImageHandleFactory;
import org.jowidgets.impl.swt.image.SwtImageHandleFactorySpi;
import org.jowidgets.impl.swt.image.SwtImageRegistry;
import org.jowidgets.impl.swt.threads.SwtUiThreadAccess;
import org.jowidgets.spi.IWidgetFactorySpi;
import org.jowidgets.spi.IWidgetsServiceProvider;
import org.jowidgets.spi.image.IImageHandleFactorySpi;

public class SwtWidgetsServiceProvider implements IWidgetsServiceProvider {

	private final Display display;
	private final SwtImageRegistry imageRegistry;
	private final SwtImageHandleFactorySpi imageHandleFactorySpi;
	private final SwtWidgetFactory widgetFactory;

	public SwtWidgetsServiceProvider() {
		this(null);
	}

	public SwtWidgetsServiceProvider(final Display display) {
		this(display, new SwtImageRegistry(new SwtImageHandleFactory()));
	}

	public SwtWidgetsServiceProvider(final Display display, final SwtImageRegistry imageRegistry) {
		super();
		this.display = display;
		this.imageRegistry = imageRegistry;
		this.imageHandleFactorySpi = new SwtImageHandleFactorySpi(imageRegistry);
		this.widgetFactory = new SwtWidgetFactory(imageRegistry);
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
	public IUiThreadAccessCommon createUiThreadAccess() {
		return new SwtUiThreadAccess(display);
	}

	@Override
	public IApplicationRunner createApplicationRunner() {
		return new SwtApplicationRunner();
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

}
