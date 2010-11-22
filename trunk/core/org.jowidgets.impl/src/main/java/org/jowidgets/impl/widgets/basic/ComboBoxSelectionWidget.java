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
import java.util.Collections;
import java.util.List;

import org.jowidgets.api.convert.IObjectStringConverter;
import org.jowidgets.api.types.AutoSelectionPolicy;
import org.jowidgets.api.widgets.IComboBox;
import org.jowidgets.api.widgets.descriptor.setup.IComboBoxSelectionSetup;
import org.jowidgets.impl.widgets.basic.factory.internal.util.VisibiliySettingsInvoker;
import org.jowidgets.spi.widgets.IComboBoxSelectionSpi;
import org.jowidgets.util.Assert;

public class ComboBoxSelectionWidget<VALUE_TYPE> extends AbstractBasicInputControl<VALUE_TYPE> implements
		IComboBox<VALUE_TYPE> {

	private final List<VALUE_TYPE> elements;
	private final List<VALUE_TYPE> elementsView;

	private final IComboBoxSelectionSpi comboBoxSelectionWidgetSpi;
	private final IObjectStringConverter<VALUE_TYPE> objectStringConverter;
	private final AutoSelectionPolicy autoSelectionPolicy;

	public ComboBoxSelectionWidget(
		final IComboBoxSelectionSpi comboBoxSelectionWidgetSpi,
		final IComboBoxSelectionSetup<VALUE_TYPE> setup) {
		super(comboBoxSelectionWidgetSpi, setup);

		this.comboBoxSelectionWidgetSpi = comboBoxSelectionWidgetSpi;
		this.objectStringConverter = setup.getObjectStringConverter();
		this.autoSelectionPolicy = setup.getAutoSelectionPolicy();
		this.elements = new ArrayList<VALUE_TYPE>();
		this.elementsView = Collections.unmodifiableList(this.elements);

		setElements(setup.getElements());

		if (setup.getValue() != null) {
			setValue(setup.getValue());
		}

		VisibiliySettingsInvoker.setVisibility(setup, this);
	}

	@Override
	public void setValue(final VALUE_TYPE value) {
		if (value == null) {
			comboBoxSelectionWidgetSpi.setSelectedIndex(-1);
		}
		else {
			final int indexOfContent = elements.indexOf(value);
			if (indexOfContent >= 0 && indexOfContent < elements.size()) {
				comboBoxSelectionWidgetSpi.setSelectedIndex(indexOfContent);
			}
			else {
				throw new IllegalArgumentException("Value '" + value + "' is not a element of this combo box");
			}
		}
	}

	@Override
	public VALUE_TYPE getValue() {
		final int selectedIndex = comboBoxSelectionWidgetSpi.getSelectedIndex();
		if (selectedIndex >= 0 && selectedIndex < elements.size()) {
			return elements.get(selectedIndex);
		}
		return null;
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
	public void setElements(final List<VALUE_TYPE> newElements) {

		Assert.paramNotNull(newElements, "newElements");

		//determinie the last selected string
		String lastSelectedString = null;
		final int lastSelectedIndex = comboBoxSelectionWidgetSpi.getSelectedIndex();
		if (comboBoxSelectionWidgetSpi.getElements() != null
			&& lastSelectedIndex >= 0
			&& lastSelectedIndex < comboBoxSelectionWidgetSpi.getElements().length - 1) {
			lastSelectedString = comboBoxSelectionWidgetSpi.getElements()[lastSelectedIndex];
		}

		//set the new elements
		final String[] spiElements = new String[newElements.size()];
		elements.clear();

		int index = 0;
		int newSelectionIndex = -1;

		for (final VALUE_TYPE element : newElements) {
			elements.add(element);
			final String elementString = objectStringConverter.convertToString(element);
			if (lastSelectedString != null && lastSelectedString.equals(elementString)) {
				newSelectionIndex = index;
			}
			spiElements[index] = elementString;
			index++;
		}
		comboBoxSelectionWidgetSpi.setElements(spiElements);

		//do auto selection
		if (AutoSelectionPolicy.FIRST_ELEMENT == autoSelectionPolicy && spiElements.length > 0) {
			comboBoxSelectionWidgetSpi.setSelectedIndex(0);
		}
		else if (AutoSelectionPolicy.LAST_ELEMENT == autoSelectionPolicy && spiElements.length > 0) {
			comboBoxSelectionWidgetSpi.setSelectedIndex(spiElements.length - 1);
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
	}

	protected IObjectStringConverter<VALUE_TYPE> getObjectStringConverter() {
		return objectStringConverter;
	}

}
