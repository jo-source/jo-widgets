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

import org.jowidgets.api.command.EnabledState;
import org.jowidgets.api.command.IActionChangeListener;
import org.jowidgets.api.command.IActionChangeObservable;
import org.jowidgets.api.command.ICommand;
import org.jowidgets.api.command.ICommandAction;
import org.jowidgets.api.command.ICommandExecutor;
import org.jowidgets.api.command.IEnabledChecker;
import org.jowidgets.api.command.IEnabledState;
import org.jowidgets.api.command.IExceptionHandler;
import org.jowidgets.api.command.IExecutionContext;
import org.jowidgets.api.controler.IChangeListener;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Accelerator;

class CommandActionImpl implements ICommandAction, IActionChangeObservable {

	private final Set<IActionChangeListener> actionChangeListeners;

	private String text;
	private String toolTipText;

	private IImageConstant icon;
	private final char mnemonic;
	private final Accelerator accelerator;

	private final IChangeListener enabledStateListener;

	private boolean enabled;

	private IEnabledState enabledState;

	private ICommand command;

	private IExceptionHandler exceptionHandler;

	public CommandActionImpl(
		final String text,
		final String toolTipText,
		final IImageConstant icon,
		final char mnemonic,
		final Accelerator accelerator,
		final boolean enabled,
		final ICommand command,
		final IExceptionHandler exceptionHandler) {
		super();

		this.enabledStateListener = new EnabledStateListener();
		this.actionChangeListeners = new HashSet<IActionChangeListener>();

		this.text = text;
		this.toolTipText = toolTipText;
		this.icon = icon;
		this.mnemonic = mnemonic;
		this.accelerator = accelerator;
		this.enabled = enabled;

		this.exceptionHandler = exceptionHandler;

		setCommand(command);
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public String getToolTipText() {
		final IEnabledState currentEnabledState = getEnabledState();
		if (!currentEnabledState.isEnabled() && currentEnabledState.getReason() != null) {
			return currentEnabledState.getReason();
		}
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
		return getEnabledState().isEnabled();
	}

	@Override
	public void setText(final String text) {
		this.text = text;
		fireTextChanged();
	}

	@Override
	public void setToolTipText(final String toolTipText) {
		this.toolTipText = toolTipText;
		fireToolTipTextChanged();
	}

	@Override
	public void setIcon(final IImageConstant icon) {
		this.icon = icon;
		fireIconChanged();
	}

	@Override
	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
		fireEnabledChanged();
		fireToolTipTextChanged();
	}

	@Override
	public void setActionExceptionHandler(final IExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

	@Override
	public IExceptionHandler getExceptionHandler() {
		return this.exceptionHandler;
	}

	@Override
	public void setCommand(final ICommand command) {

		//remove the executable state listener on the old provider
		if (this.command != null && this.command.getEnabledChecker() != null) {
			this.command.getEnabledChecker().removeChangeListener(enabledStateListener);
		}

		if (command != null) {
			this.command = command;
		}
		else {
			this.command = new Command();
		}

		if (this.command.getEnabledChecker() != null) {
			this.command.getEnabledChecker().addChangeListener(enabledStateListener);
		}

		enabledState = null;
		fireEnabledChanged();
		fireToolTipTextChanged();
	}

	@Override
	public void setCommand(final ICommandExecutor command) {
		setCommand(new Command(command));
	}

	@Override
	public void setCommand(final ICommandExecutor command, final IEnabledChecker executableStateChecker) {
		setCommand(new Command(command, executableStateChecker));
	}

	@Override
	public void setCommand(final ICommandExecutor command, final IExceptionHandler exceptionHandler) {
		setCommand(new Command(command, exceptionHandler));
	}

	@Override
	public void setCommand(
		final ICommandExecutor command,
		final IEnabledChecker enabledChecker,
		final IExceptionHandler exceptionHandler) {
		setCommand(command, enabledChecker, exceptionHandler);
	}

	@Override
	public void execute(final IExecutionContext executionContext) throws Exception {
		final ICommandExecutor commandExecutor = command.getCommandExecutor();
		if (commandExecutor != null) {
			try {
				commandExecutor.execute(executionContext);
			}
			catch (final Exception exception) {
				final IExceptionHandler commandExceptionHandler = command.getExceptionHandler();
				if (commandExceptionHandler != null) {
					commandExceptionHandler.handleException(executionContext, exception);
				}
				else {
					throw exception;
				}
			}
		}
	}

	private IEnabledState getEnabledState() {
		if (enabled) {
			if (enabledState == null) {
				if (command.getEnabledChecker() != null && command.getCommandExecutor() != null) {
					enabledState = command.getEnabledChecker().getEnabledState();
				}
				else if (command.getCommandExecutor() == null) {
					enabledState = EnabledState.DISABLED;
				}
				else {
					enabledState = EnabledState.ENABLED;
				}
			}
			return enabledState;
		}
		else {
			return EnabledState.DISABLED;
		}
	}

	@Override
	public IActionChangeObservable getActionChangeObservable() {
		return this;
	}

	@Override
	public void addActionChangeListener(final IActionChangeListener listener) {
		actionChangeListeners.add(listener);
	}

	@Override
	public void removeActionChangeListener(final IActionChangeListener listener) {
		actionChangeListeners.remove(listener);
	}

	private void fireTextChanged() {
		for (final IActionChangeListener listener : actionChangeListeners) {
			listener.textChanged();
		}
	}

	private void fireToolTipTextChanged() {
		for (final IActionChangeListener listener : actionChangeListeners) {
			listener.toolTipTextChanged();
		}
	}

	private void fireIconChanged() {
		for (final IActionChangeListener listener : actionChangeListeners) {
			listener.iconChanged();
		}
	}

	private void fireEnabledChanged() {
		for (final IActionChangeListener listener : actionChangeListeners) {
			listener.enabledChanged();
		}
	}

	private class EnabledStateListener implements IChangeListener {
		@Override
		public void changedEvent() {
			enabledState = null;
			fireEnabledChanged();
			fireToolTipTextChanged();
		}
	}

}
