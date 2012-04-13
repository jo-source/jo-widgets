/*
 * Copyright (c) 2012, David Bauknecht
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

package org.jowidgets.spi.impl.javafx.widgets;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.mask.TextMaskMode;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.InputChangeEventPolicy;
import org.jowidgets.common.types.Markup;
import org.jowidgets.spi.impl.javafx.widgets.util.IInputModfierField;
import org.jowidgets.spi.impl.javafx.widgets.util.InputModifierPasswordField;
import org.jowidgets.spi.impl.javafx.widgets.util.InputModifierTextField;
import org.jowidgets.spi.impl.javafx.widgets.util.TextVerifierDelegateImpl;
import org.jowidgets.spi.widgets.ITextControlSpi;
import org.jowidgets.spi.widgets.setup.ITextFieldSetupSpi;
import org.jowidgets.util.IProvider;

public class TextFieldImpl extends AbstractTextInputControl implements ITextControlSpi, IProvider<Boolean> {

	private final StyleDelegate styleUtil;

	private boolean programmaticChange;

	public TextFieldImpl(final ITextFieldSetupSpi setup) {
		super(setup.isPasswordPresentation() ? new InputModifierPasswordField() : new InputModifierTextField());

		this.programmaticChange = false;

		if (setup.getInputChangeEventPolicy() == InputChangeEventPolicy.EDIT_FINISHED) {
			getUiReference().focusedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(
					final ObservableValue<? extends Boolean> paramObservableValue,
					final Boolean oldValue,
					final Boolean newValue) {
					if (!newValue) {
						fireInputChanged(getUiReference().getText());
					}
				}
			});
		}

		if (setup.getMask() != null && TextMaskMode.FULL_MASK == setup.getMask().getMode()) {
			setText(setup.getMask().getPlaceholder());
			getUiReference().focusedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(
					final ObservableValue<? extends Boolean> paramObservableValue,
					final Boolean oldValue,
					final Boolean newValue) {
					if (newValue) {
						getUiReference().selectPositionCaret(0);
						if (getText().isEmpty()) {
							setText(setup.getMask().getPlaceholder());
						}
					}
				}
			});
		}

		styleUtil = new StyleDelegate(getUiReference());
		if (!setup.hasBorder()) {
			styleUtil.setNoBorder();
		}

		getInputModifierField().setTextVerifierDelegate(
				new TextVerifierDelegateImpl(
					setup.getMask(),
					this,
					setup.getInputChangeEventPolicy(),
					setup.getMaxLength(),
					this,
					this));

	}

	private IInputModfierField getInputModifierField() {
		return (IInputModfierField) getUiReference();
	}

	@Override
	public TextField getUiReference() {
		return (TextField) super.getUiReference();
	}

	@Override
	public void setEditable(final boolean editable) {
		getUiReference().setEditable(editable);

	}

	@Override
	public Dimension getMinSize() {
		return getPreferredSize();
	}

	@Override
	public String getText() {
		return getUiReference().getText();
	}

	@Override
	public void setText(final String text) {
		programmaticChange = true;
		getUiReference().setText(text);
		programmaticChange = false;
	}

	@Override
	public Boolean get() {
		return Boolean.valueOf(programmaticChange);
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		styleUtil.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		styleUtil.setBackgroundColor(colorValue);
	}

	@Override
	public void setFontSize(final int size) {
		styleUtil.setFontSize(size);
	}

	@Override
	public void setFontName(final String fontName) {
		styleUtil.setFontName(fontName);
	}

	@Override
	public void setMarkup(final Markup markup) {

	}

	@Override
	public void setSelection(final int start, final int end) {
		getUiReference().selectRange(start, end);
	}

	@Override
	public void setCaretPosition(final int pos) {
		getUiReference().positionCaret(pos);
	}

	@Override
	public int getCaretPosition() {
		return getUiReference().getCaretPosition();
	}
}
