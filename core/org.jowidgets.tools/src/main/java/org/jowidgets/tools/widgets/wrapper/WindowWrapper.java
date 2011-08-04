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

package org.jowidgets.tools.widgets.wrapper;

import java.util.List;

import org.jowidgets.api.widgets.IDisplay;
import org.jowidgets.api.widgets.IWindow;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.controller.IWindowListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;

public class WindowWrapper extends ComponentWrapper implements IWindow {

	public WindowWrapper(final IWindow widget) {
		super(widget);
	}

	@Override
	protected IWindow getWidget() {
		return (IWindow) super.getWidget();
	}

	@Override
	public void setParent(final IWindow parent) {
		getWidget().setParent(parent);
	}

	@Override
	public IWindow getParent() {
		return getWidget().getParent();
	}

	@Override
	public Rectangle getParentBounds() {
		return getWidget().getParentBounds();
	}

	@Override
	public void pack() {
		getWidget().pack();
	}

	@Override
	public void dispose() {
		getWidget().dispose();
	}

	@Override
	public void addWindowListener(final IWindowListener listener) {
		getWidget().addWindowListener(listener);
	}

	@Override
	public void removeWindowListener(final IWindowListener listener) {
		getWidget().removeWindowListener(listener);
	}

	@Override
	public void centerLocation() {
		getWidget().centerLocation();
	}

	@Override
	public <WIDGET_TYPE extends IDisplay, DESCRIPTOR_TYPE extends IWidgetDescriptor<? extends WIDGET_TYPE>> WIDGET_TYPE createChildWindow(
		final DESCRIPTOR_TYPE descriptor) {
		return getWidget().createChildWindow(descriptor);
	}

	@Override
	public List<IDisplay> getChildWindows() {
		return getWidget().getChildWindows();
	}

}
