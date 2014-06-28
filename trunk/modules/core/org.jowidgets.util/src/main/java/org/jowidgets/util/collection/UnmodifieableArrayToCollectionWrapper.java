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

import java.util.Collection;
import java.util.Iterator;

import org.jowidgets.util.Assert;
import org.jowidgets.util.NullCompatibleEquivalence;
import org.jowidgets.util.wrapper.WrapperUtil;

public final class UnmodifieableArrayToCollectionWrapper<ELEMENT_TYPE> implements Collection<ELEMENT_TYPE> {

	private final IUnmodifiableArray<ELEMENT_TYPE> original;

	public UnmodifieableArrayToCollectionWrapper(final IUnmodifiableArray<ELEMENT_TYPE> original) {
		Assert.paramNotNull(original, "original");
		this.original = original;
	}

	@Override
	public int size() {
		return original.size();
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public Iterator<ELEMENT_TYPE> iterator() {
		return original.iterator();
	}

	@Override
	public boolean contains(final Object object) {
		for (final ELEMENT_TYPE element : original) {
			if (NullCompatibleEquivalence.equals(element, object)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean containsAll(final Collection<?> c) {
		Assert.paramNotNull(c, "c");
		for (final Object object : c) {
			if (!contains(object)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Object[] toArray() {
		return toArrayImpl(new Object[original.size()]);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(final T[] a) {
		if (size() < a.length) {
			return toArrayImpl(a);
		}
		else {
			return (T[]) toArray();
		}
	}

	@SuppressWarnings("unchecked")
	private <T> T[] toArrayImpl(final T[] result) {
		@SuppressWarnings("rawtypes")
		final Iterator iterator = original.iterator();
		for (int i = 0; i < result.length; i++) {
			final T next;
			if (iterator.hasNext()) {
				next = (T) iterator.next();
			}
			else {
				next = null;
			}
			result[i] = next;
		}
		return result;
	}

	@Override
	public boolean add(final ELEMENT_TYPE e) {
		throw new UnsupportedOperationException("add is not supported");
	}

	@Override
	public boolean remove(final Object o) {
		throw new UnsupportedOperationException("remove not supported");
	}

	@Override
	public boolean addAll(final Collection<? extends ELEMENT_TYPE> c) {
		throw new UnsupportedOperationException("addAll not supported");
	}

	@Override
	public boolean removeAll(final Collection<?> c) {
		throw new UnsupportedOperationException("removeAll not supported");
	}

	@Override
	public boolean retainAll(final Collection<?> c) {
		throw new UnsupportedOperationException("retainAll not supported");
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException("clear not supported");
	}

	@Override
	public int hashCode() {
		return WrapperUtil.unwrap(original).hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		return WrapperUtil.nullCompatibleEquivalence(this, obj);
	}

}
