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

import java.util.LinkedList;
import java.util.List;

import org.jowidgets.api.validation.ValidationMessage;
import org.jowidgets.api.validation.ValidationMessageType;
import org.jowidgets.api.validation.ValidationResult;
import org.jowidgets.api.widgets.IInputWidget;
import org.jowidgets.api.widgets.ILabelWidget;
import org.jowidgets.api.widgets.IValidationLabelWidget;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.api.widgets.descriptor.IValidationLabelDescriptor;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.widgets.controler.IInputListener;
import org.jowidgets.impl.widgets.composed.wrapper.ChildWidgetWrapper;

public class ValidationLabelWidget implements IValidationLabelWidget {

	private final IValidationLabelDescriptor descriptor;
	private final ILabelWidget labelWidget;
	private final List<IInputWidget<?>> inputWidgets;
	private final IInputListener inputListener;
	private final IWidget childWidgetAdapter;
	private final boolean showLabel;
	private LabelState currentLabelState;
	private boolean hasInput;

	public ValidationLabelWidget(final ILabelWidget labelWidget, final IValidationLabelDescriptor descriptor) {

		this.currentLabelState = LabelState.EMPTY;
		this.showLabel = descriptor.isShowValidationMessage();
		this.labelWidget = labelWidget;
		this.descriptor = descriptor;
		this.childWidgetAdapter = new ChildWidgetWrapper(labelWidget);
		this.inputWidgets = new LinkedList<IInputWidget<?>>();
		this.hasInput = false;
		this.inputListener = new IInputListener() {

			@Override
			public void inputChanged(final Object source) {
				hasInput = true;
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

	@Override
	public void resetValidation() {
		hasInput = false;
		onInputChanged();
	}

	private void onInputChanged() {
		final LabelState oldState = currentLabelState;
		doValidation(isEmpty());

		if (oldState != currentLabelState) {
			labelWidget.redraw();
		}
	}

	private boolean isEmpty() {
		boolean anyFilledOut = false;

		//empty if there is any mandatory field empty
		for (final IInputWidget<?> inputWidget : inputWidgets) {
			anyFilledOut = anyFilledOut || !inputWidget.isEmpty();
			if (inputWidget.isMandatory() && inputWidget.isEmpty()) {
				return true;
			}
		}

		//or if not at least one field is filled out
		return !anyFilledOut;
	}

	private void doValidation(final boolean isEmpty) {
		final ValidationResult validationResult = new ValidationResult();
		for (final IInputWidget<?> inputWidget : inputWidgets) {
			validationResult.addValidationResult(inputWidget.validate());
		}
		setValidationResult(validationResult, isEmpty);
	}

	private void setValidationResult(final ValidationResult validationResult, final boolean isEmpty) {
		final ValidationMessage firstWorst = validationResult.getWorstFirstMessage();

		final StringBuilder messageText = new StringBuilder();
		final String context = firstWorst.getContext();

		if (context != null && !context.trim().isEmpty()) {
			messageText.append(firstWorst.getContext() + ": ");
		}
		messageText.append(firstWorst.getMessageText());

		if (firstWorst.getType() == ValidationMessageType.OK && hasInput && !isEmpty) {
			labelWidget.setIcon(descriptor.getOkIcon());
			if (showLabel) {
				labelWidget.setMarkup(descriptor.getOkMarkup());
				labelWidget.setForegroundColor(descriptor.getOkColor());
				labelWidget.setText(messageText.toString());
			}
			currentLabelState = LabelState.OK_VALIDATION;
		}
		else if (firstWorst.getType() == ValidationMessageType.WARNING) {
			labelWidget.setIcon(descriptor.getWarningIcon());
			if (showLabel) {
				labelWidget.setMarkup(descriptor.getWarningMarkup());
				labelWidget.setForegroundColor(descriptor.getWarningColor());
				labelWidget.setText(messageText.toString());
			}
			currentLabelState = LabelState.WARNING_VALIDATION;
		}
		else if (firstWorst.getType() == ValidationMessageType.ERROR) {
			labelWidget.setIcon(descriptor.getErrorIcon());
			if (showLabel) {
				labelWidget.setMarkup(descriptor.getErrorMarkup());
				labelWidget.setForegroundColor(descriptor.getErrorColor());
				labelWidget.setText(messageText.toString());
			}
			currentLabelState = LabelState.ERROR_VALIDATION;
		}
		else {
			setInputCheckResult(isEmpty);
		}
	}

	private void setInputCheckResult(final boolean isEmpty) {
		if (isEmpty) {
			labelWidget.setIcon(descriptor.getMissingInputIcon());
			labelWidget.setMarkup(descriptor.getMissingInputMarkup());
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
	public void setParent(final IWidget parent) {
		childWidgetAdapter.setParent(parent);
	}

	@Override
	public boolean isReparentable() {
		return childWidgetAdapter.isReparentable();
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

	@Override
	public void setVisible(final boolean visible) {
		childWidgetAdapter.setVisible(visible);
	}

	@Override
	public boolean isVisible() {
		return childWidgetAdapter.isVisible();
	}

	private enum LabelState {
		OK_VALIDATION,
		WARNING_VALIDATION,
		ERROR_VALIDATION,
		MISSING_INPUT,
		EMPTY;
	}

}
