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
import java.util.List;

import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IInputControl;
import org.jowidgets.api.widgets.descriptor.ICombinedCollectionInputFieldDescriptor;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.widgets.controller.IInputListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.layout.MigLayoutFactory;
import org.jowidgets.tools.widgets.wrapper.ControlWrapper;
import org.jowidgets.validation.IValidationConditionListener;
import org.jowidgets.validation.IValidationResult;
import org.jowidgets.validation.IValidator;

public class CombinedCollectionInputFieldImpl<ELEMENT_TYPE> extends ControlWrapper implements
		IInputControl<Collection<ELEMENT_TYPE>> {

	private final IInputControl<ELEMENT_TYPE> elementControl;
	private final IInputControl<Collection<ELEMENT_TYPE>> collectionControl;
	private final IInputListener inputListener;

	@SuppressWarnings("unchecked")
	public CombinedCollectionInputFieldImpl(
		final IComposite composite,
		final ICombinedCollectionInputFieldDescriptor<ELEMENT_TYPE> setup) {
		super(composite);

		composite.setLayout(new MigLayoutDescriptor("hidemode 3", "0[grow, 0::]0", "0[grow, 0::]0"));

		this.elementControl = composite.add(setup.getElementTypeControlCreator(), MigLayoutFactory.GROWING_CELL_CONSTRAINTS);
		this.collectionControl = (IInputControl<Collection<ELEMENT_TYPE>>) composite.add(
				setup.getCollectionTypeControlCreator(),
				MigLayoutFactory.GROWING_CELL_CONSTRAINTS);

		collectionControl.setVisible(false);

		this.inputListener = new IInputListener() {
			@Override
			public void inputChanged() {
				if (elementControl.getValue() != null) {
					final List<ELEMENT_TYPE> singletonList = Collections.singletonList(elementControl.getValue());
					collectionControl.setValue(singletonList);
				}
				else {
					collectionControl.setValue(null);
				}
			}
		};

		elementControl.addInputListener(inputListener);
	}

	@Override
	protected IComposite getWidget() {
		return (IComposite) super.getWidget();
	}

	@Override
	public void addValidator(final IValidator<Collection<ELEMENT_TYPE>> validator) {
		collectionControl.addValidator(validator);
	}

	@Override
	public boolean hasModifications() {
		return collectionControl.hasModifications() || elementControl.hasModifications();
	}

	@Override
	public void resetModificationState() {
		collectionControl.resetModificationState();
		elementControl.resetModificationState();
	}

	@Override
	public void setValue(final Collection<ELEMENT_TYPE> value) {
		elementControl.removeInputListener(inputListener);

		collectionControl.setValue(value);
		if (value != null && value.size() == 1) {
			elementControl.setValue(value.iterator().next());
			checkVisibility(false);
		}
		if (value == null || value.size() == 0) {
			elementControl.setValue(null);
			checkVisibility(false);
		}
		else if (value != null && value.size() > 1) {
			checkVisibility(true);
		}

		elementControl.addInputListener(inputListener);
	}

	private void checkVisibility(final boolean collection) {
		if (collection && !collectionControl.isVisible()) {
			switchVisibility();
		}
		else if (!collection && collectionControl.isVisible()) {
			switchVisibility();
		}
	}

	private void switchVisibility() {
		getWidget().layoutBegin();
		collectionControl.setVisible(!collectionControl.isVisible());
		elementControl.setVisible(!elementControl.isVisible());
		getWidget().layoutEnd();
	}

	@Override
	public Collection<ELEMENT_TYPE> getValue() {
		return collectionControl.getValue();
	}

	@Override
	public IValidationResult validate() {
		return collectionControl.validate().withResult(elementControl.validate());
	}

	@Override
	public void addValidationConditionListener(final IValidationConditionListener listener) {
		collectionControl.addValidationConditionListener(listener);
		elementControl.addValidationConditionListener(listener);
	}

	@Override
	public void removeValidationConditionListener(final IValidationConditionListener listener) {
		collectionControl.removeValidationConditionListener(listener);
		elementControl.removeValidationConditionListener(listener);
	}

	@Override
	public void setEditable(final boolean editable) {
		collectionControl.setEditable(editable);
		elementControl.setEditable(editable);
	}

	@Override
	public void setEnabled(final boolean enabled) {
		collectionControl.setEnabled(enabled);
		elementControl.setEnabled(enabled);
	}

	@Override
	public void addInputListener(final IInputListener listener) {
		collectionControl.addInputListener(listener);
		elementControl.addInputListener(listener);
	}

	@Override
	public void removeInputListener(final IInputListener listener) {
		collectionControl.removeInputListener(listener);
		elementControl.removeInputListener(listener);
	}

	@Override
	public void setToolTipText(final String toolTip) {
		collectionControl.setToolTipText(toolTip);
		elementControl.setToolTipText(toolTip);
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		collectionControl.setForegroundColor(colorValue);
		elementControl.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		collectionControl.setBackgroundColor(colorValue);
		elementControl.setBackgroundColor(colorValue);
	}
}
