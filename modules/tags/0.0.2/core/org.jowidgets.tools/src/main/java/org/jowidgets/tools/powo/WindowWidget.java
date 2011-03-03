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

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IWindowWidget;
import org.jowidgets.api.widgets.blueprint.builder.IContainerSetupBuilder;
import org.jowidgets.api.widgets.blueprint.builder.IWindowSetupBuilder;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.IContainerWidgetCommon;
import org.jowidgets.common.widgets.IWidget;
import org.jowidgets.common.widgets.IWindowWidgetCommon;
import org.jowidgets.common.widgets.controler.IWindowListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.util.Assert;

class WindowWidget<WIDGET_TYPE extends IWindowWidget & IContainerWidgetCommon, BLUE_PRINT_TYPE extends IWidgetDescriptor<WIDGET_TYPE> & IContainerSetupBuilder<BLUE_PRINT_TYPE> & IWindowSetupBuilder<BLUE_PRINT_TYPE>> extends
		ContainerWidget<WIDGET_TYPE, BLUE_PRINT_TYPE> implements IWindowWidget {

	WindowWidget(final BLUE_PRINT_TYPE bluePrint) {
		super(bluePrint);
		Assert.paramNotNull(bluePrint, "bluePrint");
		initialize(Toolkit.getWidgetFactory().create(bluePrint));
	}

	WindowWidget(final IWindowWidgetCommon parent, final BLUE_PRINT_TYPE bluePrint) {
		super(bluePrint);
		Assert.paramNotNull(parent, "parent");
		Assert.paramNotNull(bluePrint, "bluePrint");
		initialize(Toolkit.getWidgetFactory().create(parent, bluePrint));
	}

	@Override
	public <M_WIDGET_TYPE extends IWidget, DESCRIPTOR_TYPE extends IWidgetDescriptor<? extends M_WIDGET_TYPE>> M_WIDGET_TYPE createChildWindow(
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
	public final void pack() {
		getWidget().pack();
	}

	@Override
	public final void close() {
		getWidget().close();
	}

	@Override
	public final void centerLocation() {
		getWidget().centerLocation();
	}

}
