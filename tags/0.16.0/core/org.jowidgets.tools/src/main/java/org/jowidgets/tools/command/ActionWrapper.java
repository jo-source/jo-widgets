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

package org.jowidgets.tools.command;

import org.jowidgets.api.command.IAction;
import org.jowidgets.api.command.IActionChangeObservable;
import org.jowidgets.api.command.IExceptionHandler;
import org.jowidgets.api.command.IExecutionContext;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Accelerator;
import org.jowidgets.util.Assert;
import org.jowidgets.util.wrapper.IWrapper;

public class ActionWrapper implements IAction, IWrapper<IAction> {

	private final IAction action;

	public ActionWrapper(final IAction action) {
		Assert.paramNotNull(action, "action");
		this.action = action;
	}

	@Override
	public String getText() {
		return action.getText();
	}

	@Override
	public String getToolTipText() {
		return action.getToolTipText();
	}

	@Override
	public IImageConstant getIcon() {
		return action.getIcon();
	}

	@Override
	public Character getMnemonic() {
		return action.getMnemonic();
	}

	@Override
	public Accelerator getAccelerator() {
		return action.getAccelerator();
	}

	@Override
	public boolean isEnabled() {
		return action.isEnabled();
	}

	@Override
	public void execute(final IExecutionContext actionEvent) throws Exception {
		action.execute(actionEvent);
	}

	@Override
	public IExceptionHandler getExceptionHandler() {
		return action.getExceptionHandler();
	}

	@Override
	public IActionChangeObservable getActionChangeObservable() {
		return action.getActionChangeObservable();
	}

	@Override
	public IAction unwrap() {
		return action;
	}

}
