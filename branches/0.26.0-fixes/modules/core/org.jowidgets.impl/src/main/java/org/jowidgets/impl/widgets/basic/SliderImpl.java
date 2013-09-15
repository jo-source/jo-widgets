/*
 * Copyright (c) 2012, grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 *   names of its contributors may be used to endorse or promote products
 *   derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL jo-widgets.org BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */

package org.jowidgets.impl.widgets.basic;

import org.jowidgets.api.controller.IDisposeListener;
import org.jowidgets.api.controller.IParentListener;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.ISlider;
import org.jowidgets.api.widgets.descriptor.ISliderDescriptor;
import org.jowidgets.common.widgets.controller.IInputListener;
import org.jowidgets.impl.base.delegate.ControlDelegate;
import org.jowidgets.impl.widgets.basic.factory.internal.util.ColorSettingsInvoker;
import org.jowidgets.impl.widgets.basic.factory.internal.util.VisibiliySettingsInvoker;
import org.jowidgets.impl.widgets.common.wrapper.AbstractSliderSpiWrapper;
import org.jowidgets.spi.widgets.ISliderSpi;
import org.jowidgets.tools.controller.InputObservable;
import org.jowidgets.tools.validation.CompoundValidator;
import org.jowidgets.tools.validation.ValidationCache;
import org.jowidgets.tools.validation.ValidationCache.IValidationResultCreator;
import org.jowidgets.validation.IValidationConditionListener;
import org.jowidgets.validation.IValidationResult;
import org.jowidgets.validation.IValidator;

public final class SliderImpl extends AbstractSliderSpiWrapper implements ISlider {

	private final ControlDelegate controlDelegate;

	private final CompoundValidator<Integer> compoundValidator;
	private final ValidationCache validationCache;
	private final InputObservable inputObservable;

	private final int minimum;
	private final int maximum;

	private int lastUnmodifiedValue;

	private boolean editable;

	public SliderImpl(final ISliderSpi buttonSpi, final ISliderDescriptor descriptor) {
		super(buttonSpi);

		this.minimum = descriptor.getMinimum();
		this.maximum = descriptor.getMaximum();

		this.controlDelegate = new ControlDelegate(buttonSpi, this);

		VisibiliySettingsInvoker.setVisibility(descriptor, this);
		ColorSettingsInvoker.setColors(descriptor, this);

		this.inputObservable = new InputObservable();
		this.compoundValidator = new CompoundValidator<Integer>();

		this.validationCache = new ValidationCache(new IValidationResultCreator() {
			@Override
			public IValidationResult createValidationResult() {
				return compoundValidator.validate(getValue());
			}
		});

		buttonSpi.addInputListener(new IInputListener() {
			@Override
			public void inputChanged() {
				inputObservable.fireInputChanged();
				validationCache.setDirty();
			}
		});

		final Integer value = descriptor.getValue();
		if (value != null) {
			setSelection(value.intValue());
		}

		this.editable = true;

		resetModificationState();
	}

	@Override
	public IContainer getParent() {
		return controlDelegate.getParent();
	}

	@Override
	public void setParent(final IContainer parent) {
		controlDelegate.setParent(parent);
	}

	@Override
	public void addParentListener(final IParentListener<IContainer> listener) {
		controlDelegate.addParentListener(listener);
	}

	@Override
	public void removeParentListener(final IParentListener<IContainer> listener) {
		controlDelegate.removeParentListener(listener);
	}

	@Override
	public boolean isReparentable() {
		return controlDelegate.isReparentable();
	}

	@Override
	public void addDisposeListener(final IDisposeListener listener) {
		controlDelegate.addDisposeListener(listener);
	}

	@Override
	public void removeDisposeListener(final IDisposeListener listener) {
		controlDelegate.removeDisposeListener(listener);
	}

	@Override
	public boolean isDisposed() {
		return controlDelegate.isDisposed();
	}

	@Override
	public void dispose() {
		controlDelegate.dispose();
	}

	@Override
	public IPopupMenu createPopupMenu() {
		return controlDelegate.createPopupMenu();
	}

	@Override
	public void addValidator(final IValidator<Integer> validator) {
		compoundValidator.addValidator(validator);
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
	public boolean hasModifications() {
		return lastUnmodifiedValue != getSelection();
	}

	@Override
	public void resetModificationState() {
		lastUnmodifiedValue = getSelection();
	}

	@Override
	public int getMinimum() {
		return minimum;
	}

	@Override
	public int getMaximum() {
		return maximum;
	}

	@Override
	public void setValue(final Integer value) {
		if (value != null) {
			setSelection(value.intValue());
		}
		else {
			setSelection(getMinimum());
		}
	}

	@Override
	public Integer getValue() {
		return Integer.valueOf(getSelection());
	}

	@Override
	public void setEditable(final boolean editable) {
		this.editable = editable;
		getWidget().setEditable(editable);
	}

	@Override
	public boolean isEditable() {
		return editable;
	}

}
