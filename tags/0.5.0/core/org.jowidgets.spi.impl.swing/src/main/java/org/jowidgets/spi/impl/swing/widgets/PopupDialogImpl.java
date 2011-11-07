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
package org.jowidgets.spi.impl.swing.widgets;

import java.awt.Component;
import java.awt.Window;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JTextField;
import javax.swing.border.Border;

import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.IControlCommon;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.spi.impl.swing.image.SwingImageRegistry;
import org.jowidgets.spi.impl.swing.util.ColorConvert;
import org.jowidgets.spi.impl.swing.util.DecorationCalc;
import org.jowidgets.spi.impl.swing.widgets.util.ChildRemover;
import org.jowidgets.spi.widgets.IPopupDialogSpi;
import org.jowidgets.spi.widgets.setup.IPopupDialogSetupSpi;

public class PopupDialogImpl extends SwingWindow implements IPopupDialogSpi {

	private static final Border BORDER = new JTextField().getBorder();

	public PopupDialogImpl(
		final IGenericWidgetFactory factory,
		final SwingImageRegistry imageRegistry,
		final Object parentUiReference,
		final IPopupDialogSetupSpi setup) {
		super(factory, new JDialog((Window) parentUiReference), false);

		getUiReference().setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		getUiReference().setUndecorated(true);
		getUiReference().setModal(false);

		if (setup.hasBorder() && getUiReference().getContentPane() instanceof JComponent) {
			((JComponent) getUiReference().getContentPane()).setBorder(BORDER);
		}

	}

	@Override
	public JDialog getUiReference() {
		return (JDialog) super.getUiReference();
	}

	@Override
	public Rectangle getClientArea() {
		return DecorationCalc.getClientArea(getUiReference().getContentPane());
	}

	@Override
	public void setVisible(final boolean visible) {
		getUiReference().setVisible(visible);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		getUiReference().getContentPane().setBackground(ColorConvert.convert(colorValue));
		super.setBackgroundColor(colorValue);
	}

	@Override
	public boolean remove(final IControlCommon control) {
		return ChildRemover.removeChild(getUiReference().getContentPane(), (Component) control.getUiReference());
	}

	@Override
	public void removeAll() {
		getUiReference().getContentPane().removeAll();
	}

}
