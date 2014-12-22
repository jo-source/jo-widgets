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

package org.jowidgets.impl.command;

import org.jowidgets.api.command.IAction;
import org.jowidgets.api.command.IExceptionHandler;
import org.jowidgets.api.command.IExecutionContext;
import org.jowidgets.tools.command.DefaultExceptionHandler;

public class ActionExecuter {

	private final IAction action;
	private final IActionWidget actionWidget;
	private final IExceptionHandler defaultExceptionHandler;

	public ActionExecuter(final IAction action, final IActionWidget actionWidget) {
		super();
		this.action = action;
		this.actionWidget = actionWidget;
		this.defaultExceptionHandler = new DefaultExceptionHandler();
	}

	public void execute() {
		if (action.isEnabled()) {
			try {
				executeCommand();
			}
			catch (final Exception exception) {
				Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), exception);
			}
		}
		//else do nothing
	}

	private void executeCommand() throws Exception {
		final IExecutionContext executionContext = new ExecutionContext(action, actionWidget);
		try {
			action.execute(executionContext);
		}
		catch (final Exception exception) {
			if (action.getExceptionHandler() != null) {
				action.getExceptionHandler().handleException(executionContext, exception);
			}
			else {
				defaultExceptionHandler.handleException(executionContext, exception);
			}
		}
	}

}
