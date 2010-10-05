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
 * ARE DISCLAIMED. IN NO EVENT SHALL jo-widgets.org BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jo.widgets.api.widgets.factory.impl.internal;

import org.jo.widgets.api.color.IColorConstant;
import org.jo.widgets.api.look.Dimension;
import org.jo.widgets.api.look.Position;
import org.jo.widgets.api.validation.IValidator;
import org.jo.widgets.api.validation.ValidationMessage;
import org.jo.widgets.api.validation.ValidationMessageType;
import org.jo.widgets.api.validation.ValidationResult;
import org.jo.widgets.api.widgets.IButtonWidget;
import org.jo.widgets.api.widgets.ICompositeWidget;
import org.jo.widgets.api.widgets.IContainerWidget;
import org.jo.widgets.api.widgets.IDialogWidget;
import org.jo.widgets.api.widgets.IInputDialogWidget;
import org.jo.widgets.api.widgets.IWidget;
import org.jo.widgets.api.widgets.IWindowWidget;
import org.jo.widgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jo.widgets.api.widgets.blueprint.factory.impl.BluePrintFactory;
import org.jo.widgets.api.widgets.controler.IActionListener;
import org.jo.widgets.api.widgets.controler.IInputListener;
import org.jo.widgets.api.widgets.descriptor.IButtonDescriptor;
import org.jo.widgets.api.widgets.descriptor.base.IBaseInputDialogDescriptor;
import org.jo.widgets.api.widgets.descriptor.base.IBaseWidgetDescriptor;
import org.jo.widgets.api.widgets.layout.MigLayoutDescriptor;

public class InputDialogWidget<INPUT_TYPE> implements IInputDialogWidget<INPUT_TYPE> {

	private final IDialogWidget dialogWidget;
	private final InputCompositeWidget<INPUT_TYPE> inputCompositeWidget;

	private INPUT_TYPE value;
	private boolean okPressed;

	public InputDialogWidget(final IDialogWidget dialogWidget, final IBaseInputDialogDescriptor<?, INPUT_TYPE> descriptor) {
		super();
		this.okPressed = false;
		this.dialogWidget = dialogWidget;

		final IBluePrintFactory bpF = new BluePrintFactory();

		this.dialogWidget.setLayout(new MigLayoutDescriptor("[grow]", "[grow][]"));

		// composite widget
		final ICompositeWidget compositeWidget = dialogWidget.add(bpF.composite(), "growx, growy, h 0::,w 0::, wrap");
		this.inputCompositeWidget = new InputCompositeWidget<INPUT_TYPE>(compositeWidget, descriptor);

		// buttons
		final ICompositeWidget buttonBar = dialogWidget.add(bpF.composite(), "align right");
		buttonBar.setLayout(new MigLayoutDescriptor("[][]", "[]"));

		final String buttonCellConstraints = "w 100::, sg bg";

		String missingInputText = null;
		if (descriptor.getValidationLabel() != null) {
			missingInputText = descriptor.getValidationLabel().getMissingInputText();
		}
		final ValidationButton validationButton = new ValidationButton(
			descriptor.getOkButton(),
			missingInputText,
			buttonBar,
			buttonCellConstraints);

		final IButtonWidget okButton = validationButton.getButtonWidget();
		final IButtonWidget cancelButton = buttonBar.add(descriptor.getCancelButton(), buttonCellConstraints);

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
	public <WIDGET_TYPE extends IWindowWidget, DESCRIPTOR_TYPE extends IBaseWidgetDescriptor<? extends WIDGET_TYPE>> WIDGET_TYPE createChildWindow(
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
		private final IContainerWidget parentContainer;
		private final IButtonWidget buttonWidget;
		private final String missingInputText;

		ValidationButton(
			final IButtonDescriptor buttonDescriptor,
			final String missingInputText,
			final IContainerWidget parentContainer,
			final String cellConstraints) {
			super();
			this.parentContainer = parentContainer;
			this.buttonWidget = parentContainer.add(buttonDescriptor, cellConstraints);
			this.missingInputText = missingInputText;

			this.buttonDescriptor = new BluePrintFactory().button().setDescriptor(buttonDescriptor);

			inputCompositeWidget.addInputListener(new IInputListener() {

				@Override
				public void inputChanged(final Object source) {
					onInputChanged();
				}
			});

			onInputChanged();
		}

		public IButtonWidget getButtonWidget() {
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
			parentContainer.setRedraw(false);
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
			parentContainer.setRedraw(true);

		}
	}

}
