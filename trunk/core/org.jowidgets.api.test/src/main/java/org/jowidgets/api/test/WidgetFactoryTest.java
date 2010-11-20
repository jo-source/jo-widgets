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

import org.jowidgets.api.color.Colors;
import org.jowidgets.api.image.Icons;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IButtonWidget;
import org.jowidgets.api.widgets.IFrameWidget;
import org.jowidgets.api.widgets.ILabelWidget;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.api.widgets.IWindowWidget;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.types.Markup;
import org.jowidgets.common.widgets.controler.IActionListener;
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

				frame.setVisible(true);
				Assert.assertTrue(frame.isVisible());

				//create each widget and check it
				createWidgets(frame);

				checkWindowWidget(frame, gwF.create(frame.getUiReference(), bpF.dialog()));
				checkWindowWidget(frame, gwF.create(frame.getUiReference(), bpF.frame()));

				frame.close();
			}
		});
	}

	private void createWidgets(final IFrameWidget frame) {
		final IBluePrintFactory bpF = Toolkit.getBluePrintFactory();
		testButtonWidget(frame, frame.add(bpF.button(), null));
		testWidget(frame, frame.add(bpF.checkBox(), null));
		testWidget(frame, frame.add(bpF.comboBox(new String[] {}), null));
		testWidget(frame, frame.add(bpF.comboBoxSelection(new String[] {}), null));
		testWidget(frame, frame.add(bpF.composite(), null));
		testWidget(frame, frame.add(bpF.icon(), null));
		testWidget(frame, frame.add(bpF.inputFieldString(), null));
		testWidget(frame, frame.add(bpF.label(), null));
		testWidget(frame, frame.add(bpF.progressBar(), null));
		testWidget(frame, frame.add(bpF.scrollComposite(), null));
		testWidget(frame, frame.add(bpF.separator(), null));
		testWidget(frame, frame.add(bpF.splitComposite(), null));
		testWidget(frame, frame.add(bpF.textField(), null));
		testWidget(frame, frame.add(bpF.textLabel(), null));
		testWidget(frame, frame.add(bpF.textSeparator(), null));
		testWidget(frame, frame.add(bpF.toggleButton(), null));
		testWidget(frame, frame.add(bpF.validationLabel(), null));
	}

	private void testWidget(final IWidget parent, final IWidget widget) {
		Assert.assertNotNull(widget);
		Assert.assertNotNull(widget.getUiReference());
		Assert.assertTrue(widget.isVisible());
		widget.setVisible(false);
		Assert.assertFalse(widget.isVisible());
		widget.setVisible(true);
		Assert.assertTrue(widget.isVisible());

		widget.setBackgroundColor(Colors.DEFAULT);
		widget.setForegroundColor(Colors.STRONG);
		widget.redraw();

		//FIXME fix this test
		//Assert.assertTrue(widget.getParent() == parent);
	}

	private void checkWindowWidget(final IWidget parent, final IWindowWidget widget) {
		Assert.assertNotNull(widget);
		Assert.assertNotNull(widget.getUiReference());

		//FIXME fix this test
		//Assert.assertTrue(widget.getParent() == parent);
	}

	private void testButtonWidget(final IWidget parent, final IButtonWidget widget) {
		testLabelWidget(parent, widget);
		final IActionListener listener = new IActionListener() {

			@Override
			public void actionPerformed() {

			}
		};

		widget.addActionListener(listener);

		//TODO push button and check listener

		widget.removeActionListener(listener);

		//TODO push button and check listener not invoked
	}

	private void testLabelWidget(final IWidget parent, final ILabelWidget widget) {
		testWidget(parent, widget);
		widget.setIcon(Icons.ERROR);
		widget.setMarkup(Markup.STRONG);
		widget.setText("Test");
		widget.setToolTipText("TooltipTest");
	}

	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(WidgetFactoryTest.class);
	}

}
