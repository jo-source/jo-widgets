/*
 * Copyright (c) 2010, grossmann
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

package org.jowidgets.spi.impl.swt.image.util;

import java.util.HashMap;
import java.util.Map;

public final class GaussMatrix {

	public static final int MAX_DEPTH = 25;

	private static long[][] binominalCoeff = new long[MAX_DEPTH + 1][];
	private static Map<Integer, Object> matrices = new HashMap<Integer, Object>();

	private GaussMatrix() {};

	private static long[] getBinominalCoeff(final int depth) {
		if (depth > MAX_DEPTH) {
			throw new IllegalArgumentException("parameter 'depth' must be less than '" + MAX_DEPTH + "'.");
		}
		if (binominalCoeff[depth] != null) {
			return binominalCoeff[depth];
		}
		else if (depth == 0) {
			binominalCoeff[0] = new long[1];
			binominalCoeff[0][0] = 1;
			return binominalCoeff[0];
		}
		else {
			final long[] parentCoeff = getBinominalCoeff(depth - 1);
			final long[] result = new long[parentCoeff.length + 1];
			result[0] = 1;
			result[result.length - 1] = 1;
			for (int i = 0; i < parentCoeff.length - 1; i++) {
				result[i + 1] = parentCoeff[i] + parentCoeff[i + 1];
			}
			binominalCoeff[depth] = result;
			return binominalCoeff[depth];
		}
	}

	public static Double[][] getGaussMatrix(final int n) {
		final Object value = matrices.get(Integer.valueOf(n));
		if (value instanceof Double[][]) {
			return (Double[][]) value;
		}
		else {
			final Double[][] result = new Double[n][n];
			final long[] binomCoeff = getBinominalCoeff(n - 1);

			long scaleFactor = 0;

			for (int x = 0; x < n; x++) {
				for (int y = 0; y < n; y++) {
					result[x][y] = (double) (binomCoeff[x] * binomCoeff[y]);
					scaleFactor += result[x][y];
				}
			}

			for (int x = 0; x < n; x++) {
				for (int y = 0; y < n; y++) {
					result[x][y] = result[x][y] / scaleFactor;
				}
			}

			matrices.put(Integer.valueOf(n), result);
			return result;
		}
	}

}
