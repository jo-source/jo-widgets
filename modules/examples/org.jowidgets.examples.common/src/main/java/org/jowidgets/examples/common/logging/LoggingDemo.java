/*
 * Copyright (c) 2016, grossmann
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
package org.jowidgets.examples.common.logging;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.logging.api.ILogger;
import org.jowidgets.logging.api.LoggerProvider;
import org.jowidgets.tools.widgets.blueprint.BPF;

public final class LoggingDemo implements IApplication {

    private static final ILogger LOGGER = LoggerProvider.get(LoggingDemo.class);

    @Override
    public void start(final IApplicationLifecycle lifecycle) {

        final IFrame frame = Toolkit.createRootFrame(BPF.frame().setTitle("Logging demo"), lifecycle);
        frame.setLayout(new MigLayoutDescriptor("wrap", "[0::, grow]", "[]"));
        final String cc = "growx, w 0::";
        frame.add(BPF.button("Log trace"), cc).addActionListener(new IActionListener() {
            @Override
            public void actionPerformed() {
                LOGGER.trace("The message to log in trace level");
            }
        });
        frame.add(BPF.button("Log debug"), cc).addActionListener(new IActionListener() {
            @Override
            public void actionPerformed() {
                LOGGER.debug("The message to log in debug level");
            }
        });
        frame.add(BPF.button("Log info"), cc).addActionListener(new IActionListener() {
            @Override
            public void actionPerformed() {
                LOGGER.info("The message to log in info level");
            }
        });
        frame.add(BPF.button("Log warn"), cc).addActionListener(new IActionListener() {
            @Override
            public void actionPerformed() {
                LOGGER.warn("The message to log in warn level");
            }
        });
        frame.add(BPF.button("Log error"), cc).addActionListener(new IActionListener() {
            @Override
            public void actionPerformed() {
                LOGGER.error("The message to log in error level");
            }
        });
        frame.add(BPF.button("Log exception with message"), cc).addActionListener(new IActionListener() {
            @Override
            public void actionPerformed() {
                logException("The message to log with the exception");
            }
        });
        frame.add(BPF.button("Log exception without message"), cc).addActionListener(new IActionListener() {
            @Override
            public void actionPerformed() {
                logException(null);
            }
        });

        //set the frame visible
        frame.setVisible(true);
    }

    private void logException(final String message) {

        try {
            final byte[] buffer = new byte[1];
            buffer[1] = 0;
        }
        catch (final Exception e) {
            if (message != null) {
                LOGGER.error(message, e);
            }
            else {
                LOGGER.error(e);
            }
        }
    }
}
