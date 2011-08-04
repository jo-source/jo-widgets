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
package org.jowidgets.impl.base.blueprint.proxy.internal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jowidgets.api.widgets.blueprint.convenience.ISetupBuilderConvenience;
import org.jowidgets.api.widgets.blueprint.convenience.ISetupBuilderConvenienceRegistry;
import org.jowidgets.api.widgets.blueprint.convenience.anotations.ConvenienceMethods;
import org.jowidgets.api.widgets.blueprint.defaults.IDefaultInitializer;
import org.jowidgets.api.widgets.blueprint.defaults.IDefaultsInitializerRegistry;
import org.jowidgets.api.widgets.blueprint.defaults.anotations.DefaultsInitializer;
import org.jowidgets.common.widgets.builder.ISetupBuilder;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.descriptor.setup.mandatory.Mandatory;
import org.jowidgets.common.widgets.descriptor.setup.mandatory.MandatoryCheckResult;

/**
 * Achtung EXPERIMENTELL, soll so nicht bleiben ;-)
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class BluePrintProxyInvocationHandler implements InvocationHandler {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = -983268051877912134L;

	private final Map<String, Object> fields;
	private final Map<MethodKey, ISetupBuilderConvenience<ISetupBuilder<?>>> convenienceMethodsMap;

	private Class<? extends IWidgetDescriptor> bluePrintType;
	private Class<? extends IWidgetDescriptor> widgetDescrType;

	private final List<String> mandatoryFields;

	public BluePrintProxyInvocationHandler() {
		this.fields = new HashMap<String, Object>();
		this.mandatoryFields = new ArrayList<String>();
		this.convenienceMethodsMap = new HashMap<MethodKey, ISetupBuilderConvenience<ISetupBuilder<?>>>();
	}

	public void initialize(
		final ISetupBuilder<?> proxy,
		final Class<? extends IWidgetDescriptor> bluePrintType,
		final ISetupBuilderConvenienceRegistry convenienceRegistry,
		final IDefaultsInitializerRegistry defaultsRegistry) {

		this.widgetDescrType = (Class<? extends IWidgetDescriptor>) getDescriptorInterface(bluePrintType);
		if (this.widgetDescrType == null) {
			throw new IllegalArgumentException("For the blueprint-type '"
				+ bluePrintType
				+ "' there are either multiple desriptors defined or there is no descriptor defined!");
		}
		this.bluePrintType = bluePrintType;

		final List<Class<?>> visitedInterfaces = new ArrayList<Class<?>>();
		initialize(
				proxy,
				(Class<? extends ISetupBuilder<ISetupBuilder<?>>>) bluePrintType,
				convenienceRegistry,
				defaultsRegistry,
				visitedInterfaces);
	}

	@Override
	public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
		try {
			return doInvoke(proxy, method, args);
		}
		catch (final Exception e) {
			throw new RuntimeException("Error while invoking method '" + method.getName() + "' on '" + proxy + "'", e);
		}
	}

	public Object doInvoke(final Object proxy, final Method method, final Object[] args) throws Exception {
		final MethodKey methodKey = new MethodKey(method);
		if (method.getName().equals("toString")) {
			final StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("BluePrintType: " + bluePrintType.getName() + "\n");
			stringBuilder.append("WidgetType: " + widgetDescrType.getName() + "\n");
			stringBuilder.append("Fields: \n");
			for (final Entry<String, Object> entry : fields.entrySet()) {
				stringBuilder.append("Property: " + entry.getKey() + " \nValue: " + entry.getValue() + "\n");
			}
			return stringBuilder.toString();
		}
		else if (method.getName().equals("getDescriptorInterface")) {
			return widgetDescrType;
		}
		else if (method.getName().equals("setSetup")) {
			findMandatoryMethods();
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
							final String fieldname = method2.getName().substring(3);
							final Object value = method2.invoke(argument, (Object[]) null);
							fields.put(fieldname, value);
						}
						else if (method2.getName().startsWith("has")) {
							final String fieldname = method2.getName().substring(3);
							final Object value = method2.invoke(argument, (Object[]) null);
							fields.put(fieldname, value);
						}
						else if (method2.getName().startsWith("is")) {
							final Object value = method2.invoke(argument, (Object[]) null);
							final String fieldname = method2.getName().substring(2);
							fields.put(fieldname, value);
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
		else if (method.getName().equals("checkMandatoryFields")) {
			return checkMandatory();
		}
		else {
			throw new IllegalStateException("'" + method.getName() + "' not known.");
		}
	}

	private MandatoryCheckResult checkMandatory() {
		MandatoryCheckResult result = MandatoryCheckResult.OK;
		String unfilledFields = "";
		for (final String mandatoryField : mandatoryFields) {
			if (fields.get(mandatoryField) == null) {
				unfilledFields += " " + mandatoryField;
			}
		}
		if (!"".equals(unfilledFields)) {
			result = new MandatoryCheckResult("The following fields are mandatory and not filled: " + unfilledFields);
		}
		return result;
	}

	private void findMandatoryMethods() {
		final Method[] methods = ((Class<? extends ISetupBuilder<ISetupBuilder<?>>>) bluePrintType).getMethods();
		if (methods != null) {
			for (int i = 0; i < methods.length; i++) {
				final Method method2 = methods[i];
				if (null != method2.getAnnotation(Mandatory.class)) {
					String name = method2.getName();
					if (name.startsWith("get") || name.startsWith("has")) {
						name = name.substring(3);
					}
					else if (name.startsWith("is")) {
						name = name.substring(2);
					}
					else {
						continue;
					}
					mandatoryFields.add(name);
				}
			}
		}
	}

	private void initialize(
		final ISetupBuilder<?> proxy,
		final Class<? extends ISetupBuilder<ISetupBuilder<?>>> bluePrintType,
		final ISetupBuilderConvenienceRegistry convenienceRegistry,
		final IDefaultsInitializerRegistry defaultsRegistry,
		final List<Class<?>> visitedInterfaces) {

		for (final Class<?> superInterface : bluePrintType.getInterfaces()) {
			if (!visitedInterfaces.contains(superInterface)) {
				visitedInterfaces.add(superInterface);
				initialize(
						proxy,
						(Class<? extends ISetupBuilder<ISetupBuilder<?>>>) superInterface,
						convenienceRegistry,
						defaultsRegistry,
						visitedInterfaces);
			}
		}

		for (final IDefaultInitializer<ISetupBuilder<?>> registeredInitializer : defaultsRegistry.getRegistered(bluePrintType)) {
			registeredInitializer.initialize(proxy);
		}
		final IDefaultInitializer<ISetupBuilder<?>> defaultInitializer = getDefaultInitializer(bluePrintType);
		if (defaultInitializer != null) {
			defaultInitializer.initialize(proxy);
		}

		final ISetupBuilderConvenience<ISetupBuilder<?>> convenienceMethods = getConvenienceMethods(bluePrintType);
		if (convenienceMethods != null) {
			for (final Method method : convenienceMethods.getClass().getDeclaredMethods()) {
				final MethodKey methodKey = new MethodKey(method);
				convenienceMethodsMap.put(methodKey, convenienceMethods);
			}
		}
		final ISetupBuilderConvenience<ISetupBuilder<?>> setupBuilderConvenience = convenienceRegistry.getRegistered(bluePrintType);
		if (setupBuilderConvenience != null) {
			for (final Method method : convenienceRegistry.getRegistered(bluePrintType).getClass().getDeclaredMethods()) {
				final MethodKey methodKey = new MethodKey(method);
				convenienceMethodsMap.put(methodKey, setupBuilderConvenience);
			}
		}

	}

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

	/**
	 * Get interfaces that the type inherits from.
	 * 
	 * @param bluePrintType
	 * @return
	 */
	private static Class<?> getDescriptorInterface(final Class<?> bluePrintType) {
		final Set<Class<?>> possibleInterfaces = getDescriptorInterfaces(bluePrintType);
		if (possibleInterfaces.size() == 1) {
			return possibleInterfaces.iterator().next();
		}
		return null;
	}

	private static Set<Class<?>> getDescriptorInterfaces(final Class<?> in) {
		final Set<Class<?>> possibleInterfaces = new HashSet<Class<?>>();
		if (isDescriptor(in)) {
			possibleInterfaces.add(in);
		}
		final Class<?>[] interfaces = in.getInterfaces();
		for (final Class<?> c : interfaces) {
			possibleInterfaces.addAll(getDescriptorInterfaces(c));
		}
		return possibleInterfaces;
	}

	private static boolean isDescriptor(final Class<?> in) {
		final Class<?>[] interfaces = in.getInterfaces();
		for (final Class<?> c : interfaces) {
			if (c == IWidgetDescriptor.class) {
				return true;
			}
		}
		return false;
	}

}
