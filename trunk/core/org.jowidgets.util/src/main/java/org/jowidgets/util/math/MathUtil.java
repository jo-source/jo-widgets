/*
 * Copyright (c) 2013, mgrossmann
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

package org.jowidgets.util.math;

public final class MathUtil {

	private MathUtil() {}

	public static <NUMBER_TYPE extends Number> NUMBER_TYPE min(final NUMBER_TYPE number1, final NUMBER_TYPE number2) {
		if (number1 == null && number2 == null) {
			return null;
		}
		else if (number1 == null) {
			return number2;
		}
		else if (number2 == null) {
			return number1;
		}
		else if (number1.doubleValue() < number2.doubleValue()) {
			return number1;
		}
		else {
			return number2;
		}
	}

	public static <NUMBER_TYPE extends Number> NUMBER_TYPE max(final NUMBER_TYPE number1, final NUMBER_TYPE number2) {
		if (number1 == null && number2 == null) {
			return null;
		}
		else if (number1 == null) {
			return number2;
		}
		else if (number2 == null) {
			return number1;
		}
		else if (number1.doubleValue() > number2.doubleValue()) {
			return number1;
		}
		else {
			return number2;
		}
	}

}
