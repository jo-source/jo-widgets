/*
 * Copyright (c) 2011, Lukas Gross
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

import junit.framework.Assert;
import junit.framework.JUnit4TestAdapter;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.widgets.controler.IActionListener;
import org.jowidgets.test.api.toolkit.TestToolkit;
import org.jowidgets.test.api.widgets.IButtonUi;
import org.jowidgets.test.api.widgets.IFrameUi;
import org.jowidgets.test.api.widgets.blueprint.factory.IBasicSimpleTestBluePrintFactory;
import org.junit.Test;

public class TestBluePrintFactoryTest {

	private static final IBasicSimpleTestBluePrintFactory BPF = TestToolkit.getBluePrintFactory();

	@Test
	public void createTestBluePrintFactoryTest() {
		Assert.assertNotNull(TestToolkit.getInstance());

		Toolkit.getApplicationRunner().run(new IApplication() {

			@Override
			public void start(final IApplicationLifecycle lifecycle) {
				final IFrameUi frame = TestToolkit.createRootFrame(BPF.frame(), lifecycle);
				frame.setVisible(true);

				final IButtonUi button = frame.add(BPF.button(), "");
				button.addActionListener(new IActionListener() {

					@Override
					public void actionPerformed() {
						// CHECKSTYLE:OFF
						System.out.println("Button wurde gedrueckt!");
						// CHECKSTYLE:ON
					}
				});
				button.push();

				frame.dispose();
			}
		});
	}

	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(TestBluePrintFactoryTest.class);
	}
}
