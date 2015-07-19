/*
 * Copyright (c) 2015, grossmann
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

package org.jowidgets.api.worker;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

import org.jowidgets.util.IFactory;

public interface IWorkerThreadAccessBuilder {

    /**
     * Sets the ExecutorService to use
     * 
     * @param executor The executor service to set
     * 
     * @return This builder
     */
    IWorkerThreadAccessBuilder setExecutor(ScheduledExecutorService executor);

    /**
     * Sets a default ExecutorService using the given thread factory
     * 
     * @param threadFactory The thread factory to use
     * 
     * @return This builder
     */
    IWorkerThreadAccessBuilder setExecutor(ThreadFactory threadFactory);

    /**
     * Sets a default ExecutorService using a default
     * DaemonThreadFactory using the given ThreadNameFactory
     * 
     * @param threadNameFactory The thread name factory to use
     * 
     * @return this builder
     */
    IWorkerThreadAccessBuilder setExecutor(IFactory<String> threadNameFactory);

    /**
     * Sets a default ExecutorService using a defaultDaemonThreadFactory using a default
     * ThreadNameFactory where all created threads has the given thread name prefix
     * 
     * @param threadPrefix The thread name prefix to use
     * 
     * @return this builder
     */
    IWorkerThreadAccessBuilder setExecutor(String threadPrefix);

    /**
     * Sets the ScheduledExecutorService to use for progress delay
     * 
     * @param executor The executor service to set
     * 
     * @return This builder
     */
    IWorkerThreadAccessBuilder setProgressDelayExecutor(ScheduledExecutorService executor);

    /**
     * Sets a default ScheduledExecutorService using the given thread factory
     * 
     * @param threadFactory The thread factory to use
     * 
     * @return This builder
     */
    IWorkerThreadAccessBuilder setProgressDelayExecutor(ThreadFactory threadFactory);

    /**
     * Sets a default ScheduledExecutorService using a default
     * DaemonThreadFactory using the given ThreadNameFactory
     * 
     * @param threadNameFactory The thread name factory to use
     * 
     * @return this builder
     */
    IWorkerThreadAccessBuilder setProgressDelayExecutor(IFactory<String> threadNameFactory);

    /**
     * Sets a default ScheduledExecutorService using a default
     * DaemonThreadFactory using a default ThreadNameFactory where
     * all created threads has the given thread name prefix
     * 
     * @param threadPrefix The thread name prefix to use
     * 
     * @return this builder
     */
    IWorkerThreadAccessBuilder setProgressDelayExecutor(String threadPrefix);

    /**
     * Builds the worker thread access
     * 
     * @return
     */
    IWorkerThreadAccess build();

}
