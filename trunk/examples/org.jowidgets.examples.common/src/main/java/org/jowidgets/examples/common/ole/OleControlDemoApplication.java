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

package org.jowidgets.examples.common.ole;

import org.jowidgets.addons.widgets.ole.api.IOleContext;
import org.jowidgets.addons.widgets.ole.api.IOleControl;
import org.jowidgets.addons.widgets.ole.api.InvocationParameter;
import org.jowidgets.addons.widgets.ole.api.OleBPF;
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
import org.jowidgets.util.IMutableValue;
import org.jowidgets.util.IMutableValueListener;
import org.jowidgets.util.IValueChangedEvent;

public final class OleControlDemoApplication implements IApplication {

	private static final String INITIAL_URL = "www.google.de";

	private final String title;

	public OleControlDemoApplication(final String title) {
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

		//add url field
		final ITextControl urlField = frame.add(BPF.textField().setText(INITIAL_URL), "gapleft 5, gapright 5,growx, h 0::, wrap");
		frame.add(BPF.separator(), "growx, h 0::, wrap");

		//add ole content
		final IOleControl oleControl = frame.add(OleBPF.oleControl(), MigLayoutFactory.GROWING_CELL_CONSTRAINTS);

		oleControl.getContext().addMutableValueListener(new IMutableValueListener<IOleContext>() {
			@Override
			public void changed(final IValueChangedEvent<IOleContext> event) {
				onContextChange(oleControl.getContext(), urlField);
			}
		});

		urlField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(final IKeyEvent event) {
				if (event.getVirtualKey() == VirtualKey.ENTER) {
					onUrlChange(oleControl.getContext(), urlField);
				}
			}
		});
		onContextChange(oleControl.getContext(), urlField);

		frame.setVisible(true);
	}

	private void onContextChange(final IMutableValue<IOleContext> contextValue, final ITextControl urlField) {
		final IOleContext context = contextValue.getValue();
		if (context != null) {
			context.setDocument("Shell.Explorer");
			onUrlChange(contextValue, urlField);
		}
	}

	private void onUrlChange(final IMutableValue<IOleContext> contextValue, final ITextControl urlField) {
		final IOleContext context = contextValue.getValue();
		if (context != null) {
			context.getAutomation().invoke("Navigate", InvocationParameter.create("URL", urlField.getText()));
		}
	}
}
