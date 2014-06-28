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

package org.jowidgets.util.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.jowidgets.util.Assert;
import org.jowidgets.util.CollectionUtils;

public final class UnmodifiableArray {

	private static final IUnmodifiableArray<?> EMPTY_ARRAY = builder().build();

	private UnmodifiableArray() {}

	@SuppressWarnings("unchecked")
	public static <VALUE_TYPE> IUnmodifiableArray<VALUE_TYPE> emptyArray() {
		return (IUnmodifiableArray<VALUE_TYPE>) EMPTY_ARRAY;
	}

	public static <VALUE_TYPE> IUnmodifiableArray<VALUE_TYPE> singleton(final VALUE_TYPE value) {
		return new UnmodifiableArrayBuilderImpl<VALUE_TYPE>().add(value).build();
	}

	public static <VALUE_TYPE> IUnmodifiableArray<VALUE_TYPE> create(final Iterable<? extends VALUE_TYPE> values) {
		Assert.paramNotNull(values, "values");
		return new UnmodifiableArrayBuilderImpl<VALUE_TYPE>().addAll(values).build();
	}

	public static <VALUE_TYPE> IUnmodifiableArray<VALUE_TYPE> create(final Enumeration<? extends VALUE_TYPE> values) {
		Assert.paramNotNull(values, "values");
		return new UnmodifiableArrayBuilderImpl<VALUE_TYPE>().addAll(values).build();
	}

	public static <VALUE_TYPE> IUnmodifiableArray<VALUE_TYPE> create(final VALUE_TYPE... values) {
		Assert.paramNotNull(values, "values");
		return new UnmodifiableArrayBuilderImpl<VALUE_TYPE>().addAll(values).build();
	}

	public static <VALUE_TYPE> IUnmodifiableArrayBuilder<VALUE_TYPE> builder() {
		return new UnmodifiableArrayBuilderImpl<VALUE_TYPE>();
	}

	private static final class UnmodifiableArrayBuilderImpl<VALUE_TYPE> implements IUnmodifiableArrayBuilder<VALUE_TYPE> {

		private final List<VALUE_TYPE> elements;

		private UnmodifiableArrayBuilderImpl() {
			this.elements = new LinkedList<VALUE_TYPE>();
		}

		@Override
		public IUnmodifiableArrayBuilder<VALUE_TYPE> addAll(final Enumeration<? extends VALUE_TYPE> values) {
			Assert.paramNotNull(values, "values");
			while (values.hasMoreElements()) {
				add(values.nextElement());
			}
			return this;
		}

		@Override
		public IUnmodifiableArrayBuilder<VALUE_TYPE> addAll(final Iterable<? extends VALUE_TYPE> values) {
			Assert.paramNotNull(values, "values");
			for (final VALUE_TYPE value : values) {
				add(value);
			}
			return this;
		}

		@Override
		public IUnmodifiableArrayBuilder<VALUE_TYPE> addAll(final VALUE_TYPE... values) {
			Assert.paramNotNull(values, "values");
			for (final VALUE_TYPE value : values) {
				add(value);
			}
			return this;
		}

		@Override
		public IUnmodifiableArrayBuilder<VALUE_TYPE> add(final VALUE_TYPE value) {
			elements.add(value);
			return this;
		}

		@Override
		public IUnmodifiableArray<VALUE_TYPE> build() {
			return new UnmodifiableArrayImpl<VALUE_TYPE>(elements);
		}

	}

	private static final class UnmodifiableArrayImpl<VALUE_TYPE> implements IUnmodifiableArray<VALUE_TYPE> {

		private final ArrayList<VALUE_TYPE> elements;

		private UnmodifiableArrayImpl(final Collection<VALUE_TYPE> collection) {
			elements = new ArrayList<VALUE_TYPE>(collection);
		}

		@Override
		public Iterator<VALUE_TYPE> iterator() {
			return CollectionUtils.unmodifiableIterator(elements.iterator());
		}

		@Override
		public int size() {
			return elements.size();
		}

		@Override
		public VALUE_TYPE get(final int index) {
			return elements.get(index);
		}

	}
}
