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

package org.jowidgets.impl.mock;

import org.jowidgets.common.application.IApplicationRunner;
import org.jowidgets.common.image.IImageRegistry;
import org.jowidgets.common.threads.IUiThreadAccessCommon;
import org.jowidgets.impl.mock.application.MockApplicationRunner;
import org.jowidgets.impl.mock.image.MockImageHandleFactory;
import org.jowidgets.impl.mock.image.MockImageHandleFactorySpi;
import org.jowidgets.impl.mock.image.MockImageRegistry;
import org.jowidgets.impl.mock.threads.MockUiThreadAccess;
import org.jowidgets.spi.IWidgetFactorySpi;
import org.jowidgets.spi.IWidgetsServiceProvider;
import org.jowidgets.spi.image.IImageHandleFactorySpi;

public class MockWidgetsServiceProvider implements IWidgetsServiceProvider {

	private final MockImageHandleFactorySpi imageHandleFactorySpi;
	private final MockImageRegistry imageRegistry;
	private final MockWidgetFactory widgetFactory;

	public MockWidgetsServiceProvider() {
		super();
		this.imageRegistry = new MockImageRegistry(new MockImageHandleFactory());
		this.imageHandleFactorySpi = new MockImageHandleFactorySpi(imageRegistry);
		this.widgetFactory = new MockWidgetFactory(imageRegistry);
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
		return new MockUiThreadAccess();
	}

	@Override
	public IApplicationRunner createApplicationRunner() {
		return new MockApplicationRunner();
	}

	@Override
	public Object getActiveWindowUiReference() {
		//TODO
		return null;
	}

	@Override
	public void setAllWindowsEnabled(final boolean enabled) {
		// TODO
	}

}
