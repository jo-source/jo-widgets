/*
 * Copyright (c) 2012, grossmann
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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.SwingUtilities;

import org.eclipse.swt.widgets.Display;

public final class BridgedSwtEventLoop {

	private final AtomicBoolean isRunning;;
	private final long sleepMillis;
	private final int swtEventsCountInSuccession;

	private Display display;
	private Thread eventDispatchingThread;

	public BridgedSwtEventLoop() {
		this(10, 10);
	}

	public BridgedSwtEventLoop(final long sleepMillis, final int swtEventsCountInSuccession) {
		checkNotUiThread();
		this.sleepMillis = sleepMillis;
		this.swtEventsCountInSuccession = swtEventsCountInSuccession;
		this.isRunning = new AtomicBoolean(false);
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					eventDispatchingThread = Thread.currentThread();
					display = Display.getDefault();
				}
			});
		}
		catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Starts the swt event loop. This must NOT be invoked in the awt(swing) event thread.
	 */
	public void start() {
		checkNotUiThread();
		if (isRunning.get()) {
			return;
		}
		else {
			isRunning.set(true);
		}
		while (isRunning.get()) {
			final CountDownLatch latch = new CountDownLatch(1);
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					final Display currentDisplay = getCurrentDisplay();
					boolean hasMoreSwtEvents = true;
					int swtEventCount = 0;
					while (hasMoreSwtEvents
						&& swtEventCount < swtEventsCountInSuccession
						&& !currentDisplay.isDisposed()
						&& Display.getCurrent() != null
						&& isRunning.get()) {
						hasMoreSwtEvents = currentDisplay.readAndDispatch();
						swtEventCount++;
					}
					latch.countDown();
				}
			});
			try {
				latch.await();
				Thread.sleep(sleepMillis);
			}
			catch (final Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public void stop() {
		isRunning.set(false);
	}

	public boolean isRunning() {
		return isRunning.get();
	}

	public Thread getEventDispatchingThread() {
		return eventDispatchingThread;
	}

	private void checkNotUiThread() {
		if (SwingUtilities.isEventDispatchThread()) {
			throw new IllegalStateException("The swt event loop must not be started from the awt event thread");
		}
	}

	private Display getCurrentDisplay() {
		if (display == null) {
			if (SwingUtilities.isEventDispatchThread()) {
				display = Display.getCurrent();
				if (display == null) {
					throw new IllegalStateException("The swt display was not initialized");
				}
			}
			else {
				throw new IllegalStateException("The display must be created in the awt(swing) event dispatch thread");
			}
		}
		return display;
	}
}
