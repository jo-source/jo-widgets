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
import org.jowidgets.tools.widgets.wrapper.ControlWrapper;
import org.jowidgets.util.EmptyCheck;
import org.jowidgets.validation.IValidationConditionListener;
import org.jowidgets.validation.IValidationResult;
import org.jowidgets.validation.IValidator;
import org.jowidgets.validation.ValidationResult;

public class CollectionInputFieldImpl<ELEMENT_TYPE> extends ControlWrapper implements IInputControl<Collection<ELEMENT_TYPE>> {

	private final IInputField<String> textField;
	private final IButton editButton;
	private final IConverter<ELEMENT_TYPE> converter;
	private final Character separator;
	private final Character maskingCharacter;

	private Dimension lastDialogSize;

	private Collection<ELEMENT_TYPE> value;

	public CollectionInputFieldImpl(final IComposite composite, final ICollectionInputFieldDescriptor<ELEMENT_TYPE> setup) {
		super(composite);

		this.converter = setup.getConverter();
		this.separator = setup.getSeparator();
		this.maskingCharacter = setup.getMaskingCharacter();

		final ICollectionInputDialogSetup<ELEMENT_TYPE> inputDialogSetup = setup.getCollectionInputDialogSetup();

		if (inputDialogSetup != null) {
			composite.setLayout(new MigLayoutDescriptor("0[grow, 0::]2[]0", "0[grow]0"));
		}
		else {
			composite.setLayout(new MigLayoutDescriptor("0[grow, 0::]0", "0[grow]0"));
		}

		final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();
		this.textField = composite.add(bpf.inputFieldString(), "growx, growy, w 0::, id tf");

		if (inputDialogSetup != null) {

			final ICollectionInputDialogBluePrint<ELEMENT_TYPE> inputDialogBp = bpf.collectionInputDialog(inputDialogSetup.getCollectionInputControlSetup());
			inputDialogBp.setSetup(inputDialogSetup);

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
						dialog.setValue(value);
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
	}

	@Override
	protected IComposite getWidget() {
		return (IComposite) super.getWidget();
	}

	@Override
	public void addValidator(final IValidator<Collection<ELEMENT_TYPE>> validator) {

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
		this.value = value;
		if (EmptyCheck.isEmpty(value)) {
			textField.setValue(null);
		}
		else {
			final String maskingString = String.valueOf(maskingCharacter.charValue());
			final String separatorString = String.valueOf(separator.charValue());
			final StringBuilder valueString = new StringBuilder();
			for (final ELEMENT_TYPE element : value) {
				final String converted = converter.convertToString(element);
				if (converted.contains(separatorString)) {
					valueString.append(maskingString + converted + maskingString);
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
		return value;
	}

	@Override
	public IValidationResult validate() {
		return ValidationResult.ok();
	}

	@Override
	public void addValidationConditionListener(final IValidationConditionListener listener) {

	}

	@Override
	public void removeValidationConditionListener(final IValidationConditionListener listener) {

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
		textField.addInputListener(listener);
	}

	@Override
	public void removeInputListener(final IInputListener listener) {
		textField.removeInputListener(listener);
	}

}
