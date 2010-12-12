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

import org.jowidgets.api.image.Icons;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.IComponent;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IDisplay;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.ILabel;
import org.jowidgets.api.widgets.ISplitComposite;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.api.widgets.IWindow;
import org.jowidgets.api.widgets.blueprint.builder.IComponentSetupBuilder;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.color.ColorValue;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Markup;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controler.IActionListener;
import org.junit.Assert;
import org.junit.Test;

public class WidgetFactoryTest {

	private static final IColorConstant DEFAULT_FOREGROUND = new ColorValue(1, 2, 3);
	private static final IColorConstant DEFAULT_BACKGROUND = new ColorValue(222, 223, 224);

	private static final IColorConstant FOREGROUND = new ColorValue(4, 5, 6);
	private static final IColorConstant BACKGROUND = new ColorValue(219, 220, 221);

	private static final Dimension SIZE = new Dimension(145, 167);
	private static final Position POSITION = new Position(23, 19);

	private static final IBluePrintFactory BPF = Toolkit.getBluePrintFactory();

	@Test
	public void createWidgetsTest() {
		Toolkit.getApplicationRunner().run(new IApplication() {

			@Override
			public void start(final IApplicationLifecycle lifecycle) {

				final IFrame frame = Toolkit.createRootFrame(BPF.frame(), lifecycle);

				frame.setVisible(true);
				Assert.assertTrue(frame.isVisible());

				testCreateWidgets(frame);
				testAddAndRemove(frame);

				frame.dispose();
			}
		});
	}

	private void testCreateWidgets(final IFrame frame) {
		testCreateChildWidgets(frame);

		testCreateChildWidgets(frame.add(BPF.composite(), null));
		testCreateChildWidgets(frame.add(BPF.scrollComposite(), null));

		testChildWindow(frame, frame.createChildWindow(bpMod(BPF.dialog().setModal(false))));
		testChildWindow(frame, frame.createChildWindow(bpMod(BPF.frame())));
		testChildDisplay(frame, frame.createChildWindow(bpMod(BPF.questionDialog())));
		testChildDisplay(frame, frame.createChildWindow(bpMod(BPF.messageDialog())));
	}

	private void testCreateChildWidgets(final IContainer container) {
		testButtonWidget(container, container.add(bpMod(BPF.button()), null));
		testChildComponent(container, container.add(bpMod(BPF.checkBox()), null));
		testChildComponent(container, container.add(bpMod(BPF.comboBox(new String[] {})), null));
		testChildComponent(container, container.add(bpMod(BPF.comboBoxSelection(new String[] {})), null));
		testChildComponent(container, container.add(bpMod(BPF.composite()), null));
		testChildComponent(container, container.add(bpMod(BPF.icon()), null));
		testChildComponent(container, container.add(bpMod(BPF.inputFieldString()), null));
		testChildComponent(container, container.add(bpMod(BPF.label()), null));
		testChildComponent(container, container.add(bpMod(BPF.progressBar()), null));
		testChildComponent(container, container.add(bpMod(BPF.scrollComposite()), null));
		testChildComponent(container, container.add(bpMod(BPF.separator()), null));
		testSplitCompositeWidget(container, container.add(bpMod(BPF.splitComposite()), null));
		testChildComponent(container, container.add(bpMod(BPF.textField()), null));
		testChildComponent(container, container.add(bpMod(BPF.textLabel()), null));
		testChildComponent(container, container.add(bpMod(BPF.textSeparator()), null));
		testChildComponent(container, container.add(bpMod(BPF.toggleButton()), null));
		testChildComponent(container, container.add(bpMod(BPF.validationLabel()), null));

		container.removeAll();
		Assert.assertTrue(container.getChildren().size() == 0);
	}

	private <BLUE_PRINT_TYPE extends IComponentSetupBuilder<?>> BLUE_PRINT_TYPE bpMod(final BLUE_PRINT_TYPE bluePrint) {
		bluePrint.setForegroundColor(DEFAULT_FOREGROUND);
		bluePrint.setBackgroundColor(DEFAULT_BACKGROUND);
		return bluePrint;
	}

	private void testChildComponent(final IContainer parent, final IComponent widget) {
		//is widget created
		Assert.assertNotNull(widget);

		//has widget colors from setup
		Assert.assertTrue(DEFAULT_FOREGROUND.equals(widget.getForegroundColor()));
		Assert.assertTrue(DEFAULT_BACKGROUND.equals(widget.getBackgroundColor()));

		//is widget a child its parent
		Assert.assertTrue(parent.getChildren().contains(widget));

		//test the parent
		testParent(parent, widget);

		//has widget the widget a uiReference
		Assert.assertNotNull(widget.getUiReference());

		//is widget visible
		Assert.assertTrue(widget.isVisible());

		//test if widget could set invisible
		widget.setVisible(false);
		Assert.assertFalse(widget.isVisible());

		//test if widget could set visible
		widget.setVisible(true);
		Assert.assertTrue(widget.isVisible());

		//test if widget could set disabled
		widget.setEnabled(false);
		Assert.assertFalse(widget.isEnabled());

		//test if widget could set enabled
		widget.setEnabled(true);
		Assert.assertTrue(widget.isEnabled());

		//test if colors could set
		widget.setBackgroundColor(BACKGROUND);
		Assert.assertTrue(BACKGROUND.equals(widget.getBackgroundColor()));
		widget.setForegroundColor(FOREGROUND);
		Assert.assertTrue(FOREGROUND.equals(widget.getForegroundColor()));

		//test if redraw could be done
		widget.redraw();

		//test if widget has a window anchestor
		Assert.assertNotNull(Toolkit.getWidgetUtils().getWindowAncestor(widget));
	}

	private void testChildDisplay(final IWindow parent, final IDisplay widget) {
		Assert.assertNotNull(widget);

		Assert.assertTrue(DEFAULT_FOREGROUND.equals(widget.getForegroundColor()));
		Assert.assertTrue(DEFAULT_BACKGROUND.equals(widget.getBackgroundColor()));

		Assert.assertNotNull(widget.getUiReference());
		testParent(parent, widget);

		Assert.assertTrue(parent.getChildWindows().contains(widget));
	}

	private void testChildWindow(final IWindow parent, final IWindow window) {
		testChildDisplay(parent, window);
		window.setSize(SIZE);
		window.setPosition(POSITION);

		window.setVisible(true);

		Assert.assertTrue(SIZE.equals(window.getSize()));
		Assert.assertTrue(POSITION.equals(window.getPosition()));
	}

	private void testSplitCompositeWidget(final IContainer parent, final ISplitComposite widget) {
		testChildComponent(parent, widget);
		testParent(widget, widget.getFirst());
		testParent(widget, widget.getSecond());
	}

	private void testButtonWidget(final IContainer parent, final IButton widget) {
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

	private void testLabelWidget(final IContainer parent, final ILabel widget) {
		testChildComponent(parent, widget);
		widget.setIcon(Icons.ERROR);
		widget.setMarkup(Markup.STRONG);
		widget.setText("Test");
		widget.setToolTipText("TooltipTest");
	}

	private void testParent(final IWidget parent, final IWidget child) {
		Assert.assertNotNull(child.getParent());
		Assert.assertTrue(parent == child.getParent());
	}

	private void testAddAndRemove(final IFrame frame) {
		testAddAndRemoveForContainer(frame);
		testAddAndRemoveForContainer(frame.createChildWindow(BPF.frame()));
		testAddAndRemoveForContainer(frame.createChildWindow(BPF.dialog()));
	}

	private void testAddAndRemoveForContainer(final IContainer container) {
		final IComposite childComposite1 = container.add(BPF.composite(), null);
		Assert.assertTrue(container.getChildren().contains(childComposite1));

		final IComposite childComposite2 = container.add(BPF.composite(), null);
		Assert.assertTrue(container.getChildren().contains(childComposite2));

		final IComposite childComposite3 = childComposite1.add(BPF.composite(), null);
		Assert.assertTrue(childComposite1.getChildren().contains(childComposite3));

		Assert.assertTrue(container.remove(childComposite2));
		Assert.assertFalse(container.getChildren().contains(childComposite2));

		Assert.assertTrue(container.remove(childComposite1));
		Assert.assertFalse(container.getChildren().contains(childComposite1));

	}

	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(WidgetFactoryTest.class);
	}

}
