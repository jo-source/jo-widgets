/*
 * Copyright (c) 2013, grossmann
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

package org.jowidgets.examples.common.workbench.demo3.command;

import org.jowidgets.addons.icons.silkicons.SilkIcons;
import org.jowidgets.api.command.CommandAction;
import org.jowidgets.api.command.IAction;
import org.jowidgets.api.command.IActionBuilder;
import org.jowidgets.api.command.ICommandExecutor;
import org.jowidgets.api.command.IExecutionContext;
import org.jowidgets.examples.common.workbench.demo3.model.BeanTableModel;
import org.jowidgets.examples.common.workbench.demo3.model.Role;

public final class DeleteRoleActionFactory {

	private DeleteRoleActionFactory() {}

	public static IAction create(final BeanTableModel<Role> model) {
		final IActionBuilder builder = CommandAction.builder();
		builder.setText("Delete role");
		builder.setIcon(SilkIcons.GROUP_DELETE);
		builder.setCommand(new DeleteRoleCommand(model), new SingleSelectionEnabledChecker(model));
		return builder.build();
	}

	private static final class DeleteRoleCommand implements ICommandExecutor {

		private final BeanTableModel<Role> model;

		private DeleteRoleCommand(final BeanTableModel<Role> model) {
			this.model = model;
		}

		@Override
		public void execute(final IExecutionContext executionContext) throws Exception {
			final Role bean = model.getSelectedBean();
			if (bean != null) {
				model.removeBean(bean);
			}
		}

	}

}
