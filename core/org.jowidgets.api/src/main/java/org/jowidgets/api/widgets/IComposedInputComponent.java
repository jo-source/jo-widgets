/*
 * Copyright (c) 2011, grossmann
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

package org.jowidgets.api.widgets;

public interface IComposedInputComponent<VALUE_TYPE> extends IInputComponent<VALUE_TYPE> {

	/**
	 * The intermediate value describes an intermediate state of the user input.
	 * 
	 * Example1: The user enters a date in an IInputField<Date>, composed from an single ITextField.
	 * The current input (e.g. '12-11-') is incomplete so it could not be converted to a valid date,
	 * which leads to a value of 'null'. The intermediate value could then be set to the String "12-11-".
	 * After pressing the '2' key, the intermediate value could be "12-11-2", but the value is still null.
	 * After pressing the keys '0', '1' , '1', the intermediate value could be "12-11-2011" and the value is
	 * a Date object representing "12-11-2011". If the user presses the backspace key 4 times then, the value
	 * is 'null' and the intermediate value is "12-11-" again, which indicates that the field is in it's
	 * original state.
	 * 
	 * Example2: The user enters input in a form implemented by a IComposedInputComponent<Person> composed like following:
	 * - IInputField<String> initial value: null , intermediateValue: null
	 * - IInputField<Date> initial value: (Date(01-03-1972)), intermediateValue: "01-03-1972"
	 * - IComboBoxSelection<Integer> initial value: null, intermediateValue: null
	 * - ICheckBox initial value: Boolean.FALSE, no intermediateValue, ICheckBox is not composed
	 * 
	 * Because the inputs of the text field and comboBox are mandatory and not set, no Person object could be created.
	 * This leads to a value of 'null'.
	 * The intermediate value may be a List<Object> with the values {NullEntry.INSTANCE, "01-03-1972", NullEntry.INSTANCE,
	 * Boolean.FALSE}.
	 * 
	 * The following states should give an examples of further states of the form
	 * value: null, intermediateValue: {"F", "01-03-1972", NullEntry.INSTANCE, Boolean.FALSE}
	 * value: null, intermediateValue: {"Fr", "01-03-1972", NullEntry.INSTANCE, Boolean.FALSE}
	 * value: null, intermediateValue: {"Fre", "01-03-1972", NullEntry.INSTANCE, Boolean.FALSE}
	 * value: null, intermediateValue: {"Fred", "01-03-1972", NullEntry.INSTANCE, Boolean.FALSE}
	 * value: null, intermediateValue: {"Fred", "01-03-1972", NullEntry.INSTANCE, Boolean.TRUE}
	 * value: Person(Fred, Date(1.3.1972), 1, true), intermediateValue: {"Fred", "01-03-1972", Integer.valueOf(1), Boolean.TRUE}
	 * value: Person(Fredd, Date(1.3.1972), 1, true), intermediateValue: {"Fredd", "01-03-1972", Integer.valueOf(1), Boolean.TRUE}
	 * value: Person(Freddy, Date(1.3.1972), 1, true), intermediateValue: {"Freddy", "01-03-1972", Integer.valueOf(1),
	 * Boolean.TRUE}
	 * 
	 * 
	 * The intermediate value will primary be used to compare input states, e.g. to determine if the input
	 * component was modified by the user since an defined point in time. The intermediate value will
	 * usually not be (re-)offered to the user by the ui.
	 * 
	 * The intermediate value type must implement equals and hashCode in a proper way to allow comparison
	 * of intermediate input values.
	 * 
	 * REMARK: The input value observable inherited from IInputComponent must be implemented in that way,
	 * that each change of the INTERMEDIATE VALUE fires a stateChanged() event and not each change of the value!
	 * 
	 * @return The intermediate value, may be null
	 */
	Object getIntermediateValue();

}
