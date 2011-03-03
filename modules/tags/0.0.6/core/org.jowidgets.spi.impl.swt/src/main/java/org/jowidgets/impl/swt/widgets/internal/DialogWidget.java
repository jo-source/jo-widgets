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
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.impl.swt.color.IColorCache;
import org.jowidgets.impl.swt.image.SwtImageRegistry;
import org.jowidgets.impl.swt.widgets.SwtWindowWidget;
import org.jowidgets.spi.widgets.IFrameSpi;
import org.jowidgets.spi.widgets.setup.IDialogSetupSpi;

public class DialogWidget extends SwtWindowWidget implements IFrameSpi {

	private boolean programaticClose;

	public DialogWidget(
		final IGenericWidgetFactory factory,
		final IColorCache colorCache,
		final SwtImageRegistry imageRegistry,
		final Object parentUiReference,
		final IDialogSetupSpi setup) {

		super(factory, colorCache, new Shell((Shell) parentUiReference, getStyle(setup)));

		this.programaticClose = false;

		if (setup.getTitle() != null) {
			getUiReference().setText(setup.getTitle());
		}

		setLayout(setup.getLayout());
		setIcon(imageRegistry, setup.getIcon());

		getUiReference().setBackgroundMode(SWT.INHERIT_DEFAULT);

		getUiReference().addShellListener(new ShellListener() {

			@Override
			public void shellActivated(final ShellEvent e) {
				getWindowObservableDelegate().fireWindowActivated();
			}

			@Override
			public void shellDeactivated(final ShellEvent e) {
				getWindowObservableDelegate().fireWindowDeactivated();
			}

			@Override
			public void shellIconified(final ShellEvent e) {
				getWindowObservableDelegate().fireWindowIconified();
			}

			@Override
			public void shellDeiconified(final ShellEvent e) {
				getWindowObservableDelegate().fireWindowDeiconified();
			}

			@Override
			public void shellClosed(final ShellEvent e) {
				if (!programaticClose) {
					e.doit = false;
					setVisible(false);
				}
				else {
					getWindowObservableDelegate().fireWindowClosed();
				}
			}
		});

	}

	@Override
	public void setVisible(final boolean visible) {
		if (visible) {
			getUiReference().open();

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

	private static int getStyle(final IDialogSetupSpi setup) {
		int result = SWT.DIALOG_TRIM;
		if (true) {
			result = result | SWT.APPLICATION_MODAL;
		}
		if (setup.isResizable()) {
			result = result | SWT.RESIZE;
		}
		return result;
	}

	@Override
	public void close() {
		programaticClose = true;
		getUiReference().close();
		programaticClose = false;
	}

}
