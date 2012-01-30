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

import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Window;

import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.IDisplayCommon;
import org.jowidgets.common.widgets.controller.IComponentListener;
import org.jowidgets.common.widgets.controller.IFocusListener;
import org.jowidgets.common.widgets.controller.IKeyListener;
import org.jowidgets.common.widgets.controller.IMouseListener;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.common.widgets.controller.IWindowListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.spi.widgets.IPopupMenuSpi;
import org.jowidgets.spi.widgets.IWindowSpi;

public class JavafxWindow implements IWindowSpi {

	private final Window stage;

	public JavafxWindow(final IGenericWidgetFactory factory, final Stage stage, final boolean closeable) {

		this.stage = stage;

	}

	@Override
	public Rectangle getParentBounds() {
		return new Rectangle(getPosition(), getSize());
	}

	@Override
	public void pack() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

	@Override
	public void addWindowListener(final IWindowListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeWindowListener(final IWindowListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public <WIDGET_TYPE extends IDisplayCommon, DESCRIPTOR_TYPE extends IWidgetDescriptor<? extends WIDGET_TYPE>> WIDGET_TYPE createChildWindow(
		final DESCRIPTOR_TYPE descriptor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Window getUiReference() {
		return stage;
	}

	@Override
	public void setEnabled(final boolean enabled) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IPopupMenuSpi createPopupMenu() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void redraw() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setRedrawEnabled(final boolean enabled) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean requestFocus() {
		getUiReference().requestFocus();
		return getUiReference().focusedProperty().getValue();
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		stage.getScene().setFill(
				Color.rgb(
						colorValue.getDefaultValue().getRed(),
						colorValue.getDefaultValue().getGreen(),
						colorValue.getDefaultValue().getBlue()));

	}

	@Override
	public IColorConstant getForegroundColor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IColorConstant getBackgroundColor() {
		return null;
	}

	@Override
	public void setCursor(final Cursor cursor) {
		if (cursor == Cursor.ARROW) {
			getUiReference().getScene().setCursor(javafx.scene.Cursor.MOVE);
		}
		else if (cursor == Cursor.DEFAULT) {
			getUiReference().getScene().setCursor(javafx.scene.Cursor.DEFAULT);
		}
		else if (cursor == Cursor.WAIT) {
			getUiReference().getScene().setCursor(javafx.scene.Cursor.WAIT);
		}
	}

	@Override
	public void setVisible(final boolean visible) {
		if (visible) {
			((Stage) getUiReference()).show();
		}
		else {
			getUiReference().hide();
		}

	}

	@Override
	public boolean isVisible() {
		return getUiReference().isShowing();
	}

	@Override
	public Dimension getSize() {
		return new Dimension((int) stage.getWidth(), (int) stage.getHeight());
	}

	@Override
	public void setSize(final Dimension size) {
		getUiReference().setHeight(size.getHeight());
		getUiReference().setWidth(size.getWidth());

	}

	@Override
	public Position getPosition() {
		return new Position((int) stage.getX(), (int) stage.getY());
	}

	@Override
	public void setPosition(final Position position) {
		getUiReference().setX(position.getX());
		getUiReference().setY(position.getY());
	}

	@Override
	public void addComponentListener(final IComponentListener componentListener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeComponentListener(final IComponentListener componentListener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addFocusListener(final IFocusListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeFocusListener(final IFocusListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addKeyListener(final IKeyListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeKeyListener(final IKeyListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addMouseListener(final IMouseListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeMouseListener(final IMouseListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		// TODO Auto-generated method stub

	}

}
