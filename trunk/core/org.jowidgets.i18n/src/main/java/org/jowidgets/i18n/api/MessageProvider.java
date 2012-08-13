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

import java.io.Serializable;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.jowidgets.i18n.api.LocaleLocal.IValueFactory;

public final class MessageProvider {

	private MessageProvider() {}

	/**
	 * Creates a new message provider for a given resource bundle name.
	 * 
	 * @param resourceBundleName The resource bundle name to get the message provider for, never empty
	 * 
	 * @return The new created message provider
	 */
	public static IMessageProvider create(final String resourceBundleName) {
		return new MessageProviderImpl(resourceBundleName);
	}

	private static final class MessageProviderImpl implements IMessageProvider, Serializable {

		private static final long serialVersionUID = -6035175048306441676L;

		private final String resourceBundleName;
		private final ILocaleLocal<LocalizedMessageProvider> messageProviders;

		private MessageProviderImpl(final String resourceBundleName) {
			Assert.paramNotNull(resourceBundleName, "resourceBundleName");
			this.resourceBundleName = resourceBundleName;
			this.messageProviders = LocaleLocal.create(new MessageProviderFactory());
		}

		@Override
		public String getString(final String key) {
			return messageProviders.get().getString(key);
		}

		@Override
		public IMessage getMessage(final String key) {
			return new MessageImpl(this, key);
		}

		private final class MessageProviderFactory implements IValueFactory<LocalizedMessageProvider>, Serializable {

			private static final long serialVersionUID = -1926402720150772928L;

			@Override
			public LocalizedMessageProvider create() {
				return new LocalizedMessageProvider(resourceBundleName);
			}

		}
	}

	private static final class LocalizedMessageProvider implements Serializable {

		private static final long serialVersionUID = 7013591522278151364L;

		private final String resourceBundleName;
		private transient ResourceBundle resourceBundle;

		private LocalizedMessageProvider(final String resourceBundleName) {
			this.resourceBundleName = resourceBundleName;
		}

		private String getString(final String key) {
			try {
				return getResourceBundle().getString(key);
			}
			catch (final MissingResourceException e) {
				return '!' + key + '!';
			}
		}

		private ResourceBundle getResourceBundle() {
			if (resourceBundle == null) {
				resourceBundle = ResourceBundle.getBundle(resourceBundleName, LocaleHolder.getUserLocale());
			}
			return resourceBundle;
		}

	}

	private static final class MessageImpl implements IMessage, Serializable {

		private static final long serialVersionUID = -8081080214256066849L;

		private final IMessageProvider messageProvider;
		private final String key;

		private MessageImpl(final IMessageProvider messageProvider, final String key) {
			Assert.paramNotNull(messageProvider, "messageProvider");
			Assert.paramNotEmpty(key, "key");

			this.messageProvider = messageProvider;
			this.key = key;
		}

		@Override
		public String get() {
			return messageProvider.getString(key);
		}

		@Override
		public String toString() {
			return "MessageImpl [key=" + key + "]";
		}

	}
}
