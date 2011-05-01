/*
 * Copyright (c) 2011, grossmann
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

package org.jowidgets.spi.impl.verify;

import java.util.LinkedList;
import java.util.List;

import org.jowidgets.common.verify.IInputVerifier;
import org.jowidgets.common.widgets.descriptor.setup.ITextComponentSetupCommon;

public final class InputVerifierHelper {

	private InputVerifierHelper() {}

	public static IInputVerifier getInputVerifier(final ITextComponentSetupCommon setup) {
		final List<IInputVerifier> inputVerifiers = new LinkedList<IInputVerifier>();

		if (setup.getInputVerifier() != null) {
			inputVerifiers.add(setup.getInputVerifier());
		}
		for (final String regExp : setup.getAcceptingRegExps()) {
			inputVerifiers.add(new RegExpInputVerifier(regExp));
		}

		if (inputVerifiers.size() > 0) {
			return new IInputVerifier() {
				@Override
				public boolean verify(final String currentValue, final String input, final int start, final int end) {
					for (final IInputVerifier inputVerifier : inputVerifiers) {
						if (!inputVerifier.verify(currentValue, input, start, end)) {
							return false;
						}
					}
					return true;
				}
			};
		}
		else {
			return null;
		}
	}

}
