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
import org.jowidgets.workbench.api.IWorkbench;
import org.jowidgets.workbench.api.IWorkbenchContext;
import org.jowidgets.workbench.toolkit.api.IWorkbenchModel;
import org.jowidgets.workbench.toolkit.api.IWorkbenchModelBuilder;
import org.jowidgets.workbench.toolkit.api.WorkbenchToolkit;

public class Workbench implements IWorkbench {

	private final IWorkbench workbench;
	private final IWorkbenchModel model;

	public Workbench() {
		this(new WorkbenchModelBuilder());
	}

	public Workbench(final String label) {
		this(new WorkbenchModelBuilder(label));
	}

	public Workbench(final String label, final IImageConstant icon) {
		this(new WorkbenchModelBuilder(label, icon));
	}

	public Workbench(final IWorkbenchModelBuilder workbenchModelBuilder) {
		this(workbenchModelBuilder.build());
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
	public Dimension getInitialDimension() {
		return workbench.getInitialDimension();
	}

	@Override
	public Position getInitialPosition() {
		return workbench.getInitialPosition();
	}

	@Override
	public double getInitialSplitWeight() {
		return workbench.getInitialSplitWeight();
	}

	@Override
	public boolean hasApplicationNavigator() {
		return workbench.hasApplicationNavigator();
	}

	@Override
	public boolean getApplicationsCloseable() {
		return workbench.getApplicationsCloseable();
	}

	@Override
	public String getLabel() {
		return workbench.getLabel();
	}

	@Override
	public String getTooltip() {
		return workbench.getTooltip();
	}

	@Override
	public IImageConstant getIcon() {
		return workbench.getIcon();
	}

	@Override
	public void onContextInitialize(final IWorkbenchContext context) {
		workbench.onContextInitialize(context);
	}

	@Override
	public void onClose(final IVetoable vetoable) {
		workbench.onClose(vetoable);
	}

}
