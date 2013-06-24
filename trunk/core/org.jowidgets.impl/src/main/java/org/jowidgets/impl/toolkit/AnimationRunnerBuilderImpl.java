/*
 * Copyright (c) 2013, MGrossmann
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

import org.jowidgets.api.animation.IAnimationRunner;
import org.jowidgets.api.animation.IAnimationRunnerBuilder;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.util.Assert;
import org.jowidgets.util.ITypedKey;
import org.jowidgets.util.concurrent.DaemonThreadFactory;

final class AnimationRunnerBuilderImpl implements IAnimationRunnerBuilder {

	private static final ITypedKey<ScheduledExecutorService> DEFAULT_EXECUTOR_KEY = new ITypedKey<ScheduledExecutorService>() {};

	private long delay;
	private TimeUnit timeUnit;
	private ScheduledExecutorService executor;

	AnimationRunnerBuilderImpl() {
		this.delay = 100;
		this.timeUnit = TimeUnit.MILLISECONDS;
	}

	@Override
	public IAnimationRunnerBuilder setExecutor(final ScheduledExecutorService executor) {
		this.executor = executor;
		return this;
	}

	@Override
	public IAnimationRunnerBuilder setDelay(final long delay, final TimeUnit timeUnit) {
		Assert.paramNotNull(timeUnit, "timeUnit");
		this.delay = delay;
		this.timeUnit = timeUnit;
		return this;
	}

	@Override
	public IAnimationRunnerBuilder setDelay(final long delay) {
		setDelay(delay, TimeUnit.MILLISECONDS);
		return this;
	}

	private ScheduledExecutorService getExecutor() {
		if (executor != null) {
			return executor;
		}
		else {
			ScheduledExecutorService executorService = Toolkit.getValue(DEFAULT_EXECUTOR_KEY);
			if (executorService == null) {
				executorService = Executors.newSingleThreadScheduledExecutor(new DaemonThreadFactory());
				Toolkit.setValue(DEFAULT_EXECUTOR_KEY, executorService);
			}
			return executorService;
		}
	}

	@Override
	public IAnimationRunner build() {
		return new AnimationRunnerImpl(getExecutor(), delay, timeUnit);
	}

}
