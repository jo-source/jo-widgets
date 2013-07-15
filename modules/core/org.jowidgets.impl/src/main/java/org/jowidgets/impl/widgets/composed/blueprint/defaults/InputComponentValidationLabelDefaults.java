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
package org.jowidgets.impl.widgets.composed.blueprint.defaults;

import org.jowidgets.api.widgets.blueprint.builder.IInputComponentValidationLabelSetupBuilder;
import org.jowidgets.api.widgets.blueprint.defaults.IDefaultInitializer;
import org.jowidgets.util.IDecorator;
import org.jowidgets.validation.IValidationMessage;
import org.jowidgets.validation.IValidationResult;
import org.jowidgets.validation.MessageType;
import org.jowidgets.validation.ValidationResult;

public class InputComponentValidationLabelDefaults implements IDefaultInitializer<IInputComponentValidationLabelSetupBuilder<?>> {

	private static final IDecorator<IValidationResult> INITIAL_VALIDATION_DECORATOR = createDefaultInitialDecorator();
	private static final IDecorator<IValidationResult> UNMODIFIED_VALIDATION_DECORATOR = createUnmodifiedInitialDecorator();

	@Override
	public void initialize(final IInputComponentValidationLabelSetupBuilder<?> builder) {
		builder.setInitialValidationDecorator(INITIAL_VALIDATION_DECORATOR);
		builder.setUnmodifiedValidationDecorator(UNMODIFIED_VALIDATION_DECORATOR);
	}

	private static IDecorator<IValidationResult> createDefaultInitialDecorator() {
		return new IDecorator<IValidationResult>() {
			@Override
			public IValidationResult decorate(final IValidationResult original) {
				//do not do initial validation by default
				return null;
			}
		};
	}

	private static IDecorator<IValidationResult> createUnmodifiedInitialDecorator() {
		return new IDecorator<IValidationResult>() {
			@Override
			public IValidationResult decorate(final IValidationResult original) {
				final IValidationMessage worstFirst = original.getWorstFirst();
				if (worstFirst.getType().equalOrWorse(MessageType.WARNING)) {
					return original;
				}
				else {
					return ValidationResult.create().withInfo(null);
				}
			}
		};
	}
}
