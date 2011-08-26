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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jowidgets.api.layout.ILayoutFactory;
import org.jowidgets.api.toolkit.Toolkit;
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
import org.jowidgets.common.widgets.controller.IInputListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetCreator;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.common.widgets.layout.ILayouter;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.controller.InputObservable;
import org.jowidgets.tools.validation.CompoundValidator;
import org.jowidgets.tools.validation.ValidationCache;
import org.jowidgets.tools.validation.ValidationCache.IValidationResultCreator;
import org.jowidgets.tools.widgets.wrapper.ControlWrapper;
import org.jowidgets.util.Tuple;
import org.jowidgets.validation.IValidateable;
import org.jowidgets.validation.IValidationConditionListener;
import org.jowidgets.validation.IValidationResult;
import org.jowidgets.validation.IValidationResultBuilder;
import org.jowidgets.validation.IValidator;
import org.jowidgets.validation.ValidationResult;

public class InputCompositeImpl<INPUT_TYPE> extends ControlWrapper implements IInputComposite<INPUT_TYPE>, IInputContentContainer {

	private final List<Tuple<String, IInputControl<?>>> inputControls;
	private final List<Tuple<String, IValidateable>> validatables;
	private final Set<IInputControl<?>> editedControls;

	private final IInputContentCreator<INPUT_TYPE> contentCreator;
	private final IComposite composite;
	private final IComposite innerComposite;
	private final IValidationResultLabel validationLabel;
	private final InputObservable inputObservable;
	private final CompoundValidator<INPUT_TYPE> compoundValidator;
	private final ValidationCache validationCache;
	private final String missingInputHint;

	public InputCompositeImpl(final IComposite composite, final IInputCompositeSetup<INPUT_TYPE> setup) {
		super(composite);

		this.inputControls = new LinkedList<Tuple<String, IInputControl<?>>>();
		this.validatables = new LinkedList<Tuple<String, IValidateable>>();
		this.editedControls = new HashSet<IInputControl<?>>();

		this.inputObservable = new InputObservable();
		this.contentCreator = setup.getContentCreator();

		final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();
		this.composite = composite;
		this.missingInputHint = setup.getMissingInputHint();

		if (setup.getValidationLabel() != null) {
			this.composite.setLayout(new MigLayoutDescriptor("0[grow]0", "0[][grow]0"));
			final IValidationResultLabelBluePrint validationLabelBp = bpf.validationResultLabel().setSetup(
					setup.getValidationLabel());
			validationLabel = this.composite.add(validationLabelBp, "h 18::, growx, wrap");
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

		this.validationCache = new ValidationCache(new ValidationResultCreator());

		contentCreator.createContent(this);
		contentCreator.setValue(setup.getValue());

		if (validationLabel != null) {
			this.validationCache.addValidationConditionListener(new IValidationConditionListener() {
				@Override
				public void validationConditionsChanged() {
					validationLabel.setResult(validate());
				}
			});
		}

		resetModificationState();
		validationCache.setDirty();
	}

	@Override
	public void setEditable(final boolean editable) {
		for (final Tuple<String, IInputControl<?>> tuple : inputControls) {
			tuple.getSecond().setEditable(editable);
		}
	}

	@Override
	public void addValidator(final IValidator<INPUT_TYPE> validator) {
		compoundValidator.addValidator(validator);
	}

	@Override
	public boolean hasModifications() {
		for (final Tuple<String, IInputControl<?>> tuple : inputControls) {
			if (tuple.getSecond().hasModifications()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void resetModificationState() {
		for (final Tuple<String, IInputControl<?>> tuple : inputControls) {
			tuple.getSecond().resetModificationState();
		}
		editedControls.clear();
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
		contentCreator.setValue(value);
		resetModificationState();
		validationCache.setDirty();
	}

	@Override
	public INPUT_TYPE getValue() {
		return contentCreator.getValue();
	}

	@Override
	public void setTabOrder(final List<? extends IControl> tabOrder) {
		innerComposite.setTabOrder(tabOrder);
	}

	@Override
	public void register(final String validationContext, final IValidateable validateable) {
		this.validatables.add(new Tuple<String, IValidateable>(validationContext, validateable));
	}

	@Override
	public void unRegister(final String validationContext, final IValidateable validateable) {
		this.validatables.remove(new Tuple<String, IValidateable>(validationContext, validateable));
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

	@Override
	public boolean remove(final IControl control) {
		removeControlFromInputControls(control);
		return innerComposite.remove(control);
	}

	private void removeControlFromInputControls(final IControl control) {
		int index = 0;
		for (final Tuple<String, IInputControl<?>> tuple : new LinkedList<Tuple<String, IInputControl<?>>>(inputControls)) {
			if (tuple.getSecond() == control) {
				inputControls.remove(index);
				editedControls.remove(control);
				return;
			}
			index++;
		}
	}

	@Override
	public void removeAll() {
		inputControls.clear();
		innerComposite.removeAll();
	}

	@Override
	public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
		final int index,
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		final Object layoutConstraints) {

		final WIDGET_TYPE result = innerComposite.add(index, descriptor, layoutConstraints);
		afterAdd(null, result);
		return result;
	}

	@Override
	public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
		final int index,
		final ICustomWidgetCreator<WIDGET_TYPE> creator,
		final Object layoutConstraints) {

		final WIDGET_TYPE result = innerComposite.add(index, creator, layoutConstraints);
		afterAdd(null, result);
		return result;
	}

	@Override
	public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		final Object layoutConstraints) {

		final WIDGET_TYPE result = innerComposite.add(descriptor, layoutConstraints);
		afterAdd(null, result);
		return result;
	}

	@Override
	public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
		final ICustomWidgetCreator<WIDGET_TYPE> creator,
		final Object layoutConstraints) {

		final WIDGET_TYPE result = innerComposite.add(creator, layoutConstraints);
		afterAdd(null, result);
		return result;
	}

	@Override
	public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor) {
		final WIDGET_TYPE result = innerComposite.add(descriptor);
		afterAdd(null, result);
		return result;
	}

	@Override
	public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(final ICustomWidgetCreator<WIDGET_TYPE> creator) {

		final WIDGET_TYPE result = innerComposite.add(creator);
		afterAdd(null, result);
		return result;
	}

	@Override
	public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
		final String validationContext,
		final int index,
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		final Object layoutConstraints) {

		final WIDGET_TYPE result = innerComposite.add(index, descriptor, layoutConstraints);
		afterAdd(validationContext, result);
		return result;
	}

	@Override
	public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
		final String validationContext,
		final int index,
		final ICustomWidgetCreator<WIDGET_TYPE> creator,
		final Object layoutConstraints) {

		final WIDGET_TYPE result = innerComposite.add(index, creator, layoutConstraints);
		afterAdd(validationContext, result);
		return result;
	}

	@Override
	public <WIDGET_TYPE extends IInputControl<?>> WIDGET_TYPE add(
		final String validationContext,
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		final Object layoutConstraints) {

		final WIDGET_TYPE result = innerComposite.add(descriptor, layoutConstraints);
		afterAdd(validationContext, result);
		return result;
	}

	@Override
	public <WIDGET_TYPE extends IInputControl<?>> WIDGET_TYPE add(
		final String validationContext,
		final ICustomWidgetCreator<WIDGET_TYPE> creator,
		final Object layoutConstraints) {

		final WIDGET_TYPE result = innerComposite.add(creator, layoutConstraints);
		afterAdd(validationContext, result);
		return result;
	}

	@Override
	public <WIDGET_TYPE extends IInputControl<?>> WIDGET_TYPE add(
		final String validationContext,
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor) {

		final WIDGET_TYPE result = innerComposite.add(descriptor);
		afterAdd(validationContext, result);
		return result;
	}

	@Override
	public <WIDGET_TYPE extends IInputControl<?>> WIDGET_TYPE add(
		final String validationContext,
		final ICustomWidgetCreator<WIDGET_TYPE> creator) {

		final WIDGET_TYPE result = innerComposite.add(creator);
		afterAdd(validationContext, result);
		return result;
	}

	private void afterAdd(final String context, final IControl control) {
		if (control instanceof IInputControl) {
			final IInputControl<?> inputControl = (IInputControl<?>) control;

			inputControls.add(new Tuple<String, IInputControl<?>>(context, inputControl));

			inputControl.addValidationConditionListener(new IValidationConditionListener() {
				@Override
				public void validationConditionsChanged() {
					validationCache.setDirty();
				}
			});

			inputControl.addInputListener(new IInputListener() {
				@Override
				public void inputChanged() {
					editedControls.add(inputControl);
					inputObservable.fireInputChanged();
				}
			});
		}
	}

	private class ValidationResultCreator implements IValidationResultCreator {

		@Override
		public IValidationResult createValidationResult() {
			boolean hintAdded = false;

			final IValidationResultBuilder builder = ValidationResult.builder();

			for (final Tuple<String, IInputControl<?>> tuple : inputControls) {
				final IValidationResult result = validate(tuple);
				final IInputControl<?> control = tuple.getSecond();
				if (control.hasModifications() || editedControls.contains(control) || missingInputHint == null) {
					builder.addResult(validate(tuple));
				}
				else if (!hintAdded && !result.isValid()) {
					builder.addInfoError(missingInputHint);
					hintAdded = true;
				}
			}

			for (final Tuple<String, IValidateable> tuple : validatables) {
				builder.addResult(validate(tuple));
			}

			builder.addResult(compoundValidator.validate(getValue()));

			return builder.build();
		}

		private IValidationResult validate(final Tuple<String, ? extends IValidateable> tuple) {
			final String context = tuple.getFirst();
			final IValidateable inputControl = tuple.getSecond();
			return inputControl.validate().withContext(context);
		}
	}

}
