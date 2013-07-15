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

package org.jowidgets.spi.impl.dummy.application;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.application.IApplicationRunner;

public class DummyApplicationRunner implements IApplicationRunner {

	private static final Queue<AbstractDummyEvent> EVENTS = new ConcurrentLinkedQueue<AbstractDummyEvent>();
	private static final AtomicBoolean IS_RUNNING = new AtomicBoolean(false);
	private static final AtomicBoolean IS_INVOKE_AND_WAIT = new AtomicBoolean(false);

	private static Thread eventDispatcherThread;

	@Override
	public void run(final IApplication application) {

		//Create a lifecycle 
		final IApplicationLifecycle lifecycle = new IApplicationLifecycle() {

			@Override
			public synchronized void finish() {
				IS_RUNNING.set(false);
			}
		};

		//start the application
		IS_RUNNING.set(true);

		eventDispatcherThread = Thread.currentThread();

		application.start(lifecycle);
		while (IS_RUNNING.get()) {
			try {
				final Runnable event = EVENTS.poll();
				if (event != null) {
					if (event instanceof InvokeAndWaitDummyEvent) {
						final Object lock = ((InvokeAndWaitDummyEvent) event).getLock();
						event.run();
						synchronized (lock) {
							IS_INVOKE_AND_WAIT.set(false);
							lock.notify();
						}
					}
					else {
						event.run();
					}
				}
				else {
					Thread.sleep(100);
				}
			}
			catch (final InterruptedException e) {
				throw new RuntimeException();
			}
		}

		eventDispatcherThread = null;

	}

	public static boolean isEventDispatcherThread(final Thread thread) {
		return thread == eventDispatcherThread;
	}

	public static void invokeAndWait(final Runnable runnable) {
		checkEventDispatcherRunning();
		if (isEventDispatcherThread(Thread.currentThread())) {
			runnable.run();
		}
		else {
			if (!IS_INVOKE_AND_WAIT.getAndSet(true)) {
				EVENTS.add(new InvokeAndWaitDummyEvent(runnable));
			}
			else {
				throw new RuntimeException("Concurrent invocation of invoke and wait");
			}
		}
	}

	public static void invokeLater(final Runnable runnable) {
		checkEventDispatcherRunning();
		if (isEventDispatcherThread(Thread.currentThread())) {
			runnable.run();
		}
		else {
			EVENTS.add(new DummyEvent(runnable));
		}
	}

	private static void checkEventDispatcherRunning() {
		if (eventDispatcherThread == null) {
			throw new RuntimeException("No EventDispatcherThread is running");
		}
	}

}
