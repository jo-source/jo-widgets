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

import java.util.Collection;
import java.util.Map;

public final class Assert {

	private Assert() {
		super();
	}

	public static <PARAM_TYPE> PARAM_TYPE getParamNotNull(final PARAM_TYPE param, final String name) {
		paramNotNull(param, name);
		return param;
	}

	public static void paramNotNull(final Object object, final String name) {
		if (object == null) {
			throw new IllegalArgumentException("The parameter '" + name + "' must not be null!");
		}
	}

	public static void paramNotEmpty(final String string, final String name) {
		if (string == null || string.length() <= 0) {
			throw new IllegalArgumentException("The parameter '" + name + "' must not be empty!");
		}
	}

	public static void paramNotEmpty(final Collection<?> collection, final String name) {
		if (collection == null || collection.size() <= 0) {
			throw new IllegalArgumentException("The parameter '" + name + "' must not be empty!");
		}
	}

	public static void paramNotEmpty(final Map<?, ?> map, final String name) {
		if (map == null || map.size() <= 0) {
			throw new IllegalArgumentException("The parameter '" + name + "' must not be empty!");
		}
	}

	public static void paramNotEmpty(final Iterable<?> iterable, final String name) {
		paramNotNull(iterable, name);
		if (iterable.iterator() == null || !iterable.iterator().hasNext()) {
			throw new IllegalArgumentException("The parameter '" + name + "' must not be empty!");
		}
	}

	public static void paramNotEmpty(final Object[] array, final String name) {
		paramNotNull(array, name);
		if (array.length <= 0) {
			throw new IllegalArgumentException("The parameter '" + name + "' must not be empty!");
		}
	}

	public static void paramAndElementsNotEmpty(final Object[] array, final String name) {
		paramNotNull(array, name);
		if (array.length <= 0) {
			throw new IllegalArgumentException("The parameter '" + name + "' must not be empty!");
		}
		for (final Object obj : array) {
			if (obj == null) {
				throw new IllegalArgumentException("The parameter '" + name + "' must not have empty elements!");
			}
		}
	}

	public static void paramHasType(final Object object, final Class<?> type, final String name) {
		paramNotNull(object, "object");
		if (!type.isAssignableFrom(object.getClass())) {
			throw new IllegalArgumentException("The type of the parameter '"
				+ name
				+ "' must be "
				+ type.getName()
				+ "' but is '"
				+ object.getClass().getName()
				+ "'!");
		}
	}

	public static void paramInBounds(final long rightBoundary, final long value, final String name) {
		paramInBounds(0, rightBoundary, value, name);
	}

	public static void paramInBounds(final int rightBoundary, final int value, final String name) {
		paramInBounds((long) 0, (long) rightBoundary, (long) value, name);
	}

	public static void paramInBounds(final long leftBoundary, final long rightBoundary, final long value, final String name) {
		paramInBounds((double) leftBoundary, (double) rightBoundary, (double) value, name);
	}

	public static void paramInBounds(final int leftBoundary, final int rightBoundary, final int value, final String name) {
		paramInBounds((long) leftBoundary, (long) rightBoundary, (long) value, name);
	}

	public static void paramInBounds(final double leftBoundary, final double rightBoundary, final double value, final String name) {
		if (value < leftBoundary || value > rightBoundary) {
			throw new IndexOutOfBoundsException("The parameter '"
				+ name
				+ "' must be between '"
				+ leftBoundary
				+ "' and '"
				+ rightBoundary
				+ "' but is '"
				+ value
				+ "'.");
		}
	}

	public static void paramNotNegative(final int param, final String name) {
		if (param < 0) {
			throw new IllegalArgumentException("The parameter '" + name + "' must not be negative but is '" + param + "'");
		}
	}

	public static void paramLessOrEqual(final int param1, final int param2, final String name1, final String name2) {
		if (param2 < param1) {
			throw new IllegalArgumentException("The parameter '"
				+ name1
				+ "' must be less or equal '"
				+ name2
				+ "', but '"
				+ name1
				+ "' is '"
				+ param1
				+ "' and '"
				+ name2
				+ "' is'"
				+ param2
				+ "'");
		}
	}

}
