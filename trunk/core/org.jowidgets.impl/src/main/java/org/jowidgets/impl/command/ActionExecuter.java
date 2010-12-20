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
import org.jowidgets.api.image.Icons;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IWindow;
import org.jowidgets.api.widgets.blueprint.IMessageDialogBluePrint;

public class ActionExecuter {

	private final IAction action;
	private final IActionWidget actionWidget;

	public ActionExecuter(final IAction action, final IActionWidget actionWidget) {
		super();
		this.action = action;
		this.actionWidget = actionWidget;
	}

	public void execute() {
		if (action.isEnabled()) {
			executeCommand();
		}
		//else do nothing
	}

	private void executeCommand() {
		try {
			action.execute(new ExecutionContext(action, actionWidget));
		}
		catch (final Exception exception) {
			final IMessageDialogBluePrint messageDialogBp = createMessageDialogBp().setIcon(Icons.ERROR);
			messageDialogBp.setText("Error occurred\n" + exception.getLocalizedMessage());
			showMessageDialog(messageDialogBp);
		}
	}

	private IMessageDialogBluePrint createMessageDialogBp() {
		final IMessageDialogBluePrint messageDialogBp = Toolkit.getBluePrintFactory().messageDialog();
		messageDialogBp.setTitleIcon(action.getIcon());
		messageDialogBp.setTitle(action.getText());
		return messageDialogBp;
	}

	private void showMessageDialog(final IMessageDialogBluePrint messageDialogBp) {
		final IWindow parentWindow = Toolkit.getWidgetUtils().getWindowAncestor(actionWidget);
		parentWindow.createChildWindow(messageDialogBp).showMessage();
	}

}
