/*
 * Copyright (c) 2012, David Bauknecht
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
package org.jowidgets.spi.impl.javafx.widgets.util;

import java.awt.Toolkit;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

import org.jowidgets.common.mask.TextMaskMode;
import org.jowidgets.common.types.InputChangeEventPolicy;
import org.jowidgets.common.verify.IInputVerifier;
import org.jowidgets.spi.impl.controller.InputObservable;
import org.jowidgets.spi.impl.javafx.widgets.TextFieldImpl;
import org.jowidgets.spi.impl.mask.TextMaskVerifierFactory;
import org.jowidgets.spi.impl.verify.InputVerifierHelper;
import org.jowidgets.spi.widgets.setup.ITextFieldSetupSpi;

public class InputModifierTextComponent extends TextField {

	private static final InputObservable DUMMY_INPUT_OBSERVABLE = new InputObservable();

	private IInputVerifier inputVerifier;
	private InputObservable inputObservable;
	private Integer maxLength;
	private final ITextFieldSetupSpi setup;
	private boolean programaticChangeState;

	public InputModifierTextComponent(final ITextFieldSetupSpi setup) {
		super();
		this.setup = setup;
	}

	@Override
	public void deleteText(final int offs, final int len) {
		final String currentText = getText();
		if (inputVerifier == null || inputVerifier.verify(currentText, "", offs, getLength()) || programaticChangeState) {
			super.deleteText(offs, len);
			inputObservable.fireInputChanged(getText());
		}
	}

	@Override
	public void replaceText(final int offset, final int length, final String text) {
		final String currentText = getText();

		if (maxLength != null) {
			int entireLength = currentText != null ? currentText.length() : 0;
			entireLength = entireLength + (text != null ? text.length() : 0);
			if (entireLength > maxLength.intValue()) {
				Toolkit.getDefaultToolkit().beep();
				return;
			}
		}

		if (inputVerifier == null || inputVerifier.verify(currentText, text, offset, offset + length) || programaticChangeState) {
			super.replaceText(offset, length, text);
			inputObservable.fireInputChanged(getText());
		}
	}

	public void setInputObservable(final InputObservable inputObservable) {
		if (inputObservable == null) {
			this.inputObservable = DUMMY_INPUT_OBSERVABLE;
		}
		else {
			this.inputObservable = inputObservable;
		}
	}

	public void setProgramaticChangeState(final boolean programaticChangeState) {
		this.programaticChangeState = programaticChangeState;
	}

	public void init(final TextFieldImpl textcontrol) {
		maxLength = setup.getMaxLength();
		this.programaticChangeState = false;
		final IInputVerifier maskVerifier = TextMaskVerifierFactory.create(textcontrol, setup.getMask());
		inputVerifier = InputVerifierHelper.getInputVerifier(maskVerifier, setup);
		if (setup.getInputChangeEventPolicy() == InputChangeEventPolicy.ANY_CHANGE) {
			inputObservable = textcontrol;
		}
		else if (setup.getInputChangeEventPolicy() == InputChangeEventPolicy.EDIT_FINISHED) {
			inputObservable = null;
			this.focusedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(
					final ObservableValue<? extends Boolean> paramObservableValue,
					final Boolean oldValue,
					final Boolean newValue) {
					if (!newValue) {
						inputObservable.fireInputChanged(getText());
					}
				}
			});
		}
		else {
			throw new IllegalArgumentException("InputChangeEventPolicy '" + setup.getInputChangeEventPolicy() + "' is not known.");
		}

		if (setup.getMask() != null && TextMaskMode.FULL_MASK == setup.getMask().getMode()) {
			setText(setup.getMask().getPlaceholder());
			this.focusedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(
					final ObservableValue<? extends Boolean> paramObservableValue,
					final Boolean oldValue,
					final Boolean newValue) {
					if (newValue) {
						selectPositionCaret(0);
					}
				}
			});
		}
	}

}
