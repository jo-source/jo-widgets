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
package org.jowidgets.api.widgets;

import org.jowidgets.api.widgets.access.IInputValueAccessor;
import org.jowidgets.common.widgets.IInputComponentCommon;
import org.jowidgets.validation.IValidateable;
import org.jowidgets.validation.IValidator;

public interface IInputComponent<VALUE_TYPE> extends
		IComponent,
		IInputValueAccessor<VALUE_TYPE>,
		IValidateable,
		IInputComponentCommon {

	void addValidator(IValidator<VALUE_TYPE> validator);

	/**
	 * Determines if modifications has been occurred since object creation or the last invocation of
	 * resetModificationState().
	 * 
	 * Remark: The component may have modifications even if the public value not differs from its
	 * original value, Example:
	 * The user enters a date in an IInputField<Date>, composed from an single ITextField.
	 * The current input (e.g. '12-11-') is incomplete so it can not be converted to a valid date,
	 * which leads to a value of 'null'. The {@link #resetModificationState()} method will be invoked,
	 * so the method {@link #hasModifications()} returns false.
	 * After pressing the '2' key, the method {@link #hasModifications()} returns true, but the value is still null.
	 * After pressing the keys '0', '1' , '1', the method {@link #hasModifications()} returns still true and the value is
	 * a Date object representing "12-11-2011". If the user presses the backspace key 4 times, the value
	 * is 'null' and the method {@link #hasModifications()} returns false again, which indicates that the field is in it's
	 * original state.
	 * 
	 * @return True if the component has modifications, false otherwise
	 * @see IInputComponent#hasModifications()
	 */
	boolean hasModifications();

	/**
	 * Resets the modification state. After that, the {@link #hasModifications()} method must return false,
	 * until modification was made by the user or programmatically.
	 * 
	 * Remark: This method invocation must not reset the modifications. Only the modification state (modified or not)
	 * will be reseted.
	 */
	void resetModificationState();

	boolean isEditable();
}
