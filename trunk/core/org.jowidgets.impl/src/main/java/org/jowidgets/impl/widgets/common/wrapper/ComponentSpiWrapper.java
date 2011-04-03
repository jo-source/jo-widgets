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

package org.jowidgets.impl.widgets.common.wrapper;

import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IComponent;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.IComponentCommon;
import org.jowidgets.common.widgets.controler.IFocusListener;
import org.jowidgets.common.widgets.controler.IKeyListener;
import org.jowidgets.common.widgets.controler.IPopupDetectionListener;
import org.jowidgets.impl.widgets.basic.PopupMenuImpl;
import org.jowidgets.spi.widgets.IComponentSpi;

public class ComponentSpiWrapper extends WidgetSpiWrapper implements IComponentCommon {

	private final IPopupDetectionListener popupListener;
	private IMenuModel popupMenuModel;
	private IPopupMenu popupMenu;

	public ComponentSpiWrapper(final IComponentSpi component) {
		super(component);
		this.popupListener = new IPopupDetectionListener() {

			@Override
			public void popupDetected(final Position position) {
				if (popupMenuModel != null) {
					if (popupMenu == null) {
						popupMenu = new PopupMenuImpl(getWidget().createPopupMenu(), (IComponent) ComponentSpiWrapper.this);
					}
					if (popupMenu.getModel() != popupMenuModel) {
						popupMenu.setModel(popupMenuModel);
					}
					popupMenu.show(position);
				}
			}
		};
	}

	@Override
	public void redraw() {
		getWidget().redraw();
	}

	@Override
	public void setRedrawEnabled(final boolean enabled) {
		getWidget().setRedrawEnabled(enabled);
	}

	@Override
	public IComponentSpi getWidget() {
		return (IComponentSpi) super.getWidget();
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		getWidget().setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		getWidget().setBackgroundColor(colorValue);
	}

	@Override
	public IColorConstant getForegroundColor() {
		return getWidget().getForegroundColor();
	}

	@Override
	public IColorConstant getBackgroundColor() {
		return getWidget().getBackgroundColor();
	}

	@Override
	public void setCursor(final Cursor cursor) {
		getWidget().setCursor(cursor);
	}

	@Override
	public void setVisible(final boolean visible) {
		getWidget().setVisible(visible);
	}

	@Override
	public boolean isVisible() {
		return getWidget().isVisible();
	}

	@Override
	public Dimension getSize() {
		return getWidget().getSize();
	}

	@Override
	public void setSize(final Dimension size) {
		getWidget().setSize(size);
	}

	@Override
	public Position getPosition() {
		return getWidget().getPosition();
	}

	@Override
	public void setPosition(final Position position) {
		getWidget().setPosition(position);
	}

	@Override
	public void addKeyListener(final IKeyListener listener) {
		getWidget().addKeyListener(listener);
	}

	@Override
	public void removeKeyListener(final IKeyListener listener) {
		getWidget().removeKeyListener(listener);
	}

	@Override
	public void addFocusListener(final IFocusListener listener) {
		getWidget().addFocusListener(listener);
	}

	@Override
	public void removeFocusListener(final IFocusListener listener) {
		getWidget().removeFocusListener(listener);
	}

	@Override
	public boolean requestFocus() {
		return getWidget().requestFocus();
	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		getWidget().addPopupDetectionListener(listener);
	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		getWidget().removePopupDetectionListener(listener);
	}

	public void setPopupMenu(final IMenuModel popupMenuModel) {
		if (popupMenuModel == null && this.popupMenuModel != null) {
			removePopupDetectionListener(popupListener);
		}
		else if (popupMenuModel != null && this.popupMenuModel == null) {
			addPopupDetectionListener(popupListener);
		}
		this.popupMenuModel = popupMenuModel;
	}

	public Position toScreen(final Position localPosition) {
		return Toolkit.toScreen(localPosition, (IComponent) this);
	}

	public Position toLocal(final Position screenPosition) {
		return Toolkit.toLocal(screenPosition, (IComponent) this);
	}

	public Position fromComponent(final IComponentCommon component, final Position componentPosition) {
		final Position screenPosition = Toolkit.toScreen(componentPosition, (IComponent) component);
		return Toolkit.toLocal(screenPosition, (IComponent) this);
	}

	public Position toComponent(final Position componentPosition, final IComponentCommon component) {
		final Position screenPosition = Toolkit.toScreen(componentPosition, (IComponent) this);
		return Toolkit.toLocal(screenPosition, (IComponent) component);
	}
}
