/*
 * Copyright (c) 2012, David Bauknecht
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

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.IControlCommon;
import org.jowidgets.common.widgets.controller.IComponentListener;
import org.jowidgets.common.widgets.controller.IFocusListener;
import org.jowidgets.common.widgets.controller.IKeyListener;
import org.jowidgets.common.widgets.controller.IMouseListener;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetCreator;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.spi.impl.javafx.util.CursorConvert;
import org.jowidgets.spi.widgets.IContainerSpi;
import org.jowidgets.spi.widgets.IPopupMenuSpi;

public class JavafxContainer implements IContainerSpi {

	private final IGenericWidgetFactory factory;
	private final Pane pane;

	public JavafxContainer(final IGenericWidgetFactory factory, final Pane pane) {
		this.pane = pane;
		this.factory = factory;
	}

	@Override
	public IPopupMenuSpi createPopupMenu() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pane getUiReference() {
		return pane;
	}

	@Override
	public void setEnabled(final boolean enabled) {
		getUiReference().disableProperty().setValue(enabled);

	}

	@Override
	public boolean isEnabled() {
		return getUiReference().disableProperty().getValue();
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
		// TODO Auto-generated method stub

	}

	@Override
	public IColorConstant getForegroundColor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IColorConstant getBackgroundColor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCursor(final Cursor cursor) {
		getUiReference().setCursor(CursorConvert.convert(cursor));

	}

	@Override
	public void setVisible(final boolean visible) {
		getUiReference().setVisible(visible);

	}

	@Override
	public boolean isVisible() {
		return getUiReference().isVisible();
	}

	@Override
	public Dimension getSize() {
		return new Dimension((int) getUiReference().getWidth(), (int) getUiReference().getHeight());
	}

	@Override
	public void setSize(final Dimension size) {
		getUiReference().resize(size.getWidth(), size.getHeight());
	}

	@Override
	public Position getPosition() {
		return new Position((int) getUiReference().getLayoutX(), (int) getUiReference().getLayoutY());
	}

	@Override
	public void setPosition(final Position position) {
		getUiReference().setLayoutX(position.getX());
		getUiReference().setLayoutY(position.getY());
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

	@Override
	public void setLayout(final ILayoutDescriptor layoutDescriptor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void layoutBegin() {
		// TODO Auto-generated method stub

	}

	@Override
	public void layoutEnd() {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeAll() {
		getUiReference().getChildren().clear();

	}

	@Override
	public Rectangle getClientArea() {
		final Insets insets = getUiReference().getInsets();
		final int x = (int) insets.getLeft();
		final int y = (int) insets.getTop();
		final Dimension size = getSize();
		final int width = (int) (size.getWidth() - insets.getLeft() - insets.getRight());
		final int height = (int) (size.getHeight() - insets.getTop() - insets.getBottom());
		return new Rectangle(x, y, width, height);
	}

	@Override
	public Dimension computeDecoratedSize(final Dimension clientAreaSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(
		final Integer index,
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		final Object layoutConstraints) {
		final WIDGET_TYPE result = factory.create(getUiReference(), descriptor);
		getUiReference().getChildren().add((Node) result.getUiReference());

		return result;
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
		return getUiReference().getChildren().remove(control.getUiReference());
	}

	@Override
	public void setTabOrder(final List<? extends IControlCommon> tabOrder) {
		// TODO Auto-generated method stub

	}

}
