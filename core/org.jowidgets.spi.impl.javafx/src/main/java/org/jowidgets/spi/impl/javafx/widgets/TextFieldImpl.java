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

import javafx.scene.control.TextField;

import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Markup;
import org.jowidgets.spi.impl.javafx.util.StyleUtil;
import org.jowidgets.spi.impl.javafx.widgets.util.InputModifierTextComponent;
import org.jowidgets.spi.widgets.ITextControlSpi;
import org.jowidgets.spi.widgets.setup.ITextFieldSetupSpi;

public class TextFieldImpl extends AbstractTextInputControl implements ITextControlSpi {

	private static InputModifierTextComponent input;
	private final StyleUtil styleUtil;

	public TextFieldImpl(final ITextFieldSetupSpi setup) {
		//		super(setup.isPasswordPresentation() ? new PasswordField() : new TextField());
		//TODO DB find a better solution
		//CHECKSTYLE:OFF
		super(input = new InputModifierTextComponent(setup));
		//CHECKSTYLE:ON
		styleUtil = new StyleUtil(getUiReference());
		if (!setup.hasBorder()) {
			styleUtil.setNoBorder();
		}

		//		registerTextControl(getUiReference(), setup.getInputChangeEventPolicy());
		input.init(this);

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
	public String getText() {
		return getUiReference().getText();
	}

	@Override
	public void setText(final String text) {
		getUiReference().setText(text);
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
		getUiReference().selectPositionCaret(pos);
	}

	@Override
	public int getCaretPosition() {
		return getUiReference().getCaretPosition();
	}

}
