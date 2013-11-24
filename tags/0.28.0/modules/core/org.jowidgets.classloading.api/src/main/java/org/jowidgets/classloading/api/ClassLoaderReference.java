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

package org.jowidgets.classloading.api;

import org.jowidgets.util.Assert;
import org.jowidgets.util.ITypedKey;
import org.jowidgets.util.ITypedProperties;
import org.jowidgets.util.ITypedPropertiesBuilder;
import org.jowidgets.util.TypedProperties;

public final class ClassLoaderReference {

	private ClassLoaderReference() {}

	public static IClassLoaderReferenceBuilder builder() {
		return new ClassLoaderReferenceBuilderImpl();
	}

	public static IClassLoaderReference create(final ClassLoader classLoader) {
		Assert.paramNotNull(classLoader, "classLoader");
		return builder().setClassLoader(classLoader).build();
	}

	private static final class ClassLoaderReferenceBuilderImpl implements IClassLoaderReferenceBuilder {

		private final ITypedPropertiesBuilder typedPropertiesBuilder;
		private ClassLoader classLoader;

		private ClassLoaderReferenceBuilderImpl() {
			this.typedPropertiesBuilder = TypedProperties.builder();
		}

		@Override
		public IClassLoaderReferenceBuilder setClassLoader(final ClassLoader classLoader) {
			Assert.paramNotNull(classLoader, "classLoader");
			this.classLoader = classLoader;
			return this;
		}

		@Override
		public <PROPERTY_TYPE> IClassLoaderReferenceBuilder addProperty(
			final ITypedKey<PROPERTY_TYPE> key,
			final PROPERTY_TYPE value) {
			typedPropertiesBuilder.putProperty(key, value);
			return this;
		}

		@Override
		public IClassLoaderReference build() {
			return new ClassLoaderReferenceImpl(classLoader, typedPropertiesBuilder.build());
		}

	}

	private static final class ClassLoaderReferenceImpl implements IClassLoaderReference {

		private final ClassLoader classLoader;
		private final ITypedProperties properties;

		private ClassLoaderReferenceImpl(final ClassLoader classLoader, final ITypedProperties properties) {
			Assert.paramNotNull(classLoader, "classLoader");
			Assert.paramNotNull(properties, "properties");
			this.classLoader = classLoader;
			this.properties = properties;
		}

		@Override
		public ClassLoader getClassLoader() {
			return classLoader;
		}

		@Override
		public <PROPERTY_TYPE> PROPERTY_TYPE getProperty(final ITypedKey<PROPERTY_TYPE> key) {
			return properties.getProperty(key);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((classLoader == null) ? 0 : classLoader.hashCode());
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
			if (!(obj instanceof ClassLoaderReferenceImpl)) {
				return false;
			}
			final ClassLoaderReferenceImpl other = (ClassLoaderReferenceImpl) obj;
			if (classLoader == null) {
				if (other.classLoader != null) {
					return false;
				}
			}
			else if (!classLoader.equals(other.classLoader)) {
				return false;
			}
			if (properties == null) {
				if (other.properties != null) {
					return false;
				}
			}
			else if (!properties.equals(other.properties)) {
				return false;
			}
			return true;
		}

		@Override
		public String toString() {
			return "ClassLoaderReferenceImpl [classLoader=" + classLoader + ", properties=" + properties + "]";
		}

	}

}
