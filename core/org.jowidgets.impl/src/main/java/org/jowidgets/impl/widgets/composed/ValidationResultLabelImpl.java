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

import org.jowidgets.api.widgets.ILabel;
import org.jowidgets.api.widgets.IValidationResultLabel;
import org.jowidgets.api.widgets.descriptor.setup.IValidationLabelSetup;
import org.jowidgets.impl.widgets.basic.factory.internal.util.ColorSettingsInvoker;
import org.jowidgets.impl.widgets.basic.factory.internal.util.VisibiliySettingsInvoker;
import org.jowidgets.tools.widgets.wrapper.ControlWrapper;
import org.jowidgets.validation.IValidationMessage;
import org.jowidgets.validation.IValidationResult;
import org.jowidgets.validation.MessageType;

public class ValidationResultLabelImpl extends ControlWrapper implements IValidationResultLabel {

	private final IValidationLabelSetup setup;
	private final ILabel label;
	private final boolean showLabel;

	private String currentMessageText;
	private IValidationResult validationResult;
	private String hint;

	public ValidationResultLabelImpl(final ILabel labelWidget, final IValidationLabelSetup setup) {
		super(labelWidget);

		this.showLabel = setup.isShowValidationMessage();
		this.label = labelWidget;
		this.setup = setup;

		ColorSettingsInvoker.setColors(setup, this);
		VisibiliySettingsInvoker.setVisibility(setup, this);
	}

	@Override
	public void setEmpty() {
		this.hint = null;
		this.validationResult = null;

		label.setIcon(null);
		label.setText(null);
		label.setToolTipText(null);
		//label.redraw();
	}

	@Override
	public boolean isEmpty() {
		return hint == null && validationResult == null;
	}

	@Override
	public void setResult(final IValidationResult result) {
		this.validationResult = result;
		this.hint = null;

		final IValidationMessage firstWorst = result.getWorstFirst();

		final StringBuilder messageText = new StringBuilder();
		final String context = firstWorst.getContext();

		if (context != null && !context.trim().isEmpty()) {
			messageText.append(firstWorst.getContext() + ": ");
		}
		messageText.append(firstWorst.getText());
		currentMessageText = messageText.toString();

		if (firstWorst.getType() == MessageType.OK) {
			label.setIcon(setup.getOkIcon());
			if (showLabel) {
				label.setMarkup(setup.getOkMarkup());
				label.setForegroundColor(setup.getOkColor());
				label.setText(currentMessageText);
			}
			else if (currentMessageText != null && !currentMessageText.trim().isEmpty()) {
				label.setToolTipText(currentMessageText);
			}
		}
		else if (firstWorst.getType() == MessageType.WARNING) {
			label.setIcon(setup.getWarningIcon());
			if (showLabel) {
				label.setMarkup(setup.getWarningMarkup());
				label.setForegroundColor(setup.getWarningColor());
				label.setText(currentMessageText);
			}
			else if (currentMessageText != null && !currentMessageText.trim().isEmpty()) {
				label.setToolTipText(currentMessageText);
			}
		}
		else if (firstWorst.getType() == MessageType.INFO_ERROR) {
			label.setIcon(setup.getInfoErrorIcon());
			if (showLabel) {
				label.setMarkup(setup.getInfoErrorMarkup());
				label.setForegroundColor(setup.getInfoErrorColor());
				label.setText(currentMessageText);
			}
			else if (currentMessageText != null && !currentMessageText.trim().isEmpty()) {
				label.setToolTipText(currentMessageText);
			}
		}
		else if (firstWorst.getType() == MessageType.ERROR) {
			label.setIcon(setup.getErrorIcon());
			if (showLabel) {
				label.setMarkup(setup.getErrorMarkup());
				label.setForegroundColor(setup.getErrorColor());
				label.setText(currentMessageText);
			}
			else if (currentMessageText != null && !currentMessageText.trim().isEmpty()) {
				label.setToolTipText(currentMessageText);
			}
		}
		label.redraw();
	}

	@Override
	public IValidationResult getResult() {
		return validationResult;
	}

	@Override
	public void setHint(final String hint) {
		this.hint = hint;
		this.validationResult = null;
		label.setIcon(setup.getHintIcon());
		label.setMarkup(setup.getHintMarkup());
		label.setForegroundColor(setup.getHintColor());
		currentMessageText = hint;
		label.setText(currentMessageText);
		label.redraw();
	}

	@Override
	public String getHint() {
		return hint;
	}

}
