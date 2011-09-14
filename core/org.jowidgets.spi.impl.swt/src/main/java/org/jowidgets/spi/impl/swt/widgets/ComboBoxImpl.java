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
package org.jowidgets.spi.impl.swt.widgets;

import java.awt.event.KeyEvent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.jowidgets.common.mask.TextMaskMode;
import org.jowidgets.common.types.InputChangeEventPolicy;
import org.jowidgets.common.types.Markup;
import org.jowidgets.common.verify.IInputVerifier;
import org.jowidgets.spi.impl.mask.TextMaskVerifierFactory;
import org.jowidgets.spi.impl.swt.options.SwtOptions;
import org.jowidgets.spi.impl.swt.util.FontProvider;
import org.jowidgets.spi.impl.verify.InputVerifierHelper;
import org.jowidgets.spi.widgets.IComboBoxSelectionSpi;
import org.jowidgets.spi.widgets.IComboBoxSpi;
import org.jowidgets.spi.widgets.setup.IComboBoxSelectionSetupSpi;
import org.jowidgets.spi.widgets.setup.IComboBoxSetupSpi;
import org.jowidgets.util.Assert;

public class ComboBoxImpl extends AbstractInputControl implements IComboBoxSelectionSpi, IComboBoxSpi {

	private final boolean isAutoCompletionMode;
	private final boolean isSelectionMode;
	private boolean programmaticTextChange;

	public ComboBoxImpl(final Object parentUiReference, final IComboBoxSelectionSetupSpi setup) {
		super(createCombo((Composite) parentUiReference, setup));
		this.programmaticTextChange = true;
		this.isAutoCompletionMode = setup.isAutoCompletion();
		this.isSelectionMode = !(setup instanceof IComboBoxSetupSpi);

		setElements(setup.getElements());

		if (isAutoCompletionMode) {
			getUiReference().addVerifyListener(new VerifyListener() {

				@Override
				public void verifyText(final VerifyEvent event) {
					if (programmaticTextChange) {
						return;
					}

					programmaticTextChange = true;
					final String newEnteredText;
					int pos = event.start;
					if (event.keyCode != KeyEvent.VK_DELETE && event.keyCode != KeyEvent.VK_BACK_SPACE) {
						newEnteredText = event.text;
						pos = pos + event.text.length();
					}
					else {
						final Point selection = getUiReference().getSelection();
						if (event.keyCode == KeyEvent.VK_BACK_SPACE && selection.x != selection.y) {
							pos = Math.max(0, pos - 1);
						}
						newEnteredText = "";
					}

					final String previousText = getUiReference().getText();
					final String newText = previousText.substring(0, event.start)
						+ newEnteredText
						+ previousText.substring(event.end);

					if (!previousText.equals(newText)) {
						event.doit = doAutoCompletion(newText, event.keyCode, pos);
					}
					programmaticTextChange = false;
				}
			});
		}

		if (setup.getInputChangeEventPolicy() == InputChangeEventPolicy.ANY_CHANGE) {
			getUiReference().addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(final ModifyEvent e) {
					fireInputChanged(getUiReference().getText());
				}
			});
		}
		else if (setup.getInputChangeEventPolicy() == InputChangeEventPolicy.EDIT_FINISHED) {
			getUiReference().addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(final FocusEvent e) {
					fireInputChanged(getUiReference().getText());
				}
			});
			getUiReference().addSelectionListener(new SelectionListener() {
				@Override
				public void widgetSelected(final SelectionEvent e) {
					fireInputChanged(getUiReference().getText());
				}

				@Override
				public void widgetDefaultSelected(final SelectionEvent e) {
					fireInputChanged(getUiReference().getText());
				}
			});
		}
		else {
			throw new IllegalArgumentException("InputChangeEventPolicy '" + setup.getInputChangeEventPolicy() + "' is not known.");
		}

		if (!isSelectionMode) {
			final IComboBoxSetupSpi comboBoxSetup = (IComboBoxSetupSpi) setup;
			if (SwtOptions.hasInputVerification()) {
				final IInputVerifier maskVerifier = TextMaskVerifierFactory.create(this, comboBoxSetup.getMask());

				final IInputVerifier inputVerifier = InputVerifierHelper.getInputVerifier(maskVerifier, comboBoxSetup);
				if (inputVerifier != null) {
					this.getUiReference().addVerifyListener(new VerifyListener() {
						@Override
						public void verifyText(final VerifyEvent verifyEvent) {
							verifyEvent.doit = inputVerifier.verify(
									getUiReference().getText(),
									verifyEvent.text,
									verifyEvent.start,
									verifyEvent.end);
						}
					});
				}
			}

			if (comboBoxSetup.getMaxLength() != null) {
				getUiReference().setTextLimit(comboBoxSetup.getMaxLength().intValue());
			}

			if (SwtOptions.hasInputVerification()
				&& comboBoxSetup.getMask() != null
				&& TextMaskMode.FULL_MASK == comboBoxSetup.getMask().getMode()) {
				setText(comboBoxSetup.getMask().getPlaceholder());
			}
		}

		programmaticTextChange = false;
	}

	private static Combo createCombo(final Composite parent, final IComboBoxSelectionSetupSpi setup) {
		final boolean isAutoCompletionMode = setup.isAutoCompletion();
		final boolean isSelectionMode = !(setup instanceof IComboBoxSetupSpi);
		final boolean hasEditor = isAutoCompletionMode || !isSelectionMode;

		final int flags;
		if (hasEditor) {
			flags = SWT.NONE | SWT.DROP_DOWN;
		}
		else {
			flags = SWT.NONE | SWT.READ_ONLY;
		}

		return new Combo(parent, flags);
	}

	private static int getMaxTextLength(final String[] elements) {
		int max = 0;
		if (elements != null) {
			for (final String item : elements) {
				max = Math.max(max, item.length());
			}
		}

		return max;
	}

	@Override
	public Combo getUiReference() {
		return (Combo) super.getUiReference();
	}

	@Override
	public void setEditable(final boolean editable) {
		getUiReference().setEnabled(editable);
	}

	@Override
	public String[] getElements() {
		return getUiReference().getItems();
	}

	@Override
	public void setElements(final String[] elements) {
		Assert.paramNotNull(elements, "elements");
		programmaticTextChange = true;
		getUiReference().setItems(elements);
		if (isAutoCompletionMode && isSelectionMode && elements.length > 0) {
			getUiReference().setTextLimit(getMaxTextLength(elements));
		}
		programmaticTextChange = false;
	}

	@Override
	public int getSelectedIndex() {
		return getUiReference().getSelectionIndex();
	}

	@Override
	public void setSelectedIndex(final int index) {
		programmaticTextChange = true;
		if (index > -1) {
			getUiReference().select(index);
		}
		else {
			getUiReference().deselect(getUiReference().getSelectionIndex());
		}
		programmaticTextChange = false;
		if (!getUiReference().isFocusControl()) {
			fireInputChanged(getUiReference().getText());
		}
	}

	@Override
	public String getText() {
		return getUiReference().getText();
	}

	@Override
	public void setText(final String text) {
		programmaticTextChange = true;
		getUiReference().setText(text);
		programmaticTextChange = false;
		if (!getUiReference().isFocusControl()) {
			fireInputChanged(text);
		}
	}

	@Override
	public void setFontSize(final int size) {
		getUiReference().setFont(FontProvider.deriveFont(getUiReference().getFont(), size));
	}

	@Override
	public void setFontName(final String fontName) {
		getUiReference().setFont(FontProvider.deriveFont(getUiReference().getFont(), fontName));
	}

	@Override
	public void setMarkup(final Markup markup) {
		getUiReference().setFont(FontProvider.deriveFont(getUiReference().getFont(), markup));
	}

	@Override
	public void setSelection(final int start, final int end) {
		getUiReference().setSelection(new Point(start, end));
	}

	@Override
	public void setCaretPosition(final int pos) {
		setSelection(pos, pos);
	}

	@Override
	public int getCaretPosition() {
		return getUiReference().getSelection().y;
	}

	private boolean doAutoCompletion(final String text, final int keyCode, final int pos) {
		if (!isSelectionMode && (keyCode == KeyEvent.VK_DELETE || keyCode == KeyEvent.VK_BACK_SPACE)) {
			return true;
		}

		final String lowerText = text.toLowerCase();
		final boolean isEmpty = lowerText.length() == 0;
		final String[] items = getUiReference().getItems();
		boolean isPrefix = false;
		String completion = "";
		int index = 0;

		if (isEmpty) {
			for (final String item : items) {
				if (item.length() == 0) {
					isPrefix = true;
					completion = item;
					break;
				}
				index++;
			}
		}
		else {
			for (final String item : items) {
				if (item.toLowerCase().startsWith(lowerText)) {
					isPrefix = true;
					completion = item;
					break;
				}
				index++;
			}
		}

		if (isSelectionMode && !isPrefix && (keyCode == KeyEvent.VK_DELETE || keyCode == KeyEvent.VK_BACK_SPACE)) {
			getUiReference().setSelection(new Point(pos, getUiReference().getText().length()));
			return false;
		}

		if (isPrefix) {
			getUiReference().select(index);

			// Only open the popup list in selection due to windows behavior: If the list is visible,
			// Windows automatically selects an item, if the text partially matches an element 
			if (isSelectionMode && keyCode != 0 && !getUiReference().getListVisible()) {
				getUiReference().setListVisible(true);
			}

			getUiReference().setSelection(new Point(pos, completion.length()));
			return false;
		}

		return !isSelectionMode || isPrefix;
	}

}
