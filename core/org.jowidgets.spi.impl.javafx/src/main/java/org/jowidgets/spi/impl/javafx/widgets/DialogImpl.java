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

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import org.jowidgets.common.color.IColorConstant;
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
		getUiReference().initModality(Modality.WINDOW_MODAL);
		getUiReference().initOwner((Window) parentUiReference);

		final Scene scene = new Scene(new Group());
		getUiReference().setScene(scene);

		if (!setup.isDecorated()) {
			getUiReference().initStyle(StageStyle.UNDECORATED);
			//TODO DB init Border via CSS

		}

	}

	@Override
	public Stage getUiReference() {
		return (Stage) super.getUiReference();
	}

	@Override
	public void setDefaultButton(final IButtonCommon button) {
		// TODO Auto-generated method stub
	}

	@Override
	public Rectangle getClientArea() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setVisible(final boolean visible) {
		getUiReference().show();
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean remove(final IControlCommon control) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeAll() {
		// TODO Auto-generated method stub
	}

	@Override
	public IMenuBarSpi createMenuBar() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(
		final Integer index,
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		final Object layoutConstraints) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(
		final Integer index,
		final ICustomWidgetCreator<WIDGET_TYPE> creator,
		final Object layoutConstraints) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTabOrder(final List<? extends IControlCommon> tabOrder) {
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
	public Dimension computeDecoratedSize(final Dimension clientAreaSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMinSize(final Dimension minSize) {
		// TODO Auto-generated method stub

	}

}
