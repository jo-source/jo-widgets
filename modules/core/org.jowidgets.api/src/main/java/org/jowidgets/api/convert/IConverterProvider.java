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

package org.jowidgets.api.convert;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.jowidgets.common.mask.ITextMask;

public interface IConverterProvider {

	/**
	 * Generic method to get an converter for an type. This method only gets an converter for the types supported by the
	 * explicit methods of this interface or that was registered before.
	 * 
	 * @param <OBJECT_TYPE> The type of the object to get the converter for
	 * @param type the class to get the converter for
	 * @return The converter for the type or null if no converter exists for the type
	 */
	<OBJECT_TYPE> IConverter<OBJECT_TYPE> getConverter(Class<? extends OBJECT_TYPE> type);

	<OBJECT_TYPE> IObjectStringConverter<OBJECT_TYPE> getObjectStringConverter(Class<? extends OBJECT_TYPE> type);

	<OBJECT_TYPE> IObjectLabelConverter<OBJECT_TYPE> getObjectLabelConverter(Class<? extends OBJECT_TYPE> type);

	<OBJECT_TYPE> IStringObjectConverter<OBJECT_TYPE> getStringObjectConverter(Class<? extends OBJECT_TYPE> type);

	<OBJECT_TYPE> IObjectStringConverter<OBJECT_TYPE> toStringConverter();

	IObjectStringConverter<String> passwordPresentationConverter();

	<OBJECT_TYPE> IConverter<OBJECT_TYPE> mapConverter(
		Map<? extends OBJECT_TYPE, String> objectToString,
		Map<String, ? extends OBJECT_TYPE> stringToObject,
		String hint);

	<OBJECT_TYPE> IObjectStringConverter<OBJECT_TYPE> mapConverter(Map<OBJECT_TYPE, String> objectToString);

	IConverter<String> string();

	IConverter<Long> longNumber();

	IConverter<Integer> integerNumber();

	IConverter<Short> shortNumber();

	IConverter<Boolean> boolLong();

	IConverter<Boolean> boolShort();

	IConverter<Double> doubleNumber();

	IConverter<Double> doubleNumber(DecimalFormat decimalFormat, String formatHint);

	IConverter<Date> date(DateFormat dateFormat, String formatHint, ITextMask mask);

	IConverter<Date> date(DateFormat dateFormat, String formatHint);

	IConverter<Date> date();

	IConverter<Date> dateTime();

	IConverter<Date> time();

	<OBJECT_TYPE> void register(Locale locale, Class<? extends OBJECT_TYPE> type, IConverter<OBJECT_TYPE> converter);

	<OBJECT_TYPE> void register(Class<? extends OBJECT_TYPE> type, IConverter<OBJECT_TYPE> converter);

	void registerDefaultDateConverter(Locale locale, IConverter<Date> converter);

	void registerDefaultTimeConverter(Locale locale, IConverter<Date> converter);

	void registerDefaultDateTimeConverter(Locale locale, IConverter<Date> converter);

	void registerDefaultBooleanLongConverter(Locale locale, IConverter<Boolean> converter);

	void registerDefaultBooleanShortConverter(Locale locale, IConverter<Boolean> converter);

}
