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

package org.jowidgets.api.command;

public final class EnabledState implements IEnabledState {

	public static final EnabledState ENABLED = new EnabledState();
	public static final EnabledState DISABLED = new EnabledState(false, null);

	private final boolean enabled;
	private final String reason;

	private EnabledState() {
		this(true, null);
	}

	private EnabledState(final boolean enabled, final String reason) {
		this.enabled = enabled;
		this.reason = reason;
	}

	/**
	 * @return true, if the command is enabled, false otherwise
	 */
	@Override
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * If the command is not enabled, this get's the reason why. This reason should
	 * be offered to the user, e.g. in the tooltip of a disabled button or menu item
	 * 
	 * @return the reason (why is this button grey ?)
	 */
	@Override
	public String getReason() {
		return reason;
	}

	public static EnabledState disabled(final String reason) {
		return new EnabledState(false, reason);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (enabled ? 1231 : 1237);
		result = prime * result + ((reason == null) ? 0 : reason.hashCode());
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
		if (getClass() != obj.getClass()) {
			return false;
		}
		final EnabledState other = (EnabledState) obj;
		if (enabled != other.enabled) {
			return false;
		}
		if (reason == null) {
			if (other.reason != null) {
				return false;
			}
		}
		else if (!reason.equals(other.reason)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "EnabledState [enabled=" + enabled + ", reason=" + reason + "]";
	}

}
