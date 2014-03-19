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

package org.jowidgets.util.concurrent;

import org.jowidgets.util.Assert;
import org.jowidgets.util.IObservableValueListener;
import org.jowidgets.util.parameter.IParameter;
import org.jowidgets.util.parameter.ParameterWrapper;

public final class SingleThreadParameter<VALUE_TYPE> extends ParameterWrapper<VALUE_TYPE> implements IParameter<VALUE_TYPE> {

	private final IParameter<VALUE_TYPE> original;

	private final ISingleThreadAccess readThreadAccess;
	private final ISingleThreadAccess writeThreadAccess;

	public SingleThreadParameter(final IParameter<VALUE_TYPE> original, final ISingleThreadAccess singleThreadAccess) {
		this(original, singleThreadAccess, singleThreadAccess);
	}

	public SingleThreadParameter(
		final IParameter<VALUE_TYPE> original,
		final ISingleThreadAccess readThreadAccess,
		final ISingleThreadAccess writeThreadAccess) {

		super(original);
		Assert.paramNotNull(original, "original");

		this.original = original;
		this.readThreadAccess = readThreadAccess;
		this.writeThreadAccess = writeThreadAccess;
	}

	@Override
	public void setValue(final VALUE_TYPE value) {
		checkWriteThread();
		original.setValue(value);
	}

	@Override
	public VALUE_TYPE getValue() {
		checkReadThread();
		return original.getValue();
	}

	@Override
	public void addValueListener(final IObservableValueListener<VALUE_TYPE> listener) {
		checkWriteThread();
		original.addValueListener(listener);
	}

	@Override
	public void removeValueListener(final IObservableValueListener<VALUE_TYPE> listener) {
		checkWriteThread();
		original.removeValueListener(listener);
	}

	private void checkReadThread() {
		if (readThreadAccess != null) {
			if (!readThreadAccess.isSingleThread()) {
				throw new IllegalArgumentException("Operation must be invoked in read thread access!");
			}
		}
	}

	private void checkWriteThread() {
		if (writeThreadAccess != null) {
			if (!writeThreadAccess.isSingleThread()) {
				throw new IllegalArgumentException("Operation must be invoked in write thread access!");
			}
		}
	}

}
