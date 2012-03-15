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

import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Tooltip;
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
import org.jowidgets.common.widgets.descriptor.setup.ICompositeSetupCommon;
import org.jowidgets.common.widgets.factory.ICustomWidgetCreator;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.spi.widgets.ICompositeSpi;
import org.jowidgets.spi.widgets.IPopupMenuSpi;

public class CompositeImpl implements ICompositeSpi {
	private final JavafxContainer containerDelegate;

	public CompositeImpl(final IGenericWidgetFactory factory, final ICompositeSetupCommon setup) {
		containerDelegate = new JavafxContainer(factory, new Pane());
	}

	@Override
	public Pane getUiReference() {
		return containerDelegate.getUiReference();
	}

	@Override
	public IPopupMenuSpi createPopupMenu() {
		return containerDelegate.createPopupMenu();
	}

	@Override
	public void setEnabled(final boolean enabled) {
		containerDelegate.setEnabled(enabled);
	}

	@Override
	public boolean isEnabled() {
		return containerDelegate.isEnabled();
	}

	@Override
	public void redraw() {
		containerDelegate.redraw();
	}

	@Override
	public void setRedrawEnabled(final boolean enabled) {
		containerDelegate.setRedrawEnabled(enabled);
	}

	@Override
	public boolean requestFocus() {
		return containerDelegate.requestFocus();
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		containerDelegate.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		containerDelegate.setBackgroundColor(colorValue);
	}

	@Override
	public IColorConstant getForegroundColor() {
		return containerDelegate.getForegroundColor();
	}

	@Override
	public IColorConstant getBackgroundColor() {
		return containerDelegate.getBackgroundColor();
	}

	@Override
	public void setCursor(final Cursor cursor) {
		containerDelegate.setCursor(cursor);
	}

	@Override
	public void setVisible(final boolean visible) {
		containerDelegate.setVisible(visible);
	}

	@Override
	public boolean isVisible() {
		return containerDelegate.isVisible();
	}

	@Override
	public Dimension getSize() {
		return containerDelegate.getSize();
	}

	@Override
	public void setSize(final Dimension size) {
		containerDelegate.setSize(size);
		for (final Node node : getUiReference().getChildren()) {
			if (((Control) node).getPrefWidth() == -1d || ((Control) node).getPrefHeight() == -1d) {
				((Control) node).setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
			}
		}
	}

	@Override
	public Position getPosition() {
		return containerDelegate.getPosition();
	}

	@Override
	public void setPosition(final Position position) {
		containerDelegate.setPosition(position);
	}

	@Override
	public void addComponentListener(final IComponentListener componentListener) {
		containerDelegate.addComponentListener(componentListener);
	}

	@Override
	public void removeComponentListener(final IComponentListener componentListener) {
		containerDelegate.removeComponentListener(componentListener);
	}

	@Override
	public void addFocusListener(final IFocusListener listener) {
		containerDelegate.addFocusListener(listener);
	}

	@Override
	public void removeFocusListener(final IFocusListener listener) {
		containerDelegate.removeFocusListener(listener);
	}

	@Override
	public void addKeyListener(final IKeyListener listener) {
		containerDelegate.addKeyListener(listener);
	}

	@Override
	public void removeKeyListener(final IKeyListener listener) {
		containerDelegate.removeKeyListener(listener);
	}

	@Override
	public void addMouseListener(final IMouseListener listener) {
		containerDelegate.addMouseListener(listener);
	}

	@Override
	public void removeMouseListener(final IMouseListener listener) {
		containerDelegate.removeMouseListener(listener);
	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		containerDelegate.addPopupDetectionListener(listener);
	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		containerDelegate.removePopupDetectionListener(listener);
	}

	@Override
	public void setToolTipText(final String toolTip) {
		final Tooltip tool = new Tooltip(toolTip);
		if (toolTip == null || toolTip.isEmpty()) {
			Tooltip.uninstall(getUiReference(), tool);
		}
		else {
			Tooltip.install(getUiReference(), tool);
		}
	}

	@Override
	public void setLayoutConstraints(final Object layoutConstraints) {
		getUiReference().setUserData(layoutConstraints);
	}

	@Override
	public Object getLayoutConstraints() {
		return getUiReference().getUserData();
	}

	@Override
	public Dimension getMinSize() {
		return new Dimension((int) getUiReference().minHeight(Pane.USE_COMPUTED_SIZE), (int) getUiReference().minHeight(
				Pane.USE_COMPUTED_SIZE));
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension((int) getUiReference().prefWidth(Pane.USE_COMPUTED_SIZE), (int) getUiReference().prefHeight(
				Pane.USE_COMPUTED_SIZE));
	}

	@Override
	public Dimension getMaxSize() {
		return new Dimension((int) getUiReference().maxHeight(Pane.USE_COMPUTED_SIZE), (int) getUiReference().maxHeight(
				Pane.USE_COMPUTED_SIZE));
	}

	@Override
	public <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(
		final Integer index,
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		final Object layoutConstraints) {
		return containerDelegate.add(index, descriptor, layoutConstraints);

	}

	@Override
	public <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(
		final Integer index,
		final ICustomWidgetCreator<WIDGET_TYPE> creator,
		final Object layoutConstraints) {
		return containerDelegate.add(index, creator, layoutConstraints);
	}

	@Override
	public boolean remove(final IControlCommon control) {
		return containerDelegate.remove(control);
	}

	@Override
	public void setTabOrder(final List<? extends IControlCommon> tabOrder) {
		containerDelegate.setTabOrder(tabOrder);
	}

	@Override
	public void setLayout(final ILayoutDescriptor layoutDescriptor) {
		containerDelegate.setLayout(layoutDescriptor);
	}

	@Override
	public void layoutBegin() {
		containerDelegate.layoutBegin();
	}

	@Override
	public void layoutEnd() {
		containerDelegate.layoutEnd();
	}

	@Override
	public void removeAll() {
		containerDelegate.removeAll();
	}

	@Override
	public Rectangle getClientArea() {
		return containerDelegate.getClientArea();
	}

	@Override
	public Dimension computeDecoratedSize(final Dimension clientAreaSize) {
		return containerDelegate.computeDecoratedSize(clientAreaSize);
	}
}
