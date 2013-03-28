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

package org.jowidgets.util;

import java.util.Collection;

public final class Filter {

	private static final IFilter<Object> ACCEPT_ALL = new AcceptAllFilter<Object>();

	private Filter() {}

	@SuppressWarnings("unchecked")
	public static <VALUE_TYPE> IFilter<VALUE_TYPE> acceptAll() {
		return (IFilter<VALUE_TYPE>) ACCEPT_ALL;
	}

	public static <VALUE_TYPE> IFilter<VALUE_TYPE> and(final IFilter<VALUE_TYPE> filter1, final IFilter<VALUE_TYPE> filter2) {
		return FilterComposite.create(filter1, filter2);
	}

	public static <FILTER_VALUE_TYPE> IFilter<FILTER_VALUE_TYPE> and(final IFilter<FILTER_VALUE_TYPE>... filters) {
		return FilterComposite.create(filters);
	}

	public static <FILTER_VALUE_TYPE> IFilter<FILTER_VALUE_TYPE> and(
		final Collection<? extends IFilter<FILTER_VALUE_TYPE>> filters) {
		return FilterComposite.create(filters);
	}

	private static final class AcceptAllFilter<VALUE_TYPE> implements IFilter<VALUE_TYPE> {
		@Override
		public boolean accept(final VALUE_TYPE value) {
			return true;
		}
	}

}
