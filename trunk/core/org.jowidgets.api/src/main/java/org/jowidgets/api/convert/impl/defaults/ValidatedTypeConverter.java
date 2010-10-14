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
package org.jowidgets.api.convert.impl.defaults;

import java.util.HashMap;
import java.util.Map;

import org.jowidgets.api.convert.IValidatedConverter;
import org.jowidgets.util.Assert;

public final class ValidatedTypeConverter {

	public static final IValidatedConverter<String> STRING_CONVERTER = new ValidatedStringConverter();
	public static final IValidatedConverter<Long> LONG_CONVERTER = new ValidatedLongConverter();
	public static final IValidatedConverter<Integer> INTEGER_CONVERTER = new ValidatedIntegerConverter();
	public static final IValidatedConverter<Short> SHORT_CONVERTER = new ValidatedShortConverter();

	private static final Map<Class<?>, IValidatedConverter<? extends Object>> CONVERTER_MAP = createConverterMap();

	private ValidatedTypeConverter() {}

	@SuppressWarnings("unchecked")
	public static <OBJECT_TYPE> IValidatedConverter<OBJECT_TYPE> getConverter(final Class<? extends OBJECT_TYPE> type) {
		Assert.paramNotNull(type, "type");
		final Object result = CONVERTER_MAP.get(type);
		return (IValidatedConverter<OBJECT_TYPE>) result;
	}

	private static Map<Class<?>, IValidatedConverter<? extends Object>> createConverterMap() {
		final Map<Class<?>, IValidatedConverter<? extends Object>> result = new HashMap<Class<?>, IValidatedConverter<? extends Object>>();
		result.put(String.class, STRING_CONVERTER);
		result.put(Long.class, LONG_CONVERTER);
		result.put(Long.class, INTEGER_CONVERTER);
		result.put(Short.class, SHORT_CONVERTER);
		return result;
	}

}
