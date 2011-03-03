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

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jowidgets.addons.swing.SwingToJo;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.examples.common.demo.DemoForm1Creator;
import org.jowidgets.examples.common.icons.DemoIconsInitializer;

public final class PlainSwingWithJowidgetsDemo {

	private PlainSwingWithJowidgetsDemo() {}

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
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//setting the border layout for the content pane
		final Container contentPane = frame.getContentPane();
		contentPane.setLayout(new BorderLayout());

		//adding a label in swing
		final JLabel swingLabel = new JLabel("JLabel created with Swing");
		contentPane.add(BorderLayout.NORTH, swingLabel);

		//creating the center panel with swing and adding it to the content pane
		final JPanel centerPanel = new JPanel();
		contentPane.add(BorderLayout.CENTER, centerPanel);

		//now a jowidgets composite will be created with help of the swing panel
		//and the demo form 1 from examples common will be added 
		final IComposite centerComposite = SwingToJo.create(centerPanel);
		DemoForm1Creator.createDemoForm1(centerComposite);

		//show the frame
		frame.setVisible(true);
	}

}
