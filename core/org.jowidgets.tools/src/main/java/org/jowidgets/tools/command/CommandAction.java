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

import org.jowidgets.api.command.IActionBuilder;
import org.jowidgets.api.command.IActionChangeObservable;
import org.jowidgets.api.command.ICommand;
import org.jowidgets.api.command.ICommandAction;
import org.jowidgets.api.command.ICommandExecutor;
import org.jowidgets.api.command.IEnabledChecker;
import org.jowidgets.api.command.IExceptionHandler;
import org.jowidgets.api.command.IExecutionContext;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Accelerator;

public class CommandAction implements ICommandAction {

	private final ICommandAction action;

	public CommandAction() {
		this(builder());
	}

	public CommandAction(final String text) {
		this(builder(text));
	}

	public CommandAction(final String text, final IImageConstant icon) {
		this(builder(text, icon));
	}

	public CommandAction(final String text, final String toolTipText) {
		this(builder(text, toolTipText));
	}

	public CommandAction(final String text, final String toolTipText, final IImageConstant icon) {
		this(builder(text, toolTipText, icon));
	}

	public CommandAction(final IActionBuilder builder) {
		super();
		this.action = builder.build();
	}

	@Override
	public void setText(final String text) {
		action.setText(text);
	}

	@Override
	public String getText() {
		return action.getText();
	}

	@Override
	public void setToolTipText(final String toolTipText) {
		action.setToolTipText(toolTipText);
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
	public void setIcon(final IImageConstant icon) {
		action.setIcon(icon);
	}

	@Override
	public Character getMnemonic() {
		return action.getMnemonic();
	}

	@Override
	public void setEnabled(final boolean enabled) {
		action.setEnabled(enabled);
	}

	@Override
	public Accelerator getAccelerator() {
		return action.getAccelerator();
	}

	@Override
	public void setCommand(final ICommand command) {
		action.setCommand(command);
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
	public void setCommand(final ICommandExecutor command) {
		action.setCommand(command);
	}

	@Override
	public void setCommand(final ICommandExecutor command, final IEnabledChecker enabledChecker) {
		action.setCommand(command, enabledChecker);
	}

	@Override
	public IExceptionHandler getExceptionHandler() {
		return action.getExceptionHandler();
	}

	@Override
	public void setCommand(final ICommandExecutor command, final IExceptionHandler exceptionHandler) {
		action.setCommand(command, exceptionHandler);
	}

	@Override
	public void setCommand(
		final ICommandExecutor command,
		final IEnabledChecker enabledChecker,
		final IExceptionHandler exceptionHandler) {
		action.setCommand(command, enabledChecker, exceptionHandler);
	}

	@Override
	public void setActionExceptionHandler(final IExceptionHandler exceptionHandler) {
		action.setActionExceptionHandler(exceptionHandler);
	}

	@Override
	public IActionChangeObservable getActionChangeObservable() {
		return action.getActionChangeObservable();
	}

	public static IActionBuilder builder() {
		return Toolkit.getActionBuilderFactory().create();
	}

	public static IActionBuilder builder(final String text) {
		return builder().setText(text);
	}

	public static IActionBuilder builder(final String text, final IImageConstant icon) {
		return builder().setText(text).setIcon(icon);
	}

	public static IActionBuilder builder(final String text, final String toolTipText) {
		return builder().setText(text).setToolTipText(toolTipText);
	}

	public static IActionBuilder builder(final String text, final String toolTipText, final IImageConstant icon) {
		return builder().setText(text).setToolTipText(toolTipText).setIcon(icon);
	}

}
