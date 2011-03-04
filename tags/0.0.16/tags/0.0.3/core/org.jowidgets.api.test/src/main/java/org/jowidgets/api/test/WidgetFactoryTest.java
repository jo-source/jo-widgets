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
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.ILabel;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.api.widgets.IWindow;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.types.Markup;
import org.jowidgets.common.widgets.controler.IActionListener;
import org.junit.Assert;
import org.junit.Test;

public class WidgetFactoryTest {

	private static final IBluePrintFactory BPF = Toolkit.getBluePrintFactory();

	@Test
	public void createWidgetsTest() {
		Toolkit.getApplicationRunner().run(new IApplication() {

			@Override
			public void start(final IApplicationLifecycle lifecycle) {

				final IFrame frame = Toolkit.createRootFrame(BPF.frame(), lifecycle);

				frame.setVisible(true);
				Assert.assertTrue(frame.isVisible());

				createWidgets(frame);

				frame.close();
			}
		});
	}

	private void createWidgets(final IFrame frame) {
		createChildWidgets(frame);

		createChildWidgets(frame.add(BPF.composite(), null));
		createChildWidgets(frame.add(BPF.scrollComposite(), null));

		testChildWindowWidget(frame, frame.createChildWindow(BPF.dialog()));
		testChildWindowWidget(frame, frame.createChildWindow(BPF.frame()));
	}

	private void createChildWidgets(final IContainer container) {
		testButtonWidget(container, container.add(BPF.button(), null));
		testChildWidget(container, container.add(BPF.checkBox(), null));
		testChildWidget(container, container.add(BPF.comboBox(new String[] {}), null));
		testChildWidget(container, container.add(BPF.comboBoxSelection(new String[] {}), null));
		testChildWidget(container, container.add(BPF.composite(), null));
		testChildWidget(container, container.add(BPF.icon(), null));
		testChildWidget(container, container.add(BPF.inputFieldString(), null));
		testChildWidget(container, container.add(BPF.label(), null));
		testChildWidget(container, container.add(BPF.progressBar(), null));
		testChildWidget(container, container.add(BPF.scrollComposite(), null));
		testChildWidget(container, container.add(BPF.separator(), null));
		testChildWidget(container, container.add(BPF.splitComposite(), null));
		testChildWidget(container, container.add(BPF.textField(), null));
		testChildWidget(container, container.add(BPF.textLabel(), null));
		testChildWidget(container, container.add(BPF.textSeparator(), null));
		testChildWidget(container, container.add(BPF.toggleButton(), null));
		testChildWidget(container, container.add(BPF.validationLabel(), null));
	}

	private void testChildWidget(final IWidget parent, final IWidget widget) {
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
		testParent(parent, widget);

		Assert.assertNotNull(Toolkit.getWidgetUtils().getWindowAncestor(widget));
	}

	private void testChildWindowWidget(final IWidget parent, final IWindow widget) {
		Assert.assertNotNull(widget);
		Assert.assertNotNull(widget.getUiReference());
		testParent(parent, widget);
	}

	private void testButtonWidget(final IWidget parent, final IButton widget) {
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

	private void testLabelWidget(final IWidget parent, final ILabel widget) {
		testChildWidget(parent, widget);
		widget.setIcon(Icons.ERROR);
		widget.setMarkup(Markup.STRONG);
		widget.setText("Test");
		widget.setToolTipText("TooltipTest");
	}

	private void testParent(final IWidget parent, final IWidget child) {
		Assert.assertNotNull(child.getParent());
		Assert.assertTrue(parent == child.getParent());
	}

	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(WidgetFactoryTest.class);
	}

}
