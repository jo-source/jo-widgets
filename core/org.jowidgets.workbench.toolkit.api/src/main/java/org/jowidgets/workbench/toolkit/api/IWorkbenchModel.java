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

import java.util.List;

import org.jowidgets.api.model.IListModelObservable;
import org.jowidgets.api.model.item.IMenuBarModel;
import org.jowidgets.api.model.item.IToolBarModel;
import org.jowidgets.api.widgets.content.IContentCreator;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.workbench.api.ICloseCallback;
import org.jowidgets.workbench.api.IWorkbenchApplicationDescriptor;
import org.jowidgets.workbench.api.IWorkbenchDescriptor;

public interface IWorkbenchModel extends IWorkbenchDescriptor, IWorkbenchPartModel, IListModelObservable {

	IToolBarModel getToolBar();

	IMenuBarModel getMenuBar();

	List<Runnable> getShutdownHooks();

	IContentCreator getStatusBarCreator();

	ICloseCallback getCloseCallback();

	IWorkbenchInitializeCallback getInitializeCallback();

	List<IWorkbenchApplicationModel> getApplications();

	int getApplicationCount();

	void setToolBar(IToolBarModel toolBarModel);

	void setMenuBar(IMenuBarModel menuBarModel);

	void setStatusBarCreator(IContentCreator statusBarContentCreator);

	void setCloseCallback(ICloseCallback closeCallback);

	void addShutdownHook(Runnable shutdownHook);

	void removeShutdownHook(Runnable shutdownHook);

	IWorkbenchApplicationModel addApplication(IWorkbenchApplicationModel applicationModel);

	IWorkbenchApplicationModel addApplication(int index, IWorkbenchApplicationModel applicationModel);

	IWorkbenchApplicationModel addApplication(IWorkbenchApplicationModelBuilder applicationModelBuilder);

	IWorkbenchApplicationModel addApplication(int index, IWorkbenchApplicationModelBuilder applicationModelBuilder);

	IWorkbenchApplicationModel addApplication(IWorkbenchApplicationDescriptor applicationDescriptor);

	IWorkbenchApplicationModel addApplication(int index, IWorkbenchApplicationDescriptor applicationDescriptor);

	IWorkbenchApplicationModel addApplication(String id, String label, String tooltip, IImageConstant icon);

	IWorkbenchApplicationModel addApplication(String id, String label, IImageConstant icon);

	IWorkbenchApplicationModel addApplication(String id, String label, String tooltip);

	IWorkbenchApplicationModel addApplication(String id, String label);

	IWorkbenchApplicationModel addApplication(String id);

	void removeApplication(int index);

	void removeApplication(IWorkbenchApplicationModel childModel);

	void removeAllApplications();

}
