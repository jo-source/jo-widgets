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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.jowidgets.api.model.IListModelListener;
import org.jowidgets.api.model.item.IMenuBarModel;
import org.jowidgets.api.model.item.IToolBarModel;
import org.jowidgets.api.widgets.content.IContentCreator;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.util.Assert;
import org.jowidgets.workbench.api.ICloseCallback;
import org.jowidgets.workbench.api.IWorkbenchApplicationDescriptor;
import org.jowidgets.workbench.toolkit.api.IWorkbenchApplicationModel;
import org.jowidgets.workbench.toolkit.api.IWorkbenchApplicationModelBuilder;
import org.jowidgets.workbench.toolkit.api.IWorkbenchModel;
import org.jowidgets.workbench.toolkit.api.WorkbenchToolkit;

class WorkbenchModel extends WorkbenchPartModel implements IWorkbenchModel {

	private final ListModelObservable listModelObservable;

	private final Dimension initialDimension;
	private final Position initialPosition;
	private final double initialSplitWeight;
	private final boolean hasApplicationNavigator;
	private final boolean applicationsCloseable;

	private final List<Runnable> shutdownHooks;
	private final List<IWorkbenchApplicationModel> applications;

	private IToolBarModel toolBar;
	private IMenuBarModel menuBar;
	private IContentCreator statusBarCreator;
	private ICloseCallback closeCallback;

	WorkbenchModel(
		final String label,
		final String tooltip,
		final IImageConstant icon,
		final Dimension initialDimension,
		final Position initialPosition,
		final double initialSplitWeight,
		final boolean hasApplicationNavigator,
		final boolean applicationsCloseable,
		final IToolBarModel toolBar,
		final IMenuBarModel menuBar,
		final IContentCreator statusBarCreator,
		final ICloseCallback closeCallback,
		final List<IWorkbenchApplicationModel> applications,
		final List<Runnable> shutdownHooks) {
		super(label, tooltip, icon);

		this.listModelObservable = new ListModelObservable();

		this.initialDimension = initialDimension;
		this.initialPosition = initialPosition;
		this.initialSplitWeight = initialSplitWeight;
		this.hasApplicationNavigator = hasApplicationNavigator;
		this.applicationsCloseable = applicationsCloseable;
		this.toolBar = toolBar;
		this.menuBar = menuBar;
		this.statusBarCreator = statusBarCreator;
		this.closeCallback = closeCallback;
		this.applications = new LinkedList<IWorkbenchApplicationModel>();
		this.shutdownHooks = new LinkedList<Runnable>(shutdownHooks);

		for (final IWorkbenchApplicationModel application : applications) {
			//Add the children that way to ensure that the parents will be set correctly
			addApplication(application);
		}
	}

	@Override
	public Dimension getInitialDimension() {
		return initialDimension;
	}

	@Override
	public Position getInitialPosition() {
		return initialPosition;
	}

	@Override
	public double getInitialSplitWeight() {
		return initialSplitWeight;
	}

	@Override
	public boolean hasApplicationNavigator() {
		return hasApplicationNavigator;
	}

	@Override
	public boolean getApplicationsCloseable() {
		return applicationsCloseable;
	}

	@Override
	public IToolBarModel getToolBar() {
		return toolBar;
	}

	@Override
	public IMenuBarModel getMenuBar() {
		return menuBar;
	}

	@Override
	public IContentCreator getStatusBarCreator() {
		return statusBarCreator;
	}

	@Override
	public ICloseCallback getCloseCallback() {
		return closeCallback;
	}

	@Override
	public void setToolBar(final IToolBarModel toolBarModel) {
		this.toolBar = toolBarModel;
		fireModelChanged();
	}

	@Override
	public void setMenuBar(final IMenuBarModel menuBarModel) {
		this.menuBar = menuBarModel;
		fireModelChanged();
	}

	@Override
	public void setStatusBarCreator(final IContentCreator statusBarContentCreator) {
		this.statusBarCreator = statusBarContentCreator;
		fireModelChanged();
	}

	@Override
	public void setCloseCallback(final ICloseCallback closeCallback) {
		this.closeCallback = closeCallback;
	}

	@Override
	public List<Runnable> getShutdownHooks() {
		return new LinkedList<Runnable>(shutdownHooks);
	}

	@Override
	public void addShutdownHook(final Runnable shutdownHook) {
		shutdownHooks.add(shutdownHook);
	}

	@Override
	public void removeShutdownHook(final Runnable shutdownHook) {
		shutdownHooks.remove(shutdownHook);
	}

	@Override
	public List<IWorkbenchApplicationModel> getApplications() {
		return Collections.unmodifiableList(applications);
	}

	@Override
	public int getApplicationCount() {
		return applications.size();
	}

	@Override
	public IWorkbenchApplicationModel addApplication(final int index, final IWorkbenchApplicationModel applicationModel) {
		Assert.paramNotNull(applicationModel, "applicationModel");
		if (applicationModel.getWorkbench() != null) {
			throw new IllegalArgumentException("The given model was alreay added to another workbench. "
				+ "To add the model to this workbench, it must be removed from its current workbench first.");
		}
		applications.add(index, applicationModel);
		applicationModel.setWorkbench(this);
		listModelObservable.fireChildAdded(index);
		return applicationModel;
	}

	@Override
	public void removeApplication(final int index) {
		final IWorkbenchApplicationModel applicationModel = applications.remove(index);
		if (applicationModel != null) {
			applicationModel.setWorkbench(null);
			listModelObservable.fireChildRemoved(index);
		}
	}

	@Override
	public IWorkbenchApplicationModel addApplication(final IWorkbenchApplicationModel applicationModel) {
		return addApplication(getApplicationCount(), applicationModel);
	}

	@Override
	public IWorkbenchApplicationModel addApplication(final IWorkbenchApplicationModelBuilder applicationModelBuilder) {
		return addApplication(applications.size(), applicationModelBuilder);
	}

	@Override
	public IWorkbenchApplicationModel addApplication(
		final int index,
		final IWorkbenchApplicationModelBuilder applicationModelBuilder) {
		Assert.paramNotNull(applicationModelBuilder, "applicationModelBuilder");
		return addApplication(index, applicationModelBuilder.build());
	}

	@Override
	public IWorkbenchApplicationModel addApplication(final IWorkbenchApplicationDescriptor descriptor) {
		return addApplication(applicationBuilder(descriptor));
	}

	@Override
	public IWorkbenchApplicationModel addApplication(final int index, final IWorkbenchApplicationDescriptor descriptor) {
		return addApplication(index, applicationBuilder(descriptor));
	}

	@Override
	public IWorkbenchApplicationModel addApplication(
		final String id,
		final String label,
		final String tooltip,
		final IImageConstant icon) {
		return addApplication(applicationBuilder().setId(id).setLabel(label).setTooltip(tooltip).setIcon(icon));
	}

	@Override
	public IWorkbenchApplicationModel addApplication(final String id, final String label, final IImageConstant icon) {
		return addApplication(applicationBuilder().setId(id).setLabel(label).setIcon(icon));
	}

	@Override
	public IWorkbenchApplicationModel addApplication(final String id, final String label, final String tooltip) {
		return addApplication(applicationBuilder().setId(id).setLabel(label).setTooltip(tooltip));
	}

	@Override
	public IWorkbenchApplicationModel addApplication(final String id, final String label) {
		return addApplication(applicationBuilder().setId(id).setLabel(label));
	}

	@Override
	public IWorkbenchApplicationModel addApplication(final String id) {
		return addApplication(applicationBuilder().setId(id));
	}

	@Override
	public void removeApplication(final IWorkbenchApplicationModel childModel) {
		final int index = applications.indexOf(childModel);
		if (index != -1) {
			removeApplication(index);
		}
	}

	@Override
	public void removeAllApplications() {
		final int applicationCount = applications.size();
		for (int i = 0; i < applicationCount; i++) {
			removeApplication(0);
		}
	}

	@Override
	public void addListModelListener(final IListModelListener listener) {
		listModelObservable.addListModelListener(listener);
	}

	@Override
	public void removeListModelListener(final IListModelListener listener) {
		listModelObservable.removeListModelListener(listener);
	}

	private IWorkbenchApplicationModelBuilder applicationBuilder() {
		return WorkbenchToolkit.getWorkbenchPartBuilderFactory().application();
	}

	private IWorkbenchApplicationModelBuilder applicationBuilder(final IWorkbenchApplicationDescriptor descriptor) {
		return WorkbenchToolkit.getWorkbenchPartBuilderFactory().application().setDescriptor(descriptor);
	}

}
