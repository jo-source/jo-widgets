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

import java.util.LinkedList;
import java.util.List;

import org.jowidgets.api.model.item.IMenuBarModel;
import org.jowidgets.api.model.item.IToolBarModel;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.common.types.IVetoable;
import org.jowidgets.util.Assert;
import org.jowidgets.workbench.api.ILifecycleCallback;
import org.jowidgets.workbench.api.ITrayItem;
import org.jowidgets.workbench.api.IWorkbenchApplication;
import org.jowidgets.workbench.api.IWorkbenchApplicationContext;
import org.jowidgets.workbench.api.IWorkbenchContext;
import org.jowidgets.workbench.toolkit.api.IWorkbenchApplicationInitializeCallback;
import org.jowidgets.workbench.toolkit.api.IWorkbenchModel;

class ModelBasedWorkbenchContext implements IWorkbenchContext {

	private final IWorkbenchModel workbenchModel;
	private final IWorkbenchContext workbenchContext;
	private final List<IWorkbenchApplication> addedApplications;

	ModelBasedWorkbenchContext(final IWorkbenchModel workbenchModel, final IWorkbenchContext workbenchContext) {
		super();
		this.workbenchModel = workbenchModel;
		this.workbenchContext = workbenchContext;

		this.addedApplications = new LinkedList<IWorkbenchApplication>();
	}

	@Override
	public void add(final int index, final IWorkbenchApplication workbenchApplication) {
		Assert.paramNotNull(workbenchApplication, "workbenchApplication");

		final WorkbenchApplicationModelBuilder modelBuilder = new WorkbenchApplicationModelBuilder();
		modelBuilder.setId(workbenchApplication.getId());
		modelBuilder.setLabel(workbenchApplication.getLabel());
		modelBuilder.setTooltip(workbenchApplication.getTooltip());
		modelBuilder.setIcon(workbenchApplication.getIcon());

		modelBuilder.setInitializeCallback(new IWorkbenchApplicationInitializeCallback() {
			@Override
			public void onContextInitialize(final IWorkbenchApplicationContext context) {
				workbenchApplication.onContextInitialize(context);
			}
		});

		modelBuilder.setLifecycleCallback(new ILifecycleCallback() {
			@Override
			public void onClose(final IVetoable vetoable) {
				workbenchApplication.onClose(vetoable);
			}

			@Override
			public void onVisibleStateChanged(final boolean visible) {
				workbenchApplication.onVisibleStateChanged(visible);
			}

			@Override
			public void onActiveStateChanged(final boolean active) {
				workbenchApplication.onActiveStateChanged(active);
			}
		});

		addedApplications.add(index, workbenchApplication);
		workbenchModel.addApplication(index, modelBuilder);
	}

	@Override
	public void add(final IWorkbenchApplication workbenchApplication) {
		add(addedApplications.size(), workbenchApplication);
	}

	@Override
	public void remove(final IWorkbenchApplication workbenchApplication) {
		final int index = addedApplications.indexOf(workbenchApplication);
		if (index != -1) {
			workbenchModel.removeApplication(index);
		}
	}

	@Override
	public void finish() {
		workbenchContext.finish();
	}

	@Override
	public IToolBarModel getToolBar() {
		return workbenchModel.getToolBar();
	}

	@Override
	public IMenuBarModel getMenuBar() {
		return workbenchModel.getMenuBar();
	}

	@Override
	public IContainer getStatusBar() {
		return workbenchContext.getStatusBar();
	}

	@Override
	public ITrayItem getTrayItem() {
		return workbenchContext.getTrayItem();
	}

	@Override
	public void addShutdownHook(final Runnable shutdownHook) {
		workbenchModel.addShutdownHook(shutdownHook);
	}

	@Override
	public void removeShutdownHook(final Runnable shutdownHook) {
		workbenchModel.removeShutdownHook(shutdownHook);
	}

}
