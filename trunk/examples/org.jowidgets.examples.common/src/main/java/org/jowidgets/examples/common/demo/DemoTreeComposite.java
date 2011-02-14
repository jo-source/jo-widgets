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

package org.jowidgets.examples.common.demo;

import org.jowidgets.api.controler.ITreeListener;
import org.jowidgets.api.controler.ITreePopupDetectionListener;
import org.jowidgets.api.controler.ITreePopupEvent;
import org.jowidgets.api.controler.ITreeSelectionEvent;
import org.jowidgets.api.controler.ITreeSelectionListener;
import org.jowidgets.api.image.IconsSmall;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IMenu;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.ITree;
import org.jowidgets.api.widgets.ITreeNode;
import org.jowidgets.api.widgets.blueprint.ITreeBluePrint;
import org.jowidgets.api.widgets.blueprint.ITreeNodeBluePrint;
import org.jowidgets.api.widgets.blueprint.defaults.IDefaultInitializer;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controler.IPopupDetectionListener;
import org.jowidgets.common.widgets.controler.ITreeNodeListener;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;

public final class DemoTreeComposite {

	protected DemoTreeComposite(final IContainer parentContainer) {

		final IBluePrintFactory bpF = Toolkit.getBluePrintFactory();
		bpF.addDefaultsInitializer(ITreeBluePrint.class, new IDefaultInitializer<ITreeBluePrint>() {
			@Override
			public void initialize(final ITreeBluePrint bluePrint) {
				bluePrint.setDefaultInnerIcon(IconsSmall.INFO);
				bluePrint.setDefaultLeafIcon(IconsSmall.WARNING);
			}
		});

		final ILayoutDescriptor fillLayoutDescriptor = new MigLayoutDescriptor("0[grow, 0::]0", "0[grow, 0::]0");
		parentContainer.setLayout(fillLayoutDescriptor);

		final ITree tree = parentContainer.add(bpF.tree().multiSelection(), "growx, growy, w 0::, h 0::");
		final IPopupMenu popupMenu = tree.createPopupMenu();
		fillMenu("", popupMenu);

		tree.addTreeSelectionListener(new ITreeSelectionListener() {

			@Override
			public void selectionChanged(final ITreeSelectionEvent event) {
				// CHECKSTYLE:OFF
				System.out.println(event);
				// CHECKSTYLE:ON
			}
		});

		tree.addTreeListener(new ITreeListener() {

			@Override
			public void nodeExpanded(final ITreeNode node) {
				// CHECKSTYLE:OFF
				System.out.println("Tree node expanded: " + node);
				// CHECKSTYLE:ON
			}

			@Override
			public void nodeCollapsed(final ITreeNode node) {
				// CHECKSTYLE:OFF
				System.out.println("Tree node collpased: " + node);
				// CHECKSTYLE:ON
			}
		});

		tree.addPopupDetectionListener(new IPopupDetectionListener() {
			@Override
			public void popupDetected(final Position position) {
				popupMenu.show(position);
			}
		});

		tree.addTreePopupDetectionListener(new ITreePopupDetectionListener() {
			@Override
			public void popupDetected(final ITreePopupEvent event) {
				// CHECKSTYLE:OFF
				System.out.println(event);
				// CHECKSTYLE:ON
			}
		});

		for (int i = 0; i < 10; i++) {
			ITreeNodeBluePrint treeNodeBp = bpF.treeNode();
			treeNodeBp.setText("Node " + i).setToolTipText("tooltip of node " + i);
			final ITreeNode node = tree.addNode(treeNodeBp);

			registerListners(node);

			for (int j = 0; j < 10; j++) {
				treeNodeBp = bpF.treeNode();
				treeNodeBp.setText("SubNode " + j).setToolTipText("tooltip of subNode " + j);
				final ITreeNode subNode = node.addNode(treeNodeBp);
				registerListners(subNode);

				for (int k = 0; k < 10; k++) {
					treeNodeBp = bpF.treeNode();
					treeNodeBp.setText("SubSubNode " + k).setToolTipText("tooltip of subSubNode " + k);
					final ITreeNode subSubNode = subNode.addNode(treeNodeBp);
					registerListners(subSubNode);
				}
			}

		}

		tree.removeNode(tree.getChildren().get(5));

		tree.getChildren().get(2).getChildren().get(3).setSelected(true);
		tree.getChildren().get(2).getChildren().get(5).setSelected(true);
		tree.getChildren().get(3).getChildren().get(5).setIcon(IconsSmall.ERROR);
	}

	private void registerListners(final ITreeNode node) {
		node.addTreeNodeListener(new ITreeNodeListener() {

			@Override
			public void selectionChanged(final boolean selected) {
				// CHECKSTYLE:OFF
				System.out.println(node.getText() + " selected = " + selected);
				// CHECKSTYLE:ON
			}

			@Override
			public void expandedChanged(final boolean expanded) {
				// CHECKSTYLE:OFF
				System.out.println(node.getText() + " expanded = " + expanded);
				// CHECKSTYLE:ON
			}
		});

		final IPopupMenu popupMenu = node.createPopupMenu();
		fillMenu(node.getText() + " -> ", popupMenu);

		node.addPopupDetectionListener(new IPopupDetectionListener() {
			@Override
			public void popupDetected(final Position position) {
				popupMenu.show(position);
			}
		});
	}

	private void fillMenu(final String prefix, final IMenu menu) {
		final IBluePrintFactory bpF = Toolkit.getBluePrintFactory();
		menu.addItem(bpF.menuItem(prefix + "menu item 1"));
		menu.addItem(bpF.menuItem(prefix + "menu item 2"));
		menu.addItem(bpF.menuItem(prefix + "menu item 3"));
		menu.addItem(bpF.menuItem(prefix + "menu item 4"));
	}
}
