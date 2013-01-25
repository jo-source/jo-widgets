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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public final class ExpandBarExperience {

	private ExpandBarExperience() {}

	public static void main(final String[] args) throws Exception {
		createAndShowJFrame();
	}

	private static void createAndShowJFrame() {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setSize(800, 600);
		shell.setLayout(new FillLayout());

		final ExpandBar expandBar = new ExpandBar(shell, SWT.V_SCROLL);

		final Label label1 = new Label(expandBar, SWT.NONE);
		label1.setText("TEXT OF LABEL  jsfdkj sdjfh kjdsfhkja sdhfkjasdh kjsdhf kjsdhf kjdsafh kjdshfkj hdskjfh sdkjfh dksjfh");

		final Label label2 = new Label(expandBar, SWT.NONE);
		label2.setText("TEXT OF LABEL  jsfdkj sdjfh kjdsfhkja sdhfkjasdh kjsdhf kjsdhf kjdsafh kjdshfkj hdskjfh sdkjfh dksjfh");

		final ExpandItem item1 = new ExpandItem(expandBar, SWT.NONE);
		item1.setText("Hallo Welt");
		item1.setHeight(200);
		item1.setControl(label1);

		final ExpandItem item2 = new ExpandItem(expandBar, SWT.NONE);
		item2.setText("Hallo Welt2");
		item2.setHeight(200);
		item2.setControl(label2);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}
