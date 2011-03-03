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

import org.jowidgets.api.widgets.blueprint.builder.IWidgetSetupBuilder;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.IWidgetCommon;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.util.Assert;

class WidgetCommon<WIDGET_TYPE extends IWidgetCommon, BLUE_PRINT_TYPE extends IWidgetDescriptor<? extends WIDGET_TYPE> & IWidgetSetupBuilder<?>> implements
		IWidgetCommon {

	private final BLUE_PRINT_TYPE bluePrint;
	private WIDGET_TYPE widget;
	private Cursor cursor;
	private Boolean enabled;

	WidgetCommon(final BLUE_PRINT_TYPE bluePrint) {
		this.bluePrint = bluePrint;
	}

	public final boolean isInitialized() {
		return widget != null;
	}

	void initialize(final WIDGET_TYPE widget) {
		Assert.paramNotNull(widget, "widget");
		checkNotInitialized();
		this.widget = widget;
		if (cursor != null) {
			widget.setCursor(cursor);
		}
		if (enabled != null) {
			widget.setEnabled(enabled.booleanValue());
		}
	}

	final IWidgetDescriptor<? extends WIDGET_TYPE> getDescriptor() {
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
	public void setVisible(final boolean visible) {
		if (isInitialized()) {
			widget.setVisible(visible);
		}
		else {
			bluePrint.setVisible(visible);
		}
	}

	@Override
	public void setCursor(final Cursor cursor) {
		if (isInitialized()) {
			widget.setCursor(cursor);
		}
		else {
			this.cursor = cursor;
		}
	}

	@Override
	public void setEnabled(final boolean enabled) {
		if (isInitialized()) {
			widget.setEnabled(enabled);
		}
		else {
			this.enabled = Boolean.valueOf(enabled);
		}
	}

	@Override
	public boolean isEnabled() {
		checkInitialized();
		return widget.isEnabled();
	}

	@Override
	public final boolean isVisible() {
		checkInitialized();
		return widget.isVisible();
	}

	@Override
	public IColorConstant getForegroundColor() {
		checkInitialized();
		return widget.getForegroundColor();
	}

	@Override
	public IColorConstant getBackgroundColor() {
		checkInitialized();
		return widget.getBackgroundColor();
	}

	@Override
	public final Object getUiReference() {
		checkInitialized();
		return widget.getUiReference();
	}

	@Override
	public Dimension getSize() {
		checkInitialized();
		return widget.getSize();
	}

	@Override
	public final void redraw() {
		checkInitialized();
		widget.redraw();
	}

	final BLUE_PRINT_TYPE getBluePrint() {
		return bluePrint;
	}

	final WIDGET_TYPE getWidget() {
		return widget;
	}

	final void checkInitialized() {
		if (!isInitialized()) {
			throw new WidgetNotInitializedException("Widget is not yet initialized (was not added to an parent)");
		}
	}

	final void checkNotInitialized() {
		if (isInitialized()) {
			throw new WidgetAlreadyInitializedException("Widget is already initialized (was already added to an parent)");
		}
	}

}
