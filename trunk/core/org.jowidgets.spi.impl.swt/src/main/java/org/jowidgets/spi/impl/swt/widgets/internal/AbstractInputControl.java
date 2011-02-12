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
package org.jowidgets.spi.impl.swt.widgets.internal;

import org.eclipse.swt.widgets.Control;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.controler.IPopupDetectionListener;
import org.jowidgets.spi.impl.controler.InputObservable;
import org.jowidgets.spi.impl.swt.widgets.SwtControl;
import org.jowidgets.spi.widgets.IInputControlSpi;
import org.jowidgets.spi.widgets.IPopupMenuSpi;

public abstract class AbstractInputControl extends InputObservable implements IInputControlSpi {

	private final Control control;
	private final SwtControl swtControlDelegate;

	public AbstractInputControl(final Control control) {
		super();
		this.control = control;
		this.swtControlDelegate = new SwtControl(control);
	}

	@Override
	public Control getUiReference() {
		return control;
	}

	@Override
	public void setLayoutConstraints(final Object layoutConstraints) {
		swtControlDelegate.setLayoutConstraints(layoutConstraints);
	}

	@Override
	public Object getLayoutConstraints() {
		return swtControlDelegate.getLayoutConstraints();
	}

	@Override
	public void redraw() {
		swtControlDelegate.redraw();
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		swtControlDelegate.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		swtControlDelegate.setBackgroundColor(colorValue);
	}

	@Override
	public IColorConstant getForegroundColor() {
		return swtControlDelegate.getForegroundColor();
	}

	@Override
	public IColorConstant getBackgroundColor() {
		return swtControlDelegate.getBackgroundColor();
	}

	@Override
	public void setCursor(final Cursor cursor) {
		swtControlDelegate.setCursor(cursor);
	}

	@Override
	public void setEnabled(final boolean enabled) {
		swtControlDelegate.setEnabled(enabled);
	}

	@Override
	public boolean isEnabled() {
		return swtControlDelegate.isEnabled();
	}

	@Override
	public void setVisible(final boolean visible) {
		swtControlDelegate.setVisible(visible);
	}

	@Override
	public boolean isVisible() {
		return swtControlDelegate.isVisible();
	}

	@Override
	public Dimension getSize() {
		return swtControlDelegate.getSize();
	}

	@Override
	public IPopupMenuSpi createPopupMenu() {
		return swtControlDelegate.createPopupMenu();
	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		swtControlDelegate.addPopupDetectionListener(listener);
	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		swtControlDelegate.removePopupDetectionListener(listener);
	}

}
