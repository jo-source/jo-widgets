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

import java.util.Locale;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;

public class I18nTests {

	@Test
	public void localeProviderTest() {
		//if no locale provider is set, the default locale will be returned
		Assert.assertEquals(Locale.getDefault(), LocaleProvider.getUserLocale());

		//the mutable locale provider has the default locale as default
		final MutableLocaleProvider localeProvider = new MutableLocaleProvider();
		LocaleProvider.setInstance(localeProvider);
		Assert.assertEquals(Locale.getDefault(), LocaleProvider.getUserLocale());

		//test changing to canada
		localeProvider.setUserLocale(Locale.CANADA);
		Assert.assertEquals(Locale.CANADA, LocaleProvider.getUserLocale());

		//test changing to german
		localeProvider.setUserLocale(Locale.GERMAN);
		Assert.assertEquals(Locale.GERMAN, LocaleProvider.getUserLocale());

		//test resetting the locale provider
		LocaleProvider.setInstance(null);
		Assert.assertEquals(Locale.getDefault(), LocaleProvider.getUserLocale());
	}

	@Test
	public void messageProviderTest() {
		final MutableLocaleProvider localeProvider = new MutableLocaleProvider();
		LocaleProvider.setInstance(localeProvider);

		localeProvider.setUserLocale(Locale.ENGLISH);
		Assert.assertEquals("message1", Messages.getString("I18nTests.message1"));
		Assert.assertEquals("message2", Messages.getString("I18nTests.message2"));

		localeProvider.setUserLocale(Locale.GERMAN);
		Assert.assertEquals("Meldung1", Messages.getString("I18nTests.message1"));
		Assert.assertEquals("Meldung2", Messages.getString("I18nTests.message2"));
	}

	@Test
	public void messageTest() {
		final MutableLocaleProvider localeProvider = new MutableLocaleProvider();
		LocaleProvider.setInstance(localeProvider);

		final IMessage message1 = Messages.getMessage("I18nTests.message1");
		final IMessage message2 = Messages.getMessage("I18nTests.message2");

		localeProvider.setUserLocale(Locale.ENGLISH);
		Assert.assertEquals("message1", message1.get());
		Assert.assertEquals("message2", message2.get());

		localeProvider.setUserLocale(Locale.GERMAN);
		Assert.assertEquals("Meldung1", message1.get());
		Assert.assertEquals("Meldung2", message2.get());
	}

	@After
	public void tearDown() {
		LocaleProvider.setInstance(null);
	}

	private static final class MutableLocaleProvider implements ILocaleProvider {

		private Locale userLocale;

		private MutableLocaleProvider() {
			this.userLocale = Locale.getDefault();
		}

		@Override
		public Locale getUserLocale() {
			return userLocale;
		}

		private void setUserLocale(final Locale userLocale) {
			this.userLocale = userLocale;
		}

	}
}
