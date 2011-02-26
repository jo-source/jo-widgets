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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.jowidgets.addons.swt.JoToSwt;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.blueprint.IFrameBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.examples.common.demo.DemoForm1Creator;
import org.jowidgets.examples.common.icons.DemoIconsInitializer;

public final class JoWidgetsWithPlainSwtDemo {

	private JoWidgetsWithPlainSwtDemo() {}

	public static void main(final String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		DemoIconsInitializer.initialize();

		Toolkit.getApplicationRunner().run(new IApplication() {
			@Override
			public void start(final IApplicationLifecycle lifecycle) {
				startApplication(lifecycle);
			}
		});
	}

	private static void startApplication(final IApplicationLifecycle lifecycle) {
		//get the blue print factory
		final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();

		//create the root frame
		final IFrameBluePrint frameBp = bpf.frame("JoWidgetFrame").setSize(new Dimension(500, 400));
		final IFrame frame = Toolkit.createRootFrame(frameBp, lifecycle);
		frame.setLayout(new MigLayoutDescriptor("[grow, 0::]", "[grow, 0::]"));

		//create a scroll composite to put the content into
		final IComposite scrollComposite = frame.add(bpf.scrollComposite(), "growx, growy, w 0::, h 0::");
		scrollComposite.setLayout(new MigLayoutDescriptor("0[grow, 0::]0", "0[grow, 0::][0::]0"));

		//add the demo from 1 to the first row
		final IComposite composite1 = scrollComposite.add(bpf.composite(), "growx, growy, w 0::, h 0::, wrap");
		DemoForm1Creator.createDemoForm1(composite1, false);

		//add a composite to the second row and convert it to a JPanel
		final IComposite joComposite2 = scrollComposite.add(bpf.composite(), "alignx center");
		final Composite swtComposite2 = JoToSwt.convert(joComposite2);

		//add a swt label to the panel
		final Label label = new Label(swtComposite2, SWT.NONE);
		label.setText("Label created with swt");
		label.setForeground(new Color(Display.getCurrent(), new RGB(255, 0, 0)));

		//show the frame
		frame.setVisible(true);
	}

}
