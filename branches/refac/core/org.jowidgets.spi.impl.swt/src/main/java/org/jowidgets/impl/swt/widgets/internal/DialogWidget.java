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
package org.jowidgets.impl.swt.widgets.internal;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jowidgets.api.util.ColorSettingsInvoker;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.api.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.impl.swt.color.IColorCache;
import org.jowidgets.impl.swt.image.SwtImageRegistry;
import org.jowidgets.impl.swt.widgets.SwtWindowWidget;
import org.jowidgets.spi.widgets.IFrameWidgetSpi;
import org.jowidgets.spi.widgets.descriptor.setup.IDialogSetupSpi;

public class DialogWidget extends SwtWindowWidget implements IFrameWidgetSpi {

	public DialogWidget(
		final IGenericWidgetFactory factory,
		final IColorCache colorCache,
		final SwtImageRegistry imageRegistry,
		final IWidget parent,
		final IDialogSetupSpi setup) {

		super(factory, colorCache, new Shell((Shell) parent.getUiReference(), SWT.DIALOG_TRIM
			| SWT.APPLICATION_MODAL
			| SWT.RESIZE));

		if (setup.getTitle() != null) {
			getUiReference().setText(setup.getTitle());
		}

		setLayout(setup.getLayout());
		setIcon(imageRegistry, setup.getIcon());
		ColorSettingsInvoker.setColors(setup, this);
	}

	@Override
	public void setVisible(final boolean visible) {
		if (visible) {

			getUiReference().setVisible(true);

			// do not dispose the shell on closing
			getUiReference().addShellListener(new ShellAdapter() {

				@Override
				public void shellClosed(final ShellEvent e) {
					e.doit = false;
					setVisible(false);
				}

			});

			final Shell shell = getUiReference();
			final Display display = shell.getDisplay();

			while (!shell.isDisposed() && shell.isVisible()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}

		}
		else {
			getUiReference().setVisible(false);
		}
	}

}
