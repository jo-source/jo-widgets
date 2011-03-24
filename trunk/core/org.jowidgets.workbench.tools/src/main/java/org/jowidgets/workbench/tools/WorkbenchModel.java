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

package org.jowidgets.workbench.tools;

import java.util.List;

import org.jowidgets.api.model.IListModelListener;
import org.jowidgets.api.model.item.IMenuBarModel;
import org.jowidgets.api.model.item.IToolBarModel;
import org.jowidgets.api.widgets.content.IContentCreator;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.workbench.api.ICloseCallback;
import org.jowidgets.workbench.api.IWorkbenchApplicationDescriptor;
import org.jowidgets.workbench.api.IWorkbenchDescriptor;
import org.jowidgets.workbench.toolkit.api.IWorkbenchApplicationModel;
import org.jowidgets.workbench.toolkit.api.IWorkbenchApplicationModelBuilder;
import org.jowidgets.workbench.toolkit.api.IWorkbenchInitializeCallback;
import org.jowidgets.workbench.toolkit.api.IWorkbenchModel;
import org.jowidgets.workbench.toolkit.api.IWorkbenchModelBuilder;
import org.jowidgets.workbench.toolkit.api.IWorkbenchPartModelListener;
import org.jowidgets.workbench.toolkit.api.WorkbenchToolkit;

public class WorkbenchModel implements IWorkbenchModel {

	private final IWorkbenchModel model;

	public WorkbenchModel() {
		this(builder());
	}

	public WorkbenchModel(final IWorkbenchDescriptor descriptor) {
		this(builder(descriptor));
	}

	public WorkbenchModel(final String label) {
		this(builder(label));
	}

	public WorkbenchModel(final String label, final IImageConstant icon) {
		this(builder(label, icon));
	}

	public WorkbenchModel(final IWorkbenchModelBuilder modelBuilder) {
		super();
		this.model = modelBuilder.build();
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
	public void addWorkbenchPartModelListener(final IWorkbenchPartModelListener listener) {
		model.addWorkbenchPartModelListener(listener);
	}

	@Override
	public void removeWorkbenchPartModelListener(final IWorkbenchPartModelListener listener) {
		model.removeWorkbenchPartModelListener(listener);
	}

	@Override
	public void addListModelListener(final IListModelListener listener) {
		model.addListModelListener(listener);
	}

	@Override
	public void removeListModelListener(final IListModelListener listener) {
		model.removeListModelListener(listener);
	}

	@Override
	public IToolBarModel getToolBar() {
		return model.getToolBar();
	}

	@Override
	public IMenuBarModel getMenuBar() {
		return model.getMenuBar();
	}

	@Override
	public List<Runnable> getShutdownHooks() {
		return model.getShutdownHooks();
	}

	@Override
	public IContentCreator getStatusBarCreator() {
		return model.getStatusBarCreator();
	}

	@Override
	public ICloseCallback getCloseCallback() {
		return model.getCloseCallback();
	}

	@Override
	public boolean isFinished() {
		return model.isFinished();
	}

	@Override
	public void finish() {
		model.finish();
	}

	@Override
	public IWorkbenchInitializeCallback getInitializeCallback() {
		return model.getInitializeCallback();
	}

	@Override
	public List<IWorkbenchApplicationModel> getApplications() {
		return model.getApplications();
	}

	@Override
	public int getApplicationCount() {
		return model.getApplicationCount();
	}

	@Override
	public void setToolBar(final IToolBarModel toolBarModel) {
		model.setToolBar(toolBarModel);
	}

	@Override
	public void setMenuBar(final IMenuBarModel menuBarModel) {
		model.setMenuBar(menuBarModel);
	}

	@Override
	public void setStatusBarCreator(final IContentCreator statusBarContentCreator) {
		model.setStatusBarCreator(statusBarContentCreator);
	}

	@Override
	public void setCloseCallback(final ICloseCallback closeCallback) {
		model.setCloseCallback(closeCallback);
	}

	@Override
	public void addShutdownHook(final Runnable shutdownHook) {
		model.addShutdownHook(shutdownHook);
	}

	@Override
	public void removeShutdownHook(final Runnable shutdownHook) {
		model.removeShutdownHook(shutdownHook);
	}

	@Override
	public IWorkbenchApplicationModel addApplication(final IWorkbenchApplicationModel applicationModel) {
		return model.addApplication(applicationModel);
	}

	@Override
	public IWorkbenchApplicationModel addApplication(final int index, final IWorkbenchApplicationModel applicationModel) {
		return model.addApplication(index, applicationModel);
	}

	@Override
	public IWorkbenchApplicationModel addApplication(final IWorkbenchApplicationModelBuilder applicationModelBuilder) {
		return model.addApplication(applicationModelBuilder);
	}

	@Override
	public IWorkbenchApplicationModel addApplication(
		final int index,
		final IWorkbenchApplicationModelBuilder applicationModelBuilder) {
		return model.addApplication(index, applicationModelBuilder);
	}

	@Override
	public IWorkbenchApplicationModel addApplication(final IWorkbenchApplicationDescriptor applicationDescriptor) {
		return model.addApplication(applicationDescriptor);
	}

	@Override
	public IWorkbenchApplicationModel addApplication(final int index, final IWorkbenchApplicationDescriptor applicationDescriptor) {
		return model.addApplication(index, applicationDescriptor);
	}

	@Override
	public IWorkbenchApplicationModel addApplication(
		final String id,
		final String label,
		final String tooltip,
		final IImageConstant icon) {
		return model.addApplication(id, label, tooltip, icon);
	}

	@Override
	public IWorkbenchApplicationModel addApplication(final String id, final String label, final IImageConstant icon) {
		return model.addApplication(id, label, icon);
	}

	@Override
	public IWorkbenchApplicationModel addApplication(final String id, final String label, final String tooltip) {
		return model.addApplication(id, label, tooltip);
	}

	@Override
	public IWorkbenchApplicationModel addApplication(final String id, final String label) {
		return model.addApplication(id, label);
	}

	@Override
	public IWorkbenchApplicationModel addApplication(final String id) {
		return model.addApplication(id);
	}

	@Override
	public void removeApplication(final int index) {
		model.removeApplication(index);
	}

	@Override
	public void removeApplication(final IWorkbenchApplicationModel childModel) {
		model.removeApplication(childModel);
	}

	@Override
	public void removeAllApplications() {
		model.removeAllApplications();
	}

	public static IWorkbenchModelBuilder builder() {
		return WorkbenchToolkit.getWorkbenchPartBuilderFactory().workbench();
	}

	public static IWorkbenchModelBuilder builder(final String label) {
		return builder().setLabel(label);
	}

	public static IWorkbenchModelBuilder builder(final String label, final IImageConstant icon) {
		return builder(label).setIcon(icon);
	}

	public static IWorkbenchModelBuilder builder(final IWorkbenchDescriptor descriptor) {
		return builder().setDescriptor(descriptor);
	}

}
