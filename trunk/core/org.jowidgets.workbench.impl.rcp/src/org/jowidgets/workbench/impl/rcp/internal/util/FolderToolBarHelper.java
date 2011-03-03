/*
 * Copyright (c) 2011, M. Grossmann
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

package org.jowidgets.workbench.impl.rcp.internal.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.jowidgets.api.model.IListModelListener;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.model.item.IToolBarModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IToolBar;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.model.item.MenuModel;
import org.jowidgets.tools.model.item.ToolBarModel;

public final class FolderToolBarHelper {

	private final IToolBarModel toolBarModel;
	private final IMenuModel toolBarMenuModel;
	private final Composite uiReference;

	public FolderToolBarHelper(final Composite parent) {
		final IToolBarModel innerToolBarModel = new ToolBarModel();
		this.toolBarModel = new ToolBarModel();
		this.toolBarMenuModel = new MenuModel();

		uiReference = new Composite(parent, SWT.NONE);
		uiReference.setLayout(new FillLayout(SWT.HORIZONTAL));
		final IComposite joComposite = Toolkit.getWidgetWrapperFactory().createComposite(uiReference);
		joComposite.setLayout(new MigLayoutDescriptor("0[grow, 0::]0", "0[]0"));

		final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();

		final IToolBar toolBar = joComposite.add(bpf.toolBar(), "alignx right, wrap");
		toolBar.setModel(innerToolBarModel);

		toolBarModel.addListModelListener(new IListModelListener() {
			@Override
			public void childAdded(final int index) {
				innerToolBarModel.addItem(index, toolBarModel.getItems().get(index));
			}

			@Override
			public void childRemoved(final int index) {
				innerToolBarModel.removeItem(index);
			}
		});

		toolBarMenuModel.addListModelListener(new IListModelListener() {
			@Override
			public void childAdded(final int index) {
				if (toolBarMenuModel.getChildren().size() == 1) {
					innerToolBarModel.addItem(toolBarMenuModel);
				}
			}

			@Override
			public void childRemoved(final int index) {
				if (toolBarMenuModel.getChildren().size() == 0) {
					innerToolBarModel.removeItem(toolBarMenuModel);
				}
			}
		});
	}

	public Composite getUiReference() {
		return uiReference;
	}

	public IToolBarModel getToolBarModel() {
		return toolBarModel;
	}

	public IMenuModel getToolBarMenuModel() {
		return toolBarMenuModel;
	}

}
