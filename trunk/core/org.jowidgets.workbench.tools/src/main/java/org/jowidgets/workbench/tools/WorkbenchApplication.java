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
import org.jowidgets.common.types.IVetoable;
import org.jowidgets.util.Assert;
import org.jowidgets.workbench.api.IWorkbenchApplication;
import org.jowidgets.workbench.api.IWorkbenchApplicationContext;
import org.jowidgets.workbench.toolkit.api.IWorkbenchApplicationModel;
import org.jowidgets.workbench.toolkit.api.IWorkbenchApplicationModelBuilder;
import org.jowidgets.workbench.toolkit.api.WorkbenchToolkit;

public class WorkbenchApplication implements IWorkbenchApplication {

	private final IWorkbenchApplication application;
	private final IWorkbenchApplicationModel model;

	public WorkbenchApplication(final String label) {
		this(builder(label));
	}

	public WorkbenchApplication(final String label, final IImageConstant icon) {
		this(builder(label, icon));
	}

	public WorkbenchApplication(final String label, final String tooltip) {
		this(builder(label, tooltip));
	}

	public WorkbenchApplication(final String label, final String tooltip, final IImageConstant icon) {
		this(builder(label, tooltip, icon));
	}

	public WorkbenchApplication(final IWorkbenchApplicationModelBuilder builder) {
		this(build(builder));
	}

	public WorkbenchApplication(final IWorkbenchApplicationModel model) {
		Assert.paramNotNull(model, "model");
		this.application = WorkbenchToolkit.getWorkbenchPartFactory().application(model);
		this.model = model;
	}

	public final IWorkbenchApplicationModel getModel() {
		return model;
	}

	@Override
	public final String getId() {
		return application.getId();
	}

	@Override
	public final String getLabel() {
		return application.getLabel();
	}

	@Override
	public final IImageConstant getIcon() {
		return application.getIcon();
	}

	@Override
	public final String getTooltip() {
		return application.getTooltip();
	}

	@Override
	public final void onClose(final IVetoable vetoable) {
		application.onClose(vetoable);
	}

	@Override
	public final void onActiveStateChanged(final boolean active) {
		application.onActiveStateChanged(active);
	}

	@Override
	public final void onVisibleStateChanged(final boolean visible) {
		application.onVisibleStateChanged(visible);
	}

	@Override
	public final void onContextInitialize(final IWorkbenchApplicationContext context) {
		application.onContextInitialize(context);
	}

	public static IWorkbenchApplicationModelBuilder builder() {
		return WorkbenchToolkit.getWorkbenchPartBuilderFactory().application();
	}

	public static IWorkbenchApplicationModelBuilder builder(final String label) {
		return builder().setLabel(label);
	}

	public static IWorkbenchApplicationModelBuilder builder(final String label, final IImageConstant icon) {
		return builder(label).setIcon(icon);
	}

	public static IWorkbenchApplicationModelBuilder builder(final String label, final String tooltip) {
		return builder(label).setTooltip(tooltip);
	}

	public static IWorkbenchApplicationModelBuilder builder(final String label, final String tooltip, final IImageConstant icon) {
		return builder(label, tooltip).setIcon(icon);
	}

	private static IWorkbenchApplicationModel build(final IWorkbenchApplicationModelBuilder builder) {
		Assert.paramNotNull(builder, "builder");
		return builder.build();
	}

}
