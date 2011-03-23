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

import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.model.item.IToolBarModel;
import org.jowidgets.workbench.api.IWorkbenchApplicationContext;
import org.jowidgets.workbench.api.IWorkbenchContext;
import org.jowidgets.workbench.toolkit.api.IWorkbenchApplicationModel;
import org.jowidgets.workbench.toolkit.api.IWorkbenchModel;

class ModelBasedWorkbenchApplicationContext extends ModelBasedComponentNodeContainerContext implements
		IWorkbenchApplicationContext {

	private final IWorkbenchApplicationContext applicationContext;
	private final IWorkbenchApplicationModel applicationModel;

	private IWorkbenchContext workbenchContext;

	ModelBasedWorkbenchApplicationContext(final IWorkbenchApplicationContext context, final IWorkbenchApplicationModel model) {
		super(model);
		this.applicationContext = context;
		this.applicationModel = model;
	}

	@Override
	public IWorkbenchContext getWorkbenchContext() {
		if (workbenchContext == null) {
			final IWorkbenchModel wbModel = applicationModel.getWorkbench();
			final IWorkbenchContext wbContext = applicationContext.getWorkbenchContext();
			workbenchContext = new ModelBasedWorkbenchContext(wbModel, wbContext);
		}
		return workbenchContext;
	}

	@Override
	public IToolBarModel getToolBar() {
		return applicationModel.getToolBar();
	}

	@Override
	public IMenuModel getToolBarMenu() {
		return applicationModel.getToolBarMenu();
	}

	@Override
	public IMenuModel getPopupMenu() {
		return applicationModel.getPopupMenu();
	}

}
