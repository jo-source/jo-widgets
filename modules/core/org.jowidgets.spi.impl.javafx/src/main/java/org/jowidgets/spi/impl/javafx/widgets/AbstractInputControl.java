/*
 * Copyright (c) 2012, David Bauknecht
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
package org.jowidgets.spi.impl.javafx.widgets;

import javafx.scene.control.Control;

import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controller.IComponentListener;
import org.jowidgets.common.widgets.controller.IFocusListener;
import org.jowidgets.common.widgets.controller.IKeyListener;
import org.jowidgets.common.widgets.controller.IMouseListener;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.spi.impl.controller.InputObservable;
import org.jowidgets.spi.widgets.IInputControlSpi;
import org.jowidgets.spi.widgets.IPopupMenuSpi;

public abstract class AbstractInputControl extends InputObservable implements IInputControlSpi {

	private final Control control;
	private final JavafxControl javafxControlDelegate;

	public AbstractInputControl(final Control control) {
		super();
		this.control = control;
		this.javafxControlDelegate = new JavafxControl(control);
	}

	@Override
	public Control getUiReference() {
		return control;
	}

	@Override
	public void setLayoutConstraints(final Object layoutConstraints) {
		javafxControlDelegate.setLayoutConstraints(layoutConstraints);
	}

	@Override
	public Object getLayoutConstraints() {
		return javafxControlDelegate.getLayoutConstraints();
	}

	@Override
	public void redraw() {
		javafxControlDelegate.redraw();
	}

	@Override
	public void setRedrawEnabled(final boolean enabled) {
		javafxControlDelegate.setRedrawEnabled(enabled);
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		javafxControlDelegate.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		javafxControlDelegate.setBackgroundColor(colorValue);
	}

	@Override
	public IColorConstant getForegroundColor() {
		return javafxControlDelegate.getForegroundColor();
	}

	@Override
	public IColorConstant getBackgroundColor() {
		return javafxControlDelegate.getBackgroundColor();
	}

	@Override
	public void setCursor(final Cursor cursor) {
		javafxControlDelegate.setCursor(cursor);
	}

	@Override
	public void setVisible(final boolean visible) {
		javafxControlDelegate.setVisible(visible);
	}

	@Override
	public boolean isVisible() {
		return javafxControlDelegate.isVisible();
	}

	@Override
	public void setEnabled(final boolean enabled) {
		javafxControlDelegate.setEnabled(enabled);
	}

	@Override
	public boolean isEnabled() {
		return javafxControlDelegate.isEnabled();
	}

	@Override
	public Dimension getMinSize() {
		return javafxControlDelegate.getMinSize();
	}

	@Override
	public Dimension getPreferredSize() {
		return javafxControlDelegate.getPreferredSize();
	}

	@Override
	public Dimension getMaxSize() {
		return javafxControlDelegate.getMaxSize();
	}

	@Override
	public Dimension getSize() {
		return javafxControlDelegate.getSize();
	}

	@Override
	public void setSize(final Dimension size) {
		javafxControlDelegate.setSize(size);
	}

	@Override
	public Position getPosition() {
		return javafxControlDelegate.getPosition();
	}

	@Override
	public void setPosition(final Position position) {
		javafxControlDelegate.setPosition(position);
	}

	@Override
	public boolean requestFocus() {
		return javafxControlDelegate.requestFocus();
	}

	@Override
	public void addFocusListener(final IFocusListener listener) {
		javafxControlDelegate.addFocusListener(listener);
	}

	@Override
	public void removeFocusListener(final IFocusListener listener) {
		javafxControlDelegate.removeFocusListener(listener);
	}

	@Override
	public void addKeyListener(final IKeyListener listener) {
		javafxControlDelegate.addKeyListener(listener);
	}

	@Override
	public void removeKeyListener(final IKeyListener listener) {
		javafxControlDelegate.removeKeyListener(listener);
	}

	@Override
	public void addMouseListener(final IMouseListener mouseListener) {
		javafxControlDelegate.addMouseListener(mouseListener);
	}

	@Override
	public void removeMouseListener(final IMouseListener mouseListener) {
		javafxControlDelegate.removeMouseListener(mouseListener);
	}

	@Override
	public void addComponentListener(final IComponentListener componentListener) {
		javafxControlDelegate.addComponentListener(componentListener);
	}

	@Override
	public void removeComponentListener(final IComponentListener componentListener) {
		javafxControlDelegate.removeComponentListener(componentListener);
	}

	@Override
	public IPopupMenuSpi createPopupMenu() {
		return javafxControlDelegate.createPopupMenu();
	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		javafxControlDelegate.addPopupDetectionListener(listener);
	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		javafxControlDelegate.removePopupDetectionListener(listener);
	}

	@Override
	public void setToolTipText(final String toolTip) {
		javafxControlDelegate.setToolTipText(toolTip);
	}

}
