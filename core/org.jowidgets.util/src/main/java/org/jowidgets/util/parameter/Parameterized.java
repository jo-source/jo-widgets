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

package org.jowidgets.util.parameter;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jowidgets.util.Assert;
import org.jowidgets.util.ITypedKey;
import org.jowidgets.util.concurrent.ISingleThreadAccess;
import org.jowidgets.util.concurrent.SingleThreadParameter;

public final class Parameterized {

	private Parameterized() {}

	public static IParameterizedBuilder builder() {
		return new ParameterizedBuilderImpl();
	}

	private static final class ParameterizedBuilderImpl implements IParameterizedBuilder {

		private final Map<ITypedKey<?>, IParameter<?>> parameters;

		private ISingleThreadAccess readThreadAccess;
		private ISingleThreadAccess writeThreadAccess;

		private ParameterizedBuilderImpl() {
			this.parameters = new LinkedHashMap<ITypedKey<?>, IParameter<?>>();
		}

		@Override
		public IParameterizedBuilder setReadThreadAccess(final ISingleThreadAccess singleThreadAccess) {
			this.readThreadAccess = singleThreadAccess;
			return this;
		}

		@Override
		public IParameterizedBuilder setWriteThreadAccess(final ISingleThreadAccess singleThreadAccess) {
			this.writeThreadAccess = singleThreadAccess;
			return this;
		}

		@Override
		public <VALUE_TYPE> IParameterizedBuilder addParameter(
			final ITypedKey<VALUE_TYPE> key,
			final IParameter<VALUE_TYPE> parameter) {
			Assert.paramNotNull(key, "key");
			Assert.paramNotNull(parameter, "parameter");
			parameters.put(key, parameter);
			return this;
		}

		@Override
		public <VALUE_TYPE> IParameterizedBuilder addParameter(final ITypedKey<VALUE_TYPE> key, final Class<VALUE_TYPE> valueType) {
			return addParameter(key, Parameter.create(valueType));
		}

		@Override
		public <VALUE_TYPE> IParameterizedBuilder addParameter(
			final ITypedKey<VALUE_TYPE> key,
			final Class<VALUE_TYPE> valueType,
			final String label) {
			return addParameter(key, Parameter.create(valueType, label));
		}

		@Override
		public <VALUE_TYPE> IParameterizedBuilder addParameter(
			final ITypedKey<VALUE_TYPE> key,
			final Class<VALUE_TYPE> valueType,
			final String label,
			final String description) {
			return addParameter(key, Parameter.create(valueType, label, description));
		}

		@SuppressWarnings({"rawtypes", "unchecked"})
		@Override
		public IParameterized build() {
			final Map<ITypedKey<?>, IParameter<?>> parametersForImpl;
			if (readThreadAccess != null || writeThreadAccess != null) {
				parametersForImpl = new LinkedHashMap<ITypedKey<?>, IParameter<?>>();
				for (final Entry<ITypedKey<?>, IParameter<?>> entry : parameters.entrySet()) {
					final IParameter<?> synchronizedParameter = new SingleThreadParameter(
						entry.getValue(),
						readThreadAccess,
						writeThreadAccess);
					parametersForImpl.put(entry.getKey(), synchronizedParameter);
				}
			}
			else {
				parametersForImpl = new LinkedHashMap<ITypedKey<?>, IParameter<?>>(parameters);
			}
			return new ParameterizedImpl(parametersForImpl);
		}
	}

	private static final class ParameterizedImpl implements IParameterized {

		private final Map<ITypedKey<?>, IParameter<?>> parameters;
		private final List<ITypedKey<?>> availableParameters;

		private ParameterizedImpl(final Map<ITypedKey<?>, IParameter<?>> parameters) {
			Assert.paramNotNull(parameters, "parameters");
			this.parameters = parameters;
			this.availableParameters = Collections.unmodifiableList(new LinkedList<ITypedKey<?>>(parameters.keySet()));
		}

		@Override
		public List<ITypedKey<?>> getAvailableParameters() {
			return availableParameters;
		}

		@Override
		public <VALUE_TYPE> IParameter<VALUE_TYPE> getParameter(final ITypedKey<VALUE_TYPE> key) {
			Assert.paramNotNull(key, "key");
			@SuppressWarnings("unchecked")
			final IParameter<VALUE_TYPE> result = (IParameter<VALUE_TYPE>) parameters.get(key);
			if (result != null) {
				return result;
			}
			else {
				throw new IllegalArgumentException("The parameter '" + key + "' is not known");
			}
		}

	}

}
