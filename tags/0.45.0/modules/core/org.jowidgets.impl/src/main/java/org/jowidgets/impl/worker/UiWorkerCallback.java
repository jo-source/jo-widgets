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

import java.util.concurrent.atomic.AtomicBoolean;

import org.jowidgets.api.threads.IUiThreadAccess;
import org.jowidgets.api.worker.IWorkerCallback;
import org.jowidgets.util.Assert;

final class UiWorkerCallback<RESULT_TYPE, PROGRESS_TYPE> implements IWorkerCallback<RESULT_TYPE, PROGRESS_TYPE> {

    private final IUiThreadAccess uiThreadAccess;
    private final IWorkerCallback<RESULT_TYPE, PROGRESS_TYPE> original;

    private final AtomicBoolean disposed;

    UiWorkerCallback(final IUiThreadAccess uiThreadAccess, final IWorkerCallback<RESULT_TYPE, PROGRESS_TYPE> original) {
        Assert.paramNotNull(uiThreadAccess, "uiThreadAccess");
        Assert.paramNotNull(original, "original");

        this.uiThreadAccess = uiThreadAccess;
        this.original = original;
        this.disposed = new AtomicBoolean(false);
    }

    @Override
    public void progress(final PROGRESS_TYPE progress) {
        if (!disposed.get()) {
            uiThreadAccess.invokeLater(new Runnable() {
                @Override
                public void run() {
                    if (!disposed.get()) {
                        original.progress(progress);
                    }
                }
            });
        }
    }

    @Override
    public void finished(final RESULT_TYPE result) {
        if (!disposed.get()) {
            uiThreadAccess.invokeLater(new Runnable() {
                @Override
                public void run() {
                    if (!disposed.getAndSet(true)) {
                        original.finished(result);
                    }
                }
            });
        }
    }

    @Override
    public void canceled() {
        if (!disposed.get()) {
            uiThreadAccess.invokeLater(new Runnable() {
                @Override
                public void run() {
                    if (!disposed.getAndSet(true)) {
                        original.canceled();
                    }
                }
            });
        }
    }

    @Override
    public void exception(final Throwable exception) {
        if (!disposed.get()) {
            uiThreadAccess.invokeLater(new Runnable() {
                @Override
                public void run() {
                    if (!disposed.getAndSet(true)) {
                        original.exception(exception);
                    }
                }
            });
        }
    }

}
