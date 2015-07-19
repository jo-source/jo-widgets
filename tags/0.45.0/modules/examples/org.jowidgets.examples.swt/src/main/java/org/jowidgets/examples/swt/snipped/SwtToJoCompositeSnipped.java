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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IInputField;
import org.jowidgets.common.widgets.controller.IInputListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.spi.impl.swt.addons.SwtToJoWrapper;
import org.jowidgets.tools.widgets.blueprint.BPF;

public final class SwtToJoCompositeSnipped {

    private SwtToJoCompositeSnipped() {}

    public static void main(final String[] args) throws Exception {
        //create a swt display
        final Display display = new Display();

        //create a swt shell
        final Shell shell = new Shell(display);
        shell.setSize(400, 300);
        shell.setText("SwtToJo composite snipped");
        shell.setLayout(new FillLayout());

        //create a swt composite
        final Composite swtComposite = new Composite(shell, SWT.NONE);

        //create a jowidgets composite wrapper and do some jowidgets stuff
        final IComposite joComposite = SwtToJoWrapper.create(swtComposite);
        joComposite.setLayout(new MigLayoutDescriptor("[][grow]", "[]"));

        //add a label
        joComposite.add(BPF.textLabel("Name"));

        //add a input field for double values
        final IInputField<String> nameField = joComposite.add(BPF.inputFieldString(), "grow x");
        nameField.addInputListener(new IInputListener() {
            @Override
            public void inputChanged() {
                //CHECKSTYLE:OFF
                System.out.println("Hello " + nameField.getValue());
                //CHECKSTYLE:ON
            }
        });

        //open the swt shell and start event dispatching
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }

}
