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

import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.validation.ValidationMessage;
import org.jowidgets.api.validation.ValidationMessageType;
import org.jowidgets.api.validation.ValidationResult;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.api.widgets.IInputComponent;
import org.jowidgets.api.widgets.ILabel;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.IValidationLabel;
import org.jowidgets.api.widgets.descriptor.IValidationLabelDescriptor;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.IComponentCommon;
import org.jowidgets.common.widgets.controler.IComponentListener;
import org.jowidgets.common.widgets.controler.IFocusListener;
import org.jowidgets.common.widgets.controler.IInputListener;
import org.jowidgets.common.widgets.controler.IKeyListener;
import org.jowidgets.common.widgets.controler.IMouseListener;
import org.jowidgets.common.widgets.controler.IPopupDetectionListener;
import org.jowidgets.impl.widgets.basic.factory.internal.util.ColorSettingsInvoker;
import org.jowidgets.impl.widgets.basic.factory.internal.util.VisibiliySettingsInvoker;
import org.jowidgets.tools.widgets.wrapper.ControlWrapper;

public class ValidationLabelImpl implements IValidationLabel {

	private final IValidationLabelDescriptor descriptor;
	private final ILabel labelWidget;
	private final List<IInputComponent<?>> inputWidgets;
	private final IInputListener inputListener;
	private final IControl labelControl;
	private final boolean showLabel;
	private LabelState currentLabelState;
	private String currentMessageText;
	private boolean hasInput;

	public ValidationLabelImpl(final ILabel labelWidget, final IValidationLabelDescriptor descriptor) {

		this.currentLabelState = LabelState.EMPTY;
		this.showLabel = descriptor.isShowValidationMessage();
		this.labelWidget = labelWidget;
		this.descriptor = descriptor;
		this.labelControl = new ControlWrapper(labelWidget);
		this.inputWidgets = new LinkedList<IInputComponent<?>>();
		this.hasInput = false;
		this.inputListener = new IInputListener() {

			@Override
			public void inputChanged() {
				hasInput = true;
				onInputChanged();
			}
		};
		ColorSettingsInvoker.setColors(descriptor, this);
		VisibiliySettingsInvoker.setVisibility(descriptor, this);
	}

	@Override
	public void registerInputWidget(final IInputComponent<?> inputWidget) {
		inputWidgets.add(inputWidget);
		inputWidget.addInputListener(inputListener);
		onInputChanged();
		labelWidget.redraw();
	}

	@Override
	public void unRegisterInputWidget(final IInputComponent<?> inputWidget) {
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
		final String oldMessageText = currentMessageText;
		doValidation(isEmpty());

		if (oldState != currentLabelState || oldMessageText != currentMessageText) {
			labelWidget.redraw();
		}
	}

	private boolean isEmpty() {
		boolean anyFilledOut = false;

		//empty if there is any mandatory field empty
		for (final IInputComponent<?> inputWidget : inputWidgets) {
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
		for (final IInputComponent<?> inputWidget : inputWidgets) {
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
		currentMessageText = messageText.toString();

		if (firstWorst.getType() == ValidationMessageType.OK && hasInput && !isEmpty) {
			labelWidget.setIcon(descriptor.getOkIcon());
			if (showLabel) {
				labelWidget.setMarkup(descriptor.getOkMarkup());
				labelWidget.setForegroundColor(descriptor.getOkColor());
				labelWidget.setText(currentMessageText);
			}
			else if (currentMessageText != null && !currentMessageText.trim().isEmpty()) {
				labelWidget.setToolTipText(currentMessageText);
			}
			currentLabelState = LabelState.OK_VALIDATION;
		}
		else if (firstWorst.getType() == ValidationMessageType.WARNING && !isEmpty) {
			labelWidget.setIcon(descriptor.getWarningIcon());
			if (showLabel) {
				labelWidget.setMarkup(descriptor.getWarningMarkup());
				labelWidget.setForegroundColor(descriptor.getWarningColor());
				labelWidget.setText(currentMessageText);
			}
			else if (currentMessageText != null && !currentMessageText.trim().isEmpty()) {
				labelWidget.setToolTipText(currentMessageText);
			}
			currentLabelState = LabelState.WARNING_VALIDATION;
		}
		else if (firstWorst.getType() == ValidationMessageType.INFO_ERROR) {
			labelWidget.setIcon(descriptor.getInfoErrorIcon());
			if (showLabel) {
				labelWidget.setMarkup(descriptor.getInfoErrorMarkup());
				labelWidget.setForegroundColor(descriptor.getInfoErrorColor());
				labelWidget.setText(currentMessageText);
			}
			else if (currentMessageText != null && !currentMessageText.trim().isEmpty()) {
				labelWidget.setToolTipText(currentMessageText);
			}
			currentLabelState = LabelState.INFO_ERROR_VALIDATION;
		}
		else if (firstWorst.getType() == ValidationMessageType.ERROR) {
			labelWidget.setIcon(descriptor.getErrorIcon());
			if (showLabel) {
				labelWidget.setMarkup(descriptor.getErrorMarkup());
				labelWidget.setForegroundColor(descriptor.getErrorColor());
				labelWidget.setText(currentMessageText);
			}
			else if (currentMessageText != null && !currentMessageText.trim().isEmpty()) {
				labelWidget.setToolTipText(currentMessageText);
			}
			currentLabelState = LabelState.ERROR_VALIDATION;
		}
		else if (isEmpty) {
			labelWidget.setIcon(descriptor.getMissingInputIcon());
			labelWidget.setMarkup(descriptor.getMissingInputMarkup());
			labelWidget.setForegroundColor(descriptor.getMissingInputColor());
			currentMessageText = descriptor.getMissingInputText();
			labelWidget.setText(currentMessageText);
			currentLabelState = LabelState.MISSING_INPUT;
		}
		else {
			labelWidget.setIcon(null);
			labelWidget.setText(null);
			labelWidget.setToolTipText(null);
			currentLabelState = LabelState.EMPTY;
		}
	}

	@Override
	public IContainer getParent() {
		return labelControl.getParent();
	}

	@Override
	public void setParent(final IContainer parent) {
		labelControl.setParent(parent);
	}

	@Override
	public boolean isReparentable() {
		return labelControl.isReparentable();
	}

	@Override
	public Object getUiReference() {
		return labelControl.getUiReference();
	}

	@Override
	public void redraw() {
		labelControl.redraw();
	}

	@Override
	public void setRedrawEnabled(final boolean enabled) {
		labelControl.setRedrawEnabled(enabled);
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		labelControl.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		labelControl.setBackgroundColor(colorValue);
	}

	@Override
	public IColorConstant getForegroundColor() {
		return labelControl.getForegroundColor();
	}

	@Override
	public IColorConstant getBackgroundColor() {
		return labelControl.getBackgroundColor();
	}

	@Override
	public void setCursor(final Cursor cursor) {
		labelControl.setCursor(cursor);
	}

	@Override
	public void setVisible(final boolean visible) {
		labelControl.setVisible(visible);
	}

	@Override
	public boolean isVisible() {
		return labelControl.isVisible();
	}

	@Override
	public void setEnabled(final boolean enabled) {
		if (!enabled) {
			labelWidget.setText(null);
			labelWidget.setIcon(null);
		}
		else {
			resetValidation();
		}
		labelControl.setEnabled(enabled);
	}

	@Override
	public boolean isEnabled() {
		return labelControl.isEnabled();
	}

	@Override
	public Dimension getMinSize() {
		return labelControl.getMinSize();
	}

	@Override
	public Dimension getPreferredSize() {
		return labelControl.getPreferredSize();
	}

	@Override
	public Dimension getMaxSize() {
		return labelControl.getMaxSize();
	}

	@Override
	public void setMinSize(final Dimension minSize) {
		labelControl.setMinSize(minSize);
	}

	@Override
	public void setPreferredSize(final Dimension preferredSize) {
		labelControl.setPreferredSize(preferredSize);
	}

	@Override
	public void setMaxSize(final Dimension maxSize) {
		labelControl.setMaxSize(maxSize);
	}

	@Override
	public Dimension getSize() {
		return labelControl.getSize();
	}

	@Override
	public void setSize(final Dimension size) {
		labelControl.setSize(size);
	}

	@Override
	public void setSize(final int width, final int height) {
		labelControl.setSize(width, height);
	}

	@Override
	public void setPosition(final int x, final int y) {
		labelControl.setPosition(x, y);
	}

	@Override
	public Position getPosition() {
		return labelControl.getPosition();
	}

	@Override
	public void setPosition(final Position position) {
		labelControl.setPosition(position);
	}

	@Override
	public Position toScreen(final Position localPosition) {
		return labelControl.toScreen(localPosition);
	}

	@Override
	public Position toLocal(final Position screenPosition) {
		return labelControl.toLocal(screenPosition);
	}

	@Override
	public Position fromComponent(final IComponentCommon component, final Position componentPosition) {
		return labelControl.fromComponent(component, componentPosition);
	}

	@Override
	public Position toComponent(final Position componentPosition, final IComponentCommon component) {
		return labelControl.toComponent(componentPosition, component);
	}

	@Override
	public boolean requestFocus() {
		return labelControl.requestFocus();
	}

	@Override
	public void addFocusListener(final IFocusListener listener) {
		labelControl.addFocusListener(listener);
	}

	@Override
	public void removeFocusListener(final IFocusListener listener) {
		labelControl.removeFocusListener(listener);
	}

	@Override
	public void addKeyListener(final IKeyListener listener) {
		labelControl.addKeyListener(listener);
	}

	@Override
	public void removeKeyListener(final IKeyListener listener) {
		labelControl.removeKeyListener(listener);
	}

	@Override
	public void addMouseListener(final IMouseListener mouseListener) {
		labelControl.addMouseListener(mouseListener);
	}

	@Override
	public void removeMouseListener(final IMouseListener mouseListener) {
		labelControl.removeMouseListener(mouseListener);
	}

	@Override
	public void addComponentListener(final IComponentListener componentListener) {
		labelControl.addComponentListener(componentListener);
	}

	@Override
	public void removeComponentListener(final IComponentListener componentListener) {
		labelControl.removeComponentListener(componentListener);
	}

	@Override
	public IPopupMenu createPopupMenu() {
		return labelControl.createPopupMenu();
	}

	@Override
	public void setPopupMenu(final IMenuModel popupMenu) {
		labelControl.setPopupMenu(popupMenu);
	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		labelControl.addPopupDetectionListener(listener);
	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		labelControl.removePopupDetectionListener(listener);
	}

	@Override
	public void setLayoutConstraints(final Object layoutConstraints) {
		labelControl.setLayoutConstraints(layoutConstraints);
	}

	@Override
	public Object getLayoutConstraints() {
		return labelControl.getLayoutConstraints();
	}

	private enum LabelState {
		OK_VALIDATION,
		WARNING_VALIDATION,
		INFO_ERROR_VALIDATION,
		ERROR_VALIDATION,
		MISSING_INPUT,
		EMPTY;
	}

}
