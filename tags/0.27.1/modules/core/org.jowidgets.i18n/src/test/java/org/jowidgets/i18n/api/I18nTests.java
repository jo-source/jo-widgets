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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Locale;

import junit.framework.Assert;

import org.jowidgets.i18n.api.LocaleLocal.IValueFactory;
import org.junit.After;
import org.junit.Test;

public class I18nTests {

	@Test
	public void localeHolderTest() {
		//if no locale provider is set, the default locale will be returned
		Assert.assertEquals(Locale.getDefault(), LocaleHolder.getUserLocale());

		//test changing to canada
		LocaleHolder.setUserLocale(Locale.CANADA);
		Assert.assertEquals(Locale.CANADA, LocaleHolder.getUserLocale());

		//test changing to german
		LocaleHolder.setUserLocale(Locale.GERMAN);
		Assert.assertEquals(Locale.GERMAN, LocaleHolder.getUserLocale());

		//test resetting the locale provider
		LocaleHolder.clearUserLocale();
		Assert.assertEquals(Locale.getDefault(), LocaleHolder.getUserLocale());
	}

	@Test
	public void customLocaleHolderTest() {
		final LocaleHolderMock localeHolderMock = new LocaleHolderMock();
		LocaleHolder.setInstance(localeHolderMock);

		//test changing to canada
		LocaleHolder.setUserLocale(Locale.CANADA);
		Assert.assertEquals(Locale.CANADA, localeHolderMock.getUserLocale());
		Assert.assertEquals(Locale.CANADA, LocaleHolder.getUserLocale());

		//test changing to german
		LocaleHolder.setUserLocale(Locale.GERMAN);
		Assert.assertEquals(Locale.GERMAN, localeHolderMock.getUserLocale());
		Assert.assertEquals(Locale.GERMAN, LocaleHolder.getUserLocale());

		//test reseting the locale provider
		LocaleHolder.clearUserLocale();
		Assert.assertEquals(Locale.getDefault(), localeHolderMock.getUserLocale());
		Assert.assertEquals(Locale.getDefault(), LocaleHolder.getUserLocale());

		//test changing to canada
		localeHolderMock.setUserLocale(Locale.CANADA);
		Assert.assertEquals(Locale.CANADA, localeHolderMock.getUserLocale());
		Assert.assertEquals(Locale.CANADA, LocaleHolder.getUserLocale());

		//test changing to german
		localeHolderMock.setUserLocale(Locale.GERMAN);
		Assert.assertEquals(Locale.GERMAN, localeHolderMock.getUserLocale());
		Assert.assertEquals(Locale.GERMAN, LocaleHolder.getUserLocale());

		//test reseting the locale provider
		localeHolderMock.clearUserLocale();
		Assert.assertEquals(Locale.getDefault(), localeHolderMock.getUserLocale());
		Assert.assertEquals(Locale.getDefault(), LocaleHolder.getUserLocale());
	}

	private static final class LocaleHolderMock implements ILocaleHolder {

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

	@Test
	public void messageProviderTest() {
		LocaleHolder.setUserLocale(Locale.ENGLISH);
		Assert.assertEquals("message1", Messages.getString("I18nTests.message1"));
		Assert.assertEquals("message2", Messages.getString("I18nTests.message2"));

		LocaleHolder.setUserLocale(Locale.GERMAN);
		Assert.assertEquals("Meldung1", Messages.getString("I18nTests.message1"));
		Assert.assertEquals("Meldung2", Messages.getString("I18nTests.message2"));
	}

	@Test
	public void messageTest() {
		final IMessage message1 = Messages.getMessage("I18nTests.message1");
		final IMessage message2 = Messages.getMessage("I18nTests.message2");

		LocaleHolder.setUserLocale(Locale.ENGLISH);
		Assert.assertEquals("message1", message1.get());
		Assert.assertEquals("message2", message2.get());

		LocaleHolder.setUserLocale(Locale.GERMAN);
		Assert.assertEquals("Meldung1", message1.get());
		Assert.assertEquals("Meldung2", message2.get());
	}

	@Test
	public void serializationTest() {
		final IMessage message1 = Messages.getMessage("I18nTests.message1");
		final IMessage message2 = Messages.getMessage("I18nTests.message2");

		try {
			final ByteArrayOutputStream os = new ByteArrayOutputStream();
			final ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(message1);
			oos.writeObject(message2);

			final ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
			final ObjectInputStream ois = new ObjectInputStream(is);
			final IMessage message1FromStream = (IMessage) ois.readObject();
			final IMessage message2FromStream = (IMessage) ois.readObject();

			os.close();
			oos.close();
			is.close();
			ois.close();

			LocaleHolder.setUserLocale(Locale.ENGLISH);
			Assert.assertEquals("message1", message1FromStream.get());
			Assert.assertEquals("message2", message2FromStream.get());

			LocaleHolder.setUserLocale(Locale.GERMAN);
			Assert.assertEquals("Meldung1", message1FromStream.get());
			Assert.assertEquals("Meldung2", message2FromStream.get());
		}
		catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void localeLocalTest() {
		//create to icons (stop and go)
		final ILocaleLocal<IconPath> stopIcon = createLocaleLocal("stop");
		final ILocaleLocal<IconPath> goIcon = createLocaleLocal("go");

		//check if the default path will be used
		Assert.assertEquals("icons/default/stop", stopIcon.get().getIconPath());
		Assert.assertEquals("icons/default/go", goIcon.get().getIconPath());

		//set the locale to german and check if the german path will be used
		LocaleHolder.setUserLocale(Locale.GERMAN);
		Assert.assertEquals("icons/german/stop", stopIcon.get().getIconPath());
		Assert.assertEquals("icons/german/go", goIcon.get().getIconPath());

		//change the path for the currently set locale (german)
		stopIcon.setValue(new IconPath("stop2"));
		Assert.assertEquals("icons/german/stop2", stopIcon.get().getIconPath());

		//change the locale back to the default
		LocaleHolder.clearUserLocale();
		Assert.assertEquals("icons/default/stop", stopIcon.get().getIconPath());

		//change back the locale to german, check that the value set before was not lost
		LocaleHolder.setUserLocale(Locale.GERMAN);
		Assert.assertEquals("icons/german/stop2", stopIcon.get().getIconPath());
	}

	private static ILocaleLocal<IconPath> createLocaleLocal(final String pathSuffix) {
		return LocaleLocal.create(new IValueFactory<IconPath>() {
			@Override
			public IconPath create() {
				return new IconPath(pathSuffix);
			}
		});
	}

	private static final class IconPath {

		private final String iconPathPrefix;
		private final String iconPathSuffix;

		private IconPath(final String iconPathSuffix) {
			if (LocaleHolder.getUserLocale().equals(Locale.GERMAN)) {
				this.iconPathPrefix = "icons/german/";
			}
			else {
				this.iconPathPrefix = "icons/default/";
			}
			this.iconPathSuffix = iconPathSuffix;
		}

		private String getIconPath() {
			return iconPathPrefix + iconPathSuffix;
		}

	}

	@After
	public void tearDown() {
		LocaleHolder.setInstance(null);
	}

}
