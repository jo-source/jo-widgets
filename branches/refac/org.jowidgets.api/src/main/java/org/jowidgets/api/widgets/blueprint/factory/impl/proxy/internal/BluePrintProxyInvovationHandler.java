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
package org.jowidgets.api.widgets.blueprint.factory.impl.proxy.internal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.api.widgets.blueprint.base.IBaseBluePrint;
import org.jowidgets.api.widgets.blueprint.convenience.anotations.ConvenienceMethods;
import org.jowidgets.api.widgets.blueprint.convenience.anotations.IConvenienceMethods;
import org.jowidgets.api.widgets.blueprint.defaults.anotations.DefaultsInitializer;
import org.jowidgets.api.widgets.blueprint.defaults.anotations.IDefaultInitializer;
import org.jowidgets.api.widgets.descriptor.base.IBaseWidgetDescriptor;

/**
 * Achtung EXPERIMENTELL, soll so nicht bleiben ;-)
 */
public class BluePrintProxyInvovationHandler implements InvocationHandler {

	private static final long serialVersionUID = -983268051877912134L;

	private final Map<String, Object> fields;
	private final Map<MethodKey, IConvenienceMethods<IBaseBluePrint<?>>> convenienceMethodsMap;

	private Class<? extends IBaseBluePrint<IBaseBluePrint<?>>> bluePrintType;
	private Class<? extends IBaseWidgetDescriptor<? extends IWidget>> widgetType;

	public BluePrintProxyInvovationHandler() {
		this.fields = new HashMap<String, Object>();
		this.convenienceMethodsMap = new HashMap<MethodKey, IConvenienceMethods<IBaseBluePrint<?>>>();
	}

	public void initialize(
		final IBaseBluePrint<?> proxy,
		final Class<? extends IBaseBluePrint<IBaseBluePrint<?>>> bluePrintType,
		final Class<? extends IBaseWidgetDescriptor<? extends IWidget>> widgetType) {

		this.widgetType = widgetType;
		this.bluePrintType = bluePrintType;
		initialize(proxy, bluePrintType);

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
			final IConvenienceMethods<IBaseBluePrint<?>> impl = convenienceMethodsMap.get(methodKey);
			impl.setBluePrint((IBaseBluePrint<?>) proxy);
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
			System.out.println("'" + method.getName() + "' not known.");
			// return method.invoke(proxy, args);
			return proxy;
		}
	}

	@SuppressWarnings("unchecked")
	private void initialize(final IBaseBluePrint<?> proxy, final Class<? extends IBaseBluePrint<?>> bluePrintType) {

		for (final Class<?> superInterface : bluePrintType.getInterfaces()) {
			initialize(proxy, (Class<? extends IBaseBluePrint<?>>) superInterface);
		}
		final IDefaultInitializer<IBaseBluePrint<?>> defaultInitializer = getDefaultInitializer(bluePrintType);
		if (defaultInitializer != null) {
			defaultInitializer.initialize(proxy);
		}

		final IConvenienceMethods<IBaseBluePrint<?>> convenienceMethods = getConvenienceMethods(bluePrintType);
		if (convenienceMethods != null) {
			for (final Method method : convenienceMethods.getClass().getDeclaredMethods()) {
				final MethodKey methodKey = new MethodKey(method);

				// if(convenienceMethodsMap.get(methodKey) != null) {
				// System.out.println("'"
				// + method.getName()
				// + "' from '"
				// + convenienceMethodsMap.get(methodKey).getClass().getName()
				// + "' ovveriden by '"
				// + bluePrintType.getName());
				// }
				convenienceMethodsMap.put(methodKey, convenienceMethods);
			}
		}

	}

	@SuppressWarnings("unchecked")
	private IConvenienceMethods<IBaseBluePrint<?>> getConvenienceMethods(final Class<?> clazz) {
		final ConvenienceMethods convenienceMethods = clazz.getAnnotation(ConvenienceMethods.class);
		if (convenienceMethods != null) {
			final Class<? extends IConvenienceMethods<?>> convenienceMethodsClass = convenienceMethods.value();
			if (convenienceMethodsClass != null) {
				try {
					return (IConvenienceMethods<IBaseBluePrint<?>>) convenienceMethodsClass.newInstance();
				}
				catch (final Exception e) {
					throw new RuntimeException("Exception creating instance of '" + convenienceMethodsClass.getName() + "'");
				}
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private IDefaultInitializer<IBaseBluePrint<?>> getDefaultInitializer(final Class<?> clazz) {
		final DefaultsInitializer defaultsInitializer = clazz.getAnnotation(DefaultsInitializer.class);
		if (defaultsInitializer != null) {
			final Class<? extends IDefaultInitializer<?>> initializerClass = defaultsInitializer.value();
			if (initializerClass != null) {
				try {
					return (IDefaultInitializer<IBaseBluePrint<?>>) initializerClass.newInstance();
				}
				catch (final Exception e) {
					throw new RuntimeException("Exception creating instance of '" + initializerClass.getName() + "'");
				}
			}
		}
		return null;
	}

}
