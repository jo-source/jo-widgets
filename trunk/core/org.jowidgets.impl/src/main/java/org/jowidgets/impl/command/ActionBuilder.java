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

import org.jowidgets.api.command.IActionBuilder;
import org.jowidgets.api.command.ICommand;
import org.jowidgets.api.command.ICommandAction;
import org.jowidgets.api.command.ICommandExecutor;
import org.jowidgets.api.command.IEnabledChecker;
import org.jowidgets.api.command.IExceptionHandler;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Accelerator;
import org.jowidgets.common.types.Modifier;

public class ActionBuilder implements IActionBuilder {

	private String text;
	private String toolTipText;
	private IImageConstant icon;
	private char mnemonic;
	private Accelerator accelerator;
	private boolean enabled;

	private ICommand command;

	private IExceptionHandler exceptionHandler;

	public ActionBuilder() {
		this.enabled = true;
	}

	@Override
	public IActionBuilder setText(final String text) {
		this.text = text;
		return this;
	}

	@Override
	public IActionBuilder setToolTipText(final String toolTipText) {
		this.toolTipText = toolTipText;
		return this;
	}

	@Override
	public IActionBuilder setIcon(final IImageConstant icon) {
		this.icon = icon;
		return this;
	}

	@Override
	public IActionBuilder setMnemonic(final Character mnemonic) {
		this.mnemonic = mnemonic;
		return null;
	}

	@Override
	public IActionBuilder setMnemonic(final char mnemonic) {
		this.mnemonic = Character.valueOf(mnemonic);
		return this;
	}

	@Override
	public IActionBuilder setAccelerator(final Accelerator accelerator) {
		this.accelerator = accelerator;
		return this;
	}

	@Override
	public IActionBuilder setAccelerator(final char key, final Modifier... modifier) {
		return setAccelerator(new Accelerator(key, modifier));
	}

	@Override
	public IActionBuilder setEnabled(final boolean enabled) {
		this.enabled = enabled;
		return this;
	}

	@Override
	public IActionBuilder setCommand(final ICommand commandProvider) {
		this.command = commandProvider;
		return this;
	}

	@Override
	public IActionBuilder setCommand(final ICommandExecutor command) {
		this.command = new Command(command);
		return this;
	}

	@Override
	public IActionBuilder setCommand(final ICommandExecutor command, final IEnabledChecker executableStateChecker) {
		this.command = new Command(command, executableStateChecker);
		return this;
	}

	@Override
	public IActionBuilder setCommand(final ICommandExecutor command, final IExceptionHandler exceptionHandler) {
		this.command = new Command(command, exceptionHandler);
		return this;
	}

	@Override
	public IActionBuilder setCommand(
		final ICommandExecutor command,
		final IEnabledChecker enabledChecker,
		final IExceptionHandler exceptionHandler) {
		this.command = new Command(command, enabledChecker, exceptionHandler);
		return this;
	}

	@Override
	public IActionBuilder setActionExceptionHandler(final IExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
		return this;
	}

	@Override
	public ICommandAction build() {
		final ICommandAction result = new CommandAction(
			text,
			toolTipText,
			icon,
			mnemonic,
			accelerator,
			enabled,
			command,
			exceptionHandler);
		return result;
	}

}
