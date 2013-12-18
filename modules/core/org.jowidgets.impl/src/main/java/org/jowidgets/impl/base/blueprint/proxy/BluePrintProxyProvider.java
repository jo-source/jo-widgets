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
package org.jowidgets.impl.base.blueprint.proxy;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import org.jowidgets.api.widgets.blueprint.convenience.ISetupBuilderConvenienceRegistry;
import org.jowidgets.api.widgets.blueprint.defaults.IDefaultsInitializerRegistry;
import org.jowidgets.classloading.api.SharedClassLoader;
import org.jowidgets.common.widgets.builder.ISetupBuilder;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.impl.base.blueprint.proxy.internal.BluePrintProxyInvocationHandler;
import org.jowidgets.util.Assert;

public class BluePrintProxyProvider<BLUE_PRINT_TYPE extends ISetupBuilder<?>> {

	private static final Map<ClassLoader, BluePrintProxyClassLoader> BLUE_PRINT_CLASS_LOADERS = new HashMap<ClassLoader, BluePrintProxyClassLoader>();

	private final BLUE_PRINT_TYPE proxy;

	@SuppressWarnings({"unchecked", "rawtypes"})
	public BluePrintProxyProvider(
		final Class<? extends IWidgetDescriptor> bluePrintType,
		final ISetupBuilderConvenienceRegistry convenienceRegistry,
		final IDefaultsInitializerRegistry defaultsRegistry) {

		Assert.paramNotNull(bluePrintType, "bluePrintType");

		final BluePrintProxyInvocationHandler invocationHandler = new BluePrintProxyInvocationHandler();

		proxy = (BLUE_PRINT_TYPE) Proxy.newProxyInstance(
				getBluePrintClassLoader(bluePrintType.getClassLoader()),
				new Class[] {bluePrintType},
				invocationHandler);

		invocationHandler.initialize(proxy, bluePrintType, convenienceRegistry, defaultsRegistry);

	}

	public BLUE_PRINT_TYPE getBluePrint() {
		return proxy;
	}

	private static BluePrintProxyClassLoader getBluePrintClassLoader(final ClassLoader original) {
		BluePrintProxyClassLoader result = BLUE_PRINT_CLASS_LOADERS.get(original);
		if (result == null) {
			result = new BluePrintProxyClassLoader(original);
			BLUE_PRINT_CLASS_LOADERS.put(original, result);
		}
		return result;
	}

	private static final class BluePrintProxyClassLoader extends ClassLoader {

		private final ClassLoader original;

		private BluePrintProxyClassLoader(final ClassLoader classLoader) {
			this.original = classLoader;
		}

		@Override
		protected Class<?> findClass(final String name) throws ClassNotFoundException {
			try {
				return original.loadClass(name);
			}
			catch (final Exception e) {
				//Nothing to do, this loader may not know the class
			}
			return SharedClassLoader.getCompositeClassLoader().loadClass(name);
		}

	}

}
