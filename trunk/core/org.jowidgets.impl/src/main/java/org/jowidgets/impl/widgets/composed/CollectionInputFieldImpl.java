/*
 * Copyright (c) 2011, Michael Grossmann, Nikolaus Moll
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
package org.jowidgets.impl.widgets.composed;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.jowidgets.api.convert.IConverter;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IInputControl;
import org.jowidgets.api.widgets.IInputDialog;
import org.jowidgets.api.widgets.IInputField;
import org.jowidgets.api.widgets.blueprint.IButtonBluePrint;
import org.jowidgets.api.widgets.blueprint.ICollectionInputDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.api.widgets.descriptor.ICollectionInputFieldDescriptor;
import org.jowidgets.api.widgets.descriptor.setup.ICollectionInputDialogSetup;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Modifier;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.types.VirtualKey;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.common.widgets.controller.IInputListener;
import org.jowidgets.common.widgets.controller.IKeyEvent;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.controller.InputObservable;
import org.jowidgets.tools.controller.KeyAdapter;
import org.jowidgets.tools.validation.CompoundValidator;
import org.jowidgets.tools.validation.ValidationCache;
import org.jowidgets.tools.validation.ValidationCache.IValidationResultCreator;
import org.jowidgets.tools.widgets.wrapper.ControlWrapper;
import org.jowidgets.util.EmptyCheck;
import org.jowidgets.validation.IValidationConditionListener;
import org.jowidgets.validation.IValidationResult;
import org.jowidgets.validation.IValidationResultBuilder;
import org.jowidgets.validation.IValidator;
import org.jowidgets.validation.ValidationResult;

public class CollectionInputFieldImpl<ELEMENT_TYPE> extends ControlWrapper implements IInputControl<Collection<ELEMENT_TYPE>> {

	private static final String ELEMENT = Messages.getString("CollectionInputFieldImpl.element"); //$NON-NLS-1$
	private static final String EDIT = Messages.getString("CollectionInputFieldImpl.edit"); //$NON-NLS-1$
	private static final Character DEFAULT_SEPARATOR = Character.valueOf(',');

	private final boolean filterEmptyValues;
	private final boolean dublicatesAllowed;
	private final IInputField<String> textField;
	private final IButton editButton;
	private final IConverter<ELEMENT_TYPE> converter;
	private final Character separator;
	private final Character maskingCharacter;
	private final ValidationCache validationCache;
	private final CompoundValidator<Collection<ELEMENT_TYPE>> compoundValidator;
	private final InputObservable inputObservable;
	private final ICollectionInputDialogBluePrint<ELEMENT_TYPE> inputDialogBp;

	private Dimension lastDialogSize;

	private final Collection<String> value;

	private boolean editable; // TODO MG replace by getter when implemented

	public CollectionInputFieldImpl(final IComposite composite, final ICollectionInputFieldDescriptor<ELEMENT_TYPE> setup) {
		super(composite);

		this.filterEmptyValues = setup.isFilterEmptyValues();
		this.dublicatesAllowed = setup.getDublicatesAllowed();
		this.converter = setup.getConverter();

		if (setup.getSeparator() != null) {
			this.separator = setup.getSeparator();
		}
		else {
			this.separator = DEFAULT_SEPARATOR;
		}
		this.maskingCharacter = setup.getMaskingCharacter();

		final ICollectionInputDialogSetup<ELEMENT_TYPE> inputDialogSetup = setup.getCollectionInputDialogSetup();

		this.value = new LinkedList<String>();
		this.inputObservable = new InputObservable();
		this.compoundValidator = new CompoundValidator<Collection<ELEMENT_TYPE>>();

		this.editable = true;

		if (inputDialogSetup != null) {
			composite.setLayout(new MigLayoutDescriptor("0[grow, 0::]2[]0", "0[grow]0")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		else {
			composite.setLayout(new MigLayoutDescriptor("0[grow, 0::]0", "0[grow]0")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();
		this.textField = composite.add(bpf.inputFieldString(), "growx, growy, w 0::, id tf"); //$NON-NLS-1$
		textField.addInputListener(new IInputListener() {

			@Override
			public void inputChanged() {
				inputChangedListener();
			}
		});

		this.validationCache = new ValidationCache(new IValidationResultCreator() {
			@Override
			public IValidationResult createValidationResult() {
				final IValidationResultBuilder builder = ValidationResult.builder();
				int index = 1;
				for (final String element : value) {
					boolean validated = false;
					if (converter.getStringValidator() != null) {
						final IValidationResult stringResult = converter.getStringValidator().validate(element).withContext(
								Toolkit.getMessageReplacer().replace(ELEMENT, String.valueOf(index)));
						if (!stringResult.isValid()) {
							validated = true;
							builder.addResult(stringResult);
						}
					}
					if (!validated) {
						final IValidationResult elementResult = setup.getElementValidator().validate(
								converter.convertToObject(element)).withContext(
								Toolkit.getMessageReplacer().replace(ELEMENT, String.valueOf(index)));
						builder.addResult(elementResult);
					}

					index++;
				}
				final Collection<ELEMENT_TYPE> currentValue = getValue();
				builder.addResult(compoundValidator.validate(currentValue));
				if (!dublicatesAllowed
					&& currentValue != null
					&& currentValue.size() != new HashSet<ELEMENT_TYPE>(currentValue).size()) {
					builder.addError(Messages.getString("CollectionInputFieldImpl.contains_dublicates"));
				}

				return builder.build();
			}
		});

		if (inputDialogSetup != null) {
			inputDialogBp = bpf.collectionInputDialog(inputDialogSetup.getCollectionInputControlSetup());
			inputDialogBp.setSetup(inputDialogSetup);
			inputDialogBp.setValidator(setup.getValidator());

			final IButtonBluePrint buttonBp = bpf.button();
			if (setup.getEditButtonIcon() != null) {
				buttonBp.setIcon(setup.getEditButtonIcon());
				final int width = composite.getPreferredSize().getHeight() + 2;
				this.editButton = composite.add(buttonBp, "grow, h ::" + width + ", w ::" + width); //$NON-NLS-1$ //$NON-NLS-2$
			}
			else {
				buttonBp.setText(EDIT);
				this.editButton = composite.add(buttonBp, "grow, h ::" + (composite.getPreferredSize().getHeight() + 2)); //$NON-NLS-1$
			}

			this.editButton.addActionListener(new IActionListener() {

				@Override
				public void actionPerformed() {
					openDialog();
				}
			});

			textField.addKeyListener(new KeyAdapter() {

				@Override
				public void keyPressed(final IKeyEvent event) {
					if (event.getModifier().contains(Modifier.ALT) && VirtualKey.ENTER.equals(event.getVirtualKey())) {
						openDialog();
					}
				}

			});

		}
		else {
			this.inputDialogBp = null;
			this.editButton = null;
		}

		if (setup.getValidator() != null) {
			compoundValidator.addValidator(setup.getValidator());
		}
	}

	@SuppressWarnings("unchecked")
	private void openDialog() {
		final Position buttonPos = Toolkit.toScreen(editButton.getPosition(), getWidget());
		inputDialogBp.setPosition(buttonPos);
		if (lastDialogSize != null) {
			inputDialogBp.setSize(lastDialogSize);
		}
		else {
			inputDialogBp.setSize(new Dimension(300, 270));
		}
		final IInputDialog<Collection<ELEMENT_TYPE>> dialog = Toolkit.getActiveWindow().createChildWindow(inputDialogBp);

		if (EmptyCheck.isEmpty(value)) {
			dialog.setValue((Collection<ELEMENT_TYPE>) Collections.singleton(null));
		}
		else {
			dialog.setValue(getValue());
		}
		dialog.setVisible(true);

		if (dialog.isOkPressed()) {
			lastDialogSize = dialog.getSize();
			setValue(dialog.getValue());
		}

		dialog.dispose();
	}

	@Override
	protected IComposite getWidget() {
		return (IComposite) super.getWidget();
	}

	@Override
	public void addValidator(final IValidator<Collection<ELEMENT_TYPE>> validator) {
		compoundValidator.addValidator(validator);
	}

	@Override
	public boolean hasModifications() {
		return textField.hasModifications();
	}

	@Override
	public void resetModificationState() {
		textField.resetModificationState();
	}

	@Override
	public void setValue(final Collection<ELEMENT_TYPE> value) {
		this.value.clear();
		if (EmptyCheck.isEmpty(value)) {
			textField.setValue(null);
		}
		else {
			final String maskingString = String.valueOf(maskingCharacter.charValue());
			final String separatorString = String.valueOf(separator.charValue());
			final StringBuilder valueString = new StringBuilder();
			for (final ELEMENT_TYPE element : value) {
				final String converted = converter.convertToString(element);
				this.value.add(converted);

				if (converted != null) {
					final String masked = converted.replace(maskingString, maskingString + maskingString);
					if (converted.contains(separatorString) || converted.startsWith(" ")) { //$NON-NLS-1$
						valueString.append(maskingString + masked + maskingString);
					}
					else {
						valueString.append(masked);
					}
				}
				valueString.append(separator + " "); //$NON-NLS-1$
			}
			valueString.replace(valueString.length() - 2, valueString.length(), ""); //$NON-NLS-1$
			textField.setValue(valueString.toString());
		}
	}

	@Override
	public Collection<ELEMENT_TYPE> getValue() {
		final List<ELEMENT_TYPE> result = new LinkedList<ELEMENT_TYPE>();
		for (final String element : value) {
			final ELEMENT_TYPE converted = converter.convertToObject(element);
			if (!filterEmptyValues || (!EmptyCheck.isEmpty(converted) && !EmptyCheck.isEmpty(element))) {
				result.add(converted);
			}
		}
		return result;
	}

	@Override
	public IValidationResult validate() {
		return validationCache.validate();
	}

	@Override
	public void addValidationConditionListener(final IValidationConditionListener listener) {
		validationCache.addValidationConditionListener(listener);
	}

	@Override
	public void removeValidationConditionListener(final IValidationConditionListener listener) {
		validationCache.removeValidationConditionListener(listener);
	}

	@Override
	public void setEditable(final boolean editable) {
		this.editable = editable;
		textField.setEditable(editable);
		if (editButton != null) {
			editButton.setEnabled(editable && textField.isEnabled());
		}
	}

	@Override
	public void setEnabled(final boolean enabled) {
		textField.setEnabled(enabled);
		if (editButton != null) {
			editButton.setEnabled(enabled && editable);
		}
	}

	@Override
	public void addInputListener(final IInputListener listener) {
		inputObservable.addInputListener(listener);
	}

	@Override
	public void removeInputListener(final IInputListener listener) {
		inputObservable.removeInputListener(listener);
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		textField.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		textField.setBackgroundColor(colorValue);
	}

	@Override
	public IColorConstant getForegroundColor() {
		return textField.getForegroundColor();
	}

	@Override
	public IColorConstant getBackgroundColor() {
		return textField.getBackgroundColor();
	}

	private void inputChangedListener() {
		final String maskingString = String.valueOf(maskingCharacter.charValue());
		final String separatorString = String.valueOf(separator.charValue());

		value.clear();
		final String input = textField.getValue();

		boolean isQuoted = false;
		int pos = 0;
		final StringBuilder currentElement = new StringBuilder();
		while (pos < input.length()) {
			if (equalsStringPart(input, maskingString, pos)) {
				pos = pos + maskingString.length();
				if (equalsStringPart(input, maskingString, pos)) {
					currentElement.append(maskingString);
					pos = pos + maskingString.length();
				}
				else {
					isQuoted = !isQuoted;
				}
			}
			else if (equalsStringPart(input, separatorString, pos)) {
				pos = pos + separatorString.length();
				if (isQuoted) {
					currentElement.append(separatorString);
				}
				else {
					value.add(currentElement.toString());
					currentElement.setLength(0);
					pos = skipSpaces(input, pos);
				}
			}
			else {
				currentElement.append(input.charAt(pos));
				pos++;
			}
		}
		if (input.length() > 0) {
			value.add(currentElement.toString());
		}

		inputObservable.fireInputChanged();
		validationCache.setDirty();
	}

	private static boolean equalsStringPart(final String text, final String search, final int pos) {
		if (pos + search.length() > text.length()) {
			return false;
		}

		int index = 0;
		while (pos + index < text.length() && index < search.length()) {
			if (text.charAt(pos + index) != search.charAt(index)) {
				return false;
			}
			index++;
		}

		return true;
	}

	private static int skipSpaces(final String text, final int pos) {
		int result = pos;
		while (result < text.length() && text.charAt(result) == ' ') {
			result++;
		}
		return result;
	}

}
