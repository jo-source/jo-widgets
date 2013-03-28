/*
 * Copyright (c) 2012, grossmann
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

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

public final class FilterComposite {

	private FilterComposite() {}

	public static <FILTER_VALUE_TYPE> IFilter<FILTER_VALUE_TYPE> create(final IFilter<FILTER_VALUE_TYPE>... filters) {
		if (!EmptyCheck.isEmpty(filters)) {
			return create(Arrays.asList(filters));
		}
		else {
			Filter.acceptAll();
		}
		return null;
	}

	public static <FILTER_VALUE_TYPE> IFilter<FILTER_VALUE_TYPE> create(
		final Collection<? extends IFilter<FILTER_VALUE_TYPE>> filters) {
		Assert.paramNotNull(filters, "filters");
		return new FilterCollectionImpl<FILTER_VALUE_TYPE>(filters);
	}

	/**
	 * Creates a composite from two filters. If both filters a null, null will be returned.
	 * If only one of the filters is null, the other (not null) filter will be returned.
	 * If both filter are not null, a composed filter will be returned, that filters in the following manner:
	 * 
	 * @param filter1 The first filter, may be null
	 * @param filter2 The second filter, may be null
	 * 
	 * @return The composed filter or null, if both arguments was null
	 */
	public static <FILTER_VALUE_TYPE> IFilter<FILTER_VALUE_TYPE> create(
		final IFilter<FILTER_VALUE_TYPE> filter1,
		final IFilter<FILTER_VALUE_TYPE> filter2) {

		if (filter1 != null && filter2 != null) {
			return new FilterCompositeImpl<FILTER_VALUE_TYPE>(filter1, filter2);
		}
		else if (filter1 != null) {
			return filter1;
		}
		else if (filter2 != null) {
			return filter2;
		}
		else {
			return null;
		}
	}

	private static final class FilterCompositeImpl<FILTER_VALUE_TYPE> implements IFilter<FILTER_VALUE_TYPE> {

		private final IFilter<FILTER_VALUE_TYPE> filter1;
		private final IFilter<FILTER_VALUE_TYPE> filter2;

		private FilterCompositeImpl(final IFilter<FILTER_VALUE_TYPE> filter1, final IFilter<FILTER_VALUE_TYPE> filter2) {
			this.filter1 = filter1;
			this.filter2 = filter2;
		}

		@Override
		public boolean accept(final FILTER_VALUE_TYPE value) {
			final boolean accepted = filter1.accept(value);
			if (accepted) {
				return filter2.accept(value);
			}
			else {
				return false;
			}
		}

	}

	private static final class FilterCollectionImpl<FILTER_VALUE_TYPE> implements IFilter<FILTER_VALUE_TYPE> {

		private final Collection<? extends IFilter<FILTER_VALUE_TYPE>> filters;

		private FilterCollectionImpl(final Collection<? extends IFilter<FILTER_VALUE_TYPE>> filters) {
			this.filters = new LinkedList<IFilter<FILTER_VALUE_TYPE>>(filters);
		}

		@Override
		public boolean accept(final FILTER_VALUE_TYPE value) {
			for (final IFilter<FILTER_VALUE_TYPE> filter : filters) {
				final boolean accepted = filter.accept(value);
				if (!accepted) {
					return false;
				}
			}
			return true;
		}
	}

}
