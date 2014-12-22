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

public final class PriorityValue<VALUE_TYPE, PRIORITY_TYPE extends Comparable<PRIORITY_TYPE>> implements
		IPriorityValue<VALUE_TYPE, PRIORITY_TYPE> {

	private final VALUE_TYPE value;
	private final PRIORITY_TYPE priority;

	public PriorityValue(final VALUE_TYPE value, final PRIORITY_TYPE priority) {
		Assert.paramNotNull(priority, "priority");
		this.value = value;
		this.priority = priority;
	}

	@Override
	public VALUE_TYPE getValue() {
		return value;
	}

	@Override
	public PRIORITY_TYPE getPriority() {
		return priority;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((priority == null) ? 0 : priority.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof PriorityValue)) {
			return false;
		}
		final IPriorityValue<?, ?> other = (IPriorityValue<?, ?>) obj;
		if (priority == null) {
			if (other.getPriority() != null) {
				return false;
			}
		}
		else if (!priority.equals(other.getPriority())) {
			return false;
		}
		if (value == null) {
			if (other.getValue() != null) {
				return false;
			}
		}
		else if (!value.equals(other.getValue())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "PriorityValue [value=" + value + ", priority=" + priority + "]";
	}

}
