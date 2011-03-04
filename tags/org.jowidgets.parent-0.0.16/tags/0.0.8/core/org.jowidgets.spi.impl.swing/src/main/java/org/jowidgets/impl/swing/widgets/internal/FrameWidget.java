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
package org.jowidgets.impl.swing.widgets.internal;

import java.awt.Component;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.widgets.IControlCommon;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.impl.swing.image.SwingImageRegistry;
import org.jowidgets.impl.swing.util.ColorConvert;
import org.jowidgets.impl.swing.widgets.SwingWindowWidget;
import org.jowidgets.impl.swing.widgets.internal.util.ChildRemover;
import org.jowidgets.spi.widgets.IFrameSpi;
import org.jowidgets.spi.widgets.setup.IFrameSetupSpi;

public class FrameWidget extends SwingWindowWidget implements IFrameSpi {

	public FrameWidget(final IGenericWidgetFactory factory, final SwingImageRegistry imageRegistry, final IFrameSetupSpi setup) {
		super(factory, new JFrame());

		getUiReference().setTitle(setup.getTitle());
		getUiReference().setResizable(setup.isResizable());

		if (!setup.isCloseable()) {
			getUiReference().setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		}

		setIcon(setup.getIcon(), imageRegistry);
		setLayout(setup.getLayout());

	}

	@Override
	public JFrame getUiReference() {
		return (JFrame) super.getUiReference();
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
