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

import java.awt.Component;
import java.awt.event.ActionListener;

import javax.swing.ComboBoxEditor;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;

import org.jowidgets.common.verify.IInputVerifier;
import org.jowidgets.spi.impl.mask.TextMaskKeyListenerFactory;
import org.jowidgets.spi.impl.mask.TextMaskVerifierFactory;
import org.jowidgets.spi.impl.swing.threads.SwingUiThreadAccess;
import org.jowidgets.spi.impl.swing.widgets.util.InputModifierDocument;
import org.jowidgets.spi.impl.verify.InputVerifierHelper;
import org.jowidgets.spi.widgets.IComboBoxSpi;
import org.jowidgets.spi.widgets.setup.IComboBoxSetupSpi;
import org.jowidgets.util.NullCompatibleEquivalence;

public class ComboBoxImpl extends ComboBoxSelectionImpl implements IComboBoxSpi {

	private final ComboBoxEditorImpl comboBoxEditor;
	private String lastValue;
	private final Integer maxLength;

	public ComboBoxImpl(final IComboBoxSetupSpi setup) {
		super(setup);

		this.maxLength = setup.getMaxLength();

		getUiReference().setEditable(true);

		final IInputVerifier maskVerifier = TextMaskVerifierFactory.create(this, setup.getMask(), new SwingUiThreadAccess());

		this.comboBoxEditor = new ComboBoxEditorImpl(InputVerifierHelper.getInputVerifier(maskVerifier, setup));

		getUiReference().setEditor(comboBoxEditor);

		if (setup.getMask() != null) {
			setText(setup.getMask().getPlaceholder());
			addKeyListener(TextMaskKeyListenerFactory.create(this, setup.getMask()));
		}
	}

	@Override
	public void setEditable(final boolean editable) {
		super.setEditable(editable);
		getUiReference().setEditable(true);
	}

	@Override
	public String getText() {
		return comboBoxEditor.getItem();
	}

	@Override
	public void setText(final String text) {
		comboBoxEditor.setItem(text);
	}

	@Override
	public void setSelection(final int start, final int end) {
		comboBoxEditor.setSelection(start, end);
	}

	@Override
	public void setCaretPosition(final int pos) {
		comboBoxEditor.setCaretPosition(pos);
	}

	@Override
	public int getCaretPosition() {
		return comboBoxEditor.getCaretPosition();
	}

	private class ComboBoxEditorImpl implements ComboBoxEditor {

		private boolean setItemInvoked;
		private final JTextField textField;
		private final InputModifierDocument modifierDocument;

		public ComboBoxEditorImpl(final IInputVerifier inputVerifier) {
			super();
			this.textField = new JTextField();

			final Border border = (Border) UIManager.get("ComboBox.editorBorder");
			if (border != null) {
				textField.setBorder(border);
			}

			this.setItemInvoked = false;

			this.modifierDocument = new InputModifierDocument(textField, inputVerifier, ComboBoxImpl.this, maxLength);

			this.textField.setDocument(modifierDocument);
		}

		public int getCaretPosition() {
			return textField.getCaretPosition();
		}

		public void setCaretPosition(final int pos) {
			textField.setCaretPosition(pos);
		}

		public void setSelection(final int start, final int end) {
			textField.setSelectionStart(start);
			textField.setSelectionEnd(end);
		}

		@Override
		public Component getEditorComponent() {
			return textField;
		}

		@Override
		public void setItem(final Object anObject) {
			if (!setItemInvoked) {
				modifierDocument.setInputObservable(null);
				setItemInvoked = true;
				textField.setText((String) anObject);
				setItemInvoked = false;
				modifierDocument.setInputObservable(ComboBoxImpl.this);
			}
		}

		@Override
		public String getItem() {
			return textField.getText();
		}

		@Override
		public void selectAll() {
			textField.selectAll();
		}

		@Override
		public void addActionListener(final ActionListener listener) {}

		@Override
		public void removeActionListener(final ActionListener listener) {}

	}

	@Override
	public void fireInputChanged() {
		if (!NullCompatibleEquivalence.equals(getText(), lastValue)) {
			super.fireInputChanged();
			lastValue = getText();
		}
	}

}
