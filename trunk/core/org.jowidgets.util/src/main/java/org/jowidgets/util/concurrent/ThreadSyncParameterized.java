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
import org.jowidgets.util.ITypedKey;
import org.jowidgets.util.parameter.IParameter;
import org.jowidgets.util.parameter.IParameterized;
import org.jowidgets.util.parameter.IParameterizedBuilder;
import org.jowidgets.util.parameter.Parameterized;
import org.jowidgets.util.parameter.ParameterizedWrapper;

public final class ThreadSyncParameterized extends ParameterizedWrapper {

	public ThreadSyncParameterized(
		final IParameterized original,
		final ISingleThreadAccess originalThreadAccess,
		final ISingleThreadAccess newThreadAccess) {
		super(create(original, originalThreadAccess, newThreadAccess));
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public void dispose() {
		for (final ITypedKey key : getAvailableParameters()) {
			final ThreadSyncParameter parameter = (ThreadSyncParameter) getParameter(key);
			parameter.dispose();
		}
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private static IParameterized create(
		final IParameterized original,
		final ISingleThreadAccess originalThreadAccess,
		final ISingleThreadAccess newThreadAccess) {

		Assert.paramNotNull(original, "original");
		Assert.paramNotNull(originalThreadAccess, "originalThreadAccess");
		Assert.paramNotNull(newThreadAccess, "newThreadAccess");

		final IParameterizedBuilder builder = Parameterized.builder();

		for (final ITypedKey key : original.getAvailableParameters()) {
			final IParameter originalParameter = original.getParameter(key);
			final IParameter newParameter = new ThreadSyncParameter(originalParameter, originalThreadAccess, newThreadAccess);
			builder.addParameter(key, newParameter);
		}

		return builder.build();
	}
}
