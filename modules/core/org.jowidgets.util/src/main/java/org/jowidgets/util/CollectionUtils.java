/*
 * Copyright (c) 2011, grossmann
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

package org.jowidgets.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public final class CollectionUtils {

	private CollectionUtils() {}

	public static <ELEMENT_TYPE> HashSet<ELEMENT_TYPE> createHashSet(final Iterable<? extends ELEMENT_TYPE> iterable) {
		Assert.paramNotNull(iterable, "iterable");
		final HashSet<ELEMENT_TYPE> result = new HashSet<ELEMENT_TYPE>();
		for (final ELEMENT_TYPE element : iterable) {
			result.add(element);
		}
		return result;
	}

	public static <ELEMENT_TYPE> List<ELEMENT_TYPE> createList(final Iterable<? extends ELEMENT_TYPE> iterable) {
		Assert.paramNotNull(iterable, "iterable");
		final List<ELEMENT_TYPE> result = new LinkedList<ELEMENT_TYPE>();
		for (final ELEMENT_TYPE element : iterable) {
			result.add(element);
		}
		return result;
	}

	public static <ELEMENT_TYPE> Iterator<ELEMENT_TYPE> unmodifiableIterator(final Iterator<ELEMENT_TYPE> original) {
		Assert.paramNotNull(original, "original");
		return new Iterator<ELEMENT_TYPE>() {

			@Override
			public boolean hasNext() {
				return original.hasNext();
			}

			@Override
			public ELEMENT_TYPE next() {
				return original.next();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("This collection is imutable");
			}

		};
	}

	public static <ELEMENT_TYPE> ListIterator<ELEMENT_TYPE> unmodifiableListIterator(final ListIterator<ELEMENT_TYPE> original) {
		Assert.paramNotNull(original, "original");
		return new ListIterator<ELEMENT_TYPE>() {

			@Override
			public boolean hasNext() {
				return original.hasNext();
			}

			@Override
			public ELEMENT_TYPE next() {
				return original.next();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("This collection is imutable");
			}

			@Override
			public boolean hasPrevious() {
				return original.hasPrevious();
			}

			@Override
			public ELEMENT_TYPE previous() {
				return original.previous();
			}

			@Override
			public int nextIndex() {
				return original.nextIndex();
			}

			@Override
			public int previousIndex() {
				return original.previousIndex();
			}

			@Override
			public void set(final ELEMENT_TYPE e) {
				throw new UnsupportedOperationException("This collection is imutable");
			}

			@Override
			public void add(final ELEMENT_TYPE e) {
				throw new UnsupportedOperationException("This collection is imutable");
			}

		};
	}

	public static <ELEMENT_TYPE> Enumeration<ELEMENT_TYPE> enumerationFromCollection(final Collection<ELEMENT_TYPE> collection) {
		Assert.paramNotNull(collection, "collection");
		return enumerationFromIterator(collection.iterator());
	}

	public static <ELEMENT_TYPE> Enumeration<ELEMENT_TYPE> enumerationFromIterator(final Iterator<ELEMENT_TYPE> iterator) {
		Assert.paramNotNull(iterator, "iterator");
		return new Enumeration<ELEMENT_TYPE>() {

			@Override
			public boolean hasMoreElements() {
				return iterator.hasNext();
			}

			@Override
			public ELEMENT_TYPE nextElement() {
				return iterator.next();
			}
		};
	}

	public static <ELEMENT_TYPE> void addFromEnumerationToCollection(
		final Collection<ELEMENT_TYPE> collection,
		final Enumeration<ELEMENT_TYPE> enumeration) {
		Assert.paramNotNull(collection, "collection");
		Assert.paramNotNull(enumeration, "enumeration");
		while (enumeration.hasMoreElements()) {
			collection.add(enumeration.nextElement());
		}
	}

	public static <ELEMENT_TYPE> ArrayList<ELEMENT_TYPE> unmodifiableArrayList(final ArrayList<ELEMENT_TYPE> original) {
		return new ArrayList<ELEMENT_TYPE>(original) {

			private static final long serialVersionUID = 969991953708235922L;

			@Override
			public ELEMENT_TYPE remove(final int index) {
				throw new UnsupportedOperationException("This collection is immutable");
			}

			@Override
			public boolean remove(final Object o) {
				throw new UnsupportedOperationException("This collection is immutable");
			}

			@Override
			protected void removeRange(final int fromIndex, final int toIndex) {
				throw new UnsupportedOperationException("This collection is immutable");
			}

			@Override
			public Iterator<ELEMENT_TYPE> iterator() {
				return unmodifiableIterator(super.iterator());
			}

			@Override
			public ListIterator<ELEMENT_TYPE> listIterator() {
				return unmodifiableListIterator(super.listIterator());
			}

			@Override
			public ListIterator<ELEMENT_TYPE> listIterator(final int index) {
				return unmodifiableListIterator(super.listIterator(index));
			}

			@Override
			public boolean removeAll(final Collection<?> c) {
				throw new UnsupportedOperationException("This collection is immutable");
			}

			@Override
			public void trimToSize() {
				throw new UnsupportedOperationException("This collection is immutable");
			}

			@Override
			public void ensureCapacity(final int minCapacity) {
				throw new UnsupportedOperationException("This collection is immutable");
			}

			@Override
			public ELEMENT_TYPE set(final int index, final ELEMENT_TYPE element) {
				throw new UnsupportedOperationException("This collection is immutable");
			}

			@Override
			public boolean add(final ELEMENT_TYPE e) {
				throw new UnsupportedOperationException("This collection is immutable");
			}

			@Override
			public void add(final int index, final ELEMENT_TYPE element) {
				throw new UnsupportedOperationException("This collection is immutable");
			}

			@Override
			public void clear() {
				throw new UnsupportedOperationException("This collection is immutable");
			}

			@Override
			public boolean addAll(final Collection<? extends ELEMENT_TYPE> c) {
				throw new UnsupportedOperationException("This collection is immutable");
			}

			@Override
			public boolean addAll(final int index, final Collection<? extends ELEMENT_TYPE> c) {
				throw new UnsupportedOperationException("This collection is immutable");
			}

		};
	}

}
