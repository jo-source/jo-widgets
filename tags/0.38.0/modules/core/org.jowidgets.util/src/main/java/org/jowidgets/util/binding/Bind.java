/*
 * Copyright (c) 2014, grossmann
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

package org.jowidgets.util.binding;

import org.jowidgets.util.Assert;
import org.jowidgets.util.IObservableValue;
import org.jowidgets.util.IObservableValueListener;
import org.jowidgets.util.NullCompatibleEquivalence;

public final class Bind {

	@SuppressWarnings("rawtypes")
	private static final IBindingConverter IDENTITY_BINDING_CONVERTER = new IdentityBindingConverter<Object>();

	private static final IBind INSTANCE = new BindImpl();

	private Bind() {}

	public static IBind getInstance() {
		return INSTANCE;
	}

	public static <VALUE_TYPE> IBinding bind(
		final IObservableValue<VALUE_TYPE> source,
		final IObservableValue<VALUE_TYPE> destination) {
		return getInstance().bind(source, destination);
	}

	public static <SOURCE_TYPE, DESTINATION_TYPE> IBinding bind(
		final IObservableValue<SOURCE_TYPE> source,
		final IObservableValue<DESTINATION_TYPE> destination,
		final IBindingConverter<SOURCE_TYPE, DESTINATION_TYPE> converter) {
		return getInstance().bind(source, destination, converter);
	}

	private static final class BindImpl implements IBind {

		@SuppressWarnings("unchecked")
		@Override
		public <VALUE_TYPE> IBinding bind(
			final IObservableValue<VALUE_TYPE> source,
			final IObservableValue<VALUE_TYPE> destination) {
			return new BindingImpl<VALUE_TYPE, VALUE_TYPE>(source, destination, IDENTITY_BINDING_CONVERTER);
		}

		@Override
		public <SOURCE_TYPE, DESTINATION_TYPE> IBinding bind(
			final IObservableValue<SOURCE_TYPE> source,
			final IObservableValue<DESTINATION_TYPE> destination,
			final IBindingConverter<SOURCE_TYPE, DESTINATION_TYPE> converter) {
			return new BindingImpl<SOURCE_TYPE, DESTINATION_TYPE>(source, destination, converter);
		}

	}

	private static final class BindingImpl<SOURCE_TYPE, DESTINATION_TYPE> implements IBinding {

		private final IObservableValueListener<SOURCE_TYPE> sourceListener;
		private final IObservableValueListener<DESTINATION_TYPE> destinationListener;

		private IObservableValue<SOURCE_TYPE> source;
		private IObservableValue<DESTINATION_TYPE> destination;
		private IBindingConverter<SOURCE_TYPE, DESTINATION_TYPE> converter;

		private final boolean disposed;

		private boolean onSourceSet;
		private boolean onDestinationSet;

		private BindingImpl(
			final IObservableValue<SOURCE_TYPE> source,
			final IObservableValue<DESTINATION_TYPE> destination,
			final IBindingConverter<SOURCE_TYPE, DESTINATION_TYPE> converter) {

			Assert.paramNotNull(source, "source");
			Assert.paramNotNull(destination, "destination");
			Assert.paramNotNull(converter, "converter");

			this.source = source;
			this.destination = destination;
			this.converter = converter;

			this.disposed = false;
			this.onSourceSet = false;
			this.onDestinationSet = false;

			this.sourceListener = new IObservableValueListener<SOURCE_TYPE>() {
				@Override
				public void changed(final IObservableValue<SOURCE_TYPE> observableValue, final SOURCE_TYPE value) {
					setSourceToDestination();
				}
			};

			this.destinationListener = new IObservableValueListener<DESTINATION_TYPE>() {
				@Override
				public void changed(final IObservableValue<DESTINATION_TYPE> observableValue, final DESTINATION_TYPE value) {
					setDestinationToSource();
				}
			};

			bind();
		}

		private void setSourceToDestination() {
			if (onSourceSet || onDestinationSet) {
				return;
			}
			final DESTINATION_TYPE convertedSource = converter.convertSource(source.getValue());
			if (!NullCompatibleEquivalence.equals(convertedSource, destination.getValue())) {
				onDestinationSet = true;
				try {
					destination.setValue(convertedSource);
				}
				finally {
					onDestinationSet = false;
				}
			}
		}

		private void setDestinationToSource() {
			if (onSourceSet || onDestinationSet) {
				return;
			}
			final SOURCE_TYPE convertedDestination = converter.convertDestination(destination.getValue());
			if (!NullCompatibleEquivalence.equals(convertedDestination, source.getValue())) {
				onSourceSet = true;
				try {
					source.setValue(convertedDestination);
				}
				finally {
					onSourceSet = false;
				}
			}
		}

		@Override
		public void setBindingState(final boolean bind) {
			checkDisposed();
			if (bind) {
				bind();
			}
			else {
				unbind();
			}
		}

		@Override
		public void bind() {
			checkDisposed();
			setSourceToDestination();
			source.addValueListener(sourceListener);
			destination.addValueListener(destinationListener);
		}

		@Override
		public void unbind() {
			checkDisposed();
			source.removeValueListener(sourceListener);
			destination.removeValueListener(destinationListener);
		}

		@Override
		public boolean isDisposed() {
			return disposed;
		}

		@Override
		public void dispose() {
			checkDisposed();
			unbind();
			source = null;
			destination = null;
			converter = null;
		}

		private void checkDisposed() {
			if (disposed) {
				throw new IllegalStateException("Binding is disposed");
			}
		}
	}

	private static final class IdentityBindingConverter<VALUE_TYPE> implements IBindingConverter<VALUE_TYPE, VALUE_TYPE> {

		@Override
		public VALUE_TYPE convertSource(final VALUE_TYPE sourceValue) {
			return sourceValue;
		}

		@Override
		public VALUE_TYPE convertDestination(final VALUE_TYPE destinationValue) {
			return destinationValue;
		}

	}

}
