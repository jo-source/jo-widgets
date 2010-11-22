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

package org.jowidgets.impl.widgets.basic;

import org.jowidgets.api.widgets.IDisplay;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.api.widgets.descriptor.setup.IFrameSetup;
import org.jowidgets.common.widgets.IControlCommon;
import org.jowidgets.common.widgets.IDisplayCommon;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetFactory;
import org.jowidgets.impl.base.delegate.ContainerWidgetDelegate;
import org.jowidgets.impl.base.delegate.DisplayDelegate;
import org.jowidgets.impl.base.delegate.WindowWidgetDelegate;
import org.jowidgets.impl.widgets.common.wrapper.AbstractFrameWidgetCommonWrapper;
import org.jowidgets.spi.widgets.IFrameSpi;

public class FrameWidget extends AbstractFrameWidgetCommonWrapper implements IFrame {

	private final DisplayDelegate displayDelegate;
	private final WindowWidgetDelegate windowWidgetDelegate;
	private final ContainerWidgetDelegate compositeWidgetDelegate;

	public FrameWidget(final IFrameSpi frameWidgetSpi, final IFrameSetup setup) {
		super(frameWidgetSpi);
		this.displayDelegate = new DisplayDelegate();
		this.windowWidgetDelegate = new WindowWidgetDelegate(frameWidgetSpi, setup);
		this.compositeWidgetDelegate = new ContainerWidgetDelegate(frameWidgetSpi, this);
	}

	@Override
	public <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		final Object layoutConstraints) {
		return compositeWidgetDelegate.add(descriptor, layoutConstraints);
	}

	@Override
	public <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(
		final ICustomWidgetFactory<WIDGET_TYPE> factory,
		final Object layoutConstraints) {
		return compositeWidgetDelegate.add(factory, layoutConstraints);
	}

	@Override
	public <WIDGET_TYPE extends IDisplayCommon, DESCRIPTOR_TYPE extends IWidgetDescriptor<? extends WIDGET_TYPE>> WIDGET_TYPE createChildWindow(
		final DESCRIPTOR_TYPE descriptor) {
		final WIDGET_TYPE result = getWidget().createChildWindow(descriptor);
		if (result instanceof IWidget) {
			((IWidget) result).setParent(this);
		}
		return result;
	}

	@Override
	public IDisplay getParent() {
		return displayDelegate.getParent();
	}

	@Override
	public void setParent(final IWidget parent) {
		displayDelegate.setParent(parent);
	}

	@Override
	public boolean isReparentable() {
		return displayDelegate.isReparentable();
	}

	@Override
	public void centerLocation() {
		windowWidgetDelegate.centerLocation();
	}

	@Override
	public void setVisible(final boolean visible) {
		windowWidgetDelegate.setVisible(visible);
	}

}
