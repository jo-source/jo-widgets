/*
 * Copyright (c) 2015, grossmann
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

package org.jowidgets.util.binding;

import junit.framework.Assert;

import org.jowidgets.util.IObservableValue;
import org.jowidgets.util.ObservableValue;
import org.junit.Test;

public class BindingTest {

	private static String STRING_1 = "STRING_1";
	private static String STRING_2 = "STRING_2";
	private static String STRING_3 = "STRING_3";
	private static String STRING_4 = "STRING_4";
	private static String STRING_5 = "STRING_5";
	private static String STRING_6 = "STRING_6";
	private static String STRING_7 = "STRING_7";
	private static String STRING_8 = "STRING_8";
	private static String STRING_9 = "STRING_9";
	private static String STRING_10 = "STRING_10";

	@Test
	public void testBinding() {
		//Create two observable values
		final ObservableValue<String> source = new ObservableValue<String>(STRING_1);
		final ObservableValue<String> destination = new ObservableValue<String>(STRING_2);

		//create a new binding
		final IBinding binding = Bind.bind(source, destination);

		//binding is bound by default
		Assert.assertTrue(binding.isBound());

		//must be equal and must be STRING_1 (source before binding)
		testEquality(source, destination, STRING_1);

		//change source must change destination
		source.setValue(STRING_3);

		//must be equal and STRING_3
		testEquality(source, destination, STRING_3);

		//change destination must change source
		destination.setValue(STRING_4);

		//must be equal and STRING_4
		testEquality(source, destination, STRING_4);

		//unbind the values
		binding.unbind();

		//binding state is false now
		Assert.assertFalse(binding.isBound());

		//unbind must not modify values
		testEquality(source, destination, STRING_4);

		//after unbind, change source, destination changes not
		source.setValue(STRING_5);
		Assert.assertEquals(STRING_5, source.getValue());
		Assert.assertEquals(STRING_4, destination.getValue());

		//after unbind, change destination, source changes not
		destination.setValue(STRING_6);
		Assert.assertEquals(STRING_6, destination.getValue());
		Assert.assertEquals(STRING_5, source.getValue());

		//bind the values again
		binding.bind();

		//binding is bound by default
		Assert.assertTrue(binding.isBound());

		//must be equal and STRING_5 (last source value)
		testEquality(source, destination, STRING_5);

		//change source must change destination
		source.setValue(STRING_7);

		//must be equal and STRING_7
		testEquality(source, destination, STRING_7);

		//change destination must change source
		destination.setValue(STRING_8);

		//must be equal and STRING_8
		testEquality(source, destination, STRING_8);

		//dispose the binding
		binding.dispose();

		//binding is disposed
		Assert.assertTrue(binding.isDisposed());

		//dispose must not change values
		testEquality(source, destination, STRING_8);

		//after dispose, change source, destination changes not
		source.setValue(STRING_9);
		Assert.assertEquals(STRING_9, source.getValue());
		Assert.assertEquals(STRING_8, destination.getValue());

		//after dispose, change destination, source changes not
		destination.setValue(STRING_10);
		Assert.assertEquals(STRING_10, destination.getValue());
		Assert.assertEquals(STRING_9, source.getValue());

		//bind after dispose must throw exception
		boolean exception = false;
		try {
			binding.bind();
		}
		catch (final Exception e) {
			exception = true;
		}
		Assert.assertTrue(exception);
	}

	private void testEquality(
		final IObservableValue<String> source,
		final IObservableValue<String> destination,
		final String expectedValue) {

		//the values of the observable value must be equal
		Assert.assertEquals(source.getValue(), destination.getValue());

		//the destination must be the expected value
		Assert.assertEquals(expectedValue, source.getValue());

		//the destination must be the expected value
		Assert.assertEquals(expectedValue, destination.getValue());
	}

}
