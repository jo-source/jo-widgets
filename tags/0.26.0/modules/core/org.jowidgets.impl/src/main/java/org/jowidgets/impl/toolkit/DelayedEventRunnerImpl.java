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

package org.jowidgets.impl.toolkit;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.jowidgets.api.event.IDelayedEventRunner;
import org.jowidgets.api.threads.IUiThreadAccess;
import org.jowidgets.util.Assert;

final class DelayedEventRunnerImpl implements IDelayedEventRunner {

	private final IUiThreadAccess uiThreadAccess;
	private final ScheduledExecutorService executor;
	private final long delay;
	private final TimeUnit timeUnit;
	private final boolean cancelPrevious;

	private ScheduledFuture<?> scheduledFuture;

	DelayedEventRunnerImpl(
		final IUiThreadAccess uiThreadAccess,
		final ScheduledExecutorService executor,
		final long delay,
		final TimeUnit timeUnit,
		final boolean cancelPrevious) {

		Assert.paramNotNull(uiThreadAccess, "uiThreadAcccess");
		Assert.paramNotNull(executor, "executor");
		Assert.paramNotNull(timeUnit, "timeUnit");

		this.uiThreadAccess = uiThreadAccess;
		this.executor = executor;
		this.delay = delay;
		this.timeUnit = timeUnit;
		this.cancelPrevious = cancelPrevious;
	}

	@Override
	public void run(final Runnable event) {
		if (!uiThreadAccess.isUiThread()) {
			throw new IllegalStateException("This method must be invoked in the uiThread");
		}
		if (cancelPrevious && scheduledFuture != null && !scheduledFuture.isCancelled() && !scheduledFuture.isDone()) {
			scheduledFuture.cancel(false);
		}
		scheduledFuture = executor.schedule(new DelayedRunnable(event), delay, timeUnit);
	}

	private final class DelayedRunnable implements Runnable {

		private final Runnable original;

		public DelayedRunnable(final Runnable original) {
			this.original = original;
		}

		@Override
		public void run() {
			uiThreadAccess.invokeLater(new Runnable() {
				@Override
				public void run() {
					original.run();
				}
			});
		}

	}

}
