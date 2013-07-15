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

import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ComboBoxEditor;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.mask.TextMaskMode;
import org.jowidgets.common.types.InputChangeEventPolicy;
import org.jowidgets.common.types.Markup;
import org.jowidgets.common.verify.IInputVerifier;
import org.jowidgets.common.widgets.controller.IKeyListener;
import org.jowidgets.spi.impl.controller.IObservableCallback;
import org.jowidgets.spi.impl.controller.InputObservable;
import org.jowidgets.spi.impl.controller.KeyObservable;
import org.jowidgets.spi.impl.mask.TextMaskVerifierFactory;
import org.jowidgets.spi.impl.swing.common.util.ColorConvert;
import org.jowidgets.spi.impl.swing.common.util.FontProvider;
import org.jowidgets.spi.impl.swing.common.widgets.event.LazyKeyEventContentFactory;
import org.jowidgets.spi.impl.swing.common.widgets.util.InputModifierDocument;
import org.jowidgets.spi.impl.verify.InputVerifierHelper;
import org.jowidgets.spi.widgets.IComboBoxSelectionSpi;
import org.jowidgets.spi.widgets.IComboBoxSpi;
import org.jowidgets.spi.widgets.setup.IComboBoxSelectionSetupSpi;
import org.jowidgets.spi.widgets.setup.IComboBoxSetupSpi;
import org.jowidgets.util.Tuple;

public class ComboBoxImpl extends AbstractInputControl implements IComboBoxSelectionSpi, IComboBoxSpi {

	private final ComboBoxEditorImpl comboBoxEditor;
	private Integer maxLength;

	private final KeyObservable keyObservable;
	private final KeyListener keyListener;
	private final boolean isAutoCompletionMode;
	private final boolean isSelectionMode;

	private boolean inputEventsEnabled;

	private AutoCompletionModel autoCompletionModel;
	private final IInputVerifier inputVerifier;

	public ComboBoxImpl(final IComboBoxSelectionSetupSpi setup) {
		super(new JComboBox());
		this.inputEventsEnabled = true;

		this.isAutoCompletionMode = setup.isAutoCompletion();
		this.isSelectionMode = !(setup instanceof IComboBoxSetupSpi);
		final boolean hasEditor = isAutoCompletionMode || !isSelectionMode;

		getUiReference().setModel(createComboBoxModel(setup.getElements()));

		final IInputVerifier maskVerifier;
		String initialText = null;
		if (setup instanceof IComboBoxSetupSpi) {
			final IComboBoxSetupSpi comboBoxSetup = (IComboBoxSetupSpi) setup;

			this.maxLength = comboBoxSetup.getMaxLength();
			maskVerifier = TextMaskVerifierFactory.create(this, comboBoxSetup.getMask());
			inputVerifier = InputVerifierHelper.getInputVerifier(maskVerifier, comboBoxSetup);

			if (comboBoxSetup.getMask() != null && TextMaskMode.FULL_MASK == comboBoxSetup.getMask().getMode()) {
				initialText = comboBoxSetup.getMask().getPlaceholder();
			}
		}
		else {
			inputVerifier = null;
			maskVerifier = null;
			maxLength = null;
		}

		if (hasEditor) {
			getUiReference().setEditable(true);
			this.comboBoxEditor = new ComboBoxEditorImpl(inputVerifier, setup.getInputChangeEventPolicy());

			getUiReference().setEditor(comboBoxEditor);

			if (initialText != null) {
				setText(initialText);
			}

			this.keyListener = new KeyAdapter() {
				@Override
				public void keyReleased(final KeyEvent e) {
					keyObservable.fireKeyReleased(new LazyKeyEventContentFactory(e));
				}

				@Override
				public void keyPressed(final KeyEvent e) {
					keyObservable.fireKeyPressed(new LazyKeyEventContentFactory(e));
				}
			};
			final IObservableCallback keyObservableCallback = new IObservableCallback() {
				@Override
				public void onLastUnregistered() {
					comboBoxEditor.removeKeyListener(keyListener);
				}

				@Override
				public void onFirstRegistered() {
					comboBoxEditor.addKeyListener(keyListener);
				}
			};
			this.keyObservable = new KeyObservable(keyObservableCallback);

			if (isAutoCompletionMode) {
				getUiReference().addPopupMenuListener(new PopupMenuListener() {
					private boolean canceled;

					@Override
					public void popupMenuWillBecomeVisible(final PopupMenuEvent e) {
						canceled = false;
					}

					@Override
					public void popupMenuWillBecomeInvisible(final PopupMenuEvent e) {
						if (!canceled) {
							comboBoxEditor.setCaretPosition(comboBoxEditor.getItem().length());
						}
					}

					@Override
					public void popupMenuCanceled(final PopupMenuEvent e) {
						canceled = true;
					}
				});
			}
		}
		else {
			this.comboBoxEditor = null;
			this.keyObservable = null;
			this.keyListener = null;
		}

		if (setup.getInputChangeEventPolicy() == InputChangeEventPolicy.ANY_CHANGE) {
			getUiReference().addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(final ItemEvent e) {
					if (e.getID() == ItemEvent.ITEM_STATE_CHANGED && e.getStateChange() == ItemEvent.SELECTED) {
						fireInputChanged();
					}
				}
			});
		}
		else if (setup.getInputChangeEventPolicy() == InputChangeEventPolicy.EDIT_FINISHED) {
			getUiReference().addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(final FocusEvent e) {
					fireInputChanged();
				}
			});
		}
		else {
			throw new IllegalArgumentException("InputChangeEventPolicy '" + setup.getInputChangeEventPolicy() + "' is not known.");
		}
	}

	private ComboBoxModel createComboBoxModel(final String[] elements) {
		final ComboBoxModel result;
		if (isAutoCompletionMode) {
			if (isSelectionMode) {
				int max = 0;
				for (final String item : elements) {
					max = Math.max(item.length(), max);
				}
				maxLength = max;
			}

			autoCompletionModel = new AutoCompletionModel(elements);
			result = autoCompletionModel;
		}
		else {
			result = new DefaultComboBoxModel(elements);
			autoCompletionModel = null;
		}
		return result;
	}

	@Override
	public JComboBox getUiReference() {
		return (JComboBox) super.getUiReference();
	}

	@Override
	public void setEditable(final boolean editable) {
		getUiReference().setEnabled(editable);
		if (comboBoxEditor != null) {
			getUiReference().setEditable(editable);
		}
	}

	@Override
	public int getSelectedIndex() {
		return getUiReference().getSelectedIndex();
	}

	@Override
	public void setSelectedIndex(final int index) {
		getUiReference().setSelectedIndex(index);
	}

	@Override
	public String[] getElements() {
		final ComboBoxModel model = getUiReference().getModel();
		final String[] result = new String[model.getSize()];
		for (int i = 0; i < result.length; i++) {
			result[i] = (String) model.getElementAt(i);
		}
		return result;
	}

	@Override
	public void setElements(final String[] elements) {
		getUiReference().setModel(createComboBoxModel(elements));
	}

	@Override
	public String getText() {
		if (comboBoxEditor != null) {
			return comboBoxEditor.getItem();
		}
		else {
			return null;
		}
	}

	@Override
	public void setText(final String text) {
		if (comboBoxEditor != null) {
			comboBoxEditor.setItem(text);
		}
	}

	@Override
	public void setFontSize(final int size) {
		if (comboBoxEditor != null) {
			comboBoxEditor.setFontSize(size);
		}
	}

	@Override
	public void setFontName(final String fontName) {
		if (comboBoxEditor != null) {
			comboBoxEditor.setFontName(fontName);
		}
	}

	@Override
	public void setMarkup(final Markup markup) {
		if (comboBoxEditor != null) {
			comboBoxEditor.setMarkup(markup);
		}
	}

	@Override
	public void setSelection(final int start, final int end) {
		if (comboBoxEditor != null) {
			comboBoxEditor.setSelection(start, end);
		}
	}

	@Override
	public void setCaretPosition(final int pos) {
		if (comboBoxEditor != null) {
			comboBoxEditor.setCaretPosition(pos);
		}
	}

	@Override
	public int getCaretPosition() {
		if (comboBoxEditor != null) {
			return comboBoxEditor.getCaretPosition();
		}
		else {
			return 0;
		}
	}

	@Override
	public void addKeyListener(final IKeyListener listener) {
		if (keyObservable != null) {
			keyObservable.addKeyListener(listener);
		}
	}

	@Override
	public void removeKeyListener(final IKeyListener listener) {
		if (keyObservable != null) {
			keyObservable.removeKeyListener(listener);
		}
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		getUiReference().getEditor().getEditorComponent().setForeground(ColorConvert.convert(colorValue));
		super.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		getUiReference().getEditor().getEditorComponent().setBackground(ColorConvert.convert(colorValue));
		super.setBackgroundColor(colorValue);
	}

	private void fireInputChanged() {
		if (inputEventsEnabled) {
			super.fireInputChanged(new Tuple<Integer, String>(getSelectedIndex(), getText()));
		}
	}

	// Input Observable method
	@Override
	public void fireInputChanged(final Object value) {
		if (inputEventsEnabled) {
			super.fireInputChanged(new Tuple<Integer, String>(getSelectedIndex(), getText()));
		}
	}

	private class ComboBoxEditorImpl implements ComboBoxEditor {

		private boolean setItemInvoked;
		private final JTextField textField;
		private final InputModifierDocument modifierDocument;
		private final InputObservable inputObservable;
		private boolean keyPressedBackspace;
		private boolean keyPressedDelete;
		private boolean deleteOnSelection;

		public ComboBoxEditorImpl(final IInputVerifier inputVerifier, final InputChangeEventPolicy inputChangeEventPolicy) {
			super();
			this.textField = new JTextField();

			final Border border = (Border) UIManager.get("ComboBox.editorBorder");
			if (border != null) {
				textField.setBorder(border);
			}

			this.setItemInvoked = false;

			if (inputChangeEventPolicy == InputChangeEventPolicy.ANY_CHANGE) {
				inputObservable = ComboBoxImpl.this;
			}
			else if (inputChangeEventPolicy == InputChangeEventPolicy.EDIT_FINISHED) {
				inputObservable = null;
				textField.addFocusListener(new FocusAdapter() {
					@Override
					public void focusLost(final FocusEvent e) {
						fireInputChanged();
					}
				});
			}
			else {
				throw new IllegalArgumentException("InputChangeEventPolicy '" + inputChangeEventPolicy + "' is not known.");
			}

			if (isAutoCompletionMode) {
				if (isSelectionMode) {
					this.modifierDocument = new AutoCompletionSelectionDocument(
						textField,
						inputVerifier,
						inputObservable,
						maxLength);
				}
				else {
					this.modifierDocument = new AutoCompletionDocument(textField, inputVerifier, inputObservable, maxLength);
				}
			}
			else {
				this.modifierDocument = new InputModifierDocument(textField, inputVerifier, inputObservable, maxLength);
			}

			this.textField.setDocument(modifierDocument);

			this.textField.addKeyListener(new KeyAdapter() {

				@Override
				public void keyPressed(final KeyEvent e) {
					if (e.getKeyChar() != KeyEvent.CHAR_UNDEFINED && getUiReference().isDisplayable()) {
						if (!getUiReference().isPopupVisible()) {
							getUiReference().setPopupVisible(true);
						}
					}

					keyPressedBackspace = (e.getKeyCode() == KeyEvent.VK_BACK_SPACE);
					keyPressedDelete = (e.getKeyCode() == KeyEvent.VK_DELETE);

					if (keyPressedBackspace || keyPressedDelete) {
						deleteOnSelection = textField.getSelectionStart() != textField.getSelectionEnd();
					}
				}
			});

		}

		public void addKeyListener(final KeyListener keyListener) {
			textField.addKeyListener(keyListener);

		}

		public void removeKeyListener(final KeyListener keyListener) {
			textField.removeKeyListener(keyListener);
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
				modifierDocument.setInputObservable(inputObservable);
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

		public void setFontSize(final int size) {
			textField.setFont(FontProvider.deriveFont(getUiReference().getFont(), size));
		}

		public void setFontName(final String fontName) {
			textField.setFont(FontProvider.deriveFont(getUiReference().getFont(), fontName));
		}

		public void setMarkup(final Markup markup) {
			textField.setFont(FontProvider.deriveFont(getUiReference().getFont(), markup));
		}

		@Override
		public void addActionListener(final ActionListener listener) {}

		@Override
		public void removeActionListener(final ActionListener listener) {}

		public void setSelectionStart(final int start) {
			textField.setSelectionStart(start);
		}

		public void setSelectionEnd(final int end) {
			textField.setSelectionEnd(end);
		}

		public void moveCaretPosition(final int pos) {
			textField.moveCaretPosition(pos);
		}
	}

	private final class AutoCompletionModel extends DefaultComboBoxModel {
		private static final long serialVersionUID = 7679165913730011187L;

		private final String[] elements;

		public AutoCompletionModel(final String[] elements) {
			// TODO MG,NM review: copy instead?
			this.elements = elements;
		}

		@Override
		public int getSize() {
			return elements.length;
		}

		@Override
		public Object getElementAt(final int index) {
			return elements[index];
		}
	}

	private abstract class AbstractAutoCompletionDocument extends InputModifierDocument {
		private static final long serialVersionUID = 1L;
		private boolean programmaticModelUpdate;
		//private final DocumentListener[] listenerList;
		private int disableCount;

		public AbstractAutoCompletionDocument(
			final JTextComponent textComponent,
			final IInputVerifier inputVerifier,
			final InputObservable inputObservable,
			final Integer maxLength) {
			super(textComponent, inputVerifier, inputObservable, maxLength);
			disableCount = 0;
		}

		protected boolean isValidPrefix(final String text) {
			final String lowerText = text.toLowerCase();
			for (final String item : autoCompletionModel.elements) {
				if (item.toLowerCase().startsWith(lowerText)) {
					return true;
				}
			}
			return false;
		}

		protected String getFirstMatch(final String text) {
			final String lowerText = text.toLowerCase();
			String firstMatch = null;
			for (final String item : autoCompletionModel.elements) {
				final String lowerItem = item.toLowerCase();
				if (lowerItem.equals(lowerText)) {
					return item;
				}
				if (firstMatch == null && (lowerItem.startsWith(lowerText))) {
					firstMatch = item;
				}
			}
			if (firstMatch != null) {
				return firstMatch;
			}
			else if (isSelectionMode) {
				return "";
			}
			else {
				return text;
			}
		}

		protected String getStringAfterReplacing(final int offset, final int length, final String text) throws BadLocationException {
			String result = getText(0, offset) + text;
			if (offset + length < getLength()) {
				result = result + getText(offset + length + 1, getLength() - offset - length);
			}
			return result;
		}

		protected void beginProgrammaticModelUpdate() {
			programmaticModelUpdate = true;
		}

		protected void endProgrammaticModelUpdate() {
			programmaticModelUpdate = false;
		}

		protected boolean isProgrammaticModelUpdate() {
			return programmaticModelUpdate;
		}

		protected void disableEvents() {
			if (disableCount == 0) {
				inputEventsEnabled = false;
			}
			disableCount++;
		}

		protected void enableEvents() throws BadLocationException {
			disableCount--;
			if (disableCount == 0) {
				inputEventsEnabled = true;
				fireInputChanged();
			}
		}
	}

	private final class AutoCompletionDocument extends AbstractAutoCompletionDocument {

		private static final long serialVersionUID = -745323548478120663L;

		public AutoCompletionDocument(
			final JTextComponent textComponent,
			final IInputVerifier inputVerifier,
			final InputObservable inputObservable,
			final Integer maxLength) {
			super(textComponent, inputVerifier, inputObservable, maxLength);
		}

		@Override
		public void insertString(final int offs, final String str, final AttributeSet a) throws BadLocationException {
			// all changes are allowed
			if (isProgrammaticModelUpdate()) {
				return;
			}

			// check if changes are valid
			final String text = getStringAfterReplacing(offs, 0, str);
			if (!isValidPrefix(text)) {
				// no auto completion
				super.insertString(offs, str, a);
			}
			else {
				disableEvents();

				// do auto completion
				final String autoCompletion = getFirstMatch(text);
				beginProgrammaticModelUpdate();
				autoCompletionModel.setSelectedItem(autoCompletion);
				endProgrammaticModelUpdate();

				super.remove(0, getLength());
				super.insertString(0, autoCompletion, a);

				if (comboBoxEditor != null) {
					comboBoxEditor.setSelectionEnd(getLength());
					comboBoxEditor.moveCaretPosition(offs + str.length());
				}
				enableEvents();
			}
		}

		@Override
		public void replace(final int offset, final int length, final String text, final AttributeSet attrs) throws BadLocationException {
			disableEvents();
			super.replace(offset, length, text, attrs);
			enableEvents();
		}

	}

	private final class AutoCompletionSelectionDocument extends AbstractAutoCompletionDocument {

		private static final long serialVersionUID = -745323548478120664L;

		public AutoCompletionSelectionDocument(
			final JTextComponent textComponent,
			final IInputVerifier inputVerifier,
			final InputObservable inputObservable,
			final Integer maxLength) {
			super(textComponent, inputVerifier, inputObservable, maxLength);
		}

		@Override
		public void insertString(int offs, final String str, final AttributeSet a) throws BadLocationException {
			if (isProgrammaticModelUpdate()) {
				return;
			}

			// check if changes are valid
			String text = getStringAfterReplacing(offs, 0, str);
			if (!isValidPrefix(text)) {
				final Object item = getUiReference().getSelectedItem();
				if (item == null) {
					text = "";
				}
				else {
					text = item.toString();
				}
				offs = offs - str.length();
				UIManager.getLookAndFeel().provideErrorFeedback(getUiReference());
			}

			final String autoCompletion = getFirstMatch(text);
			beginProgrammaticModelUpdate();
			autoCompletionModel.setSelectedItem(autoCompletion);
			endProgrammaticModelUpdate();

			disableEvents();
			super.remove(0, getLength());
			enableEvents();
			super.insertString(0, autoCompletion, a);

			if (comboBoxEditor != null) {
				comboBoxEditor.setSelectionEnd(getLength());
				comboBoxEditor.setSelectionStart(offs + str.length());
			}
		}

		@Override
		public void remove(int offs, int len) throws BadLocationException {
			if (isProgrammaticModelUpdate()) {
				return;
			}

			if (isValidPrefix("") && offs == 0 && getLength() == len) {
				beginProgrammaticModelUpdate();
				autoCompletionModel.setSelectedItem("");
				endProgrammaticModelUpdate();
				super.remove(0, len);
			}
			else if (comboBoxEditor.keyPressedBackspace || comboBoxEditor.keyPressedDelete) {
				if (comboBoxEditor.keyPressedBackspace && offs > 0 && comboBoxEditor.deleteOnSelection) {
					offs--;
					len++;
				}

				final String newText = getStringAfterReplacing(offs, len, "");
				if (!isValidPrefix(newText)) {
					len = getLength() - offs;
				}

				if (comboBoxEditor.keyPressedBackspace || (comboBoxEditor.keyPressedDelete && !comboBoxEditor.deleteOnSelection)) {
					comboBoxEditor.setCaretPosition(getLength());
					comboBoxEditor.moveCaretPosition(offs);
				}
			}
			else {
				super.remove(offs, len);
			}
		}
	}

}
