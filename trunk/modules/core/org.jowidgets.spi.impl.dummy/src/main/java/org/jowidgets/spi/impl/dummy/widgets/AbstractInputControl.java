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
import org.jowidgets.common.widgets.controller.IComponentListener;
import org.jowidgets.common.widgets.controller.IFocusListener;
import org.jowidgets.common.widgets.controller.IKeyListener;
import org.jowidgets.common.widgets.controller.IMouseListener;
import org.jowidgets.common.widgets.controller.IMouseMotionListener;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.spi.impl.controller.InputObservable;
import org.jowidgets.spi.impl.dummy.dummyui.UIDComponent;
import org.jowidgets.spi.widgets.IInputControlSpi;
import org.jowidgets.spi.widgets.IPopupMenuSpi;

public abstract class AbstractInputControl extends InputObservable implements IInputControlSpi {

	private final DummyControl dummyControlDelegate;

	public AbstractInputControl(final UIDComponent component) {
		super();
		this.dummyControlDelegate = new DummyControl(component);
	}

	@Override
	public UIDComponent getUiReference() {
		return dummyControlDelegate.getUiReference();
	}

	@Override
	public void setLayoutConstraints(final Object layoutConstraints) {
		dummyControlDelegate.setLayoutConstraints(layoutConstraints);
	}

	@Override
	public Object getLayoutConstraints() {
		return dummyControlDelegate.getLayoutConstraints();
	}

	@Override
	public void redraw() {
		dummyControlDelegate.redraw();
	}

	@Override
	public void setRedrawEnabled(final boolean enabled) {
		dummyControlDelegate.setRedrawEnabled(enabled);
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		dummyControlDelegate.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		dummyControlDelegate.setBackgroundColor(colorValue);
	}

	@Override
	public IColorConstant getForegroundColor() {
		return dummyControlDelegate.getForegroundColor();
	}

	@Override
	public IColorConstant getBackgroundColor() {
		return dummyControlDelegate.getBackgroundColor();
	}

	@Override
	public void setCursor(final Cursor cursor) {
		dummyControlDelegate.setCursor(cursor);
	}

	@Override
	public void setVisible(final boolean visible) {
		dummyControlDelegate.setVisible(visible);
	}

	@Override
	public boolean isVisible() {
		return dummyControlDelegate.isVisible();
	}

	@Override
	public void setEnabled(final boolean enabled) {
		dummyControlDelegate.setEnabled(enabled);
	}

	@Override
	public boolean isEnabled() {
		return dummyControlDelegate.isEnabled();
	}

	@Override
	public Dimension getMinSize() {
		return dummyControlDelegate.getMinSize();
	}

	@Override
	public Dimension getPreferredSize() {
		return dummyControlDelegate.getPreferredSize();
	}

	@Override
	public Dimension getMaxSize() {
		return dummyControlDelegate.getMaxSize();
	}

	@Override
	public Dimension getSize() {
		return dummyControlDelegate.getSize();
	}

	@Override
	public void setSize(final Dimension size) {
		dummyControlDelegate.setSize(size);
	}

	@Override
	public Position getPosition() {
		return dummyControlDelegate.getPosition();
	}

	@Override
	public void setPosition(final Position position) {
		dummyControlDelegate.setPosition(position);
	}

	@Override
	public IPopupMenuSpi createPopupMenu() {
		return dummyControlDelegate.createPopupMenu();
	}

	@Override
	public boolean requestFocus() {
		return dummyControlDelegate.requestFocus();
	}

	@Override
	public void addFocusListener(final IFocusListener listener) {
		dummyControlDelegate.addFocusListener(listener);
	}

	@Override
	public void removeFocusListener(final IFocusListener listener) {
		dummyControlDelegate.removeFocusListener(listener);
	}

	@Override
	public void addKeyListener(final IKeyListener listener) {
		dummyControlDelegate.addKeyListener(listener);
	}

	@Override
	public void removeKeyListener(final IKeyListener listener) {
		dummyControlDelegate.removeKeyListener(listener);
	}

	@Override
	public void addMouseListener(final IMouseListener mouseListener) {
		dummyControlDelegate.addMouseListener(mouseListener);
	}

	@Override
	public void removeMouseListener(final IMouseListener mouseListener) {
		dummyControlDelegate.removeMouseListener(mouseListener);
	}

	@Override
	public void addMouseMotionListener(final IMouseMotionListener listener) {
		dummyControlDelegate.addMouseMotionListener(listener);
	}

	@Override
	public void removeMouseMotionListener(final IMouseMotionListener listener) {
		dummyControlDelegate.removeMouseMotionListener(listener);
	}

	@Override
	public void addComponentListener(final IComponentListener componentListener) {
		dummyControlDelegate.addComponentListener(componentListener);
	}

	@Override
	public void removeComponentListener(final IComponentListener componentListener) {
		dummyControlDelegate.removeComponentListener(componentListener);
	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		dummyControlDelegate.addPopupDetectionListener(listener);
	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		dummyControlDelegate.removePopupDetectionListener(listener);
	}

	@Override
	public void setToolTipText(final String toolTip) {
		dummyControlDelegate.setToolTipText(toolTip);
	}

}
