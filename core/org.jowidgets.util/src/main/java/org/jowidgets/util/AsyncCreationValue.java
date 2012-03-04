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

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import org.jowidgets.util.maybe.IMaybe;
import org.jowidgets.util.maybe.Nothing;
import org.jowidgets.util.maybe.Some;

public final class AsyncCreationValue<VALUE_TYPE> implements IAsyncCreationValue<VALUE_TYPE> {

	private final Set<IAsyncCreationCallback<VALUE_TYPE>> callbacks;

	private IMaybe<VALUE_TYPE> value;

	public AsyncCreationValue() {
		this.callbacks = new LinkedHashSet<IAsyncCreationCallback<VALUE_TYPE>>();
		this.value = Nothing.getInstance();
	}

	public AsyncCreationValue(final VALUE_TYPE value) {
		this();
		valueCreated(value);
	}

	@Override
	public void addCreationCallback(final IAsyncCreationCallback<VALUE_TYPE> callback) {
		Assert.paramNotNull(callback, "callback");
		if (isCreated()) {
			callback.created(value.getValue());
		}
		else {
			callbacks.add(callback);
		}
	}

	@Override
	public void removeCreationCallback(final IAsyncCreationCallback<VALUE_TYPE> callback) {
		Assert.paramNotNull(callback, "callback");
		callbacks.remove(callback);
	}

	@Override
	public boolean isCreated() {
		return !value.isNothing();
	}

	public void valueCreated(final VALUE_TYPE value) {
		if (isCreated()) {
			throw new IllegalStateException("Value must not be created twice");
		}
		else {
			this.value = new Some<VALUE_TYPE>(value);
			for (final IAsyncCreationCallback<VALUE_TYPE> callback : new LinkedList<IAsyncCreationCallback<VALUE_TYPE>>(callbacks)) {
				callback.created(value);
			}
			//the callbacks are no longer needed, so do not hold references longer on them
			callbacks.clear();
		}
	}

}
