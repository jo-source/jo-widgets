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

package org.jowidgets.util.reflection;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jowidgets.util.Assert;
import org.jowidgets.util.IIterationCallback;

/**
 * This class holds introspection / reflection result inside a cache for a faster access
 */
public final class IntrospectionCache {

	private static final Map<Class<?>, Map<String, PropertyDescriptor>> PROPERTIES_CACHE = new ConcurrentHashMap<Class<?>, Map<String, PropertyDescriptor>>();

	private static final Map<Class<?>, Map<String, PropertyDescriptor>> PROPERTIES_HIERARCHY_CACHE = new ConcurrentHashMap<Class<?>, Map<String, PropertyDescriptor>>();

	private IntrospectionCache() {}

	public static void clearCache() {
		PROPERTIES_CACHE.clear();
	}

	/**
	 * Gets a property decriptors for a specific type from the type hierarchy (including the given type)
	 * 
	 * @param type The type to get the property descriptor for
	 * @return The property descriptor as a unmodifiable collection, may be empty but never null
	 */
	public static Collection<PropertyDescriptor> getPropertyDescriptorsFromHierarchy(final Class<?> type) {
		Assert.paramNotNull(type, "type");
		final Collection<PropertyDescriptor> result = getPropertyDescriptorsFromHierarchyImpl(type).values();
		return Collections.unmodifiableCollection(result);
	}

	/**
	 * Gets the property descriptor for a specific type from the type hierarchy (including the given type)
	 * 
	 * @param type
	 * @param propertyName
	 * @return The property decriptor or null, if no such property exists
	 */
	public static PropertyDescriptor getPropertyDescriptorFromHierarchy(final Class<?> type, final String propertyName) {
		Assert.paramNotNull(type, "type");
		Assert.paramNotNull(propertyName, "propertyName");
		return getPropertyDescriptorsFromHierarchyImpl(type).get(propertyName);
	}

	/**
	 * Gets the write method for a defined property in the type hierarchy (including the given type)
	 * 
	 * @param type
	 * @param propertyName
	 * @return The write method, or null if the property does not exist or is readonly
	 */
	public static Method getWriteMethodFromHierarchy(final Class<?> type, final String propertyName) {
		final PropertyDescriptor propertyDescriptor = getPropertyDescriptorFromHierarchy(type, propertyName);
		if (propertyDescriptor != null) {
			return propertyDescriptor.getWriteMethod();
		}
		else {
			return null;
		}
	}

	/**
	 * Gets the read method for a defined property in the type hierarchy (including the given type)
	 * 
	 * @param type
	 * @param propertyName
	 * @return The read method, or null if the property does not exist or has no read method
	 */
	public static Method getReadMethodFromHierarchy(final Class<?> type, final String propertyName) {
		final PropertyDescriptor propertyDescriptor = getPropertyDescriptorFromHierarchy(type, propertyName);
		if (propertyDescriptor != null) {
			return propertyDescriptor.getReadMethod();
		}
		else {
			return null;
		}
	}

	/**
	 * Gets a property decriptors for a specific type
	 * 
	 * @param type The type to get the property descriptor for
	 * @return The property descriptor as a unmodifiable collection, may be empty but never null
	 */
	public static Collection<PropertyDescriptor> getPropertyDescriptors(final Class<?> type) {
		Assert.paramNotNull(type, "type");
		final Collection<PropertyDescriptor> result = getPropertyDescriptorsImpl(type).values();
		return Collections.unmodifiableCollection(result);
	}

	/**
	 * Gets the property descriptor for a specific type.
	 * 
	 * @param type
	 * @param propertyName
	 * @return The property decriptor or null, if no such property exists
	 */
	public static PropertyDescriptor getPropertyDescriptor(final Class<?> type, final String propertyName) {
		Assert.paramNotNull(type, "type");
		Assert.paramNotNull(propertyName, "propertyName");
		return getPropertyDescriptorsImpl(type).get(propertyName);
	}

	private static Map<String, PropertyDescriptor> getPropertyDescriptorsFromHierarchyImpl(final Class<?> type) {
		Map<String, PropertyDescriptor> properties = PROPERTIES_HIERARCHY_CACHE.get(type);
		if (properties == null) {
			properties = createPropertyDescriptorsFromHierarchy(type);
			PROPERTIES_HIERARCHY_CACHE.put(type, properties);
		}
		return properties;
	}

	private static Map<String, PropertyDescriptor> getPropertyDescriptorsImpl(final Class<?> type) {
		Map<String, PropertyDescriptor> properties = PROPERTIES_CACHE.get(type);
		if (properties == null) {
			properties = createPropertyDescriptors(type);
			PROPERTIES_CACHE.put(type, properties);
		}
		return properties;
	}

	private static Map<String, PropertyDescriptor> createPropertyDescriptorsFromHierarchy(final Class<?> type) {
		final Map<String, PropertyDescriptor> result = new HashMap<String, PropertyDescriptor>();
		final IIterationCallback<Class<?>> iterationCalback = new IIterationCallback<Class<?>>() {
			@Override
			public void next(final Class<?> iterationType) {
				result.putAll(getPropertyDescriptorsImpl(iterationType));
			}
		};
		ReflectionUtils.iterateHierarchy(type, iterationCalback);
		return result;
	}

	private static Map<String, PropertyDescriptor> createPropertyDescriptors(final Class<?> type) {
		final Map<String, PropertyDescriptor> result = new ConcurrentHashMap<String, PropertyDescriptor>();
		try {
			for (final PropertyDescriptor descriptor : Introspector.getBeanInfo(type).getPropertyDescriptors()) {
				result.put(descriptor.getName(), descriptor);
			}
		}
		catch (final Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}
}
