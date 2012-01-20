/*
 * Copyright (c) 2012, grossmann
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

package org.jowidgets.spi.impl.javafx.widgets;

import java.util.List;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.IButtonCommon;
import org.jowidgets.common.widgets.IControlCommon;
import org.jowidgets.common.widgets.IDisplayCommon;
import org.jowidgets.common.widgets.controller.IComponentListener;
import org.jowidgets.common.widgets.controller.IFocusListener;
import org.jowidgets.common.widgets.controller.IKeyListener;
import org.jowidgets.common.widgets.controller.IMouseListener;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.common.widgets.controller.IWindowListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetCreator;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.spi.widgets.IFrameSpi;
import org.jowidgets.spi.widgets.IMenuBarSpi;
import org.jowidgets.spi.widgets.IPopupMenuSpi;
import org.jowidgets.spi.widgets.setup.IFrameSetupSpi;

public class FrameImpl implements IFrameSpi {

	private final Stage stage;
	private final Group group;
	private final Scene scene;

	public FrameImpl(final IGenericWidgetFactory factory, final IFrameSetupSpi setup) {
		this.stage = new Stage();
		this.group = new Group();
		this.scene = new Scene(group);

		stage.setTitle(setup.getTitle());
	}

	@Override
	public <WIDGET_TYPE extends IDisplayCommon, DESCRIPTOR_TYPE extends IWidgetDescriptor<? extends WIDGET_TYPE>> WIDGET_TYPE createChildWindow(
		final DESCRIPTOR_TYPE descriptor) {

		return null;
	}

	@Override
	public Stage getUiReference() {
		return stage;
	}

	@Override
	public void setEnabled(final boolean enabled) {}

	@Override
	public boolean isEnabled() {
		return false;
	}

	@Override
	public IPopupMenuSpi createPopupMenu() {
		return null;
	}

	@Override
	public void redraw() {}

	@Override
	public void setRedrawEnabled(final boolean enabled) {

	}

	@Override
	public boolean requestFocus() {
		stage.requestFocus();
		return stage.focusedProperty().getValue();
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {

	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {

	}

	@Override
	public IColorConstant getForegroundColor() {
		return null;
	}

	@Override
	public IColorConstant getBackgroundColor() {
		return null;
	}

	@Override
	public void setCursor(final Cursor cursor) {
		if (cursor == Cursor.ARROW) {
			scene.setCursor(javafx.scene.Cursor.MOVE);
		}
		else if (cursor == Cursor.DEFAULT) {
			scene.setCursor(javafx.scene.Cursor.DEFAULT);
		}
		else if (cursor == Cursor.WAIT) {
			scene.setCursor(javafx.scene.Cursor.WAIT);
		}
	}

	@Override
	public void setVisible(final boolean visible) {
		if (visible) {
			stage.show();
		}
		else {
			stage.hide();
		}
	}

	@Override
	public boolean isVisible() {
		return false;
	}

	@Override
	public Dimension getSize() {
		return new Dimension((int) stage.getHeight(), (int) stage.getWidth());
	}

	@Override
	public void setSize(final Dimension size) {
		stage.setHeight(size.getHeight());
		stage.setWidth(size.getWidth());
	}

	@Override
	public Position getPosition() {
		return new Position((int) stage.getX(), (int) stage.getY());
	}

	@Override
	public void setPosition(final Position position) {
		stage.setX(position.getX());
		stage.setY(position.getY());
	}

	@Override
	public void addComponentListener(final IComponentListener componentListener) {

	}

	@Override
	public void removeComponentListener(final IComponentListener componentListener) {

	}

	@Override
	public void addFocusListener(final IFocusListener listener) {

	}

	@Override
	public void removeFocusListener(final IFocusListener listener) {

	}

	@Override
	public void addKeyListener(final IKeyListener listener) {

	}

	@Override
	public void removeKeyListener(final IKeyListener listener) {

	}

	@Override
	public void addMouseListener(final IMouseListener listener) {

	}

	@Override
	public void removeMouseListener(final IMouseListener listener) {

	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {

	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {

	}

	@Override
	public Rectangle getParentBounds() {
		return new Rectangle(getPosition(), getSize());
	}

	@Override
	public void pack() {

	}

	@Override
	public void dispose() {

	}

	@Override
	public void addWindowListener(final IWindowListener listener) {

	}

	@Override
	public void removeWindowListener(final IWindowListener listener) {

	}

	@Override
	public <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(
		final Integer index,
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		final Object layoutConstraints) {

		return null;
	}

	@Override
	public <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(
		final Integer index,
		final ICustomWidgetCreator<WIDGET_TYPE> creator,
		final Object layoutConstraints) {
		return null;
	}

	@Override
	public boolean remove(final IControlCommon control) {
		return false;
	}

	@Override
	public void setTabOrder(final List<? extends IControlCommon> tabOrder) {

	}

	@Override
	public void setLayout(final ILayoutDescriptor layoutDescriptor) {

	}

	@Override
	public void layoutBegin() {

	}

	@Override
	public void layoutEnd() {

	}

	@Override
	public void removeAll() {

	}

	@Override
	public Rectangle getClientArea() {
		return null;
	}

	@Override
	public Dimension computeDecoratedSize(final Dimension clientAreaSize) {
		return null;
	}

	@Override
	public void setMinSize(final Dimension minSize) {

	}

	@Override
	public IMenuBarSpi createMenuBar() {
		return null;
	}

	@Override
	public void setDefaultButton(final IButtonCommon button) {

	}

}
