/*
 * Copyright (c) 2012, grossmann
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

package org.jowidgets.i18n.api;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class LocaleLocal {

	private LocaleLocal() {}

	public interface IValueFactory<VALUE_TYPE> {
		VALUE_TYPE create();
	}

	public static <VALUE_TYPE> ILocaleLocal<VALUE_TYPE> create() {
		return create(null);
	}

	/**
	 * Creates a new LocaleLocal.
	 * 
	 * @param factory The factory that will be used to create new values for each different set locale
	 * 
	 * @return The new created LocaleLocal object
	 */
	public static <VALUE_TYPE> ILocaleLocal<VALUE_TYPE> create(final IValueFactory<VALUE_TYPE> factory) {
		return new LocaleLocalImpl<VALUE_TYPE>(factory);
	}

	private static final class LocaleLocalImpl<VALUE_TYPE> implements ILocaleLocal<VALUE_TYPE> {

		private final IValueFactory<VALUE_TYPE> valueFactory;
		private final Map<Locale, VALUE_TYPE> values;

		private LocaleLocalImpl(final IValueFactory<VALUE_TYPE> valueFactory) {
			this.valueFactory = valueFactory;
			this.values = new HashMap<Locale, VALUE_TYPE>();
		}

		@Override
		public VALUE_TYPE get() {
			final Locale locale = LocaleHolder.getUserLocale();
			VALUE_TYPE result = values.get(locale);
			if (result == null && valueFactory != null) {
				result = valueFactory.create();
				values.put(locale, result);
			}
			return result;
		}

		@Override
		public void setValue(final VALUE_TYPE value) {
			values.put(LocaleHolder.getUserLocale(), value);
		}

	}

}
