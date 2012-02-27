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
package org.jowidgets.spi.impl.swt.common.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.widgets.Shell;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.spi.widgets.IWindowSpi;

public class WindowImpl extends SwtWindow implements IWindowSpi {

	private boolean programaticDispose;

	public WindowImpl(final IGenericWidgetFactory factory, final Shell window, final boolean isCloseable) {
		super(factory, window);

		this.programaticDispose = false;

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
				//Do not close the shell when 'X' is pressed, except
				//dispose method was invoked
				if (!programaticDispose) {
					e.doit = false;
					if (isCloseable) {
						final boolean veto = getWindowObservableDelegate().fireWindowClosing();
						if (!veto && !getUiReference().isDisposed()) {
							getUiReference().setVisible(false);
						}
					}
					else {
						return;
					}
				}
				getWindowObservableDelegate().fireWindowClosed();
			}
		});

		getUiReference().setBackgroundMode(SWT.INHERIT_DEFAULT);
	}

	@Override
	public void dispose() {
		if (!getUiReference().isDisposed()) {
			programaticDispose = true;
			if (isVisible()) {
				setVisible(false);
			}
			getUiReference().dispose();
			programaticDispose = false;
		}
	}

	@Override
	public boolean isVisible() {
		return getUiReference().isVisible();
	}

}
