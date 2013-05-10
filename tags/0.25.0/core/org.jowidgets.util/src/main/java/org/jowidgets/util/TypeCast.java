/*
 * Copyright (c) 2010, Michael Grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jowidgets.util;

public final class TypeCast {

	private TypeCast() {
		super();
	}

	/**
	 * Converts an object to an specific type if possible. Else an IllegalStateException is thrown.
	 * 
	 * @param <RESULT_TYPE> The type to convert to
	 * @param obj The object to cast, maybe null
	 * @param type The type to convert to
	 * @return The casted object
	 * @throws IllegalStateException if object could not be casted
	 */
	@SuppressWarnings("unchecked")
	public static <RESULT_TYPE> RESULT_TYPE toType(final Object obj, final Class<RESULT_TYPE> type) {
		Assert.paramNotNull(type, "type");
		if (obj != null && !type.isAssignableFrom(obj.getClass())) {
			throw new IllegalStateException("Type '"
				+ type.getName()
				+ "' expected, but type is '"
				+ obj.getClass().getName()
				+ "'.");
		}
		return (RESULT_TYPE) obj;
	}

}
