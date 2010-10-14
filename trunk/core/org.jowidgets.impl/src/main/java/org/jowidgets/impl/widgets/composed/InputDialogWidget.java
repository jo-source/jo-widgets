/*
 * Copyright (c) 2010, Michael Grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jowidgets.impl.widgets.composed;

import org.jowidgets.api.color.IColorConstant;
import org.jowidgets.api.look.Dimension;
import org.jowidgets.api.look.Position;
import org.jowidgets.api.look.Rectangle;
import org.jowidgets.api.validation.IValidator;
import org.jowidgets.api.validation.ValidationMessage;
import org.jowidgets.api.validation.ValidationMessageType;
import org.jowidgets.api.validation.ValidationResult;
import org.jowidgets.api.widgets.IButtonWidget;
import org.jowidgets.api.widgets.IButtonWidgetCommon;
import org.jowidgets.api.widgets.ICompositeWidget;
import org.jowidgets.api.widgets.IContainerWidgetCommon;
import org.jowidgets.api.widgets.IDialogWidget;
import org.jowidgets.api.widgets.IInputDialogWidget;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.api.widgets.IWindowWidgetCommon;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.api.widgets.controler.IActionListener;
import org.jowidgets.api.widgets.controler.IInputListener;
import org.jowidgets.api.widgets.descriptor.IButtonDescriptor;
import org.jowidgets.api.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.api.widgets.descriptor.setup.IInputDialogSetup;
import org.jowidgets.api.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.impl.widgets.composed.blueprint.BluePrintFactory;

public class InputDialogWidget<INPUT_TYPE> implements IInputDialogWidget<INPUT_TYPE> {

	private final IDialogWidget dialogWidget;
	private final InputCompositeWidget<INPUT_TYPE> inputCompositeWidget;

	private INPUT_TYPE value;
	private boolean okPressed;

	public InputDialogWidget(final IDialogWidget dialogWidget, final IInputDialogSetup<INPUT_TYPE> setup) {
		super();
		this.okPressed = false;
		this.dialogWidget = dialogWidget;

		final IBluePrintFactory bpF = new BluePrintFactory();

		this.dialogWidget.setLayout(new MigLayoutDescriptor("[grow]", "[grow][]"));

		// composite widget
		final ICompositeWidget compositeWidget = dialogWidget.add(bpF.composite(), "growx, growy, h 0::,w 0::, wrap");
		this.inputCompositeWidget = new InputCompositeWidget<INPUT_TYPE>(compositeWidget, setup);

		// buttons
		final ICompositeWidget buttonBar = dialogWidget.add(bpF.composite(), "align right");
		buttonBar.setLayout(new MigLayoutDescriptor("[][]", "[]"));

		final String buttonCellConstraints = "w 100::, sg bg";

		String missingInputText = null;
		if (setup.getValidationLabel() != null) {
			missingInputText = setup.getValidationLabel().getMissingInputText();
		}
		final ValidationButton validationButton = new ValidationButton(
			setup.getOkButton(),
			missingInputText,
			buttonBar,
			buttonCellConstraints);

		final IButtonWidgetCommon okButton = validationButton.getButtonWidget();
		final IButtonWidgetCommon cancelButton = buttonBar.add(setup.getCancelButton(), buttonCellConstraints);

		okButton.addActionListener(new IActionListener() {

			@Override
			public void actionPerformed() {
				okPressed = true;
				value = inputCompositeWidget.getValue();
				setVisible(false);
			}
		});

		cancelButton.addActionListener(new IActionListener() {

			@Override
			public void actionPerformed() {
				setVisible(false);
			}
		});

	}

	@Override
	public void centerLocation() {
		dialogWidget.centerLocation();
	}

	@Override
	public void setPosition(final Position position) {
		dialogWidget.setPosition(position);
	}

	@Override
	public Position getPosition() {
		return dialogWidget.getPosition();
	}

	@Override
	public void setSize(final Dimension size) {
		dialogWidget.setSize(size);
	}

	@Override
	public Dimension getSize() {
		return dialogWidget.getSize();
	}

	@Override
	public Rectangle getParentBounds() {
		return dialogWidget.getParentBounds();
	}

	@Override
	public void pack() {
		dialogWidget.pack();
	}

	@Override
	public void setVisible(final boolean visible) {
		if (visible) {
			this.okPressed = false;
		}
		dialogWidget.setVisible(visible);
	}

	@Override
	public <WIDGET_TYPE extends IWindowWidgetCommon, DESCRIPTOR_TYPE extends IWidgetDescriptor<? extends WIDGET_TYPE>> WIDGET_TYPE createChildWindow(
		final DESCRIPTOR_TYPE descriptor) {
		return dialogWidget.createChildWindow(descriptor);
	}

	@Override
	public Object getUiReference() {
		return dialogWidget.getUiReference();
	}

	@Override
	public void redraw() {
		dialogWidget.redraw();
	}

	@Override
	public IWidget getParent() {
		return dialogWidget.getParent();
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		inputCompositeWidget.setForegroundColor(colorValue);
		dialogWidget.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		inputCompositeWidget.setBackgroundColor(colorValue);
		dialogWidget.setBackgroundColor(colorValue);
	}

	@Override
	public void addInputListener(final IInputListener listener) {
		inputCompositeWidget.addInputListener(listener);
	}

	@Override
	public void removeInputListener(final IInputListener listener) {
		inputCompositeWidget.removeInputListener(listener);
	}

	@Override
	public void setValue(final INPUT_TYPE content) {
		value = content;
		inputCompositeWidget.setValue(content);
	}

	@Override
	public INPUT_TYPE getValue() {
		return value;
	}

	@Override
	public ValidationResult validate() {
		return inputCompositeWidget.validate();
	}

	@Override
	public boolean isOkPressed() {
		return okPressed;
	}

	@Override
	public void setEditable(final boolean editable) {
		inputCompositeWidget.setEditable(editable);
	}

	@Override
	public boolean isMandatory() {
		return inputCompositeWidget.isMandatory();
	}

	@Override
	public boolean hasInput() {
		return inputCompositeWidget.hasInput();
	}

	@Override
	public boolean hasAllMandatoryInput() {
		return inputCompositeWidget.hasAllMandatoryInput();
	}

	@Override
	public void setMandatory(final boolean mandatory) {
		inputCompositeWidget.setMandatory(mandatory);
	}

	@Override
	public void addValidator(final IValidator<INPUT_TYPE> validator) {
		inputCompositeWidget.addValidator(validator);
	}

	private class ValidationButton {

		private final IButtonDescriptor buttonDescriptor;
		private final IContainerWidgetCommon parentContainer;
		private final IButtonWidget buttonWidget;
		private final String missingInputText;

		ValidationButton(
			final IButtonDescriptor buttonDescriptor,
			final String missingInputText,
			final IContainerWidgetCommon parentContainer,
			final String cellConstraints) {
			super();
			this.parentContainer = parentContainer;
			this.buttonWidget = parentContainer.add(buttonDescriptor, cellConstraints);
			this.missingInputText = missingInputText;

			this.buttonDescriptor = new BluePrintFactory().button().setSetup(buttonDescriptor);

			inputCompositeWidget.addInputListener(new IInputListener() {

				@Override
				public void inputChanged(final Object source) {
					onInputChanged();
				}
			});

			onInputChanged();
		}

		public IButtonWidgetCommon getButtonWidget() {
			return buttonWidget;
		}

		private void onInputChanged() {
			final ValidationMessage firstWorst = inputCompositeWidget.validate().getWorstFirstMessage();

			if (!isMandatory() || inputCompositeWidget.hasAllMandatoryInput()) {
				setValidationResult(firstWorst);
			}
			else {
				buttonWidget.setEnabled(false);
				buttonWidget.setToolTipText(missingInputText);
			}
		}

		private void setValidationResult(final ValidationMessage firstWorst) {
			parentContainer.layoutBegin();
			if (firstWorst.getType() == ValidationMessageType.OK) {
				buttonWidget.setEnabled(true);
				buttonWidget.setToolTipText(buttonDescriptor.getToolTipText());
			}
			else if (firstWorst.getType() == ValidationMessageType.WARNING) {
				buttonWidget.setEnabled(true);
				buttonWidget.setToolTipText(buttonDescriptor.getToolTipText());
			}
			else if (firstWorst.getType() == ValidationMessageType.ERROR) {
				buttonWidget.setEnabled(false);
				buttonWidget.setToolTipText(firstWorst.getMessageText());
			}
			parentContainer.layoutEnd();

		}
	}

}
