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
package org.jowidgets.impl.widgets.composed;

import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.IComponentCommon;
import org.jowidgets.common.widgets.controler.IComponentListener;
import org.jowidgets.common.widgets.controler.IFocusListener;
import org.jowidgets.common.widgets.controler.IKeyListener;
import org.jowidgets.common.widgets.controler.IMouseListener;
import org.jowidgets.common.widgets.controler.IPopupDetectionListener;

public class CompositeBasedControl implements IControl {

	private final IComposite composite;

	public CompositeBasedControl(final IComposite composite) {
		this.composite = composite;
	}

	protected IComposite getComposite() {
		return composite;
	}

	@Override
	public IContainer getParent() {
		return composite.getParent();
	}

	@Override
	public void setParent(final IContainer parent) {
		composite.setParent(parent);
	}

	@Override
	public boolean isReparentable() {
		return composite.isReparentable();
	}

	@Override
	public Object getUiReference() {
		return composite.getUiReference();
	}

	@Override
	public void setLayoutConstraints(final Object layoutConstraints) {
		composite.setLayoutConstraints(layoutConstraints);
	}

	@Override
	public Object getLayoutConstraints() {
		return composite.getLayoutConstraints();
	}

	@Override
	public void redraw() {
		composite.redraw();
	}

	@Override
	public void setRedrawEnabled(final boolean enabled) {
		composite.setRedrawEnabled(enabled);
	}

	@Override
	public void setCursor(final Cursor cursor) {
		composite.setCursor(cursor);
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		composite.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		composite.setBackgroundColor(colorValue);
		composite.setBackgroundColor(colorValue);
	}

	@Override
	public IColorConstant getForegroundColor() {
		return composite.getForegroundColor();
	}

	@Override
	public IColorConstant getBackgroundColor() {
		return composite.getBackgroundColor();
	}

	@Override
	public void setVisible(final boolean visible) {
		composite.setVisible(visible);
	}

	@Override
	public boolean isVisible() {
		return composite.isVisible();
	}

	@Override
	public void setEnabled(final boolean enabled) {
		composite.setEnabled(enabled);
	}

	@Override
	public boolean isEnabled() {
		return composite.isEnabled();
	}

	@Override
	public Dimension getMinSize() {
		return composite.getMinSize();
	}

	@Override
	public Dimension getPreferredSize() {
		return composite.getPreferredSize();
	}

	@Override
	public Dimension getMaxSize() {
		return composite.getMaxSize();
	}

	@Override
	public void setMinSize(final Dimension minSize) {
		composite.setMinSize(minSize);
	}

	@Override
	public void setPreferredSize(final Dimension preferredSize) {
		composite.setPreferredSize(preferredSize);
	}

	@Override
	public void setMaxSize(final Dimension maxSize) {
		composite.setMaxSize(maxSize);
	}

	@Override
	public Dimension getSize() {
		return composite.getSize();
	}

	@Override
	public void setSize(final Dimension size) {
		composite.setSize(size);
	}

	@Override
	public Position getPosition() {
		return composite.getPosition();
	}

	@Override
	public void setPosition(final Position position) {
		composite.setPosition(position);
	}

	@Override
	public Position toScreen(final Position localPosition) {
		return composite.toScreen(localPosition);
	}

	@Override
	public Position toLocal(final Position screenPosition) {
		return composite.toLocal(screenPosition);
	}

	@Override
	public Position fromComponent(final IComponentCommon component, final Position componentPosition) {
		return composite.fromComponent(component, componentPosition);
	}

	@Override
	public Position toComponent(final Position componentPosition, final IComponentCommon component) {
		return composite.toComponent(componentPosition, component);
	}

	@Override
	public boolean requestFocus() {
		return composite.requestFocus();
	}

	@Override
	public void addFocusListener(final IFocusListener listener) {
		composite.addFocusListener(listener);
	}

	@Override
	public void removeFocusListener(final IFocusListener listener) {
		composite.removeFocusListener(listener);
	}

	@Override
	public void addKeyListener(final IKeyListener listener) {
		composite.addKeyListener(listener);
	}

	@Override
	public void removeKeyListener(final IKeyListener listener) {
		composite.removeKeyListener(listener);
	}

	@Override
	public void addMouseListener(final IMouseListener mouseListener) {
		composite.addMouseListener(mouseListener);
	}

	@Override
	public void removeMouseListener(final IMouseListener mouseListener) {
		composite.removeMouseListener(mouseListener);
	}

	@Override
	public void addComponentListener(final IComponentListener componentListener) {
		composite.addComponentListener(componentListener);
	}

	@Override
	public void removeComponentListener(final IComponentListener componentListener) {
		composite.removeComponentListener(componentListener);
	}

	@Override
	public IPopupMenu createPopupMenu() {
		return composite.createPopupMenu();
	}

	@Override
	public void setPopupMenu(final IMenuModel popupMenu) {
		composite.setPopupMenu(popupMenu);
	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		composite.addPopupDetectionListener(listener);
	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		composite.removePopupDetectionListener(listener);
	}

}
