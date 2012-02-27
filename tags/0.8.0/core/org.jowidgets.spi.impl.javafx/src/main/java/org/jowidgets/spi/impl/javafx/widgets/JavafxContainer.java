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
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.IControlCommon;
import org.jowidgets.common.widgets.IWidgetCommon;
import org.jowidgets.common.widgets.controller.IComponentListener;
import org.jowidgets.common.widgets.controller.IFocusListener;
import org.jowidgets.common.widgets.controller.IKeyListener;
import org.jowidgets.common.widgets.controller.IMouseListener;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetCreator;
import org.jowidgets.common.widgets.factory.ICustomWidgetFactory;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.spi.impl.javafx.util.CursorConvert;
import org.jowidgets.spi.widgets.IContainerSpi;
import org.jowidgets.spi.widgets.IPopupMenuSpi;

public class JavafxContainer implements IContainerSpi {

	private final IGenericWidgetFactory factory;
	private final Pane pane;
	private final JavafxComponent componentDelegate;

	public JavafxContainer(final IGenericWidgetFactory factory, final Pane pane) {
		this.pane = pane;
		this.factory = factory;
		componentDelegate = new JavafxComponent(pane);
	}

	@Override
	public IPopupMenuSpi createPopupMenu() {
		return componentDelegate.createPopupMenu();
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
		componentDelegate.redraw();
	}

	@Override
	public void setRedrawEnabled(final boolean enabled) {
		componentDelegate.setRedrawEnabled(enabled);
	}

	@Override
	public boolean requestFocus() {
		getUiReference().requestFocus();
		return getUiReference().focusedProperty().getValue();
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		componentDelegate.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		componentDelegate.setBackgroundColor(colorValue);

	}

	@Override
	public IColorConstant getForegroundColor() {
		return componentDelegate.getForegroundColor();
	}

	@Override
	public IColorConstant getBackgroundColor() {
		return componentDelegate.getBackgroundColor();
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
		componentDelegate.setSize(size);
	}

	@Override
	public Position getPosition() {
		return componentDelegate.getPosition();
	}

	@Override
	public void setPosition(final Position position) {
		componentDelegate.setPosition(position);
	}

	@Override
	public void addComponentListener(final IComponentListener componentListener) {
		componentDelegate.addComponentListener(componentListener);

	}

	@Override
	public void removeComponentListener(final IComponentListener componentListener) {
		componentDelegate.removeComponentListener(componentListener);

	}

	@Override
	public void addFocusListener(final IFocusListener listener) {
		componentDelegate.addFocusListener(listener);

	}

	@Override
	public void removeFocusListener(final IFocusListener listener) {
		componentDelegate.removeFocusListener(listener);

	}

	@Override
	public void addKeyListener(final IKeyListener listener) {
		componentDelegate.addKeyListener(listener);

	}

	@Override
	public void removeKeyListener(final IKeyListener listener) {
		componentDelegate.removeKeyListener(listener);

	}

	@Override
	public void addMouseListener(final IMouseListener listener) {
		componentDelegate.addMouseListener(listener);

	}

	@Override
	public void removeMouseListener(final IMouseListener listener) {
		componentDelegate.removeMouseListener(listener);
	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		componentDelegate.addPopupDetectionListener(listener);
	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		componentDelegate.removePopupDetectionListener(listener);

	}

	@Override
	public void setLayout(final ILayoutDescriptor layoutDescriptor) {
		//TODO DB set MigLayout Port for JavaFX
	}

	@Override
	public void layoutBegin() {
		// TODO DB Auto-generated method stub
	}

	@Override
	public void layoutEnd() {
		getUiReference().layout();

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
		int width = clientAreaSize.getWidth();
		int height = clientAreaSize.getHeight();
		final Pane pane2 = (Pane) getUiReference().getScene().getRoot();
		final Insets insets = pane2.getInsets();
		if (insets != null) {
			width = (int) (width + insets.getLeft() + insets.getRight());
			height = (int) (height + insets.getTop() + insets.getBottom());
		}
		return new Dimension(width, height);
	}

	@Override
	public <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(
		final Integer index,
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		final Object layoutConstraints) {
		final WIDGET_TYPE result = factory.create(getUiReference(), descriptor);
		getUiReference().getChildren().add((Node) result.getUiReference());
		setLayoutConstraints(result, layoutConstraints);
		return result;
	}

	@Override
	public <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(
		final Integer index,
		final ICustomWidgetCreator<WIDGET_TYPE> creator,
		final Object layoutConstraints) {
		final ICustomWidgetFactory customWidgetFactory = createCustomWidgetFactory();
		final WIDGET_TYPE result = creator.create(customWidgetFactory);
		getUiReference().getChildren().add((Node) result.getUiReference());
		setLayoutConstraints(result, layoutConstraints);
		return result;
	}

	private ICustomWidgetFactory createCustomWidgetFactory() {
		return new ICustomWidgetFactory() {
			@Override
			public <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE create(
				final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor) {
				return factory.create(getUiReference(), descriptor);
			}
		};
	}

	@Override
	public boolean remove(final IControlCommon control) {
		return getUiReference().getChildren().remove(control.getUiReference());
	}

	@Override
	public void setTabOrder(final List<? extends IControlCommon> tabOrder) {
		// TODO DB Auto-generated method stub

	}

	private void setLayoutConstraints(final IWidgetCommon widget, final Object layoutConstraints) {
		final Object object = widget.getUiReference();
		if (object instanceof Parent) {
			final Parent control = (Parent) object;
			control.setUserData(layoutConstraints);
		}
		else {
			throw new IllegalArgumentException("'"
				+ Parent.class
				+ "' excpected, but '"
				+ object.getClass().getName()
				+ "' found.");
		}
	}

}
