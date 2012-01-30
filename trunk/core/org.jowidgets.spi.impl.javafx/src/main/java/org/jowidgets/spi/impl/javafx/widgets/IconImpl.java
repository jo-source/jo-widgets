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

import javafx.scene.control.Label;

import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controller.IComponentListener;
import org.jowidgets.common.widgets.controller.IFocusListener;
import org.jowidgets.common.widgets.controller.IKeyListener;
import org.jowidgets.common.widgets.controller.IMouseListener;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.spi.impl.javafx.image.JavafxImageRegistry;
import org.jowidgets.spi.widgets.IIconSpi;
import org.jowidgets.spi.widgets.IPopupMenuSpi;
import org.jowidgets.spi.widgets.setup.IIconSetupSpi;

public class IconImpl implements IIconSpi {
	private final Label label;

	public IconImpl(final IIconSetupSpi setup) {
		label = new Label();
		setIcon(setup.getIcon());

	}

	@Override
	public void setIcon(final IImageConstant icon) {
		if (icon != null) {
			getUiReference().setGraphic(JavafxImageRegistry.getInstance().getImageHandle(icon).getImage());
		}
		else {
			getUiReference().setGraphic(null);
		}
	}

	@Override
	public Label getUiReference() {
		return label;
	}

	@Override
	public IPopupMenuSpi createPopupMenu() {
		// TODO Auto-generated method stub
		return null;
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
	public void redraw() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setRedrawEnabled(final boolean enabled) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean requestFocus() {
		// TODO Auto-generated method stub
		return false;
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
		// TODO Auto-generated method stub

	}

	@Override
	public void setVisible(final boolean visible) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isVisible() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Dimension getSize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSize(final Dimension size) {
		// TODO Auto-generated method stub

	}

	@Override
	public Position getPosition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPosition(final Position position) {
		// TODO Auto-generated method stub

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
	public void setToolTipText(final String toolTip) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLayoutConstraints(final Object layoutConstraints) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getLayoutConstraints() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Dimension getMinSize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Dimension getPreferredSize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Dimension getMaxSize() {
		// TODO Auto-generated method stub
		return null;
	}

}
