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

package org.jowidgets.impl.worker;

import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;

import org.jowidgets.api.threads.IUiThreadAccess;
import org.jowidgets.api.worker.ISyncWorker;
import org.jowidgets.api.worker.IWorker;
import org.jowidgets.api.worker.IWorkerCallback;
import org.jowidgets.api.worker.IWorkerProgressDelay;
import org.jowidgets.api.worker.IWorkerThreadAccess;
import org.jowidgets.api.worker.WorkerCanceledException;
import org.jowidgets.util.ICancelCallback;

final class WorkerThreadAccessImpl implements IWorkerThreadAccess {

	private final IUiThreadAccess uiThreadAccess;
	private final Executor executor;
	@SuppressWarnings("unused")
	private final ScheduledExecutorService progressDelayExcecutor;

	WorkerThreadAccessImpl(
		final IUiThreadAccess uiThreadAccess,
		final Executor executor,
		final ScheduledExecutorService progressDelayExcecutor) {

		this.uiThreadAccess = uiThreadAccess;
		this.executor = executor;
		this.progressDelayExcecutor = progressDelayExcecutor;
	}

	@Override
	public <RESULT_TYPE, PROGRESS_TYPE> void work(
		final IWorkerCallback<RESULT_TYPE, PROGRESS_TYPE> callback,
		final ICancelCallback cancelCallback,
		final IWorker<RESULT_TYPE, PROGRESS_TYPE> worker) {

		final IWorkerCallback<RESULT_TYPE, PROGRESS_TYPE> uiCallback = new UiWorkerCallback<RESULT_TYPE, PROGRESS_TYPE>(
			uiThreadAccess,
			callback);

		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					worker.work(uiCallback, cancelCallback);
				}
				catch (final WorkerCanceledException e) {
					callback.canceled();
				}
				catch (final Throwable e) {
					callback.exception(e);
				}
			}
		});

	}

	@Override
	public <RESULT_TYPE, PROGRESS_TYPE> void work(
		final IWorkerCallback<RESULT_TYPE, PROGRESS_TYPE> callback,
		final ICancelCallback cancelCallback,
		final IWorkerProgressDelay<PROGRESS_TYPE> progressDelay,
		final IWorker<RESULT_TYPE, PROGRESS_TYPE> worker) {
		// TODO implement progress delay

	}

	@Override
	public <RESULT_TYPE, PROGRESS_TYPE> void work(
		final IWorkerCallback<RESULT_TYPE, PROGRESS_TYPE> callback,
		final ICancelCallback cancelCallback,
		final ISyncWorker<RESULT_TYPE, PROGRESS_TYPE> worker) {
		work(callback, cancelCallback, new AsyncWorkerAdapter<RESULT_TYPE, PROGRESS_TYPE>(worker));
	}

	@Override
	public <RESULT_TYPE, PROGRESS_TYPE> void work(
		final IWorkerCallback<RESULT_TYPE, PROGRESS_TYPE> callback,
		final ICancelCallback cancelCallback,
		final IWorkerProgressDelay<PROGRESS_TYPE> progressDelay,
		final ISyncWorker<RESULT_TYPE, PROGRESS_TYPE> worker) {
		work(callback, cancelCallback, progressDelay, new AsyncWorkerAdapter<RESULT_TYPE, PROGRESS_TYPE>(worker));
	}

}
