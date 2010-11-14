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

import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.widgets.IWidget;
import org.jowidgets.common.widgets.builder.IWidgetSetupBuilderCommon;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.util.Assert;

class Widget<WIDGET_TYPE extends IWidget, BLUE_PRINT_TYPE extends IWidgetDescriptor<WIDGET_TYPE> & IWidgetSetupBuilderCommon<?>> implements
		IWidget {

	private final BLUE_PRINT_TYPE bluePrint;
	private WIDGET_TYPE widget;

	Widget(final BLUE_PRINT_TYPE bluePrint) {
		this.bluePrint = bluePrint;
	}

	public final boolean isInitialized() {
		return widget != null;
	}

	void initialize(final WIDGET_TYPE widget) {
		Assert.paramNotNull(widget, "widget");
		checkNotInitialized();
		this.widget = widget;
	}

	final IWidgetDescriptor<WIDGET_TYPE> getDescriptor() {
		return bluePrint;
	}

	@Override
	public final void setForegroundColor(final IColorConstant colorValue) {
		if (isInitialized()) {
			widget.setForegroundColor(colorValue);
		}
		else {
			bluePrint.setForegroundColor(colorValue);
		}
	}

	@Override
	public final void setBackgroundColor(final IColorConstant colorValue) {
		if (isInitialized()) {
			widget.setBackgroundColor(colorValue);
		}
		else {
			bluePrint.setBackgroundColor(colorValue);
		}
	}

	@Override
	public final Object getUiReference() {
		checkInitialized();
		return widget.getUiReference();
	}

	@Override
	public final void redraw() {
		checkInitialized();
		widget.redraw();
	}

	@Override
	public final void setVisible(final boolean visible) {
		checkInitialized();
		widget.setVisible(visible);
	}

	@Override
	public final boolean isVisible() {
		checkInitialized();
		return widget.isVisible();
	}

	public final BLUE_PRINT_TYPE getBluePrint() {
		return bluePrint;
	}

	protected final WIDGET_TYPE getWidget() {
		return widget;
	}

	protected final void checkInitialized() {
		if (!isInitialized()) {
			throw new WidgetNotInitializedException("Powo is not yet initialized");
		}
	}

	protected final void checkNotInitialized() {
		if (isInitialized()) {
			throw new WidgetAlreadyInitializedException("Powo is already initialized");
		}
	}

}
