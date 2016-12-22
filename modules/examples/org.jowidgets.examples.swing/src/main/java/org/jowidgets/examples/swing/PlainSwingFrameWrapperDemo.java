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

package org.jowidgets.examples.swing;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jowidgets.api.controller.IDisposeListener;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.examples.common.icons.DemoIconsInitializer;
import org.jowidgets.spi.impl.swing.addons.SwingToJoWrapper;
import org.jowidgets.tools.widgets.blueprint.BPF;

public final class PlainSwingFrameWrapperDemo {

    private PlainSwingFrameWrapperDemo() {}

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
        //create the root frame with swing
        final JFrame frame = new JFrame();
        frame.setSize(450, 350);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        final IFrame joFrame = SwingToJoWrapper.create(frame);
        joFrame.setLayout(new MigLayoutDescriptor("", ""));
        final IButton button = joFrame.add(BPF.button("Test"));
        button.addDisposeListener(new IDisposeListener() {
            @Override
            public void onDispose() {
                //CHECKSTYLE:OFF
                System.out.println("Button disposed");
                //CHECKSTYLE:ON
            }
        });

        joFrame.addDisposeListener(new IDisposeListener() {
            @Override
            public void onDispose() {
                //CHECKSTYLE:OFF
                System.out.println("Jo Frame disposed");
                //CHECKSTYLE:ON
            }
        });

        //show the frame
        frame.setVisible(true);
    }

}
