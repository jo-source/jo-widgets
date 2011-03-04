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
package org.jowidgets.spi.impl.dummy.widgets.internal;

import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.controler.IPopupDetectionListener;
import org.jowidgets.common.widgets.controler.impl.InputObservable;
import org.jowidgets.spi.impl.dummy.dummyui.UIDComponent;
import org.jowidgets.spi.impl.dummy.widgets.DummyControl;
import org.jowidgets.spi.widgets.IInputControlSpi;
import org.jowidgets.spi.widgets.IPopupMenuSpi;

public abstract class AbstractInputControl extends InputObservable implements IInputControlSpi {

	private final DummyControl mockControlDelegate;

	public AbstractInputControl(final UIDComponent component) {
		super();
		this.mockControlDelegate = new DummyControl(component);
	}

	@Override
	public UIDComponent getUiReference() {
		return mockControlDelegate.getUiReference();
	}

	@Override
	public void setLayoutConstraints(final Object layoutConstraints) {
		mockControlDelegate.setLayoutConstraints(layoutConstraints);
	}

	@Override
	public Object getLayoutConstraints() {
		return mockControlDelegate.getLayoutConstraints();
	}

	@Override
	public void redraw() {
		mockControlDelegate.redraw();
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		mockControlDelegate.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		mockControlDelegate.setBackgroundColor(colorValue);
	}

	@Override
	public IColorConstant getForegroundColor() {
		return mockControlDelegate.getForegroundColor();
	}

	@Override
	public IColorConstant getBackgroundColor() {
		return mockControlDelegate.getBackgroundColor();
	}

	@Override
	public void setCursor(final Cursor cursor) {
		mockControlDelegate.setCursor(cursor);
	}

	@Override
	public void setVisible(final boolean visible) {
		mockControlDelegate.setVisible(visible);
	}

	@Override
	public boolean isVisible() {
		return mockControlDelegate.isVisible();
	}

	@Override
	public void setEnabled(final boolean enabled) {
		mockControlDelegate.setEnabled(enabled);
	}

	@Override
	public boolean isEnabled() {
		return mockControlDelegate.isEnabled();
	}

	@Override
	public Dimension getSize() {
		return mockControlDelegate.getSize();
	}

	@Override
	public IPopupMenuSpi createPopupMenu() {
		return mockControlDelegate.createPopupMenu();
	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		mockControlDelegate.addPopupDetectionListener(listener);
	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		mockControlDelegate.removePopupDetectionListener(listener);
	}

}
