/*
 * Copyright (c) 2010, grossmann
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jowidgets.api.controller.IDisposeListener;
import org.jowidgets.api.controller.IParentListener;
import org.jowidgets.api.convert.IObjectStringConverter;
import org.jowidgets.api.types.AutoSelectionPolicy;
import org.jowidgets.api.widgets.IComboBox;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.descriptor.setup.IComboBoxSelectionSetup;
import org.jowidgets.common.widgets.controller.IInputListener;
import org.jowidgets.impl.base.delegate.ControlDelegate;
import org.jowidgets.impl.widgets.basic.factory.internal.util.ColorSettingsInvoker;
import org.jowidgets.impl.widgets.basic.factory.internal.util.VisibiliySettingsInvoker;
import org.jowidgets.impl.widgets.common.wrapper.AbstractControlSpiWrapper;
import org.jowidgets.spi.widgets.IComboBoxSelectionSpi;
import org.jowidgets.tools.controller.InputObservable;
import org.jowidgets.tools.validation.CompoundValidator;
import org.jowidgets.tools.validation.ValidationCache;
import org.jowidgets.tools.validation.ValidationCache.IValidationResultCreator;
import org.jowidgets.util.Assert;
import org.jowidgets.util.EmptyCheck;
import org.jowidgets.util.EmptyCompatibleEquivalence;
import org.jowidgets.util.NullCompatibleEquivalence;
import org.jowidgets.validation.IValidationConditionListener;
import org.jowidgets.validation.IValidationResult;
import org.jowidgets.validation.IValidator;

public class ComboBoxSelectionImpl<VALUE_TYPE> extends AbstractControlSpiWrapper implements IComboBox<VALUE_TYPE> {

	private final List<VALUE_TYPE> elements;
	private final List<VALUE_TYPE> elementsView;
	private final boolean lenient;

	private final IComboBoxSelectionSpi comboBoxSelectionWidgetSpi;
	private final IObjectStringConverter<VALUE_TYPE> objectStringConverter;
	private final AutoSelectionPolicy autoSelectionPolicy;
	private final ValidationCache validationCache;
	private final ControlDelegate controlDelegate;
	private final CompoundValidator<VALUE_TYPE> compoundValidator;
	private final InputObservable inputObservable;
	private final IInputListener inputListener;

	private VALUE_TYPE lastUnmodifiedValue;
	private VALUE_TYPE lenientValue;
	private boolean editable;

	public ComboBoxSelectionImpl(
		final IComboBoxSelectionSpi comboBoxSelectionWidgetSpi,
		final IComboBoxSelectionSetup<VALUE_TYPE> setup) {
		super(comboBoxSelectionWidgetSpi);

		this.lenient = setup.isLenient();
		this.inputObservable = new InputObservable();
		this.comboBoxSelectionWidgetSpi = comboBoxSelectionWidgetSpi;
		this.objectStringConverter = setup.getObjectStringConverter();
		this.autoSelectionPolicy = setup.getAutoSelectionPolicy();
		this.elements = new ArrayList<VALUE_TYPE>();
		this.elementsView = Collections.unmodifiableList(this.elements);

		VisibiliySettingsInvoker.setVisibility(setup, this);
		ColorSettingsInvoker.setColors(setup, this);

		this.controlDelegate = new ControlDelegate(comboBoxSelectionWidgetSpi, this);
		this.compoundValidator = new CompoundValidator<VALUE_TYPE>();

		final IValidator<VALUE_TYPE> validator = setup.getValidator();
		if (validator != null) {
			compoundValidator.addValidator(validator);
		}

		this.validationCache = new ValidationCache(new IValidationResultCreator() {
			@Override
			public IValidationResult createValidationResult() {
				return compoundValidator.validate(getValue());
			}
		});

		this.inputListener = new IInputListener() {
			@Override
			public void inputChanged() {
				onInputChanged(true);
			}
		};

		setElements(setup.getElements());
		if (setup.getValue() != null) {
			setValue(setup.getValue());
		}

		this.editable = setup.isEditable();
		if (!setup.isEditable()) {
			setEditable(false);
		}

		getWidget().addInputListener(inputListener);
		resetModificationState();
	}

	private void onInputChanged(final boolean removeLenient) {
		if (removeLenient) {
			removeLinientValue();
		}
		getWidget().setToolTipText(objectStringConverter.getDescription(getValue()));
		inputObservable.fireInputChanged();
		validationCache.setDirty();
	}

	@Override
	public IComboBoxSelectionSpi getWidget() {
		return (IComboBoxSelectionSpi) super.getWidget();
	}

	@Override
	public boolean hasModifications() {
		return !EmptyCompatibleEquivalence.equals(lastUnmodifiedValue, getValue());
	}

	@Override
	public void resetModificationState() {
		this.lastUnmodifiedValue = getValue();
	}

	@Override
	public void setValue(final VALUE_TYPE value) {
		removeLinientValue();
		if (value == null) {
			comboBoxSelectionWidgetSpi.setSelectedIndex(-1);
			comboBoxSelectionWidgetSpi.setToolTipText(null);
		}
		else {
			final int indexOfContent = elements.indexOf(value);
			if (indexOfContent >= 0 && indexOfContent < elements.size()) {
				comboBoxSelectionWidgetSpi.setSelectedIndex(indexOfContent);
			}
			else if (lenient) {
				addLenientValue(value);
			}
			else {
				throw new IllegalArgumentException("Value '" + value + "' is not a element of this combo box");
			}
			comboBoxSelectionWidgetSpi.setToolTipText(objectStringConverter.getDescription(value));
		}
	}

	private void addLenientValue(final VALUE_TYPE value) {
		final String[] spiElements = comboBoxSelectionWidgetSpi.getElements();
		final String[] newSpiElements = new String[spiElements.length + 1];
		for (int i = 0; i < spiElements.length; i++) {
			newSpiElements[i] = spiElements[i];
		}
		newSpiElements[spiElements.length] = addLenientDecoration(convertToString(value));
		comboBoxSelectionWidgetSpi.setElements(newSpiElements);
		comboBoxSelectionWidgetSpi.setSelectedIndex(spiElements.length);
		this.lenientValue = value;
	}

	private void removeLinientValue() {
		if (lenientValue != null) {
			lenientValue = null;

			final int oldIndex = comboBoxSelectionWidgetSpi.getSelectedIndex();

			final String[] spiElements = comboBoxSelectionWidgetSpi.getElements();
			final String[] newSpiElements = new String[spiElements.length - 1];
			for (int i = 0; i < newSpiElements.length; i++) {
				newSpiElements[i] = spiElements[i];
			}
			comboBoxSelectionWidgetSpi.setElements(newSpiElements);

			if (oldIndex < newSpiElements.length) {
				comboBoxSelectionWidgetSpi.setSelectedIndex(oldIndex);
			}
		}
	}

	private String addLenientDecoration(final String string) {
		if (string != null) {
			return "* " + string;
		}
		else {
			return "* ";
		}
	}

	private String removeLenientDecoration(final String string) {
		if (string != null && string.length() >= 2) {
			return string.substring(2);
		}
		return string;
	}

	@Override
	public VALUE_TYPE getValue() {
		final int selectedIndex = comboBoxSelectionWidgetSpi.getSelectedIndex();
		if (selectedIndex >= 0 && selectedIndex < elements.size()) {
			return elements.get(selectedIndex);
		}
		else if (selectedIndex >= 0) {
			return lenientValue;
		}
		else {
			return null;
		}
	}

	@Override
	public final List<VALUE_TYPE> getElements() {
		return elementsView;
	}

	@Override
	public void setElements(final VALUE_TYPE... elements) {
		setElements(Arrays.asList(elements));
	}

	@Override
	public void setElements(final Collection<? extends VALUE_TYPE> newElements) {

		Assert.paramNotNull(newElements, "newElements");

		final VALUE_TYPE oldValue = getValue();
		boolean removeLenient = true;

		setRedrawEnabled(false);
		getWidget().removeInputListener(inputListener);

		//determine the last selected string
		String lastSelectedString = null;
		final int lastSelectedIndex = comboBoxSelectionWidgetSpi.getSelectedIndex();
		if (comboBoxSelectionWidgetSpi.getElements() != null
			&& lastSelectedIndex >= 0
			&& lastSelectedIndex < comboBoxSelectionWidgetSpi.getElements().length) {
			lastSelectedString = comboBoxSelectionWidgetSpi.getElements()[lastSelectedIndex];
		}
		if (!EmptyCheck.isEmpty(lastSelectedString) && NullCompatibleEquivalence.equals(oldValue, lenientValue)) {
			lastSelectedString = removeLenientDecoration(lastSelectedString);
		}

		//set the new elements
		final String[] spiElements = new String[newElements.size()];
		elements.clear();

		int index = 0;
		int newSelectionIndex = -1;

		for (final VALUE_TYPE element : newElements) {
			elements.add(element);
			final String elementString = convertToString(element);
			if (lastSelectedString != null && lastSelectedString.equals(elementString)) {
				newSelectionIndex = index;
			}
			spiElements[index] = elementString;
			index++;
		}
		comboBoxSelectionWidgetSpi.setElements(spiElements);
		final boolean previousSelectionPolicy = isPreviousSelectionPolicy();

		//do auto selection
		if (AutoSelectionPolicy.FIRST_ELEMENT == autoSelectionPolicy && spiElements.length > 0) {
			comboBoxSelectionWidgetSpi.setSelectedIndex(0);
		}
		else if (AutoSelectionPolicy.LAST_ELEMENT == autoSelectionPolicy && spiElements.length > 0) {
			comboBoxSelectionWidgetSpi.setSelectedIndex(spiElements.length - 1);
		}
		else if (lenientValue != null && previousSelectionPolicy && newSelectionIndex == -1) {
			addLenientValue(lenientValue);
			removeLenient = false;
		}
		else if (lenient && oldValue != null && previousSelectionPolicy && newSelectionIndex == -1) {
			addLenientValue(oldValue);
			removeLenient = false;
		}
		else if (AutoSelectionPolicy.PREVIOUS_SELECTED == autoSelectionPolicy && spiElements.length > 0) {
			if (newSelectionIndex != -1) {
				comboBoxSelectionWidgetSpi.setSelectedIndex(newSelectionIndex);
			}
		}
		else if (AutoSelectionPolicy.PREVIOUS_SELECTED_OR_FIRST == autoSelectionPolicy && spiElements.length > 0) {
			if (newSelectionIndex != -1) {
				comboBoxSelectionWidgetSpi.setSelectedIndex(newSelectionIndex);
			}
			else {
				comboBoxSelectionWidgetSpi.setSelectedIndex(0);
			}
		}
		else if (AutoSelectionPolicy.PREVIOUS_SELECTED_OR_LAST == autoSelectionPolicy && spiElements.length > 0) {
			if (newSelectionIndex != -1) {
				comboBoxSelectionWidgetSpi.setSelectedIndex(newSelectionIndex);
			}
			else {
				comboBoxSelectionWidgetSpi.setSelectedIndex(spiElements.length - 1);
			}
		}
		else if (AutoSelectionPolicy.OFF == autoSelectionPolicy || spiElements.length == 0) {
			comboBoxSelectionWidgetSpi.setSelectedIndex(-1);
		}
		else {
			throw new IllegalStateException("The value '"
				+ autoSelectionPolicy
				+ "' for '"
				+ AutoSelectionPolicy.class.getSimpleName()
				+ "' is not known");
		}

		if (removeLenient) {
			removeLinientValue();
		}

		getWidget().addInputListener(inputListener);
		setRedrawEnabled(true);

		final VALUE_TYPE newValue = getValue();
		if (!NullCompatibleEquivalence.equals(oldValue, newValue)) {
			onInputChanged(false);
		}
	}

	private boolean isPreviousSelectionPolicy() {
		return autoSelectionPolicy == AutoSelectionPolicy.PREVIOUS_SELECTED
			|| autoSelectionPolicy == AutoSelectionPolicy.PREVIOUS_SELECTED_OR_FIRST
			|| autoSelectionPolicy == AutoSelectionPolicy.PREVIOUS_SELECTED_OR_LAST;
	}

	private String convertToString(final VALUE_TYPE value) {
		final String result = objectStringConverter.convertToString(value);
		if (result != null) {
			return result;
		}
		else {
			return "";
		}
	}

	@Override
	public void setParent(final IContainer parent) {
		controlDelegate.setParent(parent);
	}

	@Override
	public IContainer getParent() {
		return controlDelegate.getParent();
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
	public void addValidator(final IValidator<VALUE_TYPE> validator) {
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
	public void setEditable(final boolean editable) {
		this.editable = editable;
		getWidget().setEditable(editable);
	}

	@Override
	public boolean isEditable() {
		return editable;
	}

	@Override
	public void addInputListener(final IInputListener listener) {
		inputObservable.addInputListener(listener);
	}

	@Override
	public void removeInputListener(final IInputListener listener) {
		inputObservable.removeInputListener(listener);
	}

}
