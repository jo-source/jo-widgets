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
package org.jowidgets.impl.swing.widgets.internal;

import java.awt.Component;
import java.awt.event.ActionListener;

import javax.swing.ComboBoxEditor;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jowidgets.impl.swing.widgets.internal.util.InputModifierDocument;
import org.jowidgets.spi.verify.IInputVerifier;
import org.jowidgets.spi.widgets.IComboBoxWidgetSpi;
import org.jowidgets.spi.widgets.setup.IComboBoxSetupSpi;

public class ComboBoxWidget extends ComboBoxSelectionWidget implements IComboBoxWidgetSpi {

	private final ComboBoxEditorImpl comboBoxEditor;

	public ComboBoxWidget(final IComboBoxSetupSpi setup) {
		super(setup);

		getUiReference().setEditable(true);
		this.comboBoxEditor = new ComboBoxEditorImpl(setup.getInputVerifier());

		getUiReference().setEditor(comboBoxEditor);

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

	private class ComboBoxEditorImpl implements ComboBoxEditor {

		private boolean setItemInvoked;
		private final JTextField textField;

		public ComboBoxEditorImpl(final IInputVerifier inputVerifier) {
			super();
			this.textField = new JTextField();
			this.textField.setBorder(new EmptyBorder(0, 0, 0, 0));

			this.setItemInvoked = false;

			this.textField.setDocument(new InputModifierDocument(textField, inputVerifier));
			this.textField.getDocument().addDocumentListener(new DocumentListener() {

				@Override
				public void removeUpdate(final DocumentEvent e) {
					if (!setItemInvoked) {
						fireInputChanged(textField);
					}
				}

				@Override
				public void insertUpdate(final DocumentEvent e) {
					if (!setItemInvoked) {
						fireInputChanged(textField);
					}
				}

				@Override
				public void changedUpdate(final DocumentEvent e) {
					if (!setItemInvoked) {
						fireInputChanged(textField);
					}
				}

			});
		}

		@Override
		public Component getEditorComponent() {
			return textField;
		}

		@Override
		public void setItem(final Object anObject) {
			if (!setItemInvoked) {
				setItemInvoked = true;
				textField.setText((String) anObject);
				setItemInvoked = false;
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

}
