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

package org.jowidgets.examples.swing.snipped;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import net.miginfocom.swing.MigLayout;

import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IInputField;
import org.jowidgets.common.widgets.controller.IInputListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.spi.impl.swing.addons.SwingToJoWrapper;
import org.jowidgets.tools.widgets.blueprint.BPF;

public final class SwingToJoCompositeSnipped {

    private SwingToJoCompositeSnipped() {}

    public static void main(final String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowJFrame();
            }
        });
    }

    private static void createAndShowJFrame() {
        //create the root frame with swing
        final JFrame frame = new JFrame("SwingToJo composite snipped");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new MigLayout("", "0[grow]0", "0[grow]0"));

        //create a jpanel
        final JPanel panel = new JPanel();
        frame.add(panel, "growx, growy");

        //create a jowidgets composite wrapper and do some jowidgets stuff
        final IComposite joComposite = SwingToJoWrapper.create(panel);
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

        //show the frame
        frame.setVisible(true);
    }

}
