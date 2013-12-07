/*
 * Copyright (c) 2013, grossmann
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

package org.jowidgets.util;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public final class TypedProperties {

	private TypedProperties() {}

	public static ITypedPropertiesBuilder builder() {
		return new TypedPropertiesBuilderImpl();
	}

	private static final class TypedPropertiesBuilderImpl implements ITypedPropertiesBuilder {

		private final Map<ITypedKey<?>, Object> properties;

		private TypedPropertiesBuilderImpl() {
			this.properties = new LinkedHashMap<ITypedKey<?>, Object>();
		}

		@Override
		public <PROPERTY_TYPE> ITypedPropertiesBuilder putProperty(final ITypedKey<PROPERTY_TYPE> key, final PROPERTY_TYPE value) {
			Assert.paramNotNull(key, "key");
			properties.put(key, value);
			return this;
		}

		@Override
		public ITypedProperties build() {
			return new TypedPropertiesImpl(properties);
		}

	}

	private static final class TypedPropertiesImpl implements ITypedProperties {

		private final Map<ITypedKey<?>, Object> properties;

		private TypedPropertiesImpl(final Map<ITypedKey<?>, Object> properties) {
			this.properties = new LinkedHashMap<ITypedKey<?>, Object>();
			this.properties.putAll(properties);
		}

		@SuppressWarnings("unchecked")
		@Override
		public <PROPERTY_TYPE> PROPERTY_TYPE getProperty(final ITypedKey<PROPERTY_TYPE> key) {
			Assert.paramNotNull(properties, "properties");
			return (PROPERTY_TYPE) properties.get(key);
		}

		@Override
		public Collection<ITypedKey<?>> getKeys() {
			return properties.keySet();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((properties == null) ? 0 : properties.hashCode());
			return result;
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof ITypedProperties)) {
				return false;
			}
			else {
				final ITypedProperties other = (ITypedProperties) obj;
				final Collection<ITypedKey<?>> keys = getKeys();
				if (keys.size() != other.getKeys().size()) {
					return false;
				}
				else {
					for (final ITypedKey<?> key : keys) {
						if (!NullCompatibleComparison.equals(getProperty(key), other.getProperty(key))) {
							return false;
						}
					}
					return true;
				}
			}
		}

		@Override
		public String toString() {
			return "TypedPropertiesImpl [properties=" + properties + "]";
		}

	}

}
