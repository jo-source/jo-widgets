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

import org.jowidgets.api.command.IAction;
import org.jowidgets.api.command.IActionChangeListener;
import org.jowidgets.api.command.ICommand;
import org.jowidgets.api.command.ICommandProvider;
import org.jowidgets.api.command.IExecutableState;
import org.jowidgets.api.command.IExecutableStateChecker;
import org.jowidgets.api.command.IExecutableStateDisplay;
import org.jowidgets.api.image.Icons;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.api.widgets.IWindow;
import org.jowidgets.api.widgets.blueprint.IMessageDialogBluePrint;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Accelerator;
import org.jowidgets.common.widgets.controler.impl.ActionObservable;

public class Action extends ActionObservable implements IAction {

	private final Set<IActionChangeListener> actionChangeListeners;

	private final String text;
	private final String toolTipText;
	private final IImageConstant icon;
	private final char mnemonic;
	private final Accelerator accelerator;
	private boolean enabled;
	private final IExecutableStateDisplay executableStateDisplay;
	private final boolean isTooltipStateDisplay;

	private ICommandProvider commandProvider;

	public Action(
		final String text,
		final String toolTipText,
		final IImageConstant icon,
		final char mnemonic,
		final Accelerator accelerator,
		final boolean enabled,
		final IExecutableStateDisplay executableStateDisplay,
		final boolean isTooltipStateDisplay,
		final ICommandProvider commandProvider) {
		super();

		this.actionChangeListeners = new HashSet<IActionChangeListener>();

		this.text = text;
		this.toolTipText = toolTipText;
		this.icon = icon;
		this.mnemonic = mnemonic;
		this.accelerator = accelerator;
		this.enabled = enabled;
		this.executableStateDisplay = executableStateDisplay;
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
	public IExecutableStateDisplay getExecutableStateDisplay() {
		return executableStateDisplay;
	}

	@Override
	public boolean isTooltipStateDisplay() {
		return isTooltipStateDisplay;
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
		this.enabled = enabled;
		fireEnabledStateChanged();
	}

	@Override
	public void setCommandProvider(final ICommandProvider commandProvider) {
		if (commandProvider != null) {
			this.commandProvider = commandProvider;
		}
		else {
			this.commandProvider = new CommandProvider();
		}
		fireCommandChanged();
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
	public void addActionChangeListener(final IActionChangeListener listener) {
		actionChangeListeners.add(listener);
	}

	@Override
	public void removeActionChangeListener(final IActionChangeListener listener) {
		actionChangeListeners.remove(listener);
	}

	@Override
	public void performAction(final IWidget source) {
		if (isEnabled()) {
			if (getExecutableStateChecker() != null) {
				final IExecutableState executableState = getExecutableStateChecker().getExecutableState();
				if (executableState != null && !executableState.isExecutable()) {
					final IMessageDialogBluePrint messageDialogBp = createMessageDialogBp().setIcon(Icons.INFO);
					final String reason = executableState.getReason();
					if (reason != null && !reason.isEmpty()) {
						messageDialogBp.setText(executableState.getReason());
					}
					else {
						messageDialogBp.setText("Could not execute action '" + getText() + "'");
					}
					showMessageDialog(source, messageDialogBp);
				}
				else {
					executeCommand(source);
				}
			}
			else {
				executeCommand(source);
			}
			fireActionPerformed();
		}
	}

	private void executeCommand(final IWidget source) {
		if (getCommand() != null) {
			try {
				getCommand().execute(this, source);
			}
			catch (final Exception exception) {
				final IMessageDialogBluePrint messageDialogBp = createMessageDialogBp().setIcon(Icons.ERROR);
				messageDialogBp.setText("Error occurred\n" + exception.getLocalizedMessage());
			}
		}
		else {
			final IMessageDialogBluePrint messageDialogBp = createMessageDialogBp().setIcon(Icons.ERROR);
			messageDialogBp.setText("No command defined for this action");
			showMessageDialog(source, messageDialogBp);
		}
	}

	private void fireCommandChanged() {
		for (final IActionChangeListener listener : actionChangeListeners) {
			listener.commandChanged();
		}
	}

	private void fireEnabledStateChanged() {
		for (final IActionChangeListener listener : actionChangeListeners) {
			listener.enabledStateChanged();
		}
	}

	private IMessageDialogBluePrint createMessageDialogBp() {
		final IMessageDialogBluePrint messageDialogBp = Toolkit.getBluePrintFactory().messageDialog();
		messageDialogBp.setTitleIcon(getIcon());
		messageDialogBp.setTitle(getText());
		return messageDialogBp;
	}

	private void showMessageDialog(final IWidget source, final IMessageDialogBluePrint messageDialogBp) {
		final IWindow parentWindow = Toolkit.getWidgetUtils().getWindowAncestor(source);
		parentWindow.createChildWindow(messageDialogBp).showMessage();
	}
}
