/*
 * Copyright (c) 2010, Michael Grossmann, Nikolaus Moll
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
package org.jowidgets.spi.impl.swing.common.widgets;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.InputChangeEventPolicy;
import org.jowidgets.common.types.Markup;
import org.jowidgets.common.verify.IInputVerifier;
import org.jowidgets.spi.impl.controller.InputObservable;
import org.jowidgets.spi.impl.swing.common.util.ColorConvert;
import org.jowidgets.spi.impl.swing.common.util.FontProvider;
import org.jowidgets.spi.impl.swing.common.widgets.util.InputModifierDocument;
import org.jowidgets.spi.impl.verify.InputVerifierHelper;
import org.jowidgets.spi.widgets.ITextAreaSpi;
import org.jowidgets.spi.widgets.setup.ITextAreaSetupSpi;

public class TextAreaImpl extends AbstractInputControl implements ITextAreaSpi {

	private final JTextArea textArea;

	static {
		final FontUIResource fontResource = (FontUIResource) UIManager.get("TextField.font");
		if (fontResource != null) {
			UIManager.put("TextArea.font", fontResource);
		}
	}

	public TextAreaImpl(final ITextAreaSetupSpi setup) {
		super(new JScrollPane(new JTextArea()));

		textArea = (JTextArea) getUiReference().getViewport().getView();

		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(setup.isLineWrap());

		textArea.setAutoscrolls(false);

		if (setup.isAlwaysShowBars()) {
			getUiReference().setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			if (!setup.isLineWrap()) {
				getUiReference().setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			}
		}

		if (!setup.hasBorder()) {
			getUiReference().setBorder(BorderFactory.createEmptyBorder());
			getUiReference().setViewportBorder(BorderFactory.createEmptyBorder());
		}

		final InputObservable inputObservable;
		if (setup.getInputChangeEventPolicy() == InputChangeEventPolicy.ANY_CHANGE) {
			inputObservable = this;
		}
		else if (setup.getInputChangeEventPolicy() == InputChangeEventPolicy.EDIT_FINISHED) {
			inputObservable = null;
			getUiReference().addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(final FocusEvent e) {
					fireInputChanged(getText());
				}
			});
		}
		else {
			throw new IllegalArgumentException("InputChangeEventPolicy '" + setup.getInputChangeEventPolicy() + "' is not known.");
		}

		final IInputVerifier inputVerifier = InputVerifierHelper.getInputVerifier(null, setup);
		textArea.setDocument(new InputModifierDocument(textArea, inputVerifier, inputObservable, setup.getMaxLength()));
	}

	@Override
	public JScrollPane getUiReference() {
		return (JScrollPane) super.getUiReference();
	}

	@Override
	public String getText() {
		return textArea.getText();
	}

	@Override
	public void setText(final String text) {
		textArea.setText(text);
		if (!textArea.isFocusOwner()) {
			fireInputChanged(getText());
		}
	}

	@Override
	public void append(final String text) {
		textArea.append(text);
		if (!textArea.isFocusOwner()) {
			fireInputChanged(getText());
		}
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		super.setForegroundColor(colorValue);
		textArea.setForeground(ColorConvert.convert(colorValue));
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		super.setBackgroundColor(colorValue);
		textArea.setBackground(ColorConvert.convert(colorValue));
	}

	@Override
	public void setFontSize(final int size) {
		textArea.setFont(FontProvider.deriveFont(textArea.getFont(), size));
	}

	@Override
	public void setFontName(final String fontName) {
		textArea.setFont(FontProvider.deriveFont(textArea.getFont(), fontName));
	}

	@Override
	public void setMarkup(final Markup markup) {
		textArea.setFont(FontProvider.deriveFont(textArea.getFont(), markup));
	}

	@Override
	public void setSelection(final int start, final int end) {
		textArea.setSelectionStart(start);
		textArea.setSelectionEnd(end);
	}

	@Override
	public void setEditable(final boolean editable) {
		textArea.setEditable(editable);
	}

	@Override
	public void setEnabled(final boolean enabled) {
		textArea.setEnabled(enabled);
	}

	@Override
	public void setCaretPosition(final int pos) {
		textArea.setCaretPosition(pos);
	}

	@Override
	public int getCaretPosition() {
		return textArea.getCaretPosition();
	}

	@Override
	public void scrollToCaretPosition() {
		final int caretPosition = textArea.getCaretPosition();
		final int usedPosition;

		final int next = Math.min(caretPosition + 1, textArea.getDocument().getLength());
		final int previous = Math.max(caretPosition - 1, 0);
		if (next != caretPosition) {
			usedPosition = next;
		}
		else {
			usedPosition = previous;
		}

		textArea.setCaretPosition(usedPosition);
		textArea.setCaretPosition(caretPosition);
	}

}
