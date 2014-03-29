/*
 * Copyright (c) 2014, Michael
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
import org.jowidgets.api.command.ITreeExpansionAction;
import org.jowidgets.api.widgets.ITreeContainer;
import org.jowidgets.tools.command.ActionWrapper;

final class TreeExpansionAction extends ActionWrapper implements ITreeExpansionAction {

	private final TreeExpansionExecutor executor;
	private final TreeExpansionEnabledChecker enabledChecker;

	TreeExpansionAction(final IActionBuilder builder, final ITreeContainer tree, final boolean expanded, final Integer pivotLevel) {
		super(createAction(builder, tree, expanded, pivotLevel));

		final ICommand command = ((ICommandAction) unwrap()).getCommand();

		this.executor = (TreeExpansionExecutor) command.getCommandExecutor();
		this.enabledChecker = (TreeExpansionEnabledChecker) command.getEnabledChecker();
	}

	private static ICommandAction createAction(
		final IActionBuilder builder,
		final ITreeContainer tree,
		final boolean expanded,
		final Integer level) {

		final ICommandExecutor executor = new TreeExpansionExecutor(tree, expanded, level);
		final IEnabledChecker enabledChecker = new TreeExpansionEnabledChecker(tree, expanded, level);

		return builder.setCommand(executor, enabledChecker).build();
	}

	@Override
	public void setPivotLevel(final Integer pivotLevel) {
		executor.setPivotLevel(pivotLevel);
		enabledChecker.setPivotLevel(pivotLevel);
	}

}
