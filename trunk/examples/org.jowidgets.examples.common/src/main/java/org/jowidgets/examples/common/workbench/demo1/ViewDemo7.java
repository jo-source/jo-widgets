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

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.ITree;
import org.jowidgets.api.widgets.ITreeNode;
import org.jowidgets.api.widgets.blueprint.ITreeBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.examples.common.icons.SilkIcons;
import org.jowidgets.examples.common.workbench.base.AbstractView;
import org.jowidgets.tools.layout.MigLayoutFactory;
import org.jowidgets.workbench.api.IView;
import org.jowidgets.workbench.api.IViewContext;

public class ViewDemo7 extends AbstractView implements IView {

	public static final String ID = ViewDemo7.class.getName();
	public static final String DEFAULT_LABEL = "Reports";
	public static final String DEFAULT_TOOLTIP = "Reports view";
	public static final IImageConstant DEFAULT_ICON = SilkIcons.ARROW_RIGHT;

	public ViewDemo7(final IViewContext context) {
		super(ID);
		final IContainer container = context.getContainer();
		container.setLayout(MigLayoutFactory.growingCellLayout());

		final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();

		final ITreeBluePrint treeBp = bpf.tree();

		final ITree tree = container.add(treeBp, MigLayoutFactory.GROWING_CELL_CONSTRAINTS);
		container.setBackgroundColor(tree.getBackgroundColor());

		final ITreeNode externalNode = tree.addNode(bpf.treeNode().setText("External").setIcon(SilkIcons.REPORT));
		final ITreeNode internalNode = tree.addNode(bpf.treeNode().setText("Internal").setIcon(SilkIcons.EMAIL));

		externalNode.addNode(bpf.treeNode().setText("Customer Report").setIcon(SilkIcons.REPORT_GO));
		externalNode.addNode(bpf.treeNode().setText("Info Post").setIcon(SilkIcons.GROUP_GO));

		internalNode.addNode(bpf.treeNode().setText("Malcom").setIcon(SilkIcons.USER_GO));
		internalNode.addNode(bpf.treeNode().setText("Paul").setIcon(SilkIcons.USER_GO));
		internalNode.addNode(bpf.treeNode().setText("Marry").setIcon(SilkIcons.USER_GO));
		internalNode.addNode(bpf.treeNode().setText("Lisa").setIcon(SilkIcons.USER_GO));
		internalNode.addNode(bpf.treeNode().setText("Pete").setIcon(SilkIcons.USER_GO));
		internalNode.addNode(bpf.treeNode().setText("Bruce").setIcon(SilkIcons.USER_GO));
		internalNode.addNode(bpf.treeNode().setText("Joe").setIcon(SilkIcons.USER_GO));

		internalNode.setExpanded(true);

		context.getToolBar().addActionItem(null, "Add external", SilkIcons.REPORT);
		context.getToolBar().addActionItem(null, "Add mail", SilkIcons.EMAIL);

	}
}
