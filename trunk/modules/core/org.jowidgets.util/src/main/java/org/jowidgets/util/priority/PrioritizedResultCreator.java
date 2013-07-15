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

package org.jowidgets.util.priority;

import org.jowidgets.util.Assert;
import org.jowidgets.util.NullCompatibleComparison;

public final class PrioritizedResultCreator<VALUE_TYPE, PRIORITY_TYPE extends Comparable<PRIORITY_TYPE>> implements
		IPriorityValue<VALUE_TYPE, PRIORITY_TYPE> {

	private final PRIORITY_TYPE maxPriority;

	private IPriorityValue<VALUE_TYPE, PRIORITY_TYPE> currentResult;

	/**
	 * Creates a new instance.
	 * 
	 * @param maxPriority The maximal priority of the priority type
	 */
	public PrioritizedResultCreator(final PRIORITY_TYPE maxPriority) {
		Assert.paramNotNull(maxPriority, "maxPriority");
		this.maxPriority = maxPriority;
	}

	/**
	 * Adds a new (partial) result.
	 * 
	 * @param result The result to add, may be null (null will be ignored)
	 * @return true is result creator has max prio, false otherwise
	 */
	public boolean addResult(final IPriorityValue<VALUE_TYPE, PRIORITY_TYPE> result) {
		if (result != null) {
			final PRIORITY_TYPE currentPrio = currentResult != null ? currentResult.getPriority() : null;
			if (NullCompatibleComparison.compareTo(result.getPriority(), currentPrio) >= 0) {
				currentResult = result;
			}
		}
		return hasMaxPrio();
	}

	/**
	 * @return True, if the max priority is reached
	 */
	public boolean hasMaxPrio() {
		if (currentResult != null) {
			return NullCompatibleComparison.compareTo(currentResult.getPriority(), maxPriority) >= 0;
		}
		else {
			return false;
		}
	}

	/**
	 * @return The current result, may be null if no results was added
	 */
	public IPriorityValue<VALUE_TYPE, PRIORITY_TYPE> getResult() {
		return currentResult;
	}

	@Override
	public VALUE_TYPE getValue() {
		if (currentResult != null) {
			return currentResult.getValue();
		}
		else {
			return null;
		}
	}

	@Override
	public PRIORITY_TYPE getPriority() {
		if (currentResult != null) {
			return currentResult.getPriority();
		}
		else {
			return null;
		}
	}

}
