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

import org.jowidgets.api.model.IListModelListener;
import org.jowidgets.api.model.item.IMenuBarModel;
import org.jowidgets.api.model.item.IToolBarModel;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.content.IContentCreator;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.IVetoable;
import org.jowidgets.common.types.Position;
import org.jowidgets.util.Assert;
import org.jowidgets.workbench.api.ICloseCallback;
import org.jowidgets.workbench.api.IView;
import org.jowidgets.workbench.api.IViewContext;
import org.jowidgets.workbench.api.IWorkbench;
import org.jowidgets.workbench.api.IWorkbenchContext;
import org.jowidgets.workbench.toolkit.api.IWorkbenchApplicationModel;
import org.jowidgets.workbench.toolkit.api.IWorkbenchModel;
import org.jowidgets.workbench.toolkit.api.IWorkbenchPartModelListener;

class Workbench implements IWorkbench {

	private final IWorkbenchModel model;
	private final Runnable shutdownHook;

	private final List<WorkbenchApplication> createdApplications;

	private IContentCreator statusBarCreator;
	private IToolBarModel toolBar;
	private IMenuBarModel menuBar;

	Workbench(final IWorkbenchModel model) {
		Assert.paramNotNull(model, "model");

		this.model = model;
		this.createdApplications = new LinkedList<WorkbenchApplication>();

		this.shutdownHook = new Runnable() {
			@Override
			public void run() {
				if (model.getShutdownHooks() != null) {
					for (final Runnable runnable : model.getShutdownHooks()) {
						runnable.run();
					}
				}
			}
		};
	}

	@Override
	public void onContextInitialize(final IWorkbenchContext context) {
		context.addShutdownHook(shutdownHook);
		for (final IWorkbenchApplicationModel applicationModel : model.getApplications()) {
			final WorkbenchApplication application = new WorkbenchApplication(applicationModel);
			context.add(application);
			createdApplications.add(application);
		}

		model.addListModelListener(new IListModelListener() {
			@Override
			public void childRemoved(final int index) {
				final WorkbenchApplication application = createdApplications.remove(index);
				if (application != null) {
					context.remove(application);
					application.dispose();
				}
			}

			@Override
			public void childAdded(final int index) {
				final IWorkbenchApplicationModel applicationModel = model.getApplications().get(index);
				final WorkbenchApplication application = new WorkbenchApplication(applicationModel);
				context.add(index, application);
				createdApplications.add(index, application);
			}
		});

		model.addWorkbenchPartModelListener(new IWorkbenchPartModelListener() {
			@Override
			public void modelChanged() {
				onModelChanged(context);
			}
		});

		onModelChanged(context);

		if (model.getInitializeCallback() != null) {
			model.getInitializeCallback().onContextInitialize(new ModelBasedWorkbenchContext(model, context));
		}
	}

	@Override
	public IView createView(final String viewId, final IViewContext viewContext) {
		if (model.getViewFactory() != null) {
			return model.getViewFactory().createView(viewId, viewContext);
		}
		return null;
	}

	@Override
	public Dimension getInitialDimension() {
		return model.getInitialDimension();
	}

	@Override
	public Position getInitialPosition() {
		return model.getInitialPosition();
	}

	@Override
	public double getInitialSplitWeight() {
		return model.getInitialSplitWeight();
	}

	@Override
	public boolean hasApplicationNavigator() {
		return model.hasApplicationNavigator();
	}

	@Override
	public boolean getApplicationsCloseable() {
		return model.getApplicationsCloseable();
	}

	@Override
	public String getLabel() {
		return model.getLabel();
	}

	@Override
	public String getTooltip() {
		return model.getTooltip();
	}

	@Override
	public IImageConstant getIcon() {
		return model.getIcon();
	}

	@Override
	public void onClose(final IVetoable vetoable) {
		final ICloseCallback closeCallback = model.getCloseCallback();
		if (closeCallback != null) {
			closeCallback.onClose(vetoable);
		}
	}

	@Override
	public void onDispose() {
		final ICloseCallback closeCallback = model.getCloseCallback();
		if (closeCallback != null) {
			closeCallback.onDispose();
		}
	}

	private void onModelChanged(final IWorkbenchContext context) {
		if (statusBarCreator != model.getStatusBarCreator()) {
			onStatusBarChanged(context);
		}
		if (toolBar != model.getToolBar()) {
			onToolBarChanged(context);
		}
		if (menuBar != model.getMenuBar()) {
			onMenuBarChanged(context);
		}
		if (model.isFinished()) {
			context.finish();
		}
	}

	private void onStatusBarChanged(final IWorkbenchContext context) {
		//Do not create the status bar for the context, if model never had
		//a status bar	
		if (model.getStatusBarCreator() != null || statusBarCreator != null) {
			final IContainer statusBar = context.getStatusBar();
			statusBar.layoutBegin();
			statusBar.removeAll();
			if (model.getStatusBarCreator() != null) {
				model.getStatusBarCreator().createContent(statusBar);
			}
			statusBar.layoutEnd();
		}
		statusBarCreator = model.getStatusBarCreator();
	}

	private void onToolBarChanged(final IWorkbenchContext context) {
		if (toolBar != null) {
			toolBar.unbind(context.getToolBar());
		}
		if (model.getToolBar() != null) {
			model.getToolBar().bind(context.getToolBar());
		}
		else {
			context.getToolBar().removeAllItems();
		}
		toolBar = model.getToolBar();
	}

	private void onMenuBarChanged(final IWorkbenchContext context) {
		if (menuBar != null) {
			menuBar.unbind(context.getMenuBar());
		}
		if (model.getMenuBar() != null) {
			model.getMenuBar().bind(context.getMenuBar());
		}
		else {
			context.getToolBar().removeAllItems();
		}
		menuBar = model.getMenuBar();
	}

}
