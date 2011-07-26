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
package org.jowidgets.impl.widgets.composed;

import java.util.Collection;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IInputControl;
import org.jowidgets.api.widgets.blueprint.IButtonBluePrint;
import org.jowidgets.api.widgets.blueprint.IInputComponentValidationLabelBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.api.widgets.descriptor.ICollectionInputControlDescriptor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.controler.IInputListener;
import org.jowidgets.common.widgets.factory.ICustomWidgetCreator;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.widgets.wrapper.ControlWrapper;
import org.jowidgets.validation.IValidationConditionListener;
import org.jowidgets.validation.IValidationResult;
import org.jowidgets.validation.IValidator;
import org.jowidgets.validation.ValidationResult;

//TODO NM implement CollectionInputControlImpl
public class CollectionInputControlImpl<INPUT_TYPE> extends ControlWrapper implements IInputControl<Collection<INPUT_TYPE>> {

	public CollectionInputControlImpl(final IComposite composite, final ICollectionInputControlDescriptor<INPUT_TYPE> setup) {
		super(composite);
		final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();

		//Gets some settings from setup
		final ICustomWidgetCreator<IInputControl<INPUT_TYPE>> widgetCreator = setup.getElementWidgetCreator();
		final IButtonBluePrint removeButtonBp = bpf.button().setSetup(setup.getRemoveButton());
		final IButtonBluePrint addButtonBp = bpf.button().setSetup(setup.getAddButton());
		final String addButtonConstraints = getCellConstraintsFromDimension(setup.getAddButtonSize());
		final String removeButtonConstraints = getCellConstraintsFromDimension(setup.getRemoveButtonSize());

		//TODO NM proper handling of the non mandatory setup params 
		//(validation label and constraints may be null so do not render them)
		final IInputComponentValidationLabelBluePrint validationLabelBp = bpf.inputComponentValidationLabel();
		validationLabelBp.setSetup(setup.getValidationLabel());
		final String validationLabelConstraints = getCellConstraintsFromDimension(setup.getValidationLabelSize());

		//TODO NM re-implement this example code 
		composite.setLayout(new MigLayoutDescriptor("wrap", "0[][]0[grow, 0::][20!]0", "0[]0[]0[]0[]0[]0[]0[]0[]0"));

		for (int i = 0; i < 8; i++) {

			final String userIndex = "" + (i + 1);

			composite.add(bpf.textLabel(userIndex));

			composite.add(removeButtonBp.setToolTipText("Remove entry " + userIndex), removeButtonConstraints);

			final IInputControl<INPUT_TYPE> inputControl = composite.add(widgetCreator, "grow, w 0::");

			//START BLOCK -> remove this block later except commented line below 
			if (i != 3 && i != 6) {
				//later only this code line is necessary , the rest is this block is for tst purpose
				composite.add(validationLabelBp.setInputComponent(inputControl));
			}
			if (i == 3) {
				composite.add(bpf.validationResultLabel().setShowValidationMessage(false), validationLabelConstraints).setResult(
						ValidationResult.create().withError("Must be a propper value"));
			}
			else if (i == 6) {
				composite.add(bpf.validationResultLabel().setShowValidationMessage(false), validationLabelConstraints).setResult(
						ValidationResult.create().withWarning("Seems unusual"));
			}
			//END BLOCK -> remove this block later except commented line above

		}

		composite.add(bpf.composite(), "w 0!, h 0!");

		composite.add(addButtonBp, addButtonConstraints);
	}

	private String getCellConstraintsFromDimension(final Dimension dimension) {
		if (dimension == null) {
			return "";
		}
		else {
			return "w " + dimension.getWidth() + "!, h " + dimension.getHeight() + "!";
		}
	}

	@Override
	public void setEditable(final boolean editable) {

	}

	@Override
	public void addValidator(final IValidator<Collection<INPUT_TYPE>> validator) {

	}

	@Override
	public boolean hasModifications() {
		return false;
	}

	@Override
	public void resetModificationState() {}

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
	public void addInputListener(final IInputListener listener) {

	}

	@Override
	public void removeInputListener(final IInputListener listener) {

	}

	@Override
	public void setValue(final Collection<INPUT_TYPE> value) {

	}

	@Override
	public Collection<INPUT_TYPE> getValue() {
		return null;
	}

}
