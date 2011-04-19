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

import org.jowidgets.util.Assert;
import org.jowidgets.workbench.api.IComponentNode;
import org.jowidgets.workbench.api.IWorkbench;
import org.jowidgets.workbench.api.IWorkbenchApplication;
import org.jowidgets.workbench.toolkit.api.IComponentNodeModel;
import org.jowidgets.workbench.toolkit.api.IComponentNodeModelBuilder;
import org.jowidgets.workbench.toolkit.api.IWorkbenchApplicationModel;
import org.jowidgets.workbench.toolkit.api.IWorkbenchApplicationModelBuilder;
import org.jowidgets.workbench.toolkit.api.IWorkbenchModel;
import org.jowidgets.workbench.toolkit.api.IWorkbenchModelBuilder;
import org.jowidgets.workbench.toolkit.api.IWorkbenchPartFactory;

class WorkbenchPartFactory implements IWorkbenchPartFactory {

	@Override
	public IWorkbench workbench(final IWorkbenchModel model) {
		Assert.paramNotNull(model, "model");
		return new Workbench(model);
	}

	@Override
	public IWorkbench workbench(final IWorkbenchModelBuilder modelBuilder) {
		Assert.paramNotNull(modelBuilder, "modelBuilder");
		return workbench(modelBuilder.build());
	}

	@Override
	public IWorkbenchApplication application(final IWorkbenchApplicationModel model) {
		return new WorkbenchApplication(model);
	}

	@Override
	public IWorkbenchApplication application(final IWorkbenchApplicationModelBuilder modelBuilder) {
		Assert.paramNotNull(modelBuilder, "modelBuilder");
		return application(modelBuilder.build());
	}

	@Override
	public IComponentNode componentNode(final IComponentNodeModel model) {
		return new ComponentNode(model);
	}

	@Override
	public IComponentNode componentNode(final IComponentNodeModelBuilder modelBuilder) {
		Assert.paramNotNull(modelBuilder, "modelBuilder");
		return componentNode(modelBuilder.build());
	}

}
