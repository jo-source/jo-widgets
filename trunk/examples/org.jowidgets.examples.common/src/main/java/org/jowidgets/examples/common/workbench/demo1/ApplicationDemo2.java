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

package org.jowidgets.examples.common.workbench.demo1;

import java.util.LinkedList;
import java.util.List;

import org.jowidgets.api.command.IAction;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.types.QuestionResult;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.IVetoable;
import org.jowidgets.examples.common.icons.SilkIcons;
import org.jowidgets.examples.common.workbench.base.AbstractApplication;
import org.jowidgets.workbench.api.IComponentNode;
import org.jowidgets.workbench.api.ITrayItem;
import org.jowidgets.workbench.api.IWorkbenchApplicationContext;

public class ApplicationDemo2 extends AbstractApplication {

	private static final String ID = ApplicationDemo2.class.getName();

	private IWorkbenchApplicationContext context;

	public ApplicationDemo2() {
		super(ID);
	}

	@Override
	public void onContextInitialize(final IWorkbenchApplicationContext context) {
		this.context = context;

		final ActionFactory actionFactory = new ActionFactory();
		final IAction addFolderAction = actionFactory.createAddFolderAction(context);

		//create menus
		context.getPopupMenu().addAction(addFolderAction);

		final List<IComponentNode> componentList3 = new LinkedList<IComponentNode>();
		componentList3.add(new ImportantComponentTreeNodeDemo1("IMPORTANT1", "Important"));
		final FolderNodeDemo folderNode2 = new FolderNodeDemo("MISC", "Misc", componentList3);
		context.add(folderNode2);
	}

	@Override
	public String getLabel() {
		return "App 2";
	}

	@Override
	public String getTooltip() {
		return "Application Demo 2";
	}

	@Override
	public IImageConstant getIcon() {
		return SilkIcons.USER_RED;
	}

	@Override
	public void onClose(final IVetoable vetoable) {
		final QuestionResult result = Toolkit.getQuestionPane().askYesNoQuestion("Would you really like to quit the application?");
		if (result != QuestionResult.YES) {
			vetoable.veto();
		}
		else {
			final ITrayItem trayItem = context.getWorkbenchContext().getTrayItem();
			if (trayItem != null) {
				trayItem.showWarning("Application closed", "You really have closed the application!");
			}
		}
	}

}
