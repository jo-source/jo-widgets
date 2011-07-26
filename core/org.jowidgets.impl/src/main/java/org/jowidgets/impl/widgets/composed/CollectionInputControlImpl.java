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

import org.jowidgets.api.image.IconsSmall;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IInputControl;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.api.widgets.descriptor.ICollectionInputControlDescriptor;
import org.jowidgets.common.widgets.controler.IInputListener;
import org.jowidgets.common.widgets.factory.ICustomWidgetCreator;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.layout.MigLayoutFactory;
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
		final ICustomWidgetCreator<IInputControl<INPUT_TYPE>> widgetCreator = setup.getElementWidgetCreator();

		composite.setLayout(MigLayoutFactory.growingInnerCellLayout());
		final IComposite innerComposite = composite.add(bpf.composite(), MigLayoutFactory.GROWING_CELL_CONSTRAINTS);

		innerComposite.setLayout(new MigLayoutDescriptor("wrap", "0[][]0[grow, 0::][20!]0", "0[]0[]0[]0[]0[]0[]0[]0[]0"));

		for (int i = 0; i < 8; i++) {

			final String userIndex = "" + (i + 1);

			innerComposite.add(bpf.textLabel(userIndex));

			innerComposite.add(bpf.button().setIcon(IconsSmall.SUB).setToolTipText("Remove entry " + userIndex), "w 21!, h 21!");

			innerComposite.add(widgetCreator, "grow, w 0::");

			if (i == 3) {
				innerComposite.add(bpf.validationResultLabel().setShowValidationMessage(false)).setResult(
						ValidationResult.create().withError("Must be a propper value"));
			}
			else if (i == 6) {
				innerComposite.add(bpf.validationResultLabel().setShowValidationMessage(false)).setResult(
						ValidationResult.create().withWarning("Seems unusual"));
			}
			else {
				innerComposite.add(bpf.composite(), "w 0!, h 0!");
			}

		}

		innerComposite.add(bpf.composite(), "w 0!, h 0!");
		//innerComposite.add(bpf.textLabel("" + 9).setForegroundColor(Colors.DISABLED));

		innerComposite.add(bpf.button().setIcon(IconsSmall.ADD).setToolTipText("Add new entry"), "w 21!, h 21!");
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
