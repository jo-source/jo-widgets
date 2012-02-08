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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.IButtonCommon;
import org.jowidgets.common.widgets.IControlCommon;
import org.jowidgets.common.widgets.IDisplayCommon;
import org.jowidgets.common.widgets.IWidgetCommon;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetCreator;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.common.widgets.layout.ILayouter;
import org.jowidgets.spi.impl.javafx.layout.LayoutManagerImpl;
import org.jowidgets.spi.widgets.IFrameSpi;
import org.jowidgets.spi.widgets.IMenuBarSpi;
import org.jowidgets.spi.widgets.setup.IFrameSetupSpi;

public class FrameImpl extends JavafxWindow implements IFrameSpi {

	private final IGenericWidgetFactory factory;
	private final Pane pane;
	private final Scene scene;

	public FrameImpl(final IGenericWidgetFactory factory, final IFrameSetupSpi setup) {
		super(factory, new Stage(), setup.isCloseable());
		getUiReference().setTitle(setup.getTitle());
		getUiReference().setResizable(setup.isResizable());
		this.factory = factory;
		pane = new Pane();
		scene = new Scene(pane);

		getUiReference().setScene(scene);
	}

	@Override
	public <WIDGET_TYPE extends IDisplayCommon, DESCRIPTOR_TYPE extends IWidgetDescriptor<? extends WIDGET_TYPE>> WIDGET_TYPE createChildWindow(
		final DESCRIPTOR_TYPE descriptor) {

		return null;
	}

	@Override
	public Stage getUiReference() {
		return (Stage) super.getUiReference();
	}

	@Override
	public IMenuBarSpi createMenuBar() {
		return null;
	}

	@Override
	public void setDefaultButton(final IButtonCommon button) {
		((Button) button.getUiReference()).setDefaultButton(true);

	}

	@Override
	public <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(
		final Integer index,
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		final Object layoutConstraints) {
		final WIDGET_TYPE result = factory.create(getUiReference().getScene(), descriptor);
		((Pane) scene.getRoot()).getChildren().add((Node) result.getUiReference());
		setLayoutConstraints(result, layoutConstraints);
		return result;
	}

	@Override
	public <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(
		final Integer index,
		final ICustomWidgetCreator<WIDGET_TYPE> creator,
		final Object layoutConstraints) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(final IControlCommon control) {
		return ((Pane) scene.getRoot()).getChildren().remove(control.getUiReference());
	}

	@Override
	public void setTabOrder(final List<? extends IControlCommon> tabOrder) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setLayout(final ILayoutDescriptor layoutDescriptor) {
		if (layoutDescriptor != null) {
			scene.setRoot(new LayoutManagerImpl((ILayouter) layoutDescriptor));
		}
	}

	@Override
	public void layoutBegin() {
		throw new UnsupportedOperationException();

	}

	@Override
	public void layoutEnd() {
		throw new UnsupportedOperationException();

	}

	@Override
	public void removeAll() {
		getUiReference().getScene().getRoot().getChildrenUnmodifiable().clear();
	}

	@Override
	public Rectangle getClientArea() {
		final Pane pane2 = (Pane) getUiReference().getScene().getRoot();
		final Insets insets = pane2.getInsets();
		final int x = (int) insets.getLeft();
		final int y = (int) insets.getTop();
		final Dimension size = new Dimension(
			(int) getUiReference().getScene().getWidth(),
			(int) getUiReference().getScene().getHeight());
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
	public void setMinSize(final Dimension minSize) {
		getUiReference().setMinHeight(minSize.getHeight());
		getUiReference().setMinWidth(minSize.getWidth());
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
