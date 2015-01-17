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
package org.jowidgets.spi.impl.swt.common.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.jowidgets.common.mask.TextMaskMode;
import org.jowidgets.common.types.InputChangeEventPolicy;
import org.jowidgets.common.types.Markup;
import org.jowidgets.common.verify.IInputVerifier;
import org.jowidgets.common.widgets.controller.IFocusListener;
import org.jowidgets.common.widgets.controller.IKeyListener;
import org.jowidgets.spi.impl.controller.FocusObservable;
import org.jowidgets.spi.impl.controller.KeyObservable;
import org.jowidgets.spi.impl.mask.TextMaskVerifierFactory;
import org.jowidgets.spi.impl.swt.common.options.SwtOptions;
import org.jowidgets.spi.impl.swt.common.util.FontProvider;
import org.jowidgets.spi.impl.swt.common.widgets.event.LazyKeyEventContentFactory;
import org.jowidgets.spi.impl.verify.InputVerifierHelper;
import org.jowidgets.spi.widgets.IComboBoxSelectionSpi;
import org.jowidgets.spi.widgets.IComboBoxSpi;
import org.jowidgets.spi.widgets.setup.IComboBoxSelectionSetupSpi;
import org.jowidgets.spi.widgets.setup.IComboBoxSetupSpi;
import org.jowidgets.util.Assert;
import org.jowidgets.util.event.IObservableCallback;

public class ComboBoxImpl extends AbstractInputControl implements IComboBoxSelectionSpi, IComboBoxSpi {

	public static final int KEY_CODE_DELETE = 0x7F; /* ASCII DEL */
	public static final int KEY_CODE_BACK_SPACE = '\b';

	private final boolean isAutoCompletionMode;
	private final boolean isSelectionMode;
	private final KeyListener keyListener;
	private final KeyObservable keyObservable;
	private final FocusObservable focusObservable;

	private boolean isPopupVisible;

	private boolean programmaticTextChange;

	public ComboBoxImpl(final Object parentUiReference, final IComboBoxSelectionSetupSpi setup) {
		super(createCombo((Composite) parentUiReference, setup));
		this.programmaticTextChange = true;
		this.isAutoCompletionMode = setup.isAutoCompletion();
		this.isSelectionMode = !(setup instanceof IComboBoxSetupSpi);
		this.isPopupVisible = false;

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
					if (event.keyCode != KEY_CODE_DELETE && event.keyCode != KEY_CODE_BACK_SPACE) {
						newEnteredText = event.text;
						pos = pos + event.text.length();
					}
					else {
						final Point selection = getUiReference().getSelection();
						if (event.keyCode == KEY_CODE_BACK_SPACE && selection.x != selection.y) {
							pos = Math.max(0, pos - 1);
						}
						newEnteredText = "";
					}

					final String previousText = getUiReference().getText();
					//linux impl of swt has event.end = -1 in some cases
					final int startIndex = Math.max(0, event.start);
					final int endIndex = Math.max(0, event.end);
					final String newText = previousText.substring(0, startIndex)
						+ newEnteredText
						+ previousText.substring(endIndex);

					if (!previousText.equals(newText)) {
						event.doit = doAutoCompletion(newText, event.keyCode, pos);
						if (isSelectionMode) {
							final int selectedIndex = getSelectedIndex();
							if (selectedIndex != -1) {
								final String[] elements = getElements();
								if (elements != null && selectedIndex < elements.length) {
									fireInputChanged(elements[selectedIndex]);
								}
							}
						}
					}

					programmaticTextChange = false;

				}
			});
		}

		getUiReference().addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				if (!programmaticTextChange) {
					fireInputChanged(getUiReference().getText());
				}
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {
				if (!programmaticTextChange) {
					fireInputChanged(getUiReference().getText());
				}
			}
		});

		if (setup.getInputChangeEventPolicy() == InputChangeEventPolicy.ANY_CHANGE) {
			if (!isSelectionMode || isAutoCompletionMode) {
				getUiReference().addModifyListener(new ModifyListener() {
					@Override
					public void modifyText(final ModifyEvent e) {
						if (!programmaticTextChange) {
							fireInputChanged(getUiReference().getText());
						}
					}
				});
			}
		}
		else if (setup.getInputChangeEventPolicy() == InputChangeEventPolicy.EDIT_FINISHED) {
			getUiReference().addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(final FocusEvent e) {
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
		else {
			getUiReference().addTraverseListener(new TraverseListener() {
				@Override
				public void keyTraversed(final TraverseEvent e) {
					if (e.detail == SWT.TRAVERSE_ESCAPE || e.detail == SWT.TRAVERSE_RETURN) {
						e.doit = false;
					}
				}
			});

		}

		if (SwtOptions.hasComboTruncateWorkaround() && (isAutoCompletionMode || !isSelectionMode)) {
			getUiReference().addListener(SWT.Resize, new ResizeTruncateWorkaroundListener());
		}

		this.keyListener = new KeyListener() {
			@Override
			public void keyReleased(final KeyEvent e) {
				keyObservable.fireKeyReleased(new LazyKeyEventContentFactory(e));
			}

			@Override
			public void keyPressed(final KeyEvent e) {
				keyObservable.fireKeyPressed(new LazyKeyEventContentFactory(e));

				if ((e.stateMask & SWT.ALT) == SWT.ALT && (e.keyCode == SWT.ARROW_UP || e.keyCode == SWT.ARROW_DOWN)) {
					isPopupVisible = true;
				}

				if (e.keyCode == SWT.ESC || e.keyCode == SWT.CR) {
					isPopupVisible = false;
				}
			}
		};

		final IObservableCallback keyObservableCallback = new IObservableCallback() {

			private boolean listenerAdded = false;

			@Override
			public void onLastUnregistered() {
				if (listenerAdded) {
					getUiReference().removeKeyListener(keyListener);
					listenerAdded = false;
				}
			}

			@Override
			public void onFirstRegistered() {
				if (!listenerAdded) {
					getUiReference().addKeyListener(keyListener);
					listenerAdded = true;
				}
			}
		};

		this.keyObservable = new KeyObservable(keyObservableCallback);

		this.focusObservable = new FocusObservable();
		getUiReference().addFocusListener(new FocusListener() {

			@Override
			public void focusLost(final FocusEvent e) {
				isPopupVisible = false;
				focusObservable.focusLost();
			}

			@Override
			public void focusGained(final FocusEvent e) {
				focusObservable.focusGained();
			}
		});

		programmaticTextChange = false;
	}

	@Override
	public void addKeyListener(final IKeyListener listener) {
		keyObservable.addKeyListener(listener);
	}

	@Override
	public void removeKeyListener(final IKeyListener listener) {
		keyObservable.removeKeyListener(listener);
	}

	@Override
	public void addFocusListener(final IFocusListener listener) {
		focusObservable.addFocusListener(listener);
	}

	@Override
	public void removeFocusListener(final IFocusListener listener) {
		focusObservable.removeFocusListener(listener);
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
		int max = 1;
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
	public void select() {
		getUiReference().setSelection(new Point(0, getUiReference().getText().length()));
	}

	@Override
	public void setCaretPosition(final int pos) {
		setSelection(pos, pos);
	}

	@Override
	public int getCaretPosition() {
		return getUiReference().getSelection().y;
	}

	@Override
	public void setPopupVisible(final boolean visible) {
		this.isPopupVisible = true;
		getUiReference().setListVisible(visible);
	}

	@Override
	public boolean isPopupVisible() {
		return isPopupVisible;
		//do not use is popup visible because it will be false if closing key events arrive,
		//so key observer can not determine if key event will open or closed the popup
		//return getUiReference().getListVisible();
	}

	private boolean doAutoCompletion(final String text, final int keyCode, final int pos) {
		if (!isSelectionMode && (keyCode == KEY_CODE_DELETE || keyCode == KEY_CODE_BACK_SPACE)) {
			return true;
		}

		final CompletionItem ci = getIndexOfPrefix(text);

		if (isSelectionMode && !ci.isPrefix && (keyCode == KEY_CODE_DELETE || keyCode == KEY_CODE_BACK_SPACE)) {
			getUiReference().setSelection(new Point(pos, getUiReference().getText().length()));
			return false;
		}

		if (ci.isPrefix) {
			getUiReference().select(ci.index);

			// Only open the popup list in selection due to windows behavior: If the list is visible,
			// Windows automatically selects an item, if the text partially matches an element 
			if (isSelectionMode && keyCode != 0 && !getUiReference().getListVisible()) {
				getUiReference().setListVisible(true);
				isPopupVisible = true;
			}

			getUiReference().setSelection(new Point(pos, ci.completion.length()));
			return false;
		}

		return !isSelectionMode || ci.isPrefix;
	}

	private CompletionItem getIndexOfPrefix(final String text) {
		String completion = "";
		boolean isPrefix = false;
		final String lowerText = text.toLowerCase();
		final boolean isEmpty = lowerText.length() == 0;
		final String[] items = getUiReference().getItems();

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
			int prefixIndex = -1;
			for (final String item : items) {
				if (item.toLowerCase().equals(lowerText)) {
					prefixIndex = -1;
					isPrefix = true;
					completion = item;
					break;
				}
				if (prefixIndex < 0 && item.toLowerCase().startsWith(lowerText)) {
					prefixIndex = index;
					isPrefix = true;
					completion = item;
				}
				index++;
			}
			if (prefixIndex >= 0) {
				index = prefixIndex;
			}
		}

		return new CompletionItem(index, isPrefix, completion);
	}

	private static final class CompletionItem {
		private final int index;
		private final boolean isPrefix;
		private final String completion;

		private CompletionItem(final int index, final boolean isPrefix, final String completion) {
			this.index = index;
			this.isPrefix = isPrefix;
			this.completion = completion;
		}
	}

	private final class ResizeTruncateWorkaroundListener implements Listener {
		@Override
		public void handleEvent(final Event event) {
			getUiReference().setSelection(new Point(0, 0));
		}
	};

}
