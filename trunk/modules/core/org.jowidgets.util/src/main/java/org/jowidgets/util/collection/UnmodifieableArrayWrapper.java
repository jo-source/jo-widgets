/*
 * Copyright (c) 2014, Michael
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

import java.util.Iterator;

import org.jowidgets.util.Assert;
import org.jowidgets.util.wrapper.IWrapper;
import org.jowidgets.util.wrapper.WrapperUtil;

public class UnmodifieableArrayWrapper<VALUE_TYPE> implements
		IUnmodifiableArray<VALUE_TYPE>,
		IWrapper<IUnmodifiableArray<VALUE_TYPE>> {

	private final IUnmodifiableArray<VALUE_TYPE> original;

	public UnmodifieableArrayWrapper(final IUnmodifiableArray<VALUE_TYPE> original) {
		Assert.paramNotNull(original, "original");
		this.original = original;
	}

	@Override
	public final Iterator<VALUE_TYPE> iterator() {
		return original.iterator();
	}

	@Override
	public final int size() {
		return original.size();
	}

	@Override
	public final VALUE_TYPE get(final int index) {
		return original.get(index);
	}

	@Override
	public String toString() {
		return "UnmodifieableArrayWrapper [original=" + original + "]";
	}

	@Override
	public IUnmodifiableArray<VALUE_TYPE> unwrap() {
		return WrapperUtil.unwrap(this);
	}

	@Override
	public int hashCode() {
		final IUnmodifiableArray<VALUE_TYPE> unwrappedThis = unwrap();

		final int prime = 31;
		int result = 1;
		result = prime * result + ((unwrappedThis == null) ? 0 : unwrappedThis.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		return WrapperUtil.nullCompatibleEquivalence(this, obj);
	}

}
