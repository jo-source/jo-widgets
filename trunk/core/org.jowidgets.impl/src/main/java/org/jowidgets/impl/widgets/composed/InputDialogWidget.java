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

import org.jowidgets.api.validation.IValidator;
import org.jowidgets.api.validation.ValidationMessage;
import org.jowidgets.api.validation.ValidationMessageType;
import org.jowidgets.api.validation.ValidationResult;
import org.jowidgets.api.widgets.IButtonWidget;
import org.jowidgets.api.widgets.ICompositeWidget;
import org.jowidgets.api.widgets.IFrameWidget;
import org.jowidgets.api.widgets.IInputDialogWidget;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.api.widgets.descriptor.IButtonDescriptor;
import org.jowidgets.api.widgets.descriptor.setup.IInputDialogSetup;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.IButtonWidgetCommon;
import org.jowidgets.common.widgets.IContainerWidgetCommon;
import org.jowidgets.common.widgets.IWidgetCommon;
import org.jowidgets.common.widgets.controler.IActionListener;
import org.jowidgets.common.widgets.controler.IInputListener;
import org.jowidgets.common.widgets.controler.IWindowListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.impl.widgets.composed.blueprint.BluePrintFactory;

public class InputDialogWidget<INPUT_TYPE> implements IInputDialogWidget<INPUT_TYPE> {

	private final IFrameWidget dialogWidget;
	private final InputCompositeWidget<INPUT_TYPE> inputCompositeWidget;

	private INPUT_TYPE value;
	private boolean okPressed;
	private final boolean autoResetValidation;

	public InputDialogWidget(final IFrameWidget dialogWidget, final IInputDialogSetup<INPUT_TYPE> setup) {
		super();
		this.okPressed = false;
		this.dialogWidget = dialogWidget;
		this.autoResetValidation = setup.isAutoResetValidation();

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
			if (autoResetValidation) {
				resetValidation();
			}
		}
		dialogWidget.setVisible(visible);
	}

	@Override
	public boolean isVisible() {
		return dialogWidget.isVisible();
	}

	@Override
	public void close() {
		dialogWidget.close();
	}

	@Override
	public void addWindowListener(final IWindowListener listener) {
		dialogWidget.addWindowListener(listener);
	}

	@Override
	public void removeWindowListener(final IWindowListener listener) {
		dialogWidget.removeWindowListener(listener);
	}

	@Override
	public <WIDGET_TYPE extends IWidgetCommon, DESCRIPTOR_TYPE extends IWidgetDescriptor<? extends WIDGET_TYPE>> WIDGET_TYPE createChildWindow(
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
	public void setParent(final IWidget parent) {
		dialogWidget.setParent(parent);
	}

	@Override
	public boolean isReparentable() {
		return dialogWidget.isReparentable();
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
	public boolean isEmpty() {
		return inputCompositeWidget.isEmpty();
	}

	@Override
	public void resetValidation() {
		inputCompositeWidget.resetValidation();
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

			if (!isMandatory() || !inputCompositeWidget.isEmpty()) {
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
