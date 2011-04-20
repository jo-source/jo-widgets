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
package org.jowidgets.spi.impl.swing.widgets;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.jowidgets.spi.impl.swing.widgets.util.InputModifierDocument;
import org.jowidgets.spi.widgets.ITextAreaSpi;
import org.jowidgets.spi.widgets.setup.ITextAreaSetupSpi;

public class TextAreaImpl extends AbstractInputControl implements ITextAreaSpi {

	private final JTextArea textArea;

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

		textArea.setDocument(new InputModifierDocument(textArea, this));
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
	}

	@Override
	public void setTooltipText(final String tooltipText) {
		textArea.setToolTipText(tooltipText);
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
	public void setCaretPosition(final int pos) {
		textArea.setCaretPosition(pos);
	}

	@Override
	public int getCaretPosition() {
		return textArea.getCaretPosition();
	}

	@Override
	public void scrollToCaretPosition() {
		//TODO MG implement scrollToCaretPosition
	}

}
