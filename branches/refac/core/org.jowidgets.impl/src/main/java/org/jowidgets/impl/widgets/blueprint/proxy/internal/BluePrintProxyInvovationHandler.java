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
package org.jowidgets.impl.widgets.blueprint.proxy.internal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.api.widgets.blueprint.convenience.ISetupBuilderConvenience;
import org.jowidgets.api.widgets.blueprint.convenience.ISetupBuilderConvenienceRegistry;
import org.jowidgets.api.widgets.blueprint.convenience.anotations.ConvenienceMethods;
import org.jowidgets.api.widgets.blueprint.defaults.IDefaultInitializer;
import org.jowidgets.api.widgets.blueprint.defaults.IDefaultsInitializerRegistry;
import org.jowidgets.api.widgets.blueprint.defaults.anotations.DefaultsInitializer;
import org.jowidgets.api.widgets.builder.ISetupBuilder;
import org.jowidgets.spi.widgets.descriptor.setup.IWidgetSetupSpi;

/**
 * Achtung EXPERIMENTELL, soll so nicht bleiben ;-)
 */
public class BluePrintProxyInvovationHandler implements InvocationHandler {

	private static final long serialVersionUID = -983268051877912134L;

	private final Map<String, Object> fields;
	private final Map<MethodKey, ISetupBuilderConvenience<ISetupBuilder<?>>> convenienceMethodsMap;

	private Class<? extends ISetupBuilder<ISetupBuilder<?>>> bluePrintType;
	private Class<? extends IWidgetSetupSpi<? extends IWidget>> widgetType;

	public BluePrintProxyInvovationHandler() {
		this.fields = new HashMap<String, Object>();
		this.convenienceMethodsMap = new HashMap<MethodKey, ISetupBuilderConvenience<ISetupBuilder<?>>>();
	}

	public void initialize(
		final ISetupBuilder<?> proxy,
		final Class<? extends ISetupBuilder<ISetupBuilder<?>>> bluePrintType,
		final Class<? extends IWidgetSetupSpi<? extends IWidget>> widgetType,
		final ISetupBuilderConvenienceRegistry convenienceRegistry,
		final IDefaultsInitializerRegistry defaultsRegistry) {

		this.widgetType = widgetType;
		this.bluePrintType = bluePrintType;
		initialize(proxy, bluePrintType, convenienceRegistry, defaultsRegistry);

	}

	@Override
	public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
		final MethodKey methodKey = new MethodKey(method);
		if (method.getName().equals("toString")) {
			final StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("BluePrintType: " + bluePrintType.getName() + "\n");
			stringBuilder.append("WidgetType: " + widgetType.getName() + "\n");
			stringBuilder.append("Fields: \n");
			for (final Entry<String, Object> entry : fields.entrySet()) {
				stringBuilder.append("Property: " + entry.getKey() + " \nValue: " + entry.getValue() + "\n");
			}
			return stringBuilder.toString();
		}
		else if (method.getName().equals("getDescriptorInterface")) {
			return widgetType;
		}
		else if (method.getName().equals("setDescriptor")) {
			final Object argument = args[0];

			if (argument != null) {
				for (final Method method2 : argument.getClass().getMethods()) {
					if (method2.getParameterTypes().length == 0) {
						if (method2.getName().contains("hashCode")) {
							continue;
						}
						else if (method2.getName().contains("isProxyClass")) {
							continue;
						}
						else if (method2.getName().startsWith("get")) {
							final Object value = method2.invoke(argument, (Object[]) null);
							fields.put(method2.getName().substring(3), value);
						}
						else if (method2.getName().startsWith("has")) {
							final Object value = method2.invoke(argument, (Object[]) null);
							fields.put(method2.getName().substring(3), value);
						}
						else if (method2.getName().startsWith("is")) {
							final Object value = method2.invoke(argument, (Object[]) null);
							fields.put(method2.getName().substring(2), value);
						}
					}
				}
				return proxy;
			}
			else {
				throw new IllegalArgumentException("argument of setDescriptor must not be null");
			}

		}
		else if (convenienceMethodsMap.get(methodKey) != null) {
			final ISetupBuilderConvenience<ISetupBuilder<?>> impl = convenienceMethodsMap.get(methodKey);
			impl.setBuilder((ISetupBuilder<?>) proxy);
			method.invoke(impl, args);
			return proxy;
		}
		else if (method.getName().startsWith("get")) {
			return fields.get(method.getName().substring(3));
		}
		else if (method.getName().startsWith("has")) {
			return fields.get(method.getName().substring(3));
		}
		else if (method.getName().startsWith("is")) {
			return fields.get(method.getName().substring(2));
		}
		else if (method.getName().startsWith("set")) {
			fields.put(method.getName().substring(3), args[0]);
			return proxy;
		}
		else {
			throw new IllegalStateException("'" + method.getName() + "' not known.");
		}
	}

	@SuppressWarnings("unchecked")
	private void initialize(
		final ISetupBuilder<?> proxy,
		final Class<? extends ISetupBuilder<?>> bluePrintType,
		final ISetupBuilderConvenienceRegistry convenienceRegistry,
		final IDefaultsInitializerRegistry defaultsRegistry) {

		for (final Class<?> superInterface : bluePrintType.getInterfaces()) {
			initialize(proxy, (Class<? extends ISetupBuilder<?>>) superInterface, convenienceRegistry, defaultsRegistry);
		}
		final IDefaultInitializer<ISetupBuilder<?>> defaultInitializer = getDefaultInitializer(bluePrintType);
		if (defaultInitializer != null) {
			defaultInitializer.initialize(proxy);
		}
		for (final IDefaultInitializer<ISetupBuilder<?>> registeredInitializer : defaultsRegistry.getRegistered(bluePrintType)) {
			registeredInitializer.initialize(proxy);
		}

		final ISetupBuilderConvenience<ISetupBuilder<?>> convenienceMethods = getConvenienceMethods(bluePrintType);
		if (convenienceMethods != null) {
			for (final Method method : convenienceMethods.getClass().getDeclaredMethods()) {
				final MethodKey methodKey = new MethodKey(method);
				convenienceMethodsMap.put(methodKey, convenienceMethods);
			}
		}
		for (final ISetupBuilderConvenience<ISetupBuilder<?>> registeredMethods : convenienceRegistry.getRegistered(bluePrintType)) {
			for (final Method method : registeredMethods.getClass().getDeclaredMethods()) {
				final MethodKey methodKey = new MethodKey(method);
				convenienceMethodsMap.put(methodKey, registeredMethods);
			}
		}

	}

	@SuppressWarnings("unchecked")
	private ISetupBuilderConvenience<ISetupBuilder<?>> getConvenienceMethods(final Class<?> clazz) {
		final ConvenienceMethods convenienceMethods = clazz.getAnnotation(ConvenienceMethods.class);
		if (convenienceMethods != null) {
			final Class<? extends ISetupBuilderConvenience<?>> convenienceMethodsClass = convenienceMethods.value();
			if (convenienceMethodsClass != null) {
				try {
					return (ISetupBuilderConvenience<ISetupBuilder<?>>) convenienceMethodsClass.newInstance();
				}
				catch (final Exception e) {
					throw new RuntimeException("Exception creating instance of '" + convenienceMethodsClass.getName() + "'");
				}
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private IDefaultInitializer<ISetupBuilder<?>> getDefaultInitializer(final Class<?> clazz) {
		final DefaultsInitializer defaultsInitializer = clazz.getAnnotation(DefaultsInitializer.class);
		if (defaultsInitializer != null) {
			final Class<? extends IDefaultInitializer<?>> initializerClass = defaultsInitializer.value();
			if (initializerClass != null) {
				try {
					return (IDefaultInitializer<ISetupBuilder<?>>) initializerClass.newInstance();
				}
				catch (final Exception e) {
					throw new RuntimeException("Exception creating instance of '" + initializerClass.getName() + "'");
				}
			}
		}
		return null;
	}

}
