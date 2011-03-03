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

package org.jowidgets.examples.swt;

import javax.swing.UIManager;

import net.miginfocom.swt.MigLayout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.jowidgets.addons.swt.SwtToJo;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.examples.common.demo.DemoForm1Creator;
import org.jowidgets.examples.common.icons.DemoIconsInitializer;

public final class PlainSwtWithJowidgetsDemo {

	private PlainSwtWithJowidgetsDemo() {}

	public static void main(final String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		DemoIconsInitializer.initialize();

		createAndShowJFrame();
	}

	private static void createAndShowJFrame() {
		//create the root shell with swt
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new MigLayout("", "[grow, 0::]", "[grow, 20!][grow, 0::]"));
		shell.setSize(500, 400);

		//creating the first swt composite
		final Composite swtComposite1 = new Composite(shell, SWT.NONE);
		swtComposite1.setLayoutData("growx, growy, w 0::, h 0::, wrap");
		swtComposite1.setLayout(new MigLayout("", "[grow, 0::]", "[grow, 0::]"));

		//adding a swt label
		final Label swtlabel = new Label(swtComposite1, SWT.NONE);
		swtlabel.setText("Label created with Swt");
		swtlabel.setLayoutData("alignx center");

		//creating the second composite with swt and adding it to the shell
		final Composite swtComposite2 = new Composite(shell, SWT.NONE);
		swtComposite2.setLayoutData("growx, growy, w 0::, h 0::");

		//now a jowidgets composite will be created with help of the swt composite
		//and the demo form 1 from examples common will be added 
		final IComposite joComposite = SwtToJo.create(swtComposite2);
		DemoForm1Creator.createDemoForm1(joComposite);

		//open the shell
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

}
