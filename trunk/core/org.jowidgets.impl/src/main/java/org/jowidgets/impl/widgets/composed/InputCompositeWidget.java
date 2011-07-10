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

import java.util.List;

import org.jowidgets.api.layout.ILayoutFactory;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IComposedInputComponent;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.api.widgets.IInputComposite;
import org.jowidgets.api.widgets.IInputControl;
import org.jowidgets.api.widgets.IValidationResultLabel;
import org.jowidgets.api.widgets.blueprint.ICompositeBluePrint;
import org.jowidgets.api.widgets.blueprint.IScrollCompositeBluePrint;
import org.jowidgets.api.widgets.blueprint.IValidationResultLabelBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.api.widgets.content.IInputContentContainer;
import org.jowidgets.api.widgets.content.IInputContentCreator;
import org.jowidgets.api.widgets.descriptor.setup.IInputCompositeSetup;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.controler.IInputListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetCreator;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.common.widgets.layout.ILayouter;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.controler.InputObservable;
import org.jowidgets.tools.validation.CompoundValidator;
import org.jowidgets.tools.validation.ValidationCache;
import org.jowidgets.tools.validation.ValidationCache.IValidationResultCreator;
import org.jowidgets.tools.widgets.wrapper.ControlWrapper;
import org.jowidgets.validation.IValidateable;
import org.jowidgets.validation.IValidationConditionListener;
import org.jowidgets.validation.IValidationResult;
import org.jowidgets.validation.IValidator;
import org.jowidgets.validation.ValidationResult;

//TODO MG inputCompositeWidget must be implemented correctly
public class InputCompositeWidget<INPUT_TYPE> extends ControlWrapper implements
		IInputComposite<INPUT_TYPE>,
		IComposedInputComponent<INPUT_TYPE>,
		IInputContentContainer {

	private final IInputContentCreator<INPUT_TYPE> contentCreator;
	private final IComposite composite;
	private final IComposite innerComposite;
	private final IValidationResultLabel validationLabel;
	private final InputObservable inputObservable;
	private final CompoundValidator<INPUT_TYPE> compoundValidator;
	private final ValidationCache validationCache;
	private final boolean isAutoResetValidation;

	public InputCompositeWidget(final IComposite composite, final IInputCompositeSetup<INPUT_TYPE> setup) {
		super(composite);

		this.inputObservable = new InputObservable();
		this.contentCreator = setup.getContentCreator();
		this.isAutoResetValidation = setup.isAutoResetValidation();

		final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();
		this.composite = composite;

		if (setup.getValidationLabel() != null) {
			this.composite.setLayout(new MigLayoutDescriptor("0[grow]0", "0[][grow]0"));
			final IValidationResultLabelBluePrint validationLabelBp = bpf.validationResultLabel().setSetup(
					setup.getValidationLabel());
			validationLabel = this.composite.add(validationLabelBp, "h 18::, wrap");// TODO MG use hide instead
		}
		else {
			validationLabel = null;
			this.composite.setLayout(new MigLayoutDescriptor("0[grow]0", "0[grow]0"));
		}

		if (setup.isContentScrolled()) {
			final IScrollCompositeBluePrint scrollCompositeBluePrint = bpf.scrollComposite();
			scrollCompositeBluePrint.setBorder(setup.getContentBorder());
			innerComposite = composite.add(scrollCompositeBluePrint, "growx, growy, h 0::,w 0::, wrap");
		}
		else {
			final ICompositeBluePrint compositeBluePrint = bpf.composite();
			compositeBluePrint.setBorder(setup.getContentBorder());
			innerComposite = composite.add(compositeBluePrint, "growx, growy, h 0::,w 0::, wrap");
		}

		this.compoundValidator = new CompoundValidator<INPUT_TYPE>();
		if (setup.getValidator() != null) {
			compoundValidator.addValidator(setup.getValidator());
		}

		this.validationCache = new ValidationCache(new IValidationResultCreator() {
			@Override
			public IValidationResult createValidationResult() {
				// TODO implement create validation result
				return ValidationResult.ok();
			}
		});

		contentCreator.createContent(this);
		if (setup.getValue() != null) {
			contentCreator.setValue(setup.getValue());
		}
	}

	@Override
	public void setEditable(final boolean editable) {
		if (isAutoResetValidation && validationLabel != null) {
			validationLabel.setEnabled(editable);
		}
	}

	@Override
	public void addValidator(final IValidator<INPUT_TYPE> validator) {
		compoundValidator.addValidator(validator);
	}

	@Override
	public void resetValidation() {
		if (validationLabel != null) {
			validationLabel.setEmpty();
		}
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
	public void addInputListener(final IInputListener listener) {
		inputObservable.addInputListener(listener);
	}

	@Override
	public void removeInputListener(final IInputListener listener) {
		inputObservable.removeInputListener(listener);
	}

	@Override
	public void setValue(final INPUT_TYPE value) {
		contentCreator.getValue();
	}

	@Override
	public INPUT_TYPE getValue() {
		return contentCreator.getValue();
	}

	@Override
	public Object getIntermediateValue() {
		//TODO MG make correct implementation of getIntermediateValue()
		return getValue();
	}

	@Override
	public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		final Object layoutConstraints) {
		return innerComposite.add(descriptor, layoutConstraints);
	}

	@Override
	public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
		final ICustomWidgetCreator<WIDGET_TYPE> creator,
		final Object layoutConstraints) {
		return innerComposite.add(creator, layoutConstraints);
	}

	@Override
	public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor) {
		return innerComposite.add(descriptor);
	}

	@Override
	public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(final ICustomWidgetCreator<WIDGET_TYPE> creator) {
		return innerComposite.add(creator);
	}

	@Override
	public <WIDGET_TYPE extends IInputControl<?>> WIDGET_TYPE add(
		final String validationContext,
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		final Object layoutConstraints) {
		return add(descriptor, layoutConstraints);
	}

	@Override
	public <WIDGET_TYPE extends IInputControl<?>> WIDGET_TYPE add(
		final String validationContext,
		final ICustomWidgetCreator<WIDGET_TYPE> creator,
		final Object layoutConstraints) {
		return add(creator, layoutConstraints);
	}

	@Override
	public <WIDGET_TYPE extends IInputControl<?>> WIDGET_TYPE add(
		final String validationContext,
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor) {
		return add(descriptor);
	}

	@Override
	public <WIDGET_TYPE extends IInputControl<?>> WIDGET_TYPE add(
		final String validationContext,
		final ICustomWidgetCreator<WIDGET_TYPE> creator) {
		return add(creator);
	}

	@Override
	public boolean remove(final IControl control) {
		return innerComposite.remove(control);
	}

	@Override
	public void register(final String validationContext, final IValidateable validateable) {
		// TODO MG implement register
	}

	@Override
	public void unRegister(final String validationContext, final IValidateable validateable) {
		// TODO MG implement unRegister
	}

	@Override
	public void setLayout(final ILayoutDescriptor layoutDescriptor) {
		innerComposite.setLayout(layoutDescriptor);
	}

	@Override
	public void layoutBegin() {
		composite.layoutBegin();
	}

	@Override
	public void layoutEnd() {
		composite.layoutEnd();
	}

	@Override
	public void removeAll() {
		innerComposite.removeAll();
	}

	@Override
	public Rectangle getClientArea() {
		return innerComposite.getClientArea();
	}

	@Override
	public Dimension computeDecoratedSize(final Dimension clientAreaSize) {
		return composite.computeDecoratedSize(clientAreaSize);
	}

	@Override
	public <LAYOUT_TYPE extends ILayouter> LAYOUT_TYPE setLayout(final ILayoutFactory<LAYOUT_TYPE> layoutFactory) {
		return innerComposite.setLayout(layoutFactory);
	}

	@Override
	public List<IControl> getChildren() {
		return innerComposite.getChildren();
	}

}
