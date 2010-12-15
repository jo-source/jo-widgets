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
import org.jowidgets.api.command.IActionBuilder;
import org.jowidgets.api.command.ICommand;
import org.jowidgets.api.command.ICommandProvider;
import org.jowidgets.api.command.IExecutableStateChecker;
import org.jowidgets.api.command.IExecutableStateDisplay;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Accelerator;

public class ActionBuilder implements IActionBuilder {

	private String text;
	private String toolTipText;
	private IImageConstant icon;
	private char mnemonic;
	private Accelerator accelerator;
	private boolean enabled;
	private IExecutableStateDisplay executableStateDisplay;
	private boolean isTooltipStateDisplay;

	private ICommandProvider commandProvider;

	public ActionBuilder() {
		this.enabled = true;
		this.isTooltipStateDisplay = true;
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
	public IActionBuilder setEnabled(final boolean enabled) {
		this.enabled = enabled;
		return this;
	}

	@Override
	public IActionBuilder setCommandProvider(final ICommandProvider commandProvider) {
		this.commandProvider = commandProvider;
		return this;
	}

	@Override
	public IActionBuilder setCommand(final ICommand command) {
		this.commandProvider = new CommandProvider(command);
		return this;
	}

	@Override
	public IActionBuilder setCommand(final ICommand command, final IExecutableStateChecker executableStateChecker) {
		this.commandProvider = new CommandProvider(command, executableStateChecker);
		return this;
	}

	@Override
	public IActionBuilder setTooltipStateDisplay(final boolean enabled) {
		this.isTooltipStateDisplay = enabled;
		return this;
	}

	@Override
	public IActionBuilder setExecutableStateDisplay(final IExecutableStateDisplay executableStateDisplay) {
		this.executableStateDisplay = executableStateDisplay;
		return this;
	}

	@Override
	public IAction build() {
		final IAction result = new Action(
			text,
			toolTipText,
			icon,
			mnemonic,
			accelerator,
			enabled,
			executableStateDisplay,
			isTooltipStateDisplay,
			commandProvider);
		return result;
	}
}
