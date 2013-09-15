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

package org.jowidgets.util.reflection;

import java.lang.reflect.Method;

import org.jowidgets.util.Assert;

public final class BeanUtils {

	private BeanUtils() {}

	public static void setProperty(final Object bean, final String propertyName, final Object value) {
		Assert.paramNotNull(bean, "bean");
		Assert.paramNotNull(propertyName, "propertyName");
		final Method writeMethod = IntrospectionCache.getWriteMethodFromHierarchy(bean.getClass(), propertyName);
		if (writeMethod != null) {
			try {
				writeMethod.invoke(bean, value);
			}
			catch (final Exception e) {
				throw new RuntimeException(e);
			}
		}
		else {
			throw new IllegalArgumentException("No write method found for property '"
				+ propertyName
				+ "' on bean '"
				+ bean
				+ "'.");
		}
	}

	public static Object getProperty(final Object bean, final String propertyName) {
		Assert.paramNotNull(bean, "bean");
		Assert.paramNotNull(propertyName, "propertyName");
		final Method readMethod = IntrospectionCache.getReadMethodFromHierarchy(bean.getClass(), propertyName);
		if (readMethod != null) {
			try {
				return readMethod.invoke(bean);
			}
			catch (final Exception e) {
				throw new RuntimeException(e);
			}
		}
		else {
			throw new IllegalArgumentException("No read method found for property '" + propertyName + "' on bean '" + bean + "'.");
		}
	}
}
