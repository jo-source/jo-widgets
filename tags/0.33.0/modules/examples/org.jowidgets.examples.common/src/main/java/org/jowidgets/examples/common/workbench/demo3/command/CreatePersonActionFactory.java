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
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IInputDialog;
import org.jowidgets.api.widgets.blueprint.IInputDialogBluePrint;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.examples.common.workbench.demo3.form.PersonContentCreator;
import org.jowidgets.examples.common.workbench.demo3.model.BeanTableModel;
import org.jowidgets.examples.common.workbench.demo3.model.Person;
import org.jowidgets.tools.widgets.blueprint.BPF;

public final class CreatePersonActionFactory {

	private CreatePersonActionFactory() {}

	public static IAction create(final BeanTableModel<Person> model) {
		final IActionBuilder builder = CommandAction.builder();
		builder.setText("Create person ...");
		builder.setIcon(SilkIcons.USER_ADD);
		builder.setCommand(new CreatePersonCommand(model));
		return builder.build();
	}

	private static final class CreatePersonCommand implements ICommandExecutor {

		private final BeanTableModel<Person> model;

		private CreatePersonCommand(final BeanTableModel<Person> model) {
			this.model = model;
		}

		@Override
		public void execute(final IExecutionContext executionContext) throws Exception {
			final IInputDialogBluePrint<Person> dialogBp = BPF.inputDialog(new PersonContentCreator(false));
			dialogBp.setMinPackSize(new Dimension(640, 480));
			dialogBp.setExecutionContext(executionContext);
			final IInputDialog<Person> dialog = Toolkit.getActiveWindow().createChildWindow(dialogBp);
			dialog.setVisible(true);
			if (dialog.isOkPressed()) {
				final Person person = dialog.getValue();
				model.addBean(person, true);
			}
		}

	}

}
