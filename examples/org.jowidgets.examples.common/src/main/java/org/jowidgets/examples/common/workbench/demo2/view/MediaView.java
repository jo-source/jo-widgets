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

package org.jowidgets.examples.common.workbench.demo2.view;

import org.jowidgets.api.command.IAction;
import org.jowidgets.api.command.IActionBuilder;
import org.jowidgets.api.command.ICommandExecutor;
import org.jowidgets.api.command.IExecutionContext;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.ITree;
import org.jowidgets.api.widgets.ITreeNode;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.examples.common.icons.SilkIcons;
import org.jowidgets.examples.common.workbench.base.AbstractDemoView;
import org.jowidgets.tools.command.ActionBuilder;
import org.jowidgets.tools.layout.MigLayoutFactory;
import org.jowidgets.tools.model.item.MenuModel;
import org.jowidgets.workbench.api.IView;
import org.jowidgets.workbench.api.IViewContext;

public class MediaView extends AbstractDemoView implements IView {

	public static final String ID = MediaView.class.getName();
	public static final String DEFAULT_LABEL = "Media";
	public static final String DEFAULT_TOOLTIP = "Media View";
	public static final IImageConstant DEFAULT_ICON = SilkIcons.ATTACH;

	private final ITree tree;
	private final IAction collapseTreeAction;
	private final IAction expandTreeAction;

	public MediaView(final IViewContext context) {
		super(ID);

		final IContainer container = context.getContainer();
		container.setLayout(MigLayoutFactory.growingCellLayout());

		final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();

		this.tree = container.add(bpf.tree(), MigLayoutFactory.GROWING_CELL_CONSTRAINTS);
		container.setBackgroundColor(tree.getBackgroundColor());
		createTreeContext(tree);

		this.collapseTreeAction = createCollapseTreeAction();
		this.expandTreeAction = createExpandTreeAction();

		tree.setPopupMenu(createPopupMenu());

		context.getToolBar().addAction(expandTreeAction);
		context.getToolBar().addAction(collapseTreeAction);
	}

	private void createTreeContext(final ITree tree) {
		final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();

		final ITreeNode cdNode = tree.addNode(bpf.treeNode().setText("CD").setIcon(SilkIcons.CD));
		final ITreeNode dvdNode = tree.addNode(bpf.treeNode().setText("DVD").setIcon(SilkIcons.DVD));
		final ITreeNode diskNode = tree.addNode(bpf.treeNode().setText("Drive").setIcon(SilkIcons.DRIVE));
		final ITreeNode floppyNode = tree.addNode(bpf.treeNode().setText("Floppy").setIcon(SilkIcons.DISK));

		cdNode.addNode(bpf.treeNode().setText("Bugs").setIcon(SilkIcons.BUG));
		cdNode.addNode(bpf.treeNode().setText("Charts").setIcon(SilkIcons.CHART_BAR));
		cdNode.addNode(bpf.treeNode().setText("Camera").setIcon(SilkIcons.CAMERA));
		cdNode.addNode(bpf.treeNode().setText("Organization").setIcon(SilkIcons.CHART_ORGANISATION));

		dvdNode.addNode(bpf.treeNode().setText("Bugs").setIcon(SilkIcons.BUG));
		dvdNode.addNode(bpf.treeNode().setText("Charts").setIcon(SilkIcons.CHART_BAR));
		dvdNode.addNode(bpf.treeNode().setText("Camera").setIcon(SilkIcons.CAMERA));
		dvdNode.addNode(bpf.treeNode().setText("Organization").setIcon(SilkIcons.CHART_ORGANISATION));

		diskNode.addNode(bpf.treeNode().setText("Bugs").setIcon(SilkIcons.BUG));
		diskNode.addNode(bpf.treeNode().setText("Charts").setIcon(SilkIcons.CHART_BAR));
		diskNode.addNode(bpf.treeNode().setText("Camera").setIcon(SilkIcons.CAMERA));
		diskNode.addNode(bpf.treeNode().setText("Organization").setIcon(SilkIcons.CHART_ORGANISATION));

		floppyNode.addNode(bpf.treeNode().setText("Bugs").setIcon(SilkIcons.BUG));
		floppyNode.addNode(bpf.treeNode().setText("Charts").setIcon(SilkIcons.CHART_BAR));
		floppyNode.addNode(bpf.treeNode().setText("Camera").setIcon(SilkIcons.CAMERA));
		floppyNode.addNode(bpf.treeNode().setText("Organization").setIcon(SilkIcons.CHART_ORGANISATION));

		diskNode.setExpanded(true);
	}

	private IMenuModel createPopupMenu() {
		final IMenuModel result = new MenuModel();
		result.addAction(expandTreeAction);
		result.addAction(collapseTreeAction);
		return result;
	}

	private IAction createExpandTreeAction() {
		final IActionBuilder builder = new ActionBuilder();
		builder.setText("Expand tree");
		builder.setToolTipText("Expands all nodes of the tree");
		builder.setIcon(SilkIcons.MAGNIFIER_ZOOM_IN);

		builder.setCommand(new ICommandExecutor() {
			@Override
			public void execute(final IExecutionContext executionContext) throws Exception {
				tree.setAllChildrenExpanded(true);
			}
		});
		return builder.build();
	}

	private IAction createCollapseTreeAction() {
		final IActionBuilder builder = new ActionBuilder();
		builder.setText("Collapse tree");
		builder.setToolTipText("Collapses all nodes of the tree");
		builder.setIcon(SilkIcons.MAGIFIER_ZOOM_OUT);

		builder.setCommand(new ICommandExecutor() {
			@Override
			public void execute(final IExecutionContext executionContext) throws Exception {
				tree.setAllChildrenExpanded(false);
			}
		});
		return builder.build();
	}

}
