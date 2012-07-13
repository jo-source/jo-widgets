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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyEvent;

import org.jowidgets.common.mask.TextMaskMode;
import org.jowidgets.common.types.InputChangeEventPolicy;
import org.jowidgets.common.types.Markup;
import org.jowidgets.spi.widgets.IComboBoxSelectionSpi;
import org.jowidgets.spi.widgets.IComboBoxSpi;
import org.jowidgets.spi.widgets.setup.IComboBoxSelectionSetupSpi;
import org.jowidgets.spi.widgets.setup.IComboBoxSetupSpi;

public class ComboBoxImpl extends AbstractInputControl implements IComboBoxSelectionSpi, IComboBoxSpi {

	private final StyleDelegate styleDelegate;
	private final boolean isAutoCompletionMode;
	private final boolean isSelectionMode;

	public ComboBoxImpl(final IComboBoxSelectionSetupSpi setup) {
		super(new ComboBox<String>());
		styleDelegate = new StyleDelegate(getUiReference());

		this.isAutoCompletionMode = setup.isAutoCompletion();
		this.isSelectionMode = !(setup instanceof IComboBoxSetupSpi);
		final boolean hasEditor = isAutoCompletionMode || !isSelectionMode;
		String initialText = null;

		if (setup instanceof IComboBoxSetupSpi) {
			final IComboBoxSetupSpi comboBoxSetup = (IComboBoxSetupSpi) setup;

			if (comboBoxSetup.getMask() != null && TextMaskMode.FULL_MASK == comboBoxSetup.getMask().getMode()) {
				initialText = comboBoxSetup.getMask().getPlaceholder();
			}

		}

		if (hasEditor) {
			getUiReference().setEditable(true);
			if (initialText != null) {
				setText(initialText);
			}
		}

		if (setup.getInputChangeEventPolicy() == InputChangeEventPolicy.ANY_CHANGE) {
			getUiReference().getEditor().setOnKeyReleased(new EventHandler<KeyEvent>() {
				@Override
				public void handle(final KeyEvent event) {
					fireInputChanged();
				}
			});

			getUiReference().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

				@Override
				public void changed(
					final ObservableValue<? extends String> observableValue,
					final String oldValue,
					final String newValue) {
					if (newValue != null) {
						if (!newValue.equals(oldValue)) {
							fireInputChanged();
						}
					}
				}
			});
		}
		else if (setup.getInputChangeEventPolicy() == InputChangeEventPolicy.EDIT_FINISHED) {
			getUiReference().focusedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(
					final ObservableValue<? extends Boolean> paramObservableValue,
					final Boolean oldValue,
					final Boolean newValue) {
					fireInputChanged();
				}
			});
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public ComboBox<String> getUiReference() {
		return (ComboBox<String>) super.getUiReference();
	}

	@Override
	public void setEditable(final boolean editable) {
		getUiReference().setEditable(editable);
	}

	@Override
	public String getText() {
		return getUiReference().getValue();
	}

	@Override
	public void setText(final String text) {
		getUiReference().setValue(text);

	}

	@Override
	public void setFontSize(final int size) {
		styleDelegate.setFontSize(size);

	}

	@Override
	public void setFontName(final String fontName) {
		styleDelegate.setFontName(fontName);

	}

	@Override
	public void setMarkup(final Markup markup) {
		styleDelegate.setMarkup(markup);

	}

	@Override
	public void setSelection(final int start, final int end) {
		getUiReference().getEditor().selectRange(start, end);
	}

	@Override
	public void setCaretPosition(final int pos) {
		getUiReference().getEditor().positionCaret(pos);
	}

	@Override
	public int getCaretPosition() {
		return getUiReference().getEditor().getCaretPosition();
	}

	@Override
	public int getSelectedIndex() {
		return getUiReference().getSelectionModel().getSelectedIndex();
	}

	@Override
	public void setSelectedIndex(final int index) {
		getUiReference().getSelectionModel().select(index);

	}

	@Override
	public String[] getElements() {
		final ObservableList<String> items = getUiReference().getItems();
		final String[] elements = new String[items.size()];
		for (int i = 0; i < items.size(); i++) {
			elements[i] = items.get(i);
		}

		return elements;
	}

	@Override
	public void setElements(final String[] elements) {
		final ObservableList<String> list = FXCollections.observableArrayList();
		for (int i = 0; i < elements.length; i++) {
			list.add(elements[i]);
		}
		getUiReference().setItems(list);
	}

	private void fireInputChanged() {
		super.fireInputChanged(getUiReference().getSelectionModel().getSelectedItem());
	}
}
