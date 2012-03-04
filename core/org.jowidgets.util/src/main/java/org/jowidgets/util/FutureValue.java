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

public final class FutureValue<VALUE_TYPE> implements IFutureValue<VALUE_TYPE> {

	private final Set<IFutureValueCallback<VALUE_TYPE>> callbacks;

	private IMaybe<VALUE_TYPE> value;

	public FutureValue() {
		this.callbacks = new LinkedHashSet<IFutureValueCallback<VALUE_TYPE>>();
		this.value = Nothing.getInstance();
	}

	public FutureValue(final VALUE_TYPE value) {
		this();
		initialize(value);
	}

	@Override
	public void addFutureCallback(final IFutureValueCallback<VALUE_TYPE> callback) {
		Assert.paramNotNull(callback, "callback");
		if (isInitialized()) {
			callback.initialized(value.getValue());
		}
		else {
			callbacks.add(callback);
		}
	}

	@Override
	public void removeFutureCallback(final IFutureValueCallback<VALUE_TYPE> callback) {
		Assert.paramNotNull(callback, "callback");
		callbacks.remove(callback);
	}

	@Override
	public boolean isInitialized() {
		return !value.isNothing();
	}

	@Override
	public VALUE_TYPE getValue() {
		if (isInitialized()) {
			return value.getValue();
		}
		else {
			throw new IllegalStateException("The future is not already initialized");
		}
	}

	/**
	 * Initializes the future with a defined value. This can only be done once.
	 * 
	 * @param value The value to initialize the future with, may be null
	 * @throws IllegalStateException if the future was already initialized
	 */
	public void initialize(final VALUE_TYPE value) {
		if (isInitialized()) {
			throw new IllegalStateException("Value must not be created twice");
		}
		else {
			this.value = new Some<VALUE_TYPE>(value);
			for (final IFutureValueCallback<VALUE_TYPE> callback : new LinkedList<IFutureValueCallback<VALUE_TYPE>>(callbacks)) {
				callback.initialized(value);
			}
			//the callbacks are no longer needed, so do not hold references longer on them
			callbacks.clear();
		}
	}

}
