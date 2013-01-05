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

package org.jowidgets.tools.powo;

import java.util.List;

import org.jowidgets.api.convert.IConverter;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IComboBox;
import org.jowidgets.api.widgets.blueprint.IComboBoxBluePrint;
import org.jowidgets.api.widgets.descriptor.IComboBoxDescriptor;
import org.jowidgets.tools.converter.Converter;

public class JoComboBox<VALUE_TYPE> extends InputControl<IComboBox<VALUE_TYPE>, IComboBoxBluePrint<VALUE_TYPE>, VALUE_TYPE> implements
		IComboBox<VALUE_TYPE> {

	public JoComboBox(final IConverter<VALUE_TYPE> converter, final List<VALUE_TYPE> elements) {
		this(bluePrint(converter).setElements(elements));
	}

	public JoComboBox(final IConverter<VALUE_TYPE> converter, final VALUE_TYPE... elements) {
		this(bluePrint(converter).setElements(elements));
	}

	public JoComboBox(final IConverter<VALUE_TYPE> converter) {
		this(bluePrint(converter));
	}

	public JoComboBox(final IComboBoxDescriptor<VALUE_TYPE> descriptor) {
		super(bluePrint(createConverter(descriptor)).setSetup(descriptor));
	}

	@Override
	public List<VALUE_TYPE> getElements() {
		if (isInitialized()) {
			return getWidget().getElements();
		}
		else {
			return getBluePrint().getElements();
		}
	}

	@Override
	public void setElements(final List<? extends VALUE_TYPE> elements) {
		if (isInitialized()) {
			getWidget().setElements(elements);
		}
		else {
			getBluePrint().setElements(elements);
		}
	}

	@Override
	public void setElements(final VALUE_TYPE... elements) {
		if (isInitialized()) {
			getWidget().setElements(elements);
		}
		else {
			getBluePrint().setElements(elements);
		}
	}

	private static <VALUE_TYPE> IConverter<VALUE_TYPE> createConverter(final IComboBoxDescriptor<VALUE_TYPE> descriptor) {
		return new Converter<VALUE_TYPE>(descriptor.getObjectStringConverter(), descriptor.getStringObjectConverter());
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	//some static blue print creation for convenience purpose from here
	////////////////////////////////////////////////////////////////////////////////////////////////////
	public static <VALUE_TYPE> IComboBoxBluePrint<VALUE_TYPE> bluePrint(final IConverter<VALUE_TYPE> converter) {
		return Toolkit.getBluePrintFactory().comboBox(converter);
	}

	public static IComboBoxBluePrint<String> bluePrintString() {
		return Toolkit.getBluePrintFactory().comboBoxString();
	}

	public static IComboBoxBluePrint<Integer> bluePrintInteger() {
		return Toolkit.getBluePrintFactory().comboBoxIntegerNumber();
	}

	public static IComboBoxBluePrint<Long> bluePrintLong() {
		return Toolkit.getBluePrintFactory().comboBoxLongNumber();
	}

	public static IComboBoxBluePrint<Short> bluePrintShort() {
		return Toolkit.getBluePrintFactory().comboBoxShortNumber();
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	//some static comboBox creation for convenience purpose from here
	////////////////////////////////////////////////////////////////////////////////////////////////////
	public static JoComboBox<String> comboBoxString() {
		return new JoComboBox<String>(bluePrintString());
	}

	public static JoComboBox<String> comboBoxString(final String... elements) {
		return new JoComboBox<String>(bluePrintString().setElements(elements));
	}

	public static JoComboBox<String> comboBoxString(final List<String> elements) {
		return new JoComboBox<String>(bluePrintString().setElements(elements));
	}

	public static JoComboBox<Integer> comboBoxInteger() {
		return new JoComboBox<Integer>(bluePrintInteger());
	}

	public static JoComboBox<Integer> comboBoxInteger(final Integer... elements) {
		return new JoComboBox<Integer>(bluePrintInteger().setElements(elements));
	}

	public static JoComboBox<Integer> comboBoxInteger(final List<Integer> elements) {
		return new JoComboBox<Integer>(bluePrintInteger().setElements(elements));
	}

	public static JoComboBox<Long> comboBoxLong() {
		return new JoComboBox<Long>(bluePrintLong());
	}

	public static JoComboBox<Long> comboBoxLong(final Long... elements) {
		return new JoComboBox<Long>(bluePrintLong().setElements(elements));
	}

	public static JoComboBox<Long> comboBoxLong(final List<Long> elements) {
		return new JoComboBox<Long>(bluePrintLong().setElements(elements));
	}

	public static JoComboBox<Short> comboBoxShort() {
		return new JoComboBox<Short>(bluePrintShort());
	}

	public static JoComboBox<Short> comboBoxShort(final Short... elements) {
		return new JoComboBox<Short>(bluePrintShort().setElements(elements));
	}

	public static JoComboBox<Short> comboBoxShort(final List<Short> elements) {
		return new JoComboBox<Short>(bluePrintShort().setElements(elements));
	}

}
