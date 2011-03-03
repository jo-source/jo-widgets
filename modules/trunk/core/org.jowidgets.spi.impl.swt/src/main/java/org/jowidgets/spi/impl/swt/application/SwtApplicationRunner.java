/*
 * Copyright (c) 2010, grossmann
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

package org.jowidgets.spi.impl.swt.application;

import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.application.IApplicationRunner;

public class SwtApplicationRunner implements IApplicationRunner {

	private final AtomicBoolean isRunning = new AtomicBoolean();
	private Display display;

	@Override
	public void run(final IApplication application) {

		isRunning.set(true);

		//now, if someone will run the application, get the display
		final Display currentDisplay = getDisplay();

		//Create a lifecycle that disposes all windows when finished
		final IApplicationLifecycle lifecycle = new IApplicationLifecycle() {

			@Override
			public void finish() {
				if (isRunning.getAndSet(false)) {
					for (final Shell shell : currentDisplay.getShells()) {
						shell.dispose();
					}
				}
			}
		};

		//start the lifecycle
		application.start(lifecycle);

		//do the event dispatching
		while (!currentDisplay.isDisposed() && isRunning.get()) {
			if (!currentDisplay.readAndDispatch()) {
				currentDisplay.sleep();
			}
		}

	}

	private Display getDisplay() {
		if (display == null) {
			display = Display.getDefault();
			if (display.getThread() != Thread.currentThread()) {
				throw new IllegalStateException(
					"A default Display exists, but the current thread is not the owner of this display.");
			}
		}
		return display;
	}

}
