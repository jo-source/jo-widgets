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
package org.jowidgets.impl.swt.widgets.internal;

import org.eclipse.swt.widgets.Control;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.IInputComponentCommon;
import org.jowidgets.common.widgets.controler.impl.InputObservable;
import org.jowidgets.impl.swt.color.IColorCache;
import org.jowidgets.impl.swt.widgets.SwtComponent;

public abstract class AbstractInputComponent extends InputObservable implements IInputComponentCommon {

	private final Control control;
	private final SwtComponent swtComponentDelegate;

	public AbstractInputComponent(final IColorCache colorCache, final Control control) {
		super();
		this.control = control;
		this.swtComponentDelegate = new SwtComponent(colorCache, control);
	}

	@Override
	public Control getUiReference() {
		return control;
	}

	@Override
	public void redraw() {
		swtComponentDelegate.redraw();
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		swtComponentDelegate.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		swtComponentDelegate.setBackgroundColor(colorValue);
	}

	@Override
	public IColorConstant getForegroundColor() {
		return swtComponentDelegate.getForegroundColor();
	}

	@Override
	public IColorConstant getBackgroundColor() {
		return swtComponentDelegate.getBackgroundColor();
	}

	@Override
	public void setCursor(final Cursor cursor) {
		swtComponentDelegate.setCursor(cursor);
	}

	@Override
	public void setEnabled(final boolean enabled) {
		swtComponentDelegate.setEnabled(enabled);
	}

	@Override
	public boolean isEnabled() {
		return swtComponentDelegate.isEnabled();
	}

	@Override
	public void setVisible(final boolean visible) {
		swtComponentDelegate.setVisible(visible);
	}

	@Override
	public boolean isVisible() {
		return swtComponentDelegate.isVisible();
	}

	@Override
	public Dimension getSize() {
		return swtComponentDelegate.getSize();
	}

}
