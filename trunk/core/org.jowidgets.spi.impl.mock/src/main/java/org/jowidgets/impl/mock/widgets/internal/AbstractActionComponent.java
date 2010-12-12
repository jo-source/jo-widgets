/*
 * Copyright (c) 2010, Michael Grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jowidgets.impl.mock.widgets.internal;

import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.IActionWidgetCommon;
import org.jowidgets.common.widgets.IComponentCommon;
import org.jowidgets.common.widgets.controler.impl.ActionObservable;
import org.jowidgets.impl.mock.mockui.UIMComponent;
import org.jowidgets.impl.mock.widgets.MockComponent;

public abstract class AbstractActionComponent extends ActionObservable implements IActionWidgetCommon, IComponentCommon {

	private final MockComponent mockComponentDelegate;

	public AbstractActionComponent(final UIMComponent component) {
		super();
		this.mockComponentDelegate = new MockComponent(component);
	}

	@Override
	public void setEnabled(final boolean enabled) {
		mockComponentDelegate.setEnabled(enabled);
	}

	@Override
	public boolean isEnabled() {
		return mockComponentDelegate.isEnabled();
	}

	@Override
	public UIMComponent getUiReference() {
		return mockComponentDelegate.getUiReference();
	}

	@Override
	public void redraw() {
		mockComponentDelegate.redraw();
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		mockComponentDelegate.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		mockComponentDelegate.setBackgroundColor(colorValue);
	}

	@Override
	public IColorConstant getForegroundColor() {
		return mockComponentDelegate.getForegroundColor();
	}

	@Override
	public IColorConstant getBackgroundColor() {
		return mockComponentDelegate.getBackgroundColor();
	}

	@Override
	public void setCursor(final Cursor cursor) {
		mockComponentDelegate.setCursor(cursor);
	}

	@Override
	public void setVisible(final boolean visible) {
		mockComponentDelegate.setVisible(visible);
	}

	@Override
	public boolean isVisible() {
		return mockComponentDelegate.isVisible();
	}

	@Override
	public Dimension getSize() {
		return mockComponentDelegate.getSize();
	}

}
