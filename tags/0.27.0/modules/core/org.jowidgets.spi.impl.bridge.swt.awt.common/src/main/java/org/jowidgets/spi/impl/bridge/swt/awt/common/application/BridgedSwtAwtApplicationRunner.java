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

package org.jowidgets.spi.impl.bridge.swt.awt.common.application;

import java.awt.Window;
import java.lang.reflect.Method;

import javax.swing.SwingUtilities;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.application.IApplicationRunner;

/**
 * This application runner ensures that all swt events and all awt events will be dispatched in the same thread.
 * 
 * This can be useful if the SWT-AWT bridge is used. Usually then there are two event dispatching threads that leads
 * to many many problems.
 * 
 * This application runner runs the awt edt and then it pumps every n millis one event on the awt event queue that
 * dispatches m swt events.
 * 
 * Remark: This application runner is a prototype that was not tested very intensive yet !!!
 * Potentially known but yet unsolved problems:
 * 
 * 1. If the awt evt has uncaught exceptions, a new thread will be created by awt.
 * After that the evt of awt and swt differs. Maybe this could be fixed by installing an uncought exception handler
 * in the awt evt.
 * 
 * 
 * 2. When modal dialogs will be created with swt or awt a new event pump will be used normally.
 * In jowidgets this may be fixed by using the same events loop pattern for modal dialogs.
 * 
 */
final class BridgedSwtAwtApplicationRunner implements IApplicationRunner {

	private BridgedSwtEventLoop swtEventLoop;
	private Thread eventDispatcherThread;
	private Thread eventPumpThread;

	@Override
	public void run(final IApplication application) {

		if (eventPumpThread != null && Thread.currentThread() != eventPumpThread) {
			throw new IllegalStateException("The application runner is already running with another event pump thread");
		}

		if (SwingUtilities.isEventDispatchThread()) {
			throw new IllegalStateException(
				"The current thread is the EventDispatcherThread. A ApplicationRunner must not be used from the EventDispatcherThread");
		}

		swtEventLoop = new BridgedSwtEventLoop();
		eventPumpThread = Thread.currentThread();
		eventDispatcherThread = swtEventLoop.getEventDispatchingThread();

		//Create a lifecycle that disposes all windows and shells when finished
		final IApplicationLifecycle lifecycle = new IApplicationLifecycle() {

			@Override
			public synchronized void finish() {
				if (eventPumpThread != null) {
					eventPumpThread = null;

					//dispose all windows
					for (final Window window : Window.getWindows()) {
						window.dispose();
					}

					//dispose all shells
					final Display currentDisplay = Display.getCurrent();
					if (currentDisplay != null && !currentDisplay.isDisposed()) {
						for (final Shell shell : currentDisplay.getShells()) {
							shell.dispose();
						}
					}

					//stop the swt event dispatching
					swtEventLoop.stop();

					//stop the awt event dispatching
					try {
						if (eventDispatcherThread != null) {
							final Class<?> edtClass = Class.forName("java.awt.EventDispatchThread");
							final Method method = edtClass.getDeclaredMethod("stopDispatching");
							method.setAccessible(true);
							method.invoke(eventDispatcherThread);
						}
					}
					catch (final Exception e) {
						throw new RuntimeException(e);
					}
				}
			}
		};

		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					application.start(lifecycle);
				}
			});
		}
		catch (final Exception e) {
			throw new RuntimeException(e);
		}

		//this will block until the lifecycle was finished
		swtEventLoop.start();
	}
}
