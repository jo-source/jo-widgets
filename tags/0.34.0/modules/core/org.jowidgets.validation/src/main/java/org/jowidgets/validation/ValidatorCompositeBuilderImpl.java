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

package org.jowidgets.validation;

import java.util.LinkedHashSet;
import java.util.Set;

final class ValidatorCompositeBuilderImpl<VALUE_TYPE> implements IValidatorCompositeBuilder<VALUE_TYPE> {

	private final Set<IValidator<VALUE_TYPE>> validators;

	ValidatorCompositeBuilderImpl() {
		this.validators = new LinkedHashSet<IValidator<VALUE_TYPE>>();
	}

	@Override
	public IValidatorCompositeBuilder<VALUE_TYPE> add(final IValidator<VALUE_TYPE> validator) {
		Assert.paramNotNull(validator, "validator");
		validators.add(validator);
		return this;
	}

	@Override
	public IValidatorCompositeBuilder<VALUE_TYPE> addAll(final Iterable<? extends IValidator<VALUE_TYPE>> validators) {
		Assert.paramNotNull(validators, "validators");
		for (final IValidator<VALUE_TYPE> validator : validators) {
			add(validator);
		}
		return this;
	}

	@Override
	public IValidator<VALUE_TYPE> build() {
		return new ValidatorCompositeImpl<VALUE_TYPE>(validators);
	}

}
