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

import java.util.LinkedList;
import java.util.List;

import org.jo.widgets.api.color.IColorConstant;
import org.jo.widgets.api.validation.ValidationMessage;
import org.jo.widgets.api.validation.ValidationMessageType;
import org.jo.widgets.api.validation.ValidationResult;
import org.jo.widgets.api.widgets.IChildWidget;
import org.jo.widgets.api.widgets.IInputWidget;
import org.jo.widgets.api.widgets.ILabelWidget;
import org.jo.widgets.api.widgets.IValidationLabelWidget;
import org.jo.widgets.api.widgets.IWidget;
import org.jo.widgets.api.widgets.content.IMandatoryInputContainer;
import org.jo.widgets.api.widgets.controler.IInputListener;
import org.jo.widgets.api.widgets.descriptor.IValidationLabelDescriptor;
import org.jo.widgets.api.widgets.impl.ChildWidget;

public class ValidationLabelWidget implements IValidationLabelWidget {

	private final IValidationLabelDescriptor descriptor;
	private final ILabelWidget labelWidget;
	private final List<IInputWidget<?>> inputWidgets;
	private final IInputListener inputListener;
	private final IChildWidget childWidgetAdapter;
	private LabelState currentLabelState;

	public ValidationLabelWidget(final ILabelWidget labelWidget, final IValidationLabelDescriptor descriptor) {

		this.currentLabelState = LabelState.EMPTY;

		this.labelWidget = labelWidget;
		this.descriptor = descriptor;
		this.childWidgetAdapter = new ChildWidget(labelWidget);
		this.inputWidgets = new LinkedList<IInputWidget<?>>();
		this.inputListener = new IInputListener() {

			@Override
			public void inputChanged(final Object source) {
				onInputChanged();
			}
		};
	}

	@Override
	public void registerInputWidget(final IInputWidget<?> inputWidget) {
		inputWidgets.add(inputWidget);
		inputWidget.addInputListener(inputListener);
		onInputChanged();
		labelWidget.redraw();
	}

	@Override
	public void unRegisterInputWidget(final IInputWidget<?> inputWidget) {
		inputWidget.removeInputListener(inputListener);
		inputWidgets.remove(inputWidget);
		onInputChanged();
		labelWidget.redraw();
	}

	private void onInputChanged() {
		final LabelState oldState = currentLabelState;
		final boolean hasInput = doInputCheck();
		doValidation(hasInput);

		if (oldState != currentLabelState) {
			labelWidget.redraw();
		}
	}

	private boolean doInputCheck() {
		final boolean hasInput;
		if (hasMandatoryWidgets()) {
			hasInput = hasAllMandatoryInput() && hasAnyInput();
		}
		else {
			hasInput = hasAnyInput();
		}
		return hasInput;
	}

	private boolean hasMandatoryWidgets() {
		for (final IInputWidget<?> inputWidget : inputWidgets) {
			if (inputWidget.isMandatory()) {
				return true;
			}
		}
		return false;
	}

	private boolean hasAnyInput() {
		for (final IInputWidget<?> inputWidget : inputWidgets) {
			if (inputWidget.hasInput()) {
				return true;
			}
		}
		return false;
	}

	private boolean hasAllMandatoryInput() {
		for (final IInputWidget<?> inputWidget : inputWidgets) {
			if (inputWidget.isMandatory()) {
				if (inputWidget instanceof IMandatoryInputContainer) {
					final IMandatoryInputContainer mandatoryInputContainer = (IMandatoryInputContainer) inputWidget;
					final boolean allMandatoryInput = mandatoryInputContainer.hasAllMandatoryInput();
					if (!allMandatoryInput) {
						return false;
					}
				}
				else {
					final boolean hasInput = inputWidget.hasInput();
					if (!hasInput) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private void doValidation(final boolean hasInput) {
		final ValidationResult validationResult = new ValidationResult();
		for (final IInputWidget<?> inputWidget : inputWidgets) {
			validationResult.addValidationResult(inputWidget.validate());
		}
		setValidationResult(validationResult, hasInput);
	}

	private void setValidationResult(final ValidationResult validationResult, final boolean hasInput) {
		final ValidationMessage firstWorst = validationResult.getWorstFirstMessage();

		final StringBuilder messageText = new StringBuilder();
		final String context = firstWorst.getContext();

		if (context != null && !context.trim().isEmpty()) {
			messageText.append(firstWorst.getContext() + ": ");
		}
		messageText.append(firstWorst.getMessageText());

		if (firstWorst.getType() == ValidationMessageType.OK && hasInput) {
			labelWidget.setMarkup(descriptor.getOkMarkup());
			labelWidget.setIcon(descriptor.getOkIcon());
			labelWidget.setForegroundColor(descriptor.getOkColor());
			labelWidget.setText(messageText.toString());
			currentLabelState = LabelState.OK_VALIDATION;
		}
		else if (firstWorst.getType() == ValidationMessageType.WARNING) {
			labelWidget.setMarkup(descriptor.getWarningMarkup());
			labelWidget.setIcon(descriptor.getWarningIcon());
			labelWidget.setForegroundColor(descriptor.getWarningColor());
			labelWidget.setText(messageText.toString());
			currentLabelState = LabelState.WARNING_VALIDATION;
		}
		else if (firstWorst.getType() == ValidationMessageType.ERROR) {
			labelWidget.setMarkup(descriptor.getErrorMarkup());
			labelWidget.setIcon(descriptor.getErrorIcon());
			labelWidget.setForegroundColor(descriptor.getErrorColor());
			labelWidget.setText(messageText.toString());
			currentLabelState = LabelState.ERROR_VALIDATION;
		}
		else {
			setInputCheckResult(hasInput);
		}
	}

	private void setInputCheckResult(final boolean hasInput) {
		if (!hasInput) {
			labelWidget.setMarkup(descriptor.getMissingInputMarkup());
			labelWidget.setIcon(descriptor.getMissingInputIcon());
			labelWidget.setForegroundColor(descriptor.getMissingInputColor());
			labelWidget.setText(descriptor.getMissingInputText());
			currentLabelState = LabelState.MISSING_INPUT;
		}
		else {
			labelWidget.setIcon(null);
			labelWidget.setText(null);
			currentLabelState = LabelState.EMPTY;
		}
	}

	@Override
	public IWidget getParent() {
		return childWidgetAdapter.getParent();
	}

	@Override
	public Object getUiReference() {
		return childWidgetAdapter.getUiReference();
	}

	@Override
	public void redraw() {
		childWidgetAdapter.redraw();
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		childWidgetAdapter.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		childWidgetAdapter.setBackgroundColor(colorValue);
	}

	private enum LabelState {
		OK_VALIDATION,
		WARNING_VALIDATION,
		ERROR_VALIDATION,
		MISSING_INPUT,
		EMPTY;
	}

}
