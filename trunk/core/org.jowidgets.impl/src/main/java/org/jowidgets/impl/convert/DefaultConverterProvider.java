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
package org.jowidgets.impl.convert;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.jowidgets.api.convert.IConverter;
import org.jowidgets.api.convert.IConverterProvider;
import org.jowidgets.api.convert.IObjectStringConverter;
import org.jowidgets.api.mask.ITextMaskBuilder;
import org.jowidgets.common.mask.ITextMask;
import org.jowidgets.impl.convert.defaults.DefaultDateConverter;
import org.jowidgets.impl.convert.defaults.DefaultIntegerConverter;
import org.jowidgets.impl.convert.defaults.DefaultLongConverter;
import org.jowidgets.impl.convert.defaults.DefaultShortConverter;
import org.jowidgets.impl.convert.defaults.DefaultStringConverter;
import org.jowidgets.impl.convert.defaults.DefaultYesNoConverterLong;
import org.jowidgets.impl.convert.defaults.DefaultYesNoConverterShort;
import org.jowidgets.impl.mask.TextMaskBuilder;
import org.jowidgets.tools.converter.Converter;
import org.jowidgets.util.Assert;

public final class DefaultConverterProvider implements IConverterProvider {

	public static final IObjectStringConverter<Object> TO_STRING_CONVERTER = new DefaultObjectStringConverter();
	public static final IConverter<String> STRING = new DefaultStringConverter();
	public static final IConverter<Long> LONG_NUMBER = new DefaultLongConverter();
	public static final IConverter<Integer> INTEGER_NUMBER = new DefaultIntegerConverter();
	public static final IConverter<Short> SHORT_NUMBER = new DefaultShortConverter();
	public static final IConverter<Boolean> BOOLEAN_YES_NO_LONG = new DefaultYesNoConverterLong();
	public static final IConverter<Boolean> BOOLEAN_YES_NO_SHORT = new DefaultYesNoConverterShort();

	private static final Map<Class<?>, IConverter<? extends Object>> CONVERTER_MAP = createConverterMap();

	private static final Locale DEFAULT_LOCALE = Locale.US;

	private Map<Locale, IConverter<Date>> defaultDateConverter;
	private Map<Locale, IConverter<Date>> defaultTimeConverter;
	private Map<Locale, IConverter<Date>> defaultDateTimeConverter;

	public DefaultConverterProvider() {}

	@SuppressWarnings("unchecked")
	private static <OBJECT_TYPE> IConverter<OBJECT_TYPE> getConverterFromMap(final Class<? extends OBJECT_TYPE> type) {
		Assert.paramNotNull(type, "type");
		final Object result = CONVERTER_MAP.get(type);
		return (IConverter<OBJECT_TYPE>) result;
	}

	private static Map<Class<?>, IConverter<? extends Object>> createConverterMap() {
		final Map<Class<?>, IConverter<? extends Object>> result = new HashMap<Class<?>, IConverter<? extends Object>>();
		result.put(String.class, STRING);
		result.put(Long.class, LONG_NUMBER);
		result.put(Integer.class, INTEGER_NUMBER);
		result.put(Short.class, SHORT_NUMBER);
		result.put(Boolean.class, BOOLEAN_YES_NO_LONG);
		result.put(boolean.class, BOOLEAN_YES_NO_LONG);
		return result;
	}

	@Override
	public void registerDefaultDateConverter(final Locale locale, final IConverter<Date> converter) {
		Assert.paramNotNull(locale, "locale");
		if (converter != null) {
			getDefaultDateConverters().put(locale, converter);
		}
		else {
			getDefaultDateConverters().remove(locale);
		}
	}

	@Override
	public void registerDefaultTimeConverter(final Locale locale, final IConverter<Date> converter) {
		Assert.paramNotNull(locale, "locale");
		if (converter != null) {
			getDefaultTimeConverters().put(locale, converter);
		}
		else {
			getDefaultTimeConverters().remove(locale);
		}
	}

	@Override
	public void registerDefaultDateTimeConverter(final Locale locale, final IConverter<Date> converter) {
		Assert.paramNotNull(locale, "locale");
		if (converter != null) {
			getDefaultDateTimeConverters().put(locale, converter);
		}
		else {
			getDefaultDateTimeConverters().remove(locale);
		}
	}

	@Override
	public <OBJECT_TYPE> IConverter<OBJECT_TYPE> mapConverter(
		final Map<? extends OBJECT_TYPE, String> objectToString,
		final Map<String, ? extends OBJECT_TYPE> stringToObject,
		final String hint) {
		Assert.paramNotNull(objectToString, "objectToString");
		Assert.paramNotNull(stringToObject, "stringToObject");
		return new Converter<OBJECT_TYPE>(
			new ObjectStringMapConverter<OBJECT_TYPE>(objectToString),
			new StringObjectMapConverter<OBJECT_TYPE>(stringToObject, hint));
	}

	@Override
	public <OBJECT_TYPE> IObjectStringConverter<OBJECT_TYPE> mapConverter(final Map<OBJECT_TYPE, String> objectToString) {
		Assert.paramNotNull(objectToString, "objectToString");
		return new ObjectStringMapConverter<OBJECT_TYPE>(objectToString);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <OBJECT_TYPE> IConverter<OBJECT_TYPE> getConverter(final Class<? extends OBJECT_TYPE> type) {
		final IConverter<OBJECT_TYPE> result = getConverterFromMap(type);
		if (result == null) {
			if (type == Date.class) {
				return (IConverter<OBJECT_TYPE>) date();
			}
		}
		return result;
	}

	@Override
	public IConverter<String> string() {
		return STRING;
	}

	@Override
	public IConverter<Long> longNumber() {
		return LONG_NUMBER;
	}

	@Override
	public IConverter<Integer> integerNumber() {
		return INTEGER_NUMBER;
	}

	@Override
	public IConverter<Short> shortNumber() {
		return SHORT_NUMBER;
	}

	@Override
	public IConverter<Boolean> boolYesNoLong() {
		return BOOLEAN_YES_NO_LONG;
	}

	@Override
	public IConverter<Boolean> boolYesNoShort() {
		return BOOLEAN_YES_NO_SHORT;
	}

	@Override
	public IConverter<Date> date(final DateFormat dateFormat, final String formatHint, final ITextMask textMask) {
		return new DefaultDateConverter(dateFormat, textMask, formatHint);
	}

	@Override
	public IConverter<Date> date(final DateFormat dateFormat, final String formatHint) {
		return new DefaultDateConverter(dateFormat, null, formatHint);
	}

	@Override
	public IConverter<Date> date() {
		IConverter<Date> converter = getDefaultDateConverters().get(Locale.getDefault());
		if (converter == null) {
			converter = getDefaultDateConverters().get(DEFAULT_LOCALE);
		}
		return converter;
	}

	@Override
	public IConverter<Date> time() {
		IConverter<Date> converter = getDefaultTimeConverters().get(Locale.getDefault());
		if (converter == null) {
			converter = getDefaultTimeConverters().get(DEFAULT_LOCALE);
		}
		return converter;
	}

	@Override
	public IConverter<Date> dateTime() {
		IConverter<Date> converter = getDefaultDateTimeConverters().get(Locale.getDefault());
		if (converter == null) {
			converter = getDefaultDateTimeConverters().get(DEFAULT_LOCALE);
		}
		return converter;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <OBJECT_TYPE> IObjectStringConverter<OBJECT_TYPE> toStringConverter() {
		return (IObjectStringConverter<OBJECT_TYPE>) TO_STRING_CONVERTER;
	}

	private Map<Locale, IConverter<Date>> getDefaultDateConverters() {
		if (defaultDateConverter == null) {
			this.defaultDateConverter = new HashMap<Locale, IConverter<Date>>();

			final ITextMaskBuilder dateMaskBuilderUS = new TextMaskBuilder();
			dateMaskBuilderUS.addCharacterMask("[0-1]", '_');
			dateMaskBuilderUS.addNumericMask('_');
			dateMaskBuilderUS.addDelimiter('/');
			dateMaskBuilderUS.addCharacterMask("[0-3]", '_');
			dateMaskBuilderUS.addNumericMask('_');
			dateMaskBuilderUS.addDelimiter('/');
			dateMaskBuilderUS.addNumericMask('_');
			dateMaskBuilderUS.addNumericMask('_');
			dateMaskBuilderUS.addNumericMask('_');
			dateMaskBuilderUS.addNumericMask('_');

			final DateFormat dateFormatUS = new SimpleDateFormat("MM/dd/yyyy");
			dateFormatUS.setLenient(false);
			defaultDateConverter.put(DEFAULT_LOCALE, date(dateFormatUS, "MM/DD/YYYY", dateMaskBuilderUS.build()));

			final ITextMaskBuilder dateMaskBuilderDE = new TextMaskBuilder();
			dateMaskBuilderDE.addCharacterMask("[0-3]", '_');
			dateMaskBuilderDE.addNumericMask('_');
			dateMaskBuilderDE.addDelimiter('-');
			dateMaskBuilderDE.addCharacterMask("[0-1]", '_');
			dateMaskBuilderDE.addNumericMask('_');
			dateMaskBuilderDE.addDelimiter('-');
			dateMaskBuilderDE.addNumericMask('_');
			dateMaskBuilderDE.addNumericMask('_');
			dateMaskBuilderDE.addNumericMask('_');
			dateMaskBuilderDE.addNumericMask('_');

			final DateFormat dateFormatDE = new SimpleDateFormat("dd-MM-yyyy");
			dateFormatDE.setLenient(false);

			final IConverter<Date> converterDE = date(dateFormatDE, "DD-MM-YYYY", dateMaskBuilderDE.build());

			defaultDateConverter.put(Locale.GERMANY, converterDE);
			defaultDateConverter.put(Locale.GERMAN, converterDE);
		}
		return defaultDateConverter;
	}

	private Map<Locale, IConverter<Date>> getDefaultTimeConverters() {
		if (defaultTimeConverter == null) {
			this.defaultTimeConverter = new HashMap<Locale, IConverter<Date>>();

			final ITextMaskBuilder timeMaskBuilder = new TextMaskBuilder();
			timeMaskBuilder.addCharacterMask("[0-2]", '_');
			timeMaskBuilder.addNumericMask('_');
			timeMaskBuilder.addDelimiter(':');
			timeMaskBuilder.addCharacterMask("[0-5]", '_');
			timeMaskBuilder.addNumericMask('_');
			timeMaskBuilder.addDelimiter(':');
			timeMaskBuilder.addCharacterMask("[0-5]", '_');
			timeMaskBuilder.addNumericMask('_');

			final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
			dateFormat.setLenient(false);

			final IConverter<Date> converter = date(dateFormat, "HH:MM:SS", timeMaskBuilder.build());

			defaultTimeConverter.put(DEFAULT_LOCALE, converter);
			defaultTimeConverter.put(Locale.GERMAN, converter);
			defaultTimeConverter.put(Locale.GERMANY, converter);
		}
		return defaultTimeConverter;
	}

	private Map<Locale, IConverter<Date>> getDefaultDateTimeConverters() {
		if (defaultDateTimeConverter == null) {
			this.defaultDateTimeConverter = new HashMap<Locale, IConverter<Date>>();

			final ITextMaskBuilder dateMaskBuilderUS = new TextMaskBuilder();
			dateMaskBuilderUS.addCharacterMask("[0-1]", '_');
			dateMaskBuilderUS.addNumericMask('_');
			dateMaskBuilderUS.addDelimiter('/');
			dateMaskBuilderUS.addCharacterMask("[0-3]", '_');
			dateMaskBuilderUS.addNumericMask('_');
			dateMaskBuilderUS.addDelimiter('/');
			dateMaskBuilderUS.addNumericMask('_');
			dateMaskBuilderUS.addNumericMask('_');
			dateMaskBuilderUS.addNumericMask('_');
			dateMaskBuilderUS.addNumericMask('_');

			dateMaskBuilderUS.addDelimiter(' ');

			dateMaskBuilderUS.addCharacterMask("[0-2]", '_');
			dateMaskBuilderUS.addNumericMask('_');
			dateMaskBuilderUS.addDelimiter(':');
			dateMaskBuilderUS.addCharacterMask("[0-5]", '_');
			dateMaskBuilderUS.addNumericMask('_');
			dateMaskBuilderUS.addDelimiter(':');
			dateMaskBuilderUS.addCharacterMask("[0-5]", '_');
			dateMaskBuilderUS.addNumericMask('_');

			final DateFormat dateFormatUS = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			dateFormatUS.setLenient(false);
			defaultDateTimeConverter.put(DEFAULT_LOCALE, date(dateFormatUS, "MM/DD/YYYY HH:MM:SS", dateMaskBuilderUS.build()));

			final ITextMaskBuilder dateMaskBuilderDE = new TextMaskBuilder();
			dateMaskBuilderDE.addCharacterMask("[0-3]", '_');
			dateMaskBuilderDE.addNumericMask('_');
			dateMaskBuilderDE.addDelimiter('-');
			dateMaskBuilderDE.addCharacterMask("[0-1]", '_');
			dateMaskBuilderDE.addNumericMask('_');
			dateMaskBuilderDE.addDelimiter('-');
			dateMaskBuilderDE.addNumericMask('_');
			dateMaskBuilderDE.addNumericMask('_');
			dateMaskBuilderDE.addNumericMask('_');
			dateMaskBuilderDE.addNumericMask('_');

			dateMaskBuilderDE.addDelimiter(' ');

			dateMaskBuilderDE.addCharacterMask("[0-2]", '_');
			dateMaskBuilderDE.addNumericMask('_');
			dateMaskBuilderDE.addDelimiter(':');
			dateMaskBuilderDE.addCharacterMask("[0-5]", '_');
			dateMaskBuilderDE.addNumericMask('_');
			dateMaskBuilderDE.addDelimiter(':');
			dateMaskBuilderDE.addCharacterMask("[0-5]", '_');
			dateMaskBuilderDE.addNumericMask('_');

			final DateFormat dateFormatDE = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			dateFormatDE.setLenient(false);

			final IConverter<Date> germanConverter = date(dateFormatDE, "DD-MM-YYYY HH:MM:SS", dateMaskBuilderDE.build());

			defaultDateTimeConverter.put(Locale.GERMAN, germanConverter);
			defaultDateTimeConverter.put(Locale.GERMANY, germanConverter);
		}
		return defaultDateTimeConverter;
	}
}
