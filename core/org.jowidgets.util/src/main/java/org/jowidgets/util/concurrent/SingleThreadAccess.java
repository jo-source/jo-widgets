/*
 * Copyright (c) 2013, grossmann
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

package org.jowidgets.util.concurrent;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jowidgets.util.Assert;

public final class SingleThreadAccess implements ISingleThreadAccess {

	private static final long SLEEP_TIME = 200;

	private final AtomicBoolean running;
	private final AtomicBoolean paused;
	private final BlockingQueue<Runnable> events;

	private Thread singleThread;
	private UncaughtExceptionHandler uncaughtExceptionHandler;

	public SingleThreadAccess() {
		this.running = new AtomicBoolean(false);
		this.paused = new AtomicBoolean(false);
		this.events = new LinkedBlockingQueue<Runnable>();
		this.uncaughtExceptionHandler = new DefaultUncaughtExeptionHandler();
	}

	@Override
	public boolean isSingleThread() {
		return Thread.currentThread() == singleThread;
	}

	@Override
	public void invoke(final Runnable runnable) {
		Assert.paramNotNull(runnable, "runnable");
		events.add(runnable);
	}

	/**
	 * Starts the events loop
	 */
	public synchronized void start() {
		if (running.getAndSet(true)) {
			throw new IllegalStateException("Event queue is already running");
		}
		else {
			singleThread = new Thread(new EventLoop());
			singleThread.setDaemon(true);
			singleThread.start();
		}
	}

	/**
	 * Pauses the event pump
	 */
	public synchronized void setPaused(final boolean paused) {
		this.paused.set(paused);
	}

	/**
	 * Stops the events loop, refuses all events
	 */
	public synchronized void stop() {
		if (running.getAndSet(true)) {

			this.singleThread = new Thread(new EventLoop());
			this.singleThread.setDaemon(true);
		}
		else {
			throw new IllegalStateException("Event queue is not running");
		}
	}

	public boolean isRunning() {
		return running.get();
	}

	public void setUncaughtExceptionHandler(final UncaughtExceptionHandler uncaughtExceptionHandler) {
		Assert.paramNotNull(uncaughtExceptionHandler, "uncaughtExceptionHandler");
		this.uncaughtExceptionHandler = uncaughtExceptionHandler;
	}

	private final class EventLoop implements Runnable {

		@Override
		public void run() {
			while (running.get()) {
				try {
					if (paused.get()) {
						Thread.sleep(SLEEP_TIME);
					}
					else {
						final Runnable runnable = events.poll(SLEEP_TIME, TimeUnit.MILLISECONDS);
						if (runnable != null) {
							try {
								runnable.run();
							}
							catch (final Exception e) {
								uncaughtExceptionHandler.uncaughtException(Thread.currentThread(), e);
							}
						}
					}
				}
				catch (final InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
			events.clear();
		}

	}

	private final class DefaultUncaughtExeptionHandler implements UncaughtExceptionHandler {

		@Override
		public void uncaughtException(final Thread t, final Throwable e) {
			//CHECKSTYLE:OFF
			e.printStackTrace();
			//CHECKSTYLE:ON
		}

	}

}
