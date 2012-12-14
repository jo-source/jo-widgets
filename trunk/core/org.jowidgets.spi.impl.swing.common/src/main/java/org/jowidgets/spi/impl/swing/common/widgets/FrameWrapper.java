/*
 * Copyright (c) 2010, Michael Grossmann
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
package org.jowidgets.spi.impl.swing.common.widgets;

import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JRootPane;

import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.IButtonCommon;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.spi.impl.swing.common.util.DecorationCalc;
import org.jowidgets.spi.impl.swing.common.util.FrameUtil;
import org.jowidgets.spi.widgets.IFrameSpi;
import org.jowidgets.spi.widgets.IMenuBarSpi;
import org.jowidgets.util.TypeCast;

public class FrameWrapper extends SwingWindow implements IFrameSpi {

	public FrameWrapper(final IGenericWidgetFactory factory, final Window uiReference) {
		super(factory, uiReference, true);
	}

	@Override
	public Window getUiReference() {
		return super.getUiReference();
	}

	@Override
	public IMenuBarSpi createMenuBar() {
		final JMenuBar menuBar = new JMenuBar();
		if (getUiReference() instanceof JDialog) {
			((JDialog) getUiReference()).setJMenuBar(menuBar);
		}
		else if (getUiReference() instanceof JFrame) {
			((JFrame) getUiReference()).setJMenuBar(menuBar);
		}
		else {
			throw new IllegalStateException("Type '" + getUiReference().getClass().getName() + "' does not support  a menu bar");
		}

		return new MenuBarImpl(menuBar);
	}

	@Override
	public Rectangle getClientArea() {
		if (getUiReference() instanceof JDialog) {
			return DecorationCalc.getClientArea(((JDialog) getUiReference()).getContentPane());
		}
		else if (getUiReference() instanceof JFrame) {
			return DecorationCalc.getClientArea(((JFrame) getUiReference()).getContentPane());
		}
		else {
			return super.getClientArea();
		}
	}

	@Override
	public void setTitle(final String title) {
		if (getUiReference() instanceof JDialog) {
			((JDialog) getUiReference()).setTitle(title);
		}
		else if (getUiReference() instanceof JFrame) {
			((JFrame) getUiReference()).setTitle(title);
		}
	}

	@Override
	public void setDefaultButton(final IButtonCommon button) {
		if (getUiReference() instanceof JFrame) {
			setDefaultButton(((JFrame) getUiReference()).getRootPane(), button);
		}
		else if (getUiReference() instanceof JDialog) {
			setDefaultButton(((JDialog) getUiReference()).getRootPane(), button);
		}
	}

	private void setDefaultButton(final JRootPane rootPane, final IButtonCommon button) {
		if (button != null) {
			rootPane.setDefaultButton(TypeCast.toType(button.getUiReference(), JButton.class));
		}
		else {
			rootPane.setDefaultButton(null);
		}
	}

	@Override
	public void setMaximized(final boolean maximized) {
		if (getUiReference() instanceof JFrame) {
			FrameUtil.setMaximized((JFrame) getUiReference(), maximized);
		}
	}

	@Override
	public boolean isMaximized() {
		if (getUiReference() instanceof JFrame) {
			return FrameUtil.isMaximized((JFrame) getUiReference());
		}
		else {
			return false;
		}
	}

	@Override
	public void setIconfied(final boolean iconfied) {
		if (getUiReference() instanceof JFrame) {
			FrameUtil.setIconfied((JFrame) getUiReference(), iconfied);
		}
	}

	@Override
	public boolean isIconfied() {
		if (getUiReference() instanceof JFrame) {
			return FrameUtil.isIconfied((JFrame) getUiReference());
		}
		else {
			return false;
		}
	}

}
