/*
 * Copyright (c) 2013, grossmann
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

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IInputField;
import org.jowidgets.api.widgets.ITabFolder;
import org.jowidgets.api.widgets.ITabItem;
import org.jowidgets.api.widgets.ITextControl;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.tools.widgets.blueprint.BPF;
import org.junit.Assert;
import org.junit.Test;

public class VisibilityTest {

	@Test
	public void simpleFrameTest() {
		runTest(new IFrameTestRunner() {
			@Override
			public void run(final IFrame frame) {
				Assert.assertFalse(frame.isShowing());

				frame.setVisible(true);
				Assert.assertTrue(frame.isShowing());

				frame.setVisible(false);
				Assert.assertFalse(frame.isShowing());
			}
		});
	}

	@Test
	public void simpleContainerTest() {
		runTest(new IFrameTestRunner() {
			@Override
			public void run(final IFrame frame) {
				final ITextControl textField = frame.add(BPF.textField());
				final IInputField<Integer> inputField = frame.add(BPF.inputFieldIntegerNumber());
				final IButton button = frame.add(BPF.button("test"));

				Assert.assertFalse(textField.isShowing());
				Assert.assertFalse(inputField.isShowing());
				Assert.assertFalse(button.isShowing());

				frame.setVisible(true);
				Assert.assertTrue(textField.isShowing());
				Assert.assertTrue(inputField.isShowing());
				Assert.assertTrue(button.isShowing());

				frame.setVisible(false);
				Assert.assertFalse(textField.isShowing());
				Assert.assertFalse(inputField.isShowing());
				Assert.assertFalse(button.isShowing());
			}
		});
	}

	@Test
	public void composedContainerTest() {
		runTest(new IFrameTestRunner() {
			@Override
			public void run(final IFrame frame) {
				final IComposite composite1 = frame.add(BPF.composite());
				final IComposite composite2 = frame.add(BPF.composite());

				final ITextControl textField1 = composite1.add(BPF.textField());
				final IInputField<Integer> inputField1 = composite1.add(BPF.inputFieldIntegerNumber());
				final IButton button1 = composite1.add(BPF.button("test"));

				final ITextControl textField2 = composite2.add(BPF.textField());
				final IInputField<Integer> inputField2 = composite2.add(BPF.inputFieldIntegerNumber());
				final IButton button2 = composite2.add(BPF.button("test"));

				Assert.assertFalse(textField1.isShowing());
				Assert.assertFalse(inputField1.isShowing());
				Assert.assertFalse(button1.isShowing());
				Assert.assertFalse(textField2.isShowing());
				Assert.assertFalse(inputField2.isShowing());
				Assert.assertFalse(button2.isShowing());

				frame.setVisible(true);
				Assert.assertTrue(textField1.isShowing());
				Assert.assertTrue(inputField1.isShowing());
				Assert.assertTrue(button1.isShowing());
				Assert.assertTrue(textField2.isShowing());
				Assert.assertTrue(inputField2.isShowing());
				Assert.assertTrue(button2.isShowing());

				composite1.setVisible(false);
				Assert.assertFalse(textField1.isShowing());
				Assert.assertFalse(inputField1.isShowing());
				Assert.assertFalse(button1.isShowing());
				Assert.assertTrue(textField2.isShowing());
				Assert.assertTrue(inputField2.isShowing());
				Assert.assertTrue(button2.isShowing());

				composite2.setVisible(false);
				Assert.assertFalse(textField1.isShowing());
				Assert.assertFalse(inputField1.isShowing());
				Assert.assertFalse(button1.isShowing());
				Assert.assertFalse(textField2.isShowing());
				Assert.assertFalse(inputField2.isShowing());
				Assert.assertFalse(button2.isShowing());

				composite1.setVisible(true);
				composite2.setVisible(true);
				Assert.assertTrue(textField1.isShowing());
				Assert.assertTrue(inputField1.isShowing());
				Assert.assertTrue(button1.isShowing());
				Assert.assertTrue(textField2.isShowing());
				Assert.assertTrue(inputField2.isShowing());
				Assert.assertTrue(button2.isShowing());

				frame.setVisible(false);
				Assert.assertFalse(textField1.isShowing());
				Assert.assertFalse(inputField1.isShowing());
				Assert.assertFalse(button1.isShowing());
				Assert.assertFalse(textField2.isShowing());
				Assert.assertFalse(inputField2.isShowing());
				Assert.assertFalse(button2.isShowing());

			}
		});
	}

	@Test
	public void tabFolderTest() {
		runTest(new IFrameTestRunner() {
			@Override
			public void run(final IFrame frame) {
				final ITabFolder tabFolder = frame.add(BPF.tabFolder());

				final ITabItem item1 = tabFolder.addItem(BPF.tabItem());
				final ITabItem item2 = tabFolder.addItem(BPF.tabItem());
				final ITabItem item3 = tabFolder.addItem(BPF.tabItem());

				final IButton button1 = item1.add(BPF.button());
				final IButton button2 = item2.add(BPF.button());
				final IButton button3 = item3.add(BPF.button());

				Assert.assertFalse(item1.isShowing());
				Assert.assertFalse(item2.isShowing());
				Assert.assertFalse(item3.isShowing());
				Assert.assertFalse(button1.isShowing());
				Assert.assertFalse(button2.isShowing());
				Assert.assertFalse(button3.isShowing());

				frame.setVisible(true);
				Assert.assertTrue(item1.isShowing());
				Assert.assertFalse(item2.isShowing());
				Assert.assertFalse(item3.isShowing());
				Assert.assertTrue(button1.isShowing());
				Assert.assertFalse(button2.isShowing());
				Assert.assertFalse(button3.isShowing());

				item2.select();
				Assert.assertFalse(item1.isSelected());
				Assert.assertTrue(item2.isSelected());
				Assert.assertFalse(item3.isSelected());
				Assert.assertFalse(item1.isShowing());
				Assert.assertTrue(item2.isShowing());
				Assert.assertFalse(item3.isShowing());
				Assert.assertFalse(button1.isShowing());
				Assert.assertTrue(button2.isShowing());
				Assert.assertFalse(button3.isShowing());

				item3.select();
				Assert.assertFalse(item1.isSelected());
				Assert.assertFalse(item2.isSelected());
				Assert.assertTrue(item3.isSelected());
				Assert.assertFalse(item1.isShowing());
				Assert.assertFalse(item2.isShowing());
				Assert.assertTrue(item3.isShowing());
				Assert.assertFalse(button1.isShowing());
				Assert.assertFalse(button2.isShowing());
				Assert.assertTrue(button3.isShowing());

				frame.setVisible(false);
				Assert.assertFalse(item1.isShowing());
				Assert.assertFalse(item2.isShowing());
				Assert.assertFalse(item3.isShowing());
			}
		});
	}

	private void runTest(final IFrameTestRunner runner) {
		Toolkit.getApplicationRunner().run(new IApplication() {
			@Override
			public void start(final IApplicationLifecycle lifecycle) {
				final IFrame frame = Toolkit.createRootFrame(BPF.frame().setAutoDispose(false));
				runner.run(frame);
				frame.dispose();
				lifecycle.finish();
			}
		});
	}

	interface IFrameTestRunner {
		void run(IFrame frame);
	}
}
