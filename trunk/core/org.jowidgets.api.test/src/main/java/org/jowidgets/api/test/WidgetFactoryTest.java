/*
 * Copyright (c) 2010, grossmann
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

package org.jowidgets.api.test;

import junit.framework.JUnit4TestAdapter;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IFrameWidget;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.widgets.controler.impl.WindowAdapter;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.junit.Assert;
import org.junit.Test;

public class WidgetFactoryTest {

	@Test
	public void createWidgetsTest() {
		Toolkit.getApplicationRunner().run(new IApplication() {

			@Override
			public void start(final IApplicationLifecycle lifecycle) {
				final IBluePrintFactory bpF = Toolkit.getBluePrintFactory();
				final IGenericWidgetFactory gwF = Toolkit.getWidgetFactory();

				final IFrameWidget frame = gwF.create(bpF.frame());
				frame.addWindowListener(new WindowAdapter() {

					@Override
					public void windowClosed() {
						lifecycle.finish();
					}
				});

				frame.add(bpF.button(), null);
				frame.add(bpF.checkBox(), null);
				frame.add(bpF.comboBox(new String[] {}), null);
				frame.add(bpF.comboBoxSelection(new String[] {}), null);
				frame.add(bpF.composite(), null);
				frame.add(bpF.icon(), null);
				frame.add(bpF.inputFieldString(), null);
				frame.add(bpF.label(), null);
				frame.add(bpF.progressBar(), null);
				frame.add(bpF.scrollComposite(), null);
				frame.add(bpF.separator(), null);
				frame.add(bpF.splitComposite(), null);
				frame.add(bpF.textField(), null);
				frame.add(bpF.textLabel(), null);
				frame.add(bpF.textSeparator(), null);
				frame.add(bpF.toggleButton(), null);
				frame.add(bpF.validationLabel(), null);

				gwF.create(frame.getUiReference(), bpF.dialog());
				gwF.create(frame.getUiReference(), bpF.frame());

				frame.setVisible(true);

				Assert.assertTrue(frame.isVisible());

				frame.close();
			}
		});
	}

	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(WidgetFactoryTest.class);
	}

}
