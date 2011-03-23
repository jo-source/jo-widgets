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

package org.jowidgets.workbench.toolkit.api;

import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.model.item.IToolBarModel;
import org.jowidgets.workbench.api.ILifecycleCallback;
import org.jowidgets.workbench.api.IWorkbenchApplicationDescriptor;

public interface IWorkbenchApplicationModel extends
		IWorkbenchApplicationDescriptor,
		IComponentNodeContainerModel,
		IWorkbenchPartModel {

	IWorkbenchApplicationInitializeCallback getInitializeCallback();

	IMenuModel getPopupMenu();

	IToolBarModel getToolBar();

	IMenuModel getToolBarMenu();

	ILifecycleCallback getLifecycleCallback();

	void setPopupMenu(IMenuModel menuModel);

	void setToolBar(IToolBarModel toolBarModel);

	void setToolBarMenu(IMenuModel toolBarMenu);

	void setLifecycleCallback(ILifecycleCallback lifecycleCallback);

	IWorkbenchModel getWorkbench();

	/**
	 * Sets the workbench of this application. This method will be invoked
	 * by the API implementation, when this application will be added as a child to an workbench or
	 * when it was removed from its workbench.
	 * 
	 * If this method will be invoked by the API user (client code) the following happens:
	 * 
	 * 1. If this application is already associated with a workbench, it will be removed from it.
	 * 
	 * 2. If the given workbench is not null and this application is not already a child of the given workbench,
	 * this application will be appended to the given workbench.
	 * 
	 * @param workbench The workbench to set or null if the application was/should be removed from its workbench
	 */
	void setWorkbench(IWorkbenchModel workbench);

}
