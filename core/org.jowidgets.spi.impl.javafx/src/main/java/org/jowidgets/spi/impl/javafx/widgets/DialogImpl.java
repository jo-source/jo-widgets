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

import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.IButtonCommon;
import org.jowidgets.common.widgets.IControlCommon;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetCreator;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.spi.widgets.IFrameSpi;
import org.jowidgets.spi.widgets.IMenuBarSpi;
import org.jowidgets.spi.widgets.setup.IDialogSetupSpi;

public class DialogImpl extends JavafxWindow implements IFrameSpi {

	public DialogImpl(final IGenericWidgetFactory factory, final Object parentUiReference, final IDialogSetupSpi setup) {
		super(factory, new Stage(), setup.isCloseable());
		getUiReference().setTitle(setup.getTitle());
		getUiReference().setResizable(setup.isResizable());
		if (setup.isModal()) {
			getUiReference().initModality(Modality.WINDOW_MODAL);
		}
		getUiReference().initOwner((Window) parentUiReference);

		if (!setup.isDecorated()) {
			getUiReference().initStyle(StageStyle.UNDECORATED);
		}
	}

	@Override
	public Stage getUiReference() {
		return super.getUiReference();
	}

	@Override
	public void setDefaultButton(final IButtonCommon button) {
		((Button) button.getUiReference()).setDefaultButton(true);
	}

	@Override
	public void setTitle(final String title) {
		getUiReference().setTitle(title);
	}

	@Override
	public Rectangle getClientArea() {
		final Pane paneTmp = (Pane) getUiReference().getScene().getRoot();
		final Insets insets = paneTmp.getInsets();
		final int x = (int) insets.getLeft();
		final int y = (int) insets.getTop();
		final Dimension size = new Dimension((int) paneTmp.getWidth(), (int) paneTmp.getHeight());
		final int width = (int) (size.getWidth() - insets.getLeft() - insets.getRight());
		final int height = (int) (size.getHeight() - insets.getTop() - insets.getBottom());
		return new Rectangle(x, y, width, height);
	}

	@Override
	public Dimension computeDecoratedSize(final Dimension clientAreaSize) {
		int width = clientAreaSize.getWidth();
		int height = clientAreaSize.getHeight();
		final Pane paneTmp = (Pane) getUiReference().getScene().getRoot();
		final Insets insets = paneTmp.getInsets();
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

	@Override
	public <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(
		final Integer index,
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		final Object layoutConstraints) {
		final WIDGET_TYPE widget = getContainerDelegate().add(index, descriptor, layoutConstraints);
		getUiReference().sizeToScene();
		return widget;
	}

	@Override
	public <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(
		final Integer index,
		final ICustomWidgetCreator<WIDGET_TYPE> creator,
		final Object layoutConstraints) {
		final WIDGET_TYPE widget = getContainerDelegate().add(index, creator, layoutConstraints);
		getUiReference().sizeToScene();
		return widget;
	}

	@Override
	public boolean remove(final IControlCommon control) {
		return ((Pane) getUiReference().getScene().getRoot()).getChildren().remove(control.getUiReference());
	}

	@Override
	public void setTabOrder(final List<? extends IControlCommon> tabOrder) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setLayout(final ILayoutDescriptor layoutDescriptor) {
		getContainerDelegate().setLayout(layoutDescriptor);
	}

	@Override
	public void layoutBegin() {
		getUiReference().getScene().getRoot().layout();
	}

	@Override
	public void layoutEnd() {
		getUiReference().getScene().getRoot().layout();
	}

	@Override
	public void removeAll() {
		((Pane) getUiReference().getScene().getRoot()).getChildren().clear();
	}

	@Override
	public IMenuBarSpi createMenuBar() {
		final MenuBar bar = new MenuBar();
		final Parent oldRoot = getUiReference().getScene().getRoot();
		final VBox newRoot = new VBox();
		newRoot.getChildren().addAll(bar, oldRoot);
		getUiReference().getScene().setRoot(newRoot);
		return new MenuBarImpl(bar);
	}
}
