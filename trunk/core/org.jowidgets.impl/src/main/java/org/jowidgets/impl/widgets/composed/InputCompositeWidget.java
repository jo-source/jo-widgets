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

import org.jowidgets.api.color.IColorConstant;
import org.jowidgets.api.validation.IValidator;
import org.jowidgets.api.validation.ValidationResult;
import org.jowidgets.api.widgets.ICompositeWidget;
import org.jowidgets.api.widgets.IInputCompositeWidget;
import org.jowidgets.api.widgets.IValidationLabelWidget;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.api.widgets.blueprint.ICompositeBluePrint;
import org.jowidgets.api.widgets.controler.IInputListener;
import org.jowidgets.api.widgets.descriptor.setup.IInputCompositeSetup;
import org.jowidgets.api.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.impl.widgets.composed.blueprint.BluePrintFactory;
import org.jowidgets.impl.widgets.composed.internal.InputContentContainer;

public class InputCompositeWidget<INPUT_TYPE> implements IInputCompositeWidget<INPUT_TYPE> {

	private final ICompositeWidget parentComposite;
	private final ICompositeWidget composite;
	private final InputContentContainer<INPUT_TYPE> contentContainer;

	public InputCompositeWidget(final ICompositeWidget composite, final IInputCompositeSetup<INPUT_TYPE> setup) {
		super();

		this.parentComposite = composite;
		this.parentComposite.setLayout(new MigLayoutDescriptor("0[grow]0", "0[grow]0"));

		final ICompositeBluePrint compositeBp = new BluePrintFactory().composite().setBorder(setup.getBorder());

		this.composite = parentComposite.add(compositeBp, "growx, growy, h 0::, w 0::");

		IValidationLabelWidget validationLabel = null;
		if (setup.getValidationLabel() != null) {
			this.composite.setLayout(new MigLayoutDescriptor("0[grow]0", "0[][grow][]0"));
			validationLabel = this.composite.add(setup.getValidationLabel(), "h 18::, wrap");// TODO use hide instead
		}
		else {
			this.composite.setLayout(new MigLayoutDescriptor("0[grow]0", "0[grow][]0"));
		}

		this.contentContainer = new InputContentContainer<INPUT_TYPE>(
			this.composite,
			setup.getContentCreator(),
			setup.isContentScrolled(),
			setup.getContentBorder());

		if (validationLabel != null) {
			validationLabel.registerInputWidget(contentContainer);
		}

	}

	@Override
	public void setEditable(final boolean editable) {
		contentContainer.setEditable(editable);
	}

	@Override
	public boolean isMandatory() {
		return contentContainer.isMandatory();
	}

	@Override
	public void setMandatory(final boolean mandatory) {
		contentContainer.setMandatory(mandatory);
	}

	@Override
	public void addValidator(final IValidator<INPUT_TYPE> validator) {
		contentContainer.addValidator(validator);
	}

	@Override
	public boolean hasInput() {
		return contentContainer.hasInput();
	}

	@Override
	public IWidget getParent() {
		return parentComposite.getParent();
	}

	@Override
	public Object getUiReference() {
		return parentComposite.getUiReference();
	}

	@Override
	public void redraw() {
		contentContainer.redraw();
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		composite.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		composite.setBackgroundColor(colorValue);
	}

	@Override
	public void setValue(final INPUT_TYPE value) {
		contentContainer.setValue(value);
	}

	@Override
	public INPUT_TYPE getValue() {
		return contentContainer.getValue();
	}

	@Override
	public ValidationResult validate() {
		return contentContainer.validate();
	}

	@Override
	public void addInputListener(final IInputListener listener) {
		contentContainer.addInputListener(listener);
	}

	@Override
	public void removeInputListener(final IInputListener listener) {
		contentContainer.removeInputListener(listener);
	}

	@Override
	public boolean hasAllMandatoryInput() {
		return contentContainer.hasAllMandatoryInput();
	}

}
