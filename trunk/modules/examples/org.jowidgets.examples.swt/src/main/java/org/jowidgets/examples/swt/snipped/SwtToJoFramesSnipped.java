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
package org.jowidgets.examples.swt.snipped;

import net.miginfocom.swt.MigLayout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.blueprint.IDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.IFrameBluePrint;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.tools.widgets.blueprint.BPF;

public final class SwtToJoFramesSnipped {

    private SwtToJoFramesSnipped() {}

    public static void main(final String[] args) throws Exception {
        final Display display = new Display();

        final Shell shell = new Shell(display);
        shell.setSize(1024, 768);
        shell.setLayout(new MigLayout("", "[]", "[][]"));

        //add button to open a jowidgets root frame
        final Button frameButton = new Button(shell, SWT.NONE);
        frameButton.setText("Create root frame");
        frameButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                final IFrameBluePrint frameBp = BPF.frame("Jowidgets Root Frame");
                frameBp.setSize(new Dimension(400, 300));
                frameBp.setAutoDispose(true);
                final IFrame frame = Toolkit.createRootFrame(frameBp);
                frame.add(BPF.textLabel("This is a jowidgets root frame"));
                frame.setVisible(true);
            }

        });

        //add button to open a jowidgets modal dialog
        final Button dialogButton = new Button(shell, SWT.NONE);
        dialogButton.setText("Create dialog");
        dialogButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                final IDialogBluePrint dialogBp = BPF.dialog("Jowidgets dialog");
                dialogBp.setSize(new Dimension(400, 300));
                final IFrame dialog = Toolkit.getWidgetFactory().create(shell, dialogBp);
                dialog.add(BPF.textLabel("This is a jowidgets modal dialog"));
                dialog.setVisible(true);
            }

        });

        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }

}
