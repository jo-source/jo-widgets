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

public final class ExecutableState implements IExecutableState {

	public static final ExecutableState EXECUTABLE = new ExecutableState();

	private final boolean executable;
	private final String reason;

	private ExecutableState() {
		this(true, null);
	}

	private ExecutableState(final boolean executable, final String reason) {
		this.executable = executable;
		this.reason = reason;
	}

	/**
	 * @return true, if the action is executable, false otherwise
	 */
	@Override
	public boolean isExecutable() {
		return executable;
	}

	/**
	 * If the action is not executable, this get's the reason why. This reason should
	 * be offered to the user, e.g. in the tooltip of a disabled button or menu item
	 * 
	 * @return the reason (why is this button grey ?)
	 */
	@Override
	public String getReason() {
		return reason;
	}

	public static ExecutableState notExecutable(final String reason) {
		return new ExecutableState(false, reason);
	}
}
