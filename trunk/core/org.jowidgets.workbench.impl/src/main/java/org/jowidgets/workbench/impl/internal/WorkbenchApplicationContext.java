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

package org.jowidgets.workbench.impl.internal;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.ITabItem;
import org.jowidgets.api.widgets.ITree;
import org.jowidgets.api.widgets.ITreeNode;
import org.jowidgets.api.widgets.blueprint.ITreeBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.tools.layout.MigLayoutFactory;
import org.jowidgets.workbench.api.IComponentTreeNode;
import org.jowidgets.workbench.api.IWorkbenchApplication;
import org.jowidgets.workbench.api.IWorkbenchApplicationContext;
import org.jowidgets.workbench.api.IWorkbenchContext;

public class WorkbenchApplicationContext implements IWorkbenchApplicationContext {

	private final WorkbenchContext workbenchContext;
	private final ITree tree;
	private final IContainer contentContainer;

	public WorkbenchApplicationContext(
		final WorkbenchContext workbenchContext,
		final ITabItem tabItem,
		final IContainer contentContainer,
		final IWorkbenchApplication application) {

		this.workbenchContext = workbenchContext;
		this.contentContainer = contentContainer;

		final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();

		tabItem.setText(application.getLabel());
		tabItem.setToolTipText(application.getTooltip());
		tabItem.setIcon(application.getIcon());

		tabItem.setLayout(MigLayoutFactory.growingInnerCellLayout());

		final ITreeBluePrint treeBp = bpf.tree().singleSelection().setContentScrolled(true);
		this.tree = tabItem.add(treeBp, MigLayoutFactory.GROWING_CELL_CONSTRAINTS);

		for (final IComponentTreeNode treeNode : application.createComponentTreeNodes()) {
			add(treeNode);
		}

		application.onContextInitialize(this);
	}

	@Override
	public void add(final IComponentTreeNode componentTreeNode) {
		add(tree.getChildren().size(), componentTreeNode);
	}

	@Override
	public void add(final int index, final IComponentTreeNode componentTreeNode) {
		final ITreeNode node = tree.addNode(index);
		new ComponentTreeNodeContext(componentTreeNode, node, null, this, workbenchContext);
	}

	@Override
	public void remove(final IComponentTreeNode componentTreeNode) {
		// TODO MG implement remove component tree node
	}

	@Override
	public IWorkbenchContext getWorkbenchContext() {
		return workbenchContext;
	}

	protected IContainer getContentContainer() {
		return contentContainer;
	}

}
