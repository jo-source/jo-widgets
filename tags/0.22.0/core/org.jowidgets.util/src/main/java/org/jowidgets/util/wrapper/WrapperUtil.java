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

package org.jowidgets.util.wrapper;

import org.jowidgets.util.Assert;

public final class WrapperUtil {

	private WrapperUtil() {}

	/**
	 * Try to cast an object to an type. If the object implements the {@link IWrapper} interface,
	 * the cast will also be be tried on the unwrapped object (recursively).
	 * 
	 * @param object The object to cast, may be null
	 * @param type The type to cast into, not null
	 * 
	 * @return The casted object or null, if neither the object itself can be casted nor the unwrapped objects
	 */
	@SuppressWarnings("unchecked")
	public static <TYPE> TYPE tryToCast(final Object object, final Class<TYPE> type) {
		Assert.paramNotNull(type, "type");
		if (object != null) {
			if (type.isAssignableFrom(object.getClass())) {
				return (TYPE) object;
			}
			else if (object instanceof IWrapper<?>) {
				return tryToCast(((IWrapper<?>) object).unwrap(), type);
			}
		}
		return null;
	}
}
