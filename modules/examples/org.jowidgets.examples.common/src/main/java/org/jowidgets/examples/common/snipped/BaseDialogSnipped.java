/*
 * Copyright (c) 2015, grossmann
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
package org.jowidgets.examples.common.snipped;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IWindow;
import org.jowidgets.api.widgets.blueprint.IFrameBluePrint;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.widgets.base.Dialog;
import org.jowidgets.tools.widgets.blueprint.BPF;

public final class BaseDialogSnipped implements IApplication {

    @Override
    public void start(final IApplicationLifecycle lifecycle) {

        //create the root frame
        final IFrameBluePrint frameBp = BPF.frame().setTitle("Base dialog Snipped");
        frameBp.setSize(new Dimension(300, 200));
        final IFrame frame = Toolkit.createRootFrame(frameBp, lifecycle);
        frame.setLayout(new MigLayoutDescriptor("[]", "[]"));

        //create a dialog
        final IFrame dialog = new MyDialog(frame);

        //create a button that opens the dialog
        final IButton button = frame.add(BPF.button("Open dialog"));
        button.addActionListener(new IActionListener() {
            @Override
            public void actionPerformed() {
                dialog.setVisible(true);
            }
        });

        //set the frame visible
        frame.setVisible(true);
    }

    private final class MyDialog extends Dialog {

        MyDialog(final IWindow parent) {
            super(parent, "My Dialog");

            setMinPackSize(new Dimension(300, 0));

            setLayout(new MigLayoutDescriptor("wrap", "[][grow]", "[][]"));

            add(BPF.textLabel("Label 1"));
            add(BPF.inputFieldString(), "growx");

            add(BPF.textLabel("Label 2"));
            add(BPF.inputFieldString(), "growx");
        }

    }
}
