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

package org.jowidgets.workbench.toolkit.impl;

import java.util.List;

import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.model.item.IToolBarModel;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.workbench.api.ILifecycleCallback;
import org.jowidgets.workbench.toolkit.api.IComponentNodeModel;
import org.jowidgets.workbench.toolkit.api.IWorkbenchApplicationInitializeCallback;
import org.jowidgets.workbench.toolkit.api.IWorkbenchApplicationModel;
import org.jowidgets.workbench.toolkit.api.IWorkbenchModel;
import org.jowidgets.workbench.toolkit.api.IWorkbenchPartModelListener;

class WorkbenchApplicationModel extends ComponentNodeContainerModel implements IWorkbenchApplicationModel {

	private final WorkbenchPartModelObservable workbenchPartModelObservable;

	private final String label;
	private final String tooltip;
	private final IImageConstant icon;
	private final IWorkbenchApplicationInitializeCallback initializeCallback;

	private IMenuModel popupMenu;
	private IToolBarModel toolBarModel;
	private IMenuModel toolBarMenu;
	private ILifecycleCallback lifecycleCallback;

	private IWorkbenchModel workbench;

	WorkbenchApplicationModel(
		final String id,
		final String label,
		final String tooltip,
		final IImageConstant icon,
		final IMenuModel popupMenu,
		final IToolBarModel toolBarModel,
		final IMenuModel toolBarMenu,
		final ILifecycleCallback lifecycleCallback,
		final IWorkbenchApplicationInitializeCallback initializeCallback,
		final List<IComponentNodeModel> children) {
		super(id, children);

		this.workbenchPartModelObservable = new WorkbenchPartModelObservable();
		this.label = label;
		this.tooltip = tooltip;
		this.icon = icon;
		this.popupMenu = popupMenu;
		this.toolBarModel = toolBarModel;
		this.toolBarMenu = toolBarMenu;
		this.lifecycleCallback = lifecycleCallback;
		this.initializeCallback = initializeCallback;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public String getTooltip() {
		return tooltip;
	}

	@Override
	public IImageConstant getIcon() {
		return icon;
	}

	@Override
	public IMenuModel getPopupMenu() {
		return popupMenu;
	}

	@Override
	public IToolBarModel getToolBar() {
		return toolBarModel;
	}

	@Override
	public IMenuModel getToolBarMenu() {
		return toolBarMenu;
	}

	@Override
	public ILifecycleCallback getLifecycleCallback() {
		return lifecycleCallback;
	}

	@Override
	public IWorkbenchApplicationInitializeCallback getInitializeCallback() {
		return initializeCallback;
	}

	@Override
	public void setPopupMenu(final IMenuModel popupMenu) {
		this.popupMenu = popupMenu;
		fireModelChanged();
	}

	@Override
	public void setToolBar(final IToolBarModel toolBarModel) {
		this.toolBarModel = toolBarModel;
		fireModelChanged();
	}

	@Override
	public void setToolBarMenu(final IMenuModel toolBarMenu) {
		this.toolBarMenu = toolBarMenu;
		fireModelChanged();
	}

	@Override
	public void setLifecycleCallback(final ILifecycleCallback lifecycleCallback) {
		this.lifecycleCallback = lifecycleCallback;
	}

	@Override
	public IWorkbenchModel getWorkbench() {
		return workbench;
	}

	@Override
	public void setWorkbench(final IWorkbenchModel workbench) {
		if (this.workbench != workbench) {
			this.workbench = workbench;
			if (this.workbench != null) {
				this.workbench.removeApplication(this);
			}
			if (workbench != null) {
				if (!workbench.getApplications().contains(this)) {
					workbench.addApplication(this);
				}
			}
		}
	}

	@Override
	public void addWorkbenchPartModelListener(final IWorkbenchPartModelListener listener) {
		workbenchPartModelObservable.addWorkbenchPartModelListener(listener);
	}

	@Override
	public void removeWorkbenchPartModelListener(final IWorkbenchPartModelListener listener) {
		workbenchPartModelObservable.removeWorkbenchPartModelListener(listener);
	}

	private void fireModelChanged() {
		workbenchPartModelObservable.fireModelChanged();
	}

}
