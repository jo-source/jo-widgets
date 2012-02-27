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
package org.jowidgets.spi.impl.javafx.widgets;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextArea;

import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Markup;
import org.jowidgets.spi.impl.javafx.widgets.util.InputModifierTextArea;
import org.jowidgets.spi.widgets.ITextAreaSpi;
import org.jowidgets.spi.widgets.setup.ITextAreaSetupSpi;

public class TextAreaImpl extends AbstractTextInputControl implements ITextAreaSpi {

	private final TextArea textArea;
	private final StyleUtil styleUtil;

	public TextAreaImpl(final ITextAreaSetupSpi setup) {
		super(new ScrollPane());
		getUiReference().setContent(new InputModifierTextArea());

		styleUtil = new StyleUtil(getUiReference().getContent());
		textArea = (TextArea) getUiReference().getContent();
		textArea.setWrapText(setup.isLineWrap());
		getUiReference().fitToWidthProperty().set(true);
		getUiReference().fitToHeightProperty().set(true);
		if (setup.isAlwaysShowBars()) {
			getUiReference().setVbarPolicy(ScrollBarPolicy.ALWAYS);

			if (!setup.isLineWrap()) {
				getUiReference().setHbarPolicy(ScrollBarPolicy.ALWAYS);

			}
		}
		if (!setup.hasBorder()) {
			styleUtil.setNoBorder();
		}

	}

	@Override
	public ScrollPane getUiReference() {
		return (ScrollPane) super.getUiReference();
	}

	@Override
	public String getText() {
		return textArea.getText();
	}

	@Override
	public void setText(final String text) {
		textArea.setText(text);
		if (!textArea.focusedProperty().getValue()) {
			fireInputChanged(getText());
		}
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
		throw new UnsupportedOperationException();
	}

	@Override
	public void setSelection(final int start, final int end) {
		textArea.selectRange(start, end);
	}

	@Override
	public void setEditable(final boolean editable) {
		textArea.setEditable(editable);
	}

	@Override
	public void setCaretPosition(final int pos) {
		textArea.selectPositionCaret(pos);
	}

	@Override
	public int getCaretPosition() {
		return textArea.getCaretPosition();
	}

	@Override
	public void scrollToCaretPosition() {
		final int caretPosition = textArea.getCaretPosition();
		final int usedPosition;

		final int next = Math.min(caretPosition + 1, textArea.getParagraphs().size());
		final int previous = Math.max(caretPosition - 1, 0);
		if (next != caretPosition) {
			usedPosition = next;
		}
		else {
			usedPosition = previous;
		}

		textArea.selectPositionCaret(usedPosition);
		textArea.selectPositionCaret(caretPosition);
	}

	@Override
	public Dimension getMinSize() {
		return new Dimension(
			(int) textArea.minWidth(TextArea.USE_COMPUTED_SIZE),
			(int) textArea.minHeight(TextArea.USE_COMPUTED_SIZE));
	}

}
