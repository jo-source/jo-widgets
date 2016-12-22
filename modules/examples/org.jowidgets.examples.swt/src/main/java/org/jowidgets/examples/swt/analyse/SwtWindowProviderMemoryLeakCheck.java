/*
 * Copyright (c) 2011, grossmann
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

package org.jowidgets.examples.swt.analyse;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jowidgets.api.animation.IAnimationRunner;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.examples.common.icons.DemoIconsInitializer;

public final class SwtWindowProviderMemoryLeakCheck {

    private SwtWindowProviderMemoryLeakCheck() {}

    public static void main(final String[] args) throws Exception {
        DemoIconsInitializer.initialize();

        createAndShowJFrame();
    }

    private static void createAndShowJFrame() {

        final Display display = new Display();
        final Shell shell = new Shell(display);
        shell.setSize(500, 400);

        final IAnimationRunner animationRunner = Toolkit.getAnimationRunnerBuilder().setDelay(1).build();
        animationRunner.run(new Runnable() {
            @Override
            public void run() {
                final Shell window = new Shell(Display.getDefault());
                window.setSize(1, 1);
                window.open();
                Toolkit.getActiveWindow();
                window.dispose();
                animationRunner.run(this);
            }
        });
        animationRunner.start();

        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }

}
