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

package org.jowidgets.tools.powo;

import java.util.List;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IDisplay;
import org.jowidgets.api.widgets.IWindow;
import org.jowidgets.api.widgets.blueprint.builder.IContainerSetupBuilder;
import org.jowidgets.api.widgets.blueprint.builder.IWindowSetupBuilder;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.controller.IWindowListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.util.Assert;

class Window<WIDGET_TYPE extends IWindow & IContainer, BLUE_PRINT_TYPE extends IWidgetDescriptor<WIDGET_TYPE> & IContainerSetupBuilder<BLUE_PRINT_TYPE> & IWindowSetupBuilder<BLUE_PRINT_TYPE>> extends
		Container<WIDGET_TYPE, BLUE_PRINT_TYPE> implements IWindow {

	Window(final BLUE_PRINT_TYPE bluePrint) {
		this(null, bluePrint);
	}

	Window(final IWindow parent, final BLUE_PRINT_TYPE bluePrint) {
		super(bluePrint);
		Assert.paramNotNull(bluePrint, "bluePrint");
		if (parent != null) {
			initialize(parent.createChildWindow(bluePrint));
		}
		else {
			initialize(Toolkit.getWidgetFactory().create(bluePrint));
		}
	}

	@Override
	public <M_WIDGET_TYPE extends IDisplay, DESCRIPTOR_TYPE extends IWidgetDescriptor<? extends M_WIDGET_TYPE>> M_WIDGET_TYPE createChildWindow(
		final DESCRIPTOR_TYPE descriptor) {
		return getWidget().createChildWindow(descriptor);
	}

	@Override
	public final void addWindowListener(final IWindowListener listener) {
		getWidget().addWindowListener(listener);
	}

	@Override
	public final void removeWindowListener(final IWindowListener listener) {
		getWidget().removeWindowListener(listener);
	}

	@Override
	public final void setPosition(final Position position) {
		getWidget().setPosition(position);
	}

	@Override
	public final Position getPosition() {
		return getWidget().getPosition();
	}

	@Override
	public final void setSize(final Dimension size) {
		getWidget().setSize(size);
	}

	@Override
	public final Dimension getSize() {
		return getWidget().getSize();
	}

	@Override
	public final Rectangle getParentBounds() {
		return getWidget().getParentBounds();
	}

	@Override
	public void setMinPackSize(final Dimension size) {
		getWidget().setMinPackSize(size);
	}

	@Override
	public void setMaxPackSize(final Dimension size) {
		getWidget().setMaxPackSize(size);
	}

	@Override
	public final void pack() {
		getWidget().pack();
	}

	@Override
	public final void dispose() {
		getWidget().dispose();
	}

	@Override
	public final void centerLocation() {
		getWidget().centerLocation();
	}

	@Override
	public final IWindow getParent() {
		return getWidget().getParent();
	}

	@Override
	public void setParent(final IWindow parent) {
		getWidget().setParent(parent);
	}

	@Override
	public List<IDisplay> getChildWindows() {
		return getWidget().getChildWindows();
	}

	@Override
	public boolean isReparentable() {
		return getWidget().isReparentable();
	}

}
