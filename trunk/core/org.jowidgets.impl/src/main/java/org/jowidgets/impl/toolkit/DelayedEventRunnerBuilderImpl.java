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

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.jowidgets.api.event.IDelayedEventRunner;
import org.jowidgets.api.event.IDelayedEventRunnerBuilder;
import org.jowidgets.api.threads.IUiThreadAccess;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.util.Assert;
import org.jowidgets.util.ITypedKey;
import org.jowidgets.util.concurrent.DaemonThreadFactory;

final class DelayedEventRunnerBuilderImpl implements IDelayedEventRunnerBuilder {

	private static final int DEFAULT_DELAY = 50;
	private static final ITypedKey<ScheduledExecutorService> DEFAULT_EXECUTOR_KEY = new ITypedKey<ScheduledExecutorService>() {};

	private ScheduledExecutorService executor;
	private long delay;
	private TimeUnit timeUnit;
	private boolean cancelPrevious;

	DelayedEventRunnerBuilderImpl() {
		this.delay = DEFAULT_DELAY;
		this.timeUnit = TimeUnit.MILLISECONDS;
		this.cancelPrevious = true;
	}

	@Override
	public IDelayedEventRunnerBuilder setExecutor(final ScheduledExecutorService executor) {
		this.executor = executor;
		return this;
	}

	@Override
	public IDelayedEventRunnerBuilder setDelay(final long delay, final TimeUnit timeUnit) {
		Assert.paramNotNull(timeUnit, "timeUnit");
		this.delay = delay;
		this.timeUnit = timeUnit;
		return this;
	}

	@Override
	public IDelayedEventRunnerBuilder setDelay(final long delay) {
		return setDelay(delay, TimeUnit.MILLISECONDS);
	}

	@Override
	public IDelayedEventRunnerBuilder setCancelPrevious(final boolean cancelPrevious) {
		this.cancelPrevious = cancelPrevious;
		return this;
	}

	private ScheduledExecutorService getExecutor() {
		if (executor != null) {
			return executor;
		}
		else {
			ScheduledExecutorService executorService = Toolkit.getValue(DEFAULT_EXECUTOR_KEY);
			if (executorService == null) {
				executorService = Executors.newScheduledThreadPool(1, new DaemonThreadFactory());
				Toolkit.setValue(DEFAULT_EXECUTOR_KEY, executorService);
			}
			return executorService;
		}
	}

	@Override
	public IDelayedEventRunner build() {
		final IUiThreadAccess uiThreadAccess = Toolkit.getUiThreadAccess();
		return new DelayedEventRunnerImpl(uiThreadAccess, getExecutor(), delay, timeUnit, cancelPrevious);
	}

}
