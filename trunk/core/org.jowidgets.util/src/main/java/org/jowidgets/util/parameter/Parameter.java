/*
 * Copyright (c) 2013, MGrossmann
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

import org.jowidgets.util.Assert;
import org.jowidgets.util.ObservableValue;

public final class Parameter {

	private Parameter() {}

	public static <VALUE_TYPE> IParameter<VALUE_TYPE> create(final Class<VALUE_TYPE> valueType) {
		return create(valueType, null);
	}

	public static <VALUE_TYPE> IParameter<VALUE_TYPE> create(final Class<VALUE_TYPE> valueType, final String label) {
		return create(valueType, label, null);
	}

	public static <VALUE_TYPE> IParameter<VALUE_TYPE> create(
		final Class<VALUE_TYPE> valueType,
		final String label,
		final String descripion) {
		final IParameterBuilder<VALUE_TYPE> builder = builder();
		builder.setValueType(valueType).setLabel(label).setDescription(descripion);
		return builder.build();
	}

	public static <VALUE_TYPE> IParameterBuilder<VALUE_TYPE> builder() {
		return new ParameterBuilderImpl<VALUE_TYPE>();
	}

	private static final class ParameterBuilderImpl<VALUE_TYPE> implements IParameterBuilder<VALUE_TYPE> {

		private Class<VALUE_TYPE> valueType;
		private VALUE_TYPE value;
		private String label;
		private String description;

		@Override
		public IParameterBuilder<VALUE_TYPE> setValueType(final Class<VALUE_TYPE> valueType) {
			Assert.paramNotNull(valueType, "valueType");
			this.valueType = valueType;
			return this;
		}

		@Override
		public IParameterBuilder<VALUE_TYPE> setValue(final VALUE_TYPE value) {
			this.value = value;
			return this;
		}

		@Override
		public IParameterBuilder<VALUE_TYPE> setLabel(final String label) {
			this.label = label;
			return this;
		}

		@Override
		public IParameterBuilder<VALUE_TYPE> setDescription(final String description) {
			this.description = description;
			return this;
		}

		@Override
		public IParameter<VALUE_TYPE> build() {
			return new ParameterImpl<VALUE_TYPE>(valueType, value, label, description);
		}

	}

	private static final class ParameterImpl<VALUE_TYPE> extends ObservableValue<VALUE_TYPE> implements IParameter<VALUE_TYPE> {

		private final String label;
		private final String description;

		private ParameterImpl(
			final Class<VALUE_TYPE> valueType,
			final VALUE_TYPE value,
			final String label,
			final String description) {
			super(valueType);

			this.label = label;
			this.description = description;
			setValue(value);
		}

		@Override
		public String getLabel() {
			return label;
		}

		@Override
		public String getDescription() {
			return description;
		}

	}

}
