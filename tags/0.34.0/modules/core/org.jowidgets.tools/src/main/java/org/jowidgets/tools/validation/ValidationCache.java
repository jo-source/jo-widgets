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

package org.jowidgets.tools.validation;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import org.jowidgets.util.Assert;
import org.jowidgets.validation.IValidateable;
import org.jowidgets.validation.IValidationConditionListener;
import org.jowidgets.validation.IValidationResult;

public class ValidationCache implements IValidateable {

	private final IValidationResultCreator validationResultCreator;
	private final Set<IValidationConditionListener> validationConditionListener;

	private boolean chacheDirty;
	private IValidationResult validationResult;

	public ValidationCache(final IValidationResultCreator validationResultCreator) {
		Assert.paramNotNull(validationResultCreator, "validationResultCreator");
		this.validationConditionListener = new LinkedHashSet<IValidationConditionListener>();
		this.validationResultCreator = validationResultCreator;
		this.chacheDirty = true;
	}

	/**
	 * Marks the cache to be dirty. The currently cached value will be rejected.
	 */
	public final void setDirty() {
		this.chacheDirty = true;
		for (final IValidationConditionListener listener : new LinkedList<IValidationConditionListener>(
			validationConditionListener)) {
			listener.validationConditionsChanged();
		}
	}

	@Override
	public final IValidationResult validate() {
		if (chacheDirty) {
			this.validationResult = validationResultCreator.createValidationResult();
			this.chacheDirty = false;
		}
		return validationResult;
	}

	@Override
	public final void addValidationConditionListener(final IValidationConditionListener listener) {
		Assert.paramNotNull(listener, "listener");
		validationConditionListener.add(listener);
	}

	@Override
	public final void removeValidationConditionListener(final IValidationConditionListener listener) {
		Assert.paramNotNull(listener, "listener");
		validationConditionListener.remove(listener);
	}

	public final void dispose() {
		validationConditionListener.clear();
	}

	public interface IValidationResultCreator {
		IValidationResult createValidationResult();
	}

}
