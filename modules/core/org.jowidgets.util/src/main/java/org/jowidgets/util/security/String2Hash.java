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

package org.jowidgets.util.security;

import java.security.MessageDigest;

import org.jowidgets.util.Assert;

public final class String2Hash {

	private String2Hash() {}

	public static String encode(final String string) {
		return encode(string, "MD5", "UTF-8");
	}

	public static String encode(final String string, final String algorithm, final String charsetName) {
		Assert.paramNotNull(string, "string");
		Assert.paramNotNull(algorithm, "algorithm");
		Assert.paramNotNull(charsetName, "charsetName");

		try {
			final MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			final StringBuilder result = new StringBuilder();
			messageDigest.update(string.getBytes(charsetName));

			for (final byte byteValue : messageDigest.digest()) {
				result.append(byteToHexString(byteValue));
			}

			return result.toString();
		}
		catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static String byteToHexString(final byte byteValue) {
		final int value = (byteValue & 0x7F) + (byteValue < 0 ? 128 : 0);
		String result = value < 16 ? "0" : "";
		result = result + Integer.toHexString(value).toLowerCase();
		return result;
	}

}
