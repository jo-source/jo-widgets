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

import org.jowidgets.api.convert.IConverter;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IInputControl;
import org.jowidgets.api.widgets.blueprint.IInputFieldBluePrint;
import org.jowidgets.api.widgets.descriptor.IInputFieldDescriptor;

public class JoInputField<VALUE_TYPE> extends InputWidget<IInputControl<VALUE_TYPE>, IInputFieldBluePrint<VALUE_TYPE>, VALUE_TYPE> implements
		IInputControl<VALUE_TYPE> {

	public JoInputField(final IConverter<VALUE_TYPE> converter) {
		this(bluePrint(converter));
	}

	public JoInputField(final IInputFieldDescriptor<VALUE_TYPE> descriptor) {
		super(bluePrint(descriptor.getConverter()).setSetup(descriptor));
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	//some static blue print creation for convenience purpose from here
	////////////////////////////////////////////////////////////////////////////////////////////////////
	public static <VALUE_TYPE> IInputFieldBluePrint<VALUE_TYPE> bluePrint(final IConverter<VALUE_TYPE> converter) {
		return Toolkit.getBluePrintFactory().inputField(converter);
	}

	public static IInputFieldBluePrint<String> bluePrintString() {
		return Toolkit.getBluePrintFactory().inputFieldString();
	}

	public static IInputFieldBluePrint<Integer> bluePrintInteger() {
		return Toolkit.getBluePrintFactory().inputFieldIntegerNumber();
	}

	public static IInputFieldBluePrint<Long> bluePrintLong() {
		return Toolkit.getBluePrintFactory().inputFieldLongNumber();
	}

	public static IInputFieldBluePrint<Short> bluePrintShort() {
		return Toolkit.getBluePrintFactory().inputFieldShortNumber();
	}

	public static IInputFieldBluePrint<String> bluePrintString(final int maxLength) {
		return Toolkit.getBluePrintFactory().inputFieldString().setMaxLength(maxLength);
	}

	public static IInputFieldBluePrint<Integer> bluePrintInteger(final int maxLength) {
		return Toolkit.getBluePrintFactory().inputFieldIntegerNumber().setMaxLength(maxLength);
	}

	public static IInputFieldBluePrint<Long> bluePrintLong(final int maxLength) {
		return Toolkit.getBluePrintFactory().inputFieldLongNumber().setMaxLength(maxLength);
	}

	public static IInputFieldBluePrint<Short> bluePrintShort(final int maxLength) {
		return Toolkit.getBluePrintFactory().inputFieldShortNumber().setMaxLength(maxLength);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	//some static JoInputField creation for convenience purpose from here
	////////////////////////////////////////////////////////////////////////////////////////////////////
	public static JoInputField<String> inputFieldString() {
		return new JoInputField<String>(bluePrintString());
	}

	public static JoInputField<Integer> inputFieldInteger() {
		return new JoInputField<Integer>(bluePrintInteger());
	}

	public static JoInputField<Long> inputFieldLong() {
		return new JoInputField<Long>(bluePrintLong());
	}

	public static JoInputField<Short> inputFieldShort() {
		return new JoInputField<Short>(bluePrintShort());
	}

	public static JoInputField<String> inputFieldString(final int maxLength) {
		return new JoInputField<String>(bluePrintString(maxLength));
	}

	public static JoInputField<Integer> inputFieldInteger(final int maxLength) {
		return new JoInputField<Integer>(bluePrintInteger(maxLength));
	}

	public static JoInputField<Long> inputFieldLong(final int maxLength) {
		return new JoInputField<Long>(bluePrintLong(maxLength));
	}

	public static JoInputField<Short> inputFieldShort(final int maxLength) {
		return new JoInputField<Short>(bluePrintShort(maxLength));
	}

}
