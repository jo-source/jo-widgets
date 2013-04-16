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

package org.jowidgets.workbench.impl;

import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.model.item.IToolBarModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.api.widgets.IToolBar;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.controller.ListModelAdapter;
import org.jowidgets.tools.layout.MigLayoutFactory;
import org.jowidgets.tools.model.item.MenuModel;
import org.jowidgets.tools.model.item.ToolBarModel;

public class ViewWithToolBar {

	private final IComposite viewContent;

	private final IComposite toolBarContainer;
	private final IToolBar toolBar;
	private final IToolBarModel toolBarModel;
	private final IMenuModel toolBarMenuModel;

	public ViewWithToolBar(final IContainer container) {
		final IToolBarModel innerToolBarModel = new ToolBarModel();
		this.toolBarModel = new ToolBarModel();
		this.toolBarMenuModel = new MenuModel();

		final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();

		container.setLayout(new MigLayoutDescriptor("hidemode 2", "0[grow, 0::]0", "0[]0[]0[grow, 0::]0"));

		toolBarContainer = container.add(bpf.composite(), "growx, w 0::, hidemode 2, wrap");
		toolBarContainer.setLayout(new MigLayoutDescriptor("0[grow, 0::]0[0::]0", "0[]0"));
		toolBarContainer.add(bpf.toolBar(), "growx, growy, w 0::, h 0::");
		toolBar = toolBarContainer.add(bpf.toolBar(), "w 0::");
		toolBar.setModel(innerToolBarModel);
		toolBarContainer.setVisible(false);
		final IControl toolBarSeparator = container.add(bpf.separator(), "growx, w 0::, hidemode 2, wrap");
		toolBarSeparator.setVisible(false);

		toolBarModel.addListModelListener(new ListModelAdapter() {

			@Override
			public void afterChildAdded(final int index) {
				innerToolBarModel.addItem(index, toolBarModel.getItems().get(index));
			}

			@Override
			public void afterChildRemoved(final int index) {
				innerToolBarModel.removeItem(index);
			}

		});

		toolBarMenuModel.addListModelListener(new ListModelAdapter() {

			@Override
			public void afterChildAdded(final int index) {
				if (toolBarMenuModel.getChildren().size() == 1) {
					innerToolBarModel.addItem(toolBarMenuModel);
				}
			}

			@Override
			public void afterChildRemoved(final int index) {
				if (toolBarMenuModel.getChildren().size() == 0) {
					innerToolBarModel.removeItem(toolBarMenuModel);
				}
			}

		});

		innerToolBarModel.addListModelListener(new ListModelAdapter() {

			@Override
			public void afterChildAdded(final int index) {
				if (innerToolBarModel.getItems().size() == 1) {
					toolBarContainer.setVisible(true);
					toolBarSeparator.setVisible(true);
				}
			}

			@Override
			public void afterChildRemoved(final int index) {
				if (innerToolBarModel.getItems().size() == 0) {
					innerToolBarModel.removeItem(innerToolBarModel.getItems().size());
					toolBarContainer.setVisible(false);
					toolBarSeparator.setVisible(false);
				}
			}

		});

		viewContent = container.add(bpf.composite(), MigLayoutFactory.GROWING_CELL_CONSTRAINTS);
		viewContent.setLayout(MigLayoutFactory.growingInnerCellLayout());
	}

	public IComposite getViewContent() {
		return viewContent;
	}

	public IToolBarModel getToolBarModel() {
		return toolBarModel;
	}

	public IMenuModel getToolBarMenuModel() {
		return toolBarMenuModel;
	}

	public void pack() {
		toolBar.pack();
	}
}
