/*
 * Copyright (c) 2011, H.Westphal
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 *   names of its contributors may be used to endorse or promote products
 *   derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL jo-widgets.org BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */

package org.jowidgets.addons.map.swing;

import java.awt.Canvas;

import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

abstract class AbstractSwtThread<T> extends Thread {

	interface IWidgetCallback<T> {
		void onWidgetCreated(T widget);
	}

	private final Canvas canvas;
	private final IWidgetCallback<T> callback;

	AbstractSwtThread(final Canvas canvas, final IWidgetCallback<T> callback) {
		this.canvas = canvas;
		this.callback = callback;
		setName("swt-" + System.currentTimeMillis());
		setDaemon(true);
	}

	@Override
	public void run() {
		while (!canvas.isDisplayable()) {
			try {
				Thread.sleep(100);
			}
			catch (final InterruptedException e) {
				Thread.currentThread().interrupt();
				return;
			}
		}
		final Display display = new Display();
		try {
			final Shell shell = SWT_AWT.new_Shell(display, canvas);
			shell.setLayout(new FillLayout());
			final T widget = createWidget(shell);
			display.asyncExec(new Runnable() {
				@Override
				public void run() {
					callback.onWidgetCreated(widget);
				}
			});
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		}
		finally {
			display.dispose();
		}
	}

	protected abstract T createWidget(Shell shell);

}
