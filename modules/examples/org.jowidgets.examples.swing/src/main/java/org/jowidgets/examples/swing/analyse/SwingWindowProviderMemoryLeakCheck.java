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

package org.jowidgets.examples.swing.analyse;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jowidgets.api.animation.IAnimationRunner;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IWindow;
import org.jowidgets.examples.common.icons.DemoIconsInitializer;

public final class SwingWindowProviderMemoryLeakCheck {

    private SwingWindowProviderMemoryLeakCheck() {}

    public static void main(final String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        DemoIconsInitializer.initialize();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowJFrame();
            }
        });
    }

    private static void createAndShowJFrame() {

        final JFrame frame = new JFrame();
        frame.setSize(450, 350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final IAnimationRunner animationRunner = Toolkit.getAnimationRunnerBuilder().setDelay(1).build();
        animationRunner.run(new Runnable() {
            @Override
            public void run() {
                final JFrame window = new JFrame() {
                    private static final long serialVersionUID = 5049447815415149630L;
                    @SuppressWarnings("unused")
                    private final int[] memory = new int[64000];
                };
                window.setSize(2, 2);

                window.setAlwaysOnTop(true);
                window.setVisible(true);
                final IWindow activeWindow = Toolkit.getActiveWindow();
                if (activeWindow != null) {
                    activeWindow.dispose();
                }
                animationRunner.run(this);
            }
        });
        animationRunner.start();

        //show the frame
        frame.setVisible(true);
    }

}
