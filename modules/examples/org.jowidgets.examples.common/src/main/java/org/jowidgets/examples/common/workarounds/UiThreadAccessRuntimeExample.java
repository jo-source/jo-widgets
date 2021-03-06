/*
 * Copyright (c) 2016, Michael Grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jowidgets.examples.common.workarounds;

import org.jowidgets.api.threads.IUiThreadAccess;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.blueprint.IFrameBluePrint;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.tools.widgets.blueprint.BPF;

public final class UiThreadAccessRuntimeExample implements IApplication {

    public void start() {
        Toolkit.getApplicationRunner().run(this);
    }

    @Override
    public void start(final IApplicationLifecycle lifecycle) {

        // create the root window
        final IFrameBluePrint frameBp = BPF.frame().setTitle("UiThreadAccessRuntimeExample");
        final IFrame frame = Toolkit.createRootFrame(frameBp, lifecycle);
        frame.setMinPackSize(new Dimension(300, 300));
        frame.add(BPF.textLabel("Iconfy and then deiconfy the window!!!"));

        final IUiThreadAccess uiThreadAccess = Toolkit.getUiThreadAccess();

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (!frame.isDisposed()) {
                        final long timeBeforeAsyncExec = System.currentTimeMillis();
                        uiThreadAccess.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                if (!frame.isDisposed()) {
                                    frame.setTitle("UiThreadAccessRuntimeExample");
                                }
                            }
                        });
                        final long timeAfterAsyncExec = System.currentTimeMillis();
                        final long asyncExcecDuration = timeAfterAsyncExec - timeBeforeAsyncExec;
                        if (asyncExcecDuration > 100) {
                            //CHECKSTYLE:OFF
                            System.out.println("Duration of async exec is: " + asyncExcecDuration);
                            //CHECKSTYLE:ON
                        }
                    }

                    try {
                        Thread.sleep(10);
                    }
                    catch (final InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        };
        final Thread worker = new Thread(runnable);
        worker.setDaemon(true);
        worker.start();

        frame.setVisible(true);

    }

}
