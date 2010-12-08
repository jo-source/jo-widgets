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

import java.util.HashMap;
import java.util.Map;

import org.jowidgets.api.convert.IConverter;
import org.jowidgets.api.convert.IConverterProvider;
import org.jowidgets.api.convert.IObjectStringConverter;
import org.jowidgets.impl.convert.defaults.DefaultIntegerConverter;
import org.jowidgets.impl.convert.defaults.DefaultLongConverter;
import org.jowidgets.impl.convert.defaults.DefaultShortConverter;
import org.jowidgets.impl.convert.defaults.DefaultStringConverter;
import org.jowidgets.util.Assert;

public final class DefaultConverterProvider implements IConverterProvider {

	public static final IObjectStringConverter<Object> TO_STRING_CONVERTER = new DefaultObjectStringConverter();
	public static final IConverter<String> STRING = new DefaultStringConverter();
	public static final IConverter<Long> LONG_NUMBER = new DefaultLongConverter();
	public static final IConverter<Integer> INTEGER_NUMBER = new DefaultIntegerConverter();
	public static final IConverter<Short> SHORT_NUMBER = new DefaultShortConverter();

	private static final Map<Class<?>, IConverter<? extends Object>> CONVERTER_MAP = createConverterMap();

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
		return result;
	}

	@Override
	public <OBJECT_TYPE> IConverter<OBJECT_TYPE> getConverter(final Class<? extends OBJECT_TYPE> type) {
		return getConverterFromMap(type);
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

	@SuppressWarnings("unchecked")
	@Override
	public <OBJECT_TYPE> IObjectStringConverter<OBJECT_TYPE> toStringConverter() {
		return (IObjectStringConverter<OBJECT_TYPE>) TO_STRING_CONVERTER;
	}

}
