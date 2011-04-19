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

package org.jowidgets.addons.testtool.internal;

import org.jowidgets.api.image.IconsSmall;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.validation.ValidationMessage;
import org.jowidgets.api.validation.ValidationResult;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IInputControl;
import org.jowidgets.api.widgets.IInputDialog;
import org.jowidgets.api.widgets.blueprint.IInputDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.api.widgets.content.IInputContentContainer;
import org.jowidgets.api.widgets.content.IInputContentCreator;
import org.jowidgets.common.types.IVetoable;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.IWidgetCommon;
import org.jowidgets.common.widgets.controler.IWindowListener;
import org.jowidgets.common.widgets.factory.IWidgetFactoryListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;

public class TestToolViewUtilities {

	private boolean mainWindowFound;

	public IInputDialog<String> createInputDialog(final IFrame parent, final String title, final String inputName) {
		final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();
		final IInputDialogBluePrint<String> inputDialogBp = bpf.inputDialog(createLabelDialogCreator(inputName));
		inputDialogBp.setTitle(title);
		inputDialogBp.setMissingInputText("Please enter the " + inputName);
		inputDialogBp.setMissingInputIcon(IconsSmall.INFO);
		inputDialogBp.setCloseable(true);
		inputDialogBp.setResizable(false);

		return parent.createChildWindow(inputDialogBp);
	}

	public IInputContentCreator<String> createLabelDialogCreator(final String inputName) {
		return new IInputContentCreator<String>() {

			private IInputControl<String> inputField;

			@Override
			public void createContent(final IInputContentContainer contentContainer) {
				final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();
				contentContainer.setLayout(new MigLayoutDescriptor("[][grow]", "[]"));
				contentContainer.add(bpf.textLabel(inputName), "");
				inputField = contentContainer.add(bpf.inputFieldString(), "growx, w 180:180:180");

				contentContainer.registerInputWidget(inputName, inputField);
			}

			@Override
			public void setValue(final String value) {
				inputField.setValue(value);
			}

			@Override
			public String getValue() {
				return inputField.getValue();
			}

			@Override
			public ValidationResult validate() {
				return new ValidationResult(ValidationMessage.OK_MESSAGE);
			}

			@Override
			public boolean isMandatory() {
				return true;
			}
		};
	}

	// TODO LG when supported add a resize and move window listener to dock TestToolView permanent to main window.
	public void setPositionRelativeToMainWindow(final IFrame frame) {
		mainWindowFound = false;
		Toolkit.getWidgetFactory().addWidgetFactoryListener(new IWidgetFactoryListener() {

			@Override
			public void widgetCreated(final IWidgetCommon widget) {
				if (widget instanceof IFrame) {
					final IFrame main = (IFrame) widget;
					main.addWindowListener(new IWindowListener() {

						@Override
						public void windowIconified() {}

						@Override
						public void windowDeiconified() {}

						@Override
						public void windowDeactivated() {}

						@Override
						public void windowClosing(final IVetoable vetoable) {}

						@Override
						public void windowClosed() {}

						@Override
						public void windowActivated() {
							if (!mainWindowFound) {
								final Position mainWinPos = main.getPosition();
								final Position childWinPos = new Position(
									mainWinPos.getX() + main.getSize().getWidth(),
									mainWinPos.getY());
								frame.setPosition(childWinPos);
								frame.setVisible(true);
								mainWindowFound = true;
							}
						}
					});
				}
			}
		});
	}
}
