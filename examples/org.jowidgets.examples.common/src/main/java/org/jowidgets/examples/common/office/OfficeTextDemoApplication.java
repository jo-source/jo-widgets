/*
 * Copyright (c) 2012, grossmann
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

package org.jowidgets.examples.common.office;

import java.io.File;

import org.jowidgets.addons.widgets.office.api.IOfficeControl;
import org.jowidgets.addons.widgets.office.api.OfficeBPF;
import org.jowidgets.api.color.Colors;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.ITextControl;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.types.VirtualKey;
import org.jowidgets.common.widgets.controller.IKeyEvent;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.controller.KeyAdapter;
import org.jowidgets.tools.layout.MigLayoutFactory;
import org.jowidgets.tools.widgets.blueprint.BPF;

public final class OfficeTextDemoApplication implements IApplication {

	private final String title;

	public OfficeTextDemoApplication(final String title) {
		this.title = title;
	}

	public void start() {
		Toolkit.getApplicationRunner().run(this);
	}

	@Override
	public void start(final IApplicationLifecycle lifecycle) {
		final IFrame frame = Toolkit.createRootFrame(BPF.frame().setTitle(title).autoPackOff(), lifecycle);
		frame.setBackgroundColor(Colors.WHITE);
		frame.setSize(1024, 768);
		frame.setLayout(new MigLayoutDescriptor("0[grow, 0::]0", "[][]0[grow, 0::]0"));

		//add filename field
		final ITextControl filenameField = frame.add(BPF.textField(), "gapleft 5, gapright 5,growx, h 0::, wrap");
		frame.add(BPF.separator(), "growx, h 0::, wrap");

		//add ole content
		final IOfficeControl officeControl = frame.add(OfficeBPF.text(), MigLayoutFactory.GROWING_CELL_CONSTRAINTS);

		filenameField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(final IKeyEvent event) {
				if (event.getVirtualKey() == VirtualKey.ENTER) {
					officeControl.openDocument(new File(filenameField.getText()));
				}
			}
		});

		frame.setVisible(true);
	}

}
