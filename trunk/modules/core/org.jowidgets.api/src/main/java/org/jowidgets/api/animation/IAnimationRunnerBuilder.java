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

package org.jowidgets.api.animation;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.jowidgets.util.IFactory;

public interface IAnimationRunnerBuilder {

	/**
	 * Sets the ScheduledExecutorService to use
	 * 
	 * @param executor The executor service to set
	 * 
	 * @return This builder
	 */
	IAnimationRunnerBuilder setExecutor(ScheduledExecutorService executor);

	/**
	 * Sets a default ScheduledExecutorService (Executors.newSingleThreadScheduledExecutor()) using the given thread factory
	 * 
	 * @param threadFactory The thread factory to use
	 * 
	 * @return This builder
	 */
	IAnimationRunnerBuilder setExecutor(ThreadFactory threadFactory);

	/**
	 * Sets a default ScheduledExecutorService (Executors.newSingleThreadScheduledExecutor()) using a default
	 * DaemonThreadFactory using the given ThreadNameFactory
	 * 
	 * @param threadNameFactory The thread name factory to use
	 * 
	 * @return this builder
	 */
	IAnimationRunnerBuilder setExecutor(IFactory<String> threadNameFactory);

	/**
	 * Sets a default ScheduledExecutorService (Executors.newSingleThreadScheduledExecutor()) using a default
	 * DaemonThreadFactory using a default ThreadNameFactory where all created threads has the given thread name prefix
	 * 
	 * @param threadPrefix The thread name prefix to use
	 * 
	 * @return this builder
	 */
	IAnimationRunnerBuilder setExecutor(String threadPrefix);

	IAnimationRunnerBuilder setDelay(long delay, TimeUnit timeUnit);

	IAnimationRunnerBuilder setDelay(long delay);

	IAnimationRunner build();

}
