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
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.common.widgets.controller.IInputListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.controller.InputObservable;
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

	private final IInputField<String> textField;
	private final IButton editButton;
	private final IConverter<ELEMENT_TYPE> converter;
	private final Character separator;
	private final Character maskingCharacter;
	private final ValidationCache validationCache;
	private final CompoundValidator<Collection<ELEMENT_TYPE>> compoundValidator;
	private final InputObservable inputObservable;

	private Dimension lastDialogSize;

	private final Collection<String> value;

	public CollectionInputFieldImpl(final IComposite composite, final ICollectionInputFieldDescriptor<ELEMENT_TYPE> setup) {
		super(composite);

		this.converter = setup.getConverter();
		this.separator = setup.getSeparator();
		this.maskingCharacter = setup.getMaskingCharacter();

		final ICollectionInputDialogSetup<ELEMENT_TYPE> inputDialogSetup = setup.getCollectionInputDialogSetup();

		this.value = new LinkedList<String>();
		this.inputObservable = new InputObservable();
		this.compoundValidator = new CompoundValidator<Collection<ELEMENT_TYPE>>();

		if (inputDialogSetup != null) {
			composite.setLayout(new MigLayoutDescriptor("0[grow, 0::]2[]0", "0[grow]0"));
		}
		else {
			composite.setLayout(new MigLayoutDescriptor("0[grow, 0::]0", "0[grow]0"));
		}

		final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();
		this.textField = composite.add(bpf.inputFieldString(), "growx, growy, w 0::, id tf");
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
				if (converter.getStringValidator() != null) {
					int index = 1;
					for (final String element : value) {
						setup.getCollectionInputDialogSetup().getCollectionInputControlSetup().getElementWidgetCreator();
						final IValidationResult controlResult = converter.getStringValidator().validate(element).withContext(
								"Element " + index);
						builder.addResult(controlResult);
						index++;
					}
				}
				builder.addResult(compoundValidator.validate(getValue()));

				return builder.build();
			}
		});

		if (inputDialogSetup != null) {

			final ICollectionInputDialogBluePrint<ELEMENT_TYPE> inputDialogBp = bpf.collectionInputDialog(inputDialogSetup.getCollectionInputControlSetup());
			inputDialogBp.setSetup(inputDialogSetup);
			inputDialogBp.setValidator(setup.getValidator());

			final IButtonBluePrint buttonBp = bpf.button();
			if (setup.getEditButtonIcon() != null) {
				buttonBp.setIcon(setup.getEditButtonIcon());
				final int width = composite.getPreferredSize().getHeight() + 2;
				this.editButton = composite.add(buttonBp, "grow, h ::" + width + ", w ::" + width);
			}
			else {
				buttonBp.setText("Edit");
				this.editButton = composite.add(buttonBp, "grow, h ::" + (composite.getPreferredSize().getHeight() + 2));
			}

			this.editButton.addActionListener(new IActionListener() {

				@SuppressWarnings("unchecked")
				@Override
				public void actionPerformed() {
					final Position buttonPos = Toolkit.toScreen(editButton.getPosition(), composite);
					inputDialogBp.setPosition(buttonPos);
					if (lastDialogSize != null) {
						inputDialogBp.setSize(lastDialogSize);
					}
					else {
						inputDialogBp.setSize(new Dimension(300, 270));
					}
					final IInputDialog<Collection<ELEMENT_TYPE>> dialog = Toolkit.getActiveWindow().createChildWindow(
							inputDialogBp);

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
			});
		}
		else {
			this.editButton = null;
		}

		if (setup.getValidator() != null) {
			compoundValidator.addValidator(setup.getValidator());
		}
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
				if (converted.contains(separatorString)) {
					valueString.append(maskingString
						+ converted.replace(maskingString, maskingString + maskingString)
						+ maskingString);
				}
				else {
					valueString.append(converted);
				}
				valueString.append(separator + " ");
			}
			valueString.replace(valueString.length() - 2, valueString.length(), "");
			textField.setValue(valueString.toString());
		}
	}

	@Override
	public Collection<ELEMENT_TYPE> getValue() {
		final List<ELEMENT_TYPE> result = new LinkedList<ELEMENT_TYPE>();
		for (final String element : value) {
			result.add(converter.convertToObject(element));
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
		textField.setEditable(editable);
		if (editButton != null) {
			editButton.setEnabled(editable);
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

	private void inputChangedListener() {
		final String maskingString = String.valueOf(maskingCharacter.charValue());
		final String separatorString = String.valueOf(separator.charValue());

		value.clear();
		final String input = textField.getValue();

		int pos = 0;
		while (true) {
			pos = skipSpaces(input, pos);
			if (pos >= input.length()) {
				break;
			}

			final boolean startsWithMask = equalsStringPart(input, maskingString, pos);
			if (startsWithMask) {
				pos = pos + maskingString.length();
			}

			final boolean isQuoted = startsWithMask && !equalsStringPart(input, maskingString, pos);

			int currentEndPos = pos;
			if (isQuoted) {
				while (currentEndPos > 0) {
					currentEndPos = input.indexOf(maskingString, currentEndPos);
					if (currentEndPos < 0) {
						break;
					}
					if (equalsStringPart(input, maskingString + maskingString, currentEndPos)) {
						currentEndPos = currentEndPos + 2 * maskingString.length();
					}
					else {
						break;
					}
				}
			}
			else {
				currentEndPos = input.indexOf(separatorString, pos);
			}

			if (currentEndPos < 0) {
				currentEndPos = input.length();
			}
			final String element = input.substring(pos, currentEndPos).replace(maskingString + maskingString, maskingString);
			if (!"".equals(element)) {
				value.add(element);
			}

			pos = skipSpaces(input, currentEndPos);
			if (isQuoted && equalsStringPart(input, maskingString, pos)) {
				pos = pos + maskingString.length();
				pos = skipSpaces(input, pos);
			}

			if (equalsStringPart(input, separatorString, pos)) {
				pos = pos + separatorString.length();
			}
			pos = skipSpaces(input, pos);
		}

		validationCache.setDirty();
		inputObservable.fireInputChanged();
	}

	private static boolean equalsStringPart(final String text, final String search, final int pos) {
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
