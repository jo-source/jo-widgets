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

import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.IVetoable;
import org.jowidgets.common.types.Position;
import org.jowidgets.util.Assert;
import org.jowidgets.workbench.api.IView;
import org.jowidgets.workbench.api.IViewContext;
import org.jowidgets.workbench.api.IWorkbench;
import org.jowidgets.workbench.api.IWorkbenchContext;
import org.jowidgets.workbench.api.IWorkbenchDescriptor;
import org.jowidgets.workbench.toolkit.api.IWorkbenchModel;
import org.jowidgets.workbench.toolkit.api.IWorkbenchModelBuilder;
import org.jowidgets.workbench.toolkit.api.WorkbenchToolkit;

public class Workbench implements IWorkbench {

	private final IWorkbench workbench;
	private final IWorkbenchModel model;

	public Workbench(final IWorkbenchDescriptor descriptor) {
		this(builder(descriptor));
	}

	public Workbench(final String label) {
		this(builder(label));
	}

	public Workbench(final String label, final IImageConstant icon) {
		this(builder(label, icon));
	}

	public Workbench(final IWorkbenchModelBuilder builder) {
		this(build(builder));
	}

	public Workbench(final IWorkbenchModel model) {
		Assert.paramNotNull(model, "model");
		this.model = model;
		this.workbench = WorkbenchToolkit.getWorkbenchPartFactory().workbench(model);
	}

	public final IWorkbenchModel getModel() {
		return model;
	}

	@Override
	public final Dimension getInitialDimension() {
		return workbench.getInitialDimension();
	}

	@Override
	public boolean isInitialMaximized() {
		return workbench.isInitialMaximized();
	}

	@Override
	public boolean isDecorated() {
		return workbench.isDecorated();
	}

	@Override
	public final Position getInitialPosition() {
		return workbench.getInitialPosition();
	}

	@Override
	public final double getInitialSplitWeight() {
		return workbench.getInitialSplitWeight();
	}

	@Override
	public final boolean hasApplicationNavigator() {
		return workbench.hasApplicationNavigator();
	}

	@Override
	public final boolean getApplicationsCloseable() {
		return workbench.getApplicationsCloseable();
	}

	@Override
	public final String getLabel() {
		return workbench.getLabel();
	}

	@Override
	public final String getTooltip() {
		return workbench.getTooltip();
	}

	@Override
	public final IImageConstant getIcon() {
		return workbench.getIcon();
	}

	@Override
	public final void onContextInitialize(final IWorkbenchContext context) {
		workbench.onContextInitialize(context);
	}

	@Override
	public final IView createView(final String viewId, final IViewContext viewContext) {
		return workbench.createView(viewId, viewContext);
	}

	@Override
	public final void onClose(final IVetoable vetoable) {
		workbench.onClose(vetoable);
	}

	@Override
	public void onLogin(final IVetoable vetoable) {
		workbench.onLogin(vetoable);
	}

	@Override
	public void onDispose() {
		workbench.onDispose();
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
		Assert.paramNotNull(descriptor, "descriptor");
		return builder().setDescriptor(descriptor);
	}

	private static IWorkbenchModel build(final IWorkbenchModelBuilder builder) {
		Assert.paramNotNull(builder, "builder");
		return builder.build();
	}

}
