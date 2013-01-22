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

import org.jowidgets.api.controller.IShowingStateListener;
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

				final ShowingStateListener listener = new ShowingStateListener();
				Assert.assertNull(listener.getLastState());

				frame.addShowingStateListener(listener);
				Assert.assertNull(listener.getLastState());

				frame.setVisible(true);
				Assert.assertTrue(listener.getLastState());
				Assert.assertTrue(frame.isShowing());

				frame.setVisible(false);
				Assert.assertFalse(listener.getLastState());
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

				final ShowingStateListener textFieldListener = new ShowingStateListener();
				Assert.assertNull(textFieldListener.getLastState());
				textField.addShowingStateListener(textFieldListener);

				final ShowingStateListener inputFieldListener = new ShowingStateListener();
				Assert.assertNull(inputFieldListener.getLastState());
				inputField.addShowingStateListener(inputFieldListener);

				final ShowingStateListener buttonListener = new ShowingStateListener();
				Assert.assertNull(buttonListener.getLastState());
				button.addShowingStateListener(buttonListener);

				Assert.assertFalse(textField.isShowing());
				Assert.assertNull(textFieldListener.getLastState());
				Assert.assertFalse(inputField.isShowing());
				Assert.assertNull(inputFieldListener.getLastState());
				Assert.assertFalse(button.isShowing());
				Assert.assertNull(buttonListener.getLastState());

				frame.setVisible(true);
				Assert.assertTrue(textField.isShowing());
				Assert.assertTrue(textFieldListener.getLastState());
				Assert.assertTrue(inputField.isShowing());
				Assert.assertTrue(inputFieldListener.getLastState());
				Assert.assertTrue(button.isShowing());
				Assert.assertTrue(buttonListener.getLastState());

				frame.setVisible(false);
				Assert.assertFalse(textField.isShowing());
				Assert.assertFalse(textFieldListener.getLastState());
				Assert.assertFalse(inputField.isShowing());
				Assert.assertFalse(inputFieldListener.getLastState());
				Assert.assertFalse(button.isShowing());
				Assert.assertFalse(buttonListener.getLastState());
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

				final ShowingStateListener textFieldListener1 = new ShowingStateListener();
				Assert.assertNull(textFieldListener1.getLastState());
				textField1.addShowingStateListener(textFieldListener1);

				final ShowingStateListener inputFieldListener1 = new ShowingStateListener();
				Assert.assertNull(inputFieldListener1.getLastState());
				inputField1.addShowingStateListener(inputFieldListener1);

				final ShowingStateListener buttonListener1 = new ShowingStateListener();
				Assert.assertNull(buttonListener1.getLastState());
				button1.addShowingStateListener(buttonListener1);

				final ITextControl textField2 = composite2.add(BPF.textField());
				final IInputField<Integer> inputField2 = composite2.add(BPF.inputFieldIntegerNumber());
				final IButton button2 = composite2.add(BPF.button("test"));

				final ShowingStateListener textFieldListener2 = new ShowingStateListener();
				Assert.assertNull(textFieldListener2.getLastState());
				textField2.addShowingStateListener(textFieldListener2);

				final ShowingStateListener inputFieldListener2 = new ShowingStateListener();
				Assert.assertNull(inputFieldListener2.getLastState());
				inputField2.addShowingStateListener(inputFieldListener2);

				final ShowingStateListener buttonListener2 = new ShowingStateListener();
				Assert.assertNull(buttonListener2.getLastState());
				button2.addShowingStateListener(buttonListener2);

				Assert.assertFalse(textField1.isShowing());
				Assert.assertFalse(inputField1.isShowing());
				Assert.assertFalse(button1.isShowing());
				Assert.assertFalse(textField2.isShowing());
				Assert.assertFalse(inputField2.isShowing());
				Assert.assertFalse(button2.isShowing());
				Assert.assertNull(textFieldListener1.getLastState());
				Assert.assertNull(inputFieldListener1.getLastState());
				Assert.assertNull(buttonListener1.getLastState());
				Assert.assertNull(textFieldListener2.getLastState());
				Assert.assertNull(inputFieldListener2.getLastState());
				Assert.assertNull(buttonListener2.getLastState());

				frame.setVisible(true);
				Assert.assertTrue(textField1.isShowing());
				Assert.assertTrue(inputField1.isShowing());
				Assert.assertTrue(button1.isShowing());
				Assert.assertTrue(textField2.isShowing());
				Assert.assertTrue(inputField2.isShowing());
				Assert.assertTrue(button2.isShowing());
				Assert.assertTrue(textFieldListener1.getLastState());
				Assert.assertTrue(inputFieldListener1.getLastState());
				Assert.assertTrue(buttonListener1.getLastState());
				Assert.assertTrue(textFieldListener2.getLastState());
				Assert.assertTrue(inputFieldListener2.getLastState());
				Assert.assertTrue(buttonListener2.getLastState());

				composite1.setVisible(false);
				Assert.assertFalse(textField1.isShowing());
				Assert.assertFalse(inputField1.isShowing());
				Assert.assertFalse(button1.isShowing());
				Assert.assertTrue(textField2.isShowing());
				Assert.assertTrue(inputField2.isShowing());
				Assert.assertTrue(button2.isShowing());
				Assert.assertFalse(textFieldListener1.getLastState());
				Assert.assertFalse(inputFieldListener1.getLastState());
				Assert.assertFalse(buttonListener1.getLastState());
				Assert.assertTrue(textFieldListener2.getLastState());
				Assert.assertTrue(inputFieldListener2.getLastState());
				Assert.assertTrue(buttonListener2.getLastState());

				composite2.setVisible(false);
				Assert.assertFalse(textField1.isShowing());
				Assert.assertFalse(inputField1.isShowing());
				Assert.assertFalse(button1.isShowing());
				Assert.assertFalse(textField2.isShowing());
				Assert.assertFalse(inputField2.isShowing());
				Assert.assertFalse(button2.isShowing());
				Assert.assertFalse(textFieldListener1.getLastState());
				Assert.assertFalse(inputFieldListener1.getLastState());
				Assert.assertFalse(buttonListener1.getLastState());
				Assert.assertFalse(textFieldListener2.getLastState());
				Assert.assertFalse(inputFieldListener2.getLastState());
				Assert.assertFalse(buttonListener2.getLastState());

				composite1.setVisible(true);
				composite2.setVisible(true);
				Assert.assertTrue(textField1.isShowing());
				Assert.assertTrue(inputField1.isShowing());
				Assert.assertTrue(button1.isShowing());
				Assert.assertTrue(textField2.isShowing());
				Assert.assertTrue(inputField2.isShowing());
				Assert.assertTrue(button2.isShowing());
				Assert.assertTrue(textFieldListener1.getLastState());
				Assert.assertTrue(inputFieldListener1.getLastState());
				Assert.assertTrue(buttonListener1.getLastState());
				Assert.assertTrue(textFieldListener2.getLastState());
				Assert.assertTrue(inputFieldListener2.getLastState());
				Assert.assertTrue(buttonListener2.getLastState());

				frame.setVisible(false);
				Assert.assertFalse(textField1.isShowing());
				Assert.assertFalse(inputField1.isShowing());
				Assert.assertFalse(button1.isShowing());
				Assert.assertFalse(textField2.isShowing());
				Assert.assertFalse(inputField2.isShowing());
				Assert.assertFalse(button2.isShowing());
				Assert.assertFalse(textFieldListener1.getLastState());
				Assert.assertFalse(inputFieldListener1.getLastState());
				Assert.assertFalse(buttonListener1.getLastState());
				Assert.assertFalse(textFieldListener2.getLastState());
				Assert.assertFalse(inputFieldListener2.getLastState());
				Assert.assertFalse(buttonListener2.getLastState());

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

				final ShowingStateListener item1Listener = new ShowingStateListener();
				Assert.assertNull(item1Listener.getLastState());
				item1.addShowingStateListener(item1Listener);

				final ShowingStateListener item2Listener = new ShowingStateListener();
				Assert.assertNull(item2Listener.getLastState());
				item2.addShowingStateListener(item2Listener);

				final ShowingStateListener item3Listener = new ShowingStateListener();
				Assert.assertNull(item3Listener.getLastState());
				item3.addShowingStateListener(item3Listener);

				final ShowingStateListener button1Listener = new ShowingStateListener();
				Assert.assertNull(button1Listener.getLastState());
				button1.addShowingStateListener(button1Listener);

				final ShowingStateListener button2Listener = new ShowingStateListener();
				Assert.assertNull(button2Listener.getLastState());
				button2.addShowingStateListener(button2Listener);

				final ShowingStateListener button3Listener = new ShowingStateListener();
				Assert.assertNull(button3Listener.getLastState());
				button3.addShowingStateListener(button3Listener);

				Assert.assertFalse(item1.isShowing());
				Assert.assertFalse(item2.isShowing());
				Assert.assertFalse(item3.isShowing());
				Assert.assertFalse(button1.isShowing());
				Assert.assertFalse(button2.isShowing());
				Assert.assertFalse(button3.isShowing());
				Assert.assertNull(item1Listener.getLastState());
				Assert.assertNull(item2Listener.getLastState());
				Assert.assertNull(item3Listener.getLastState());
				Assert.assertNull(button1Listener.getLastState());
				Assert.assertNull(button2Listener.getLastState());
				Assert.assertNull(button3Listener.getLastState());

				frame.setVisible(true);
				Assert.assertTrue(item1.isShowing());
				Assert.assertFalse(item2.isShowing());
				Assert.assertFalse(item3.isShowing());
				Assert.assertTrue(button1.isShowing());
				Assert.assertFalse(button2.isShowing());
				Assert.assertFalse(button3.isShowing());
				Assert.assertTrue(item1Listener.getLastState());
				Assert.assertNull(item2Listener.getLastState());
				Assert.assertNull(item3Listener.getLastState());
				Assert.assertTrue(button1Listener.getLastState());
				Assert.assertNull(button2Listener.getLastState());
				Assert.assertNull(button3Listener.getLastState());

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
				Assert.assertFalse(item1Listener.getLastState());
				Assert.assertTrue(item2Listener.getLastState());
				Assert.assertNull(item3Listener.getLastState());
				Assert.assertFalse(button1Listener.getLastState());
				Assert.assertTrue(button2Listener.getLastState());
				Assert.assertNull(button3Listener.getLastState());

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
				Assert.assertFalse(item1Listener.getLastState());
				Assert.assertFalse(item2Listener.getLastState());
				Assert.assertTrue(item3Listener.getLastState());
				Assert.assertFalse(button1Listener.getLastState());
				Assert.assertFalse(button2Listener.getLastState());
				Assert.assertTrue(button3Listener.getLastState());

				frame.setVisible(false);
				Assert.assertFalse(item1.isShowing());
				Assert.assertFalse(item2.isShowing());
				Assert.assertFalse(item3.isShowing());
				Assert.assertFalse(button1.isShowing());
				Assert.assertFalse(button2.isShowing());
				Assert.assertFalse(button3.isShowing());
				Assert.assertFalse(item1Listener.getLastState());
				Assert.assertFalse(item2Listener.getLastState());
				Assert.assertFalse(item3Listener.getLastState());
				Assert.assertFalse(button1Listener.getLastState());
				Assert.assertFalse(button2Listener.getLastState());
				Assert.assertFalse(button3Listener.getLastState());
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

	private static final class ShowingStateListener implements IShowingStateListener {

		private Boolean lastState;

		@Override
		public void showingStateChanged(final boolean isShowing) {
			lastState = Boolean.valueOf(isShowing);
		}

		private Boolean getLastState() {
			return lastState;
		}

	}
}
