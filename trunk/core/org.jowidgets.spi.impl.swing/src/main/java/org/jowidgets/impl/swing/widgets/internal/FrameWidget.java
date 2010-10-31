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

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import org.jowidgets.common.util.ColorSettingsInvoker;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.impl.swing.image.SwingImageRegistry;
import org.jowidgets.impl.swing.widgets.SwingWindowWidget;
import org.jowidgets.spi.widgets.IFrameWidgetSpi;
import org.jowidgets.spi.widgets.setup.IFrameSetupSpi;

public class FrameWidget extends SwingWindowWidget implements IFrameWidgetSpi {

	public FrameWidget(final IGenericWidgetFactory factory, final SwingImageRegistry imageRegistry, final IFrameSetupSpi setup) {
		super(factory, new JFrame());

		getUiReference().setTitle(setup.getTitle());
		getUiReference().setResizable(setup.isResizable());

		setIcon(setup.getIcon(), imageRegistry);
		setLayout(setup.getLayout());
		ColorSettingsInvoker.setColors(setup, this);

		//dispose a frame when window closed
		getUiReference().addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(final WindowEvent e) {
				close();
			}

		});
	}

	@Override
	public JFrame getUiReference() {
		return (JFrame) super.getUiReference();
	}

}
