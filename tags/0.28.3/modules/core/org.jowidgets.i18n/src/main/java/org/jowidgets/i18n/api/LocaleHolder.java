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

import java.util.Iterator;
import java.util.Locale;
import java.util.ServiceLoader;

import org.jowidgets.classloading.api.SharedClassLoader;

public final class LocaleHolder {

	private static ILocaleHolder instance;

	private LocaleHolder() {}

	/**
	 * The the instance of the value holder. The previously used instance will be overridden.
	 * 
	 * @param localeProvider The locale provider to set
	 */
	public static void setInstance(final ILocaleHolder localeProvider) {
		instance = localeProvider;
	}

	/**
	 * @return The user locale of the currently set LocaleHolder
	 */
	public static Locale getUserLocale() {
		return getInstance().getUserLocale();
	}

	/**
	 * Sets the user locale on the currently set LocaleHolder
	 * 
	 * @param userLocale The locale to set, may be null
	 */
	public static void setUserLocale(final Locale userLocale) {
		getInstance().setUserLocale(userLocale);
	}

	/**
	 * Clears the user locale on the currently set LocaleHolder
	 */
	public static void clearUserLocale() {
		getInstance().clearUserLocale();
	}

	/**
	 * Gets the instance of the currently set value holder. If no value holder was injected before, an
	 * default implementation will be returned
	 * 
	 * @return The currently set value holder
	 */
	public static synchronized ILocaleHolder getInstance() {
		if (instance == null) {
			final ServiceLoader<ILocaleHolder> loader = ServiceLoader.load(
					ILocaleHolder.class,
					SharedClassLoader.getCompositeClassLoader());
			final Iterator<ILocaleHolder> iterator = loader.iterator();

			if (!iterator.hasNext()) {
				instance = new DefaultUserLocaleProvider();
			}
			else {
				instance = iterator.next();
				if (iterator.hasNext()) {
					throw new IllegalStateException("More than one implementation found for '"
						+ ILocaleHolder.class.getName()
						+ "'");
				}
			}

		}
		return instance;
	}

	private static final class DefaultUserLocaleProvider implements ILocaleHolder {

		private Locale userLocale;

		@Override
		public Locale getUserLocale() {
			if (userLocale != null) {
				return userLocale;
			}
			else {
				return Locale.getDefault();
			}
		}

		@Override
		public void setUserLocale(final Locale userLocale) {
			this.userLocale = userLocale;
		}

		@Override
		public void clearUserLocale() {
			this.userLocale = null;
		}

	}

}
