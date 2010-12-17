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

import java.util.HashSet;
import java.util.Set;

import org.jowidgets.api.command.ExecutableState;
import org.jowidgets.api.command.IAction;
import org.jowidgets.api.command.ICommand;
import org.jowidgets.api.command.ICommandProvider;
import org.jowidgets.api.command.IExecutableState;
import org.jowidgets.api.command.IExecutableStateChecker;
import org.jowidgets.api.command.IExecutableStateListener;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Accelerator;
import org.jowidgets.common.widgets.controler.impl.ActionObservable;

public class Action extends ActionObservable implements IAction, IExecutableStateChecker {

	private static final IExecutableState ACTION_DISABLED_STATE = ExecutableState.notExecutable("Action is disabled!");
	private static final IExecutableState NO_COMMAND_STATE = ExecutableState.notExecutable("Action has no command!");

	private final Set<IExecutableStateListener> executableStateListeners;

	private final String text;
	private final String toolTipText;
	private final IImageConstant icon;
	private final char mnemonic;
	private final Accelerator accelerator;
	private final boolean isAutoDisableItems;
	private final boolean isTooltipStateDisplay;
	private final IExecutableStateListener executableStateListener;

	private boolean enabled;
	private IExecutableState executableState;

	private ICommandProvider commandProvider;

	public Action(
		final String text,
		final String toolTipText,
		final IImageConstant icon,
		final char mnemonic,
		final Accelerator accelerator,
		final boolean enabled,
		final boolean isAutoDisableItems,
		final boolean isTooltipStateDisplay,
		final ICommandProvider commandProvider) {
		super();

		this.executableStateListener = new ExecutabelStateListener();
		this.executableStateListeners = new HashSet<IExecutableStateListener>();

		this.text = text;
		this.toolTipText = toolTipText;
		this.icon = icon;
		this.mnemonic = mnemonic;
		this.accelerator = accelerator;
		this.enabled = enabled;
		this.isAutoDisableItems = isAutoDisableItems;
		this.isTooltipStateDisplay = isTooltipStateDisplay;

		setCommandProvider(commandProvider);
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public String getToolTipText() {
		return toolTipText;
	}

	@Override
	public IImageConstant getIcon() {
		return icon;
	}

	@Override
	public Character getMnemonic() {
		return mnemonic;
	}

	@Override
	public Accelerator getAccelerator() {
		return accelerator;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public boolean isTooltipShowExecutableState() {
		return isTooltipStateDisplay;
	}

	@Override
	public boolean isAutoDisableItems() {
		return isAutoDisableItems;
	}

	@Override
	public ICommand getCommand() {
		return commandProvider.getCommand();
	}

	@Override
	public IExecutableStateChecker getExecutableStateChecker() {
		return commandProvider.getExecutableStateChecker();
	}

	@Override
	public void setEnabled(final boolean enabled) {
		if (enabled != this.enabled) {
			this.enabled = enabled;
			fireExecutableStateChanged();
		}
	}

	@Override
	public void setCommandProvider(final ICommandProvider commandProvider) {

		//remove the executable state listener on the old provider
		if (isAutoDisableItems() && this.commandProvider != null && this.commandProvider.getExecutableStateChecker() != null) {
			this.commandProvider.getExecutableStateChecker().removeExecutableStateListener(executableStateListener);
		}

		if (commandProvider != null) {
			this.commandProvider = commandProvider;
		}
		else {
			this.commandProvider = new CommandProvider();
		}

		if (this.commandProvider.getExecutableStateChecker() != null) {
			this.commandProvider.getExecutableStateChecker().addExecutableStateListener(executableStateListener);
		}

	}

	@Override
	public void setCommand(final ICommand command) {
		setCommandProvider(new CommandProvider(command));
	}

	@Override
	public void setCommand(final ICommand command, final IExecutableStateChecker executableStateChecker) {
		setCommandProvider(new CommandProvider(command, executableStateChecker));
	}

	@Override
	public IExecutableState getExecutableState() {
		if (isEnabled()) {
			if (executableState == null) {
				if (commandProvider.getExecutableStateChecker() != null) {
					executableState = commandProvider.getExecutableStateChecker().getExecutableState();
				}
				else if (commandProvider.getCommand() == null) {
					executableState = NO_COMMAND_STATE;
				}
				else {
					executableState = ExecutableState.EXECUTABLE;
				}
			}
			return executableState;
		}
		else {
			return ACTION_DISABLED_STATE;
		}
	}

	@Override
	public void addExecutableStateListener(final IExecutableStateListener listener) {
		this.executableStateListeners.add(listener);
	}

	@Override
	public void removeExecutableStateListener(final IExecutableStateListener listener) {
		this.executableStateListeners.remove(listener);
	}

	private void fireExecutableStateChanged() {
		for (final IExecutableStateListener listener : executableStateListeners) {
			listener.executableStateChanged();
		}
	}

	private class ExecutabelStateListener implements IExecutableStateListener {
		@Override
		public void executableStateChanged() {
			executableState = null;
			if (isAutoDisableItems || isTooltipStateDisplay) {
				fireExecutableStateChanged();
			}
		}
	}

}
