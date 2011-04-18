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
package org.jowidgets.spi.impl.dummy.widgets;

import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controler.IFocusListener;
import org.jowidgets.common.widgets.controler.IKeyListener;
import org.jowidgets.common.widgets.controler.IMouseListener;
import org.jowidgets.common.widgets.controler.IPopupDetectionListener;
import org.jowidgets.spi.impl.dummy.dummyui.UIDComponent;
import org.jowidgets.spi.widgets.IActionWidgetSpi;
import org.jowidgets.spi.widgets.IComponentSpi;
import org.jowidgets.spi.widgets.IPopupMenuSpi;

public abstract class AbstractActionComponent extends AbstractActionWidget implements IActionWidgetSpi, IComponentSpi {

	private final DummyComponent dummyComponentDelegate;

	public AbstractActionComponent(final UIDComponent component) {
		super(component);
		this.dummyComponentDelegate = new DummyComponent(component);
	}

	@Override
	public void redraw() {
		dummyComponentDelegate.redraw();
	}

	@Override
	public void setRedrawEnabled(final boolean enabled) {
		dummyComponentDelegate.setRedrawEnabled(enabled);
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		dummyComponentDelegate.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		dummyComponentDelegate.setBackgroundColor(colorValue);
	}

	@Override
	public IColorConstant getForegroundColor() {
		return dummyComponentDelegate.getForegroundColor();
	}

	@Override
	public IColorConstant getBackgroundColor() {
		return dummyComponentDelegate.getBackgroundColor();
	}

	@Override
	public void setCursor(final Cursor cursor) {
		dummyComponentDelegate.setCursor(cursor);
	}

	@Override
	public void setVisible(final boolean visible) {
		dummyComponentDelegate.setVisible(visible);
	}

	@Override
	public boolean isVisible() {
		return dummyComponentDelegate.isVisible();
	}

	@Override
	public Dimension getSize() {
		return dummyComponentDelegate.getSize();
	}

	@Override
	public void setSize(final Dimension size) {
		dummyComponentDelegate.setSize(size);
	}

	@Override
	public Position getPosition() {
		return dummyComponentDelegate.getPosition();
	}

	@Override
	public void setPosition(final Position position) {
		dummyComponentDelegate.setPosition(position);
	}

	@Override
	public boolean requestFocus() {
		return dummyComponentDelegate.requestFocus();
	}

	@Override
	public void addFocusListener(final IFocusListener listener) {
		dummyComponentDelegate.addFocusListener(listener);
	}

	@Override
	public void removeFocusListener(final IFocusListener listener) {
		dummyComponentDelegate.removeFocusListener(listener);
	}

	@Override
	public void addKeyListener(final IKeyListener listener) {
		dummyComponentDelegate.addKeyListener(listener);
	}

	@Override
	public void removeKeyListener(final IKeyListener listener) {
		dummyComponentDelegate.removeKeyListener(listener);
	}

	@Override
	public void addMouseListener(final IMouseListener mouseListener) {
		dummyComponentDelegate.addMouseListener(mouseListener);
	}

	@Override
	public void removeMouseListener(final IMouseListener mouseListener) {
		dummyComponentDelegate.removeMouseListener(mouseListener);
	}

	@Override
	public IPopupMenuSpi createPopupMenu() {
		return dummyComponentDelegate.createPopupMenu();
	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		dummyComponentDelegate.addPopupDetectionListener(listener);
	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		dummyComponentDelegate.removePopupDetectionListener(listener);
	}

}
