/*
 * Copyright (c) 2011, M. Woelker, H. Westphal
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * * Neither the name of jo-widgets.org nor the
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

package org.jowidgets.workbench.impl.rcp.internal.part;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.jowidgets.workbench.api.IFolderLayout;
import org.jowidgets.workbench.api.ILayout;
import org.jowidgets.workbench.api.ILayoutContainer;
import org.jowidgets.workbench.api.ISplitLayout;
import org.jowidgets.workbench.api.IViewLayout;
import org.jowidgets.workbench.impl.rcp.RcpView;

public final class PartSupport {

	private static final PartSupport INSTANCE = new PartSupport();

	private final Map<String, IViewContainerContext> viewContainerContextMap = new HashMap<String, IViewContainerContext>();
	private final Map<String, IViewLayout> viewMap = new HashMap<String, IViewLayout>();

	private PartSupport() {}

	public static PartSupport getInstance() {
		return INSTANCE;
	}

	public IViewContainerContext getViewContainerContext(final String perspectiveId) {
		return viewContainerContextMap.get(perspectiveId);
	}

	public IViewLayout getView(final String viewId) {
		return viewMap.get(viewId);
	}

	public void showEmptyPerspective() {
		final IPerspectiveRegistry perspectiveRegistry = PlatformUI.getWorkbench().getPerspectiveRegistry();
		final IPerspectiveDescriptor perspectiveDescriptor = perspectiveRegistry.findPerspectiveWithId(DynamicPerspective.ID);
		final IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		activePage.setPerspective(perspectiveDescriptor);
		perspectiveRegistry.setDefaultPerspective(DynamicPerspective.ID);
	}

	public void showPerspective(final String nodeId, final ILayout perspective) {
		final IPerspectiveRegistry perspectiveRegistry = PlatformUI.getWorkbench().getPerspectiveRegistry();
		final IPerspectiveDescriptor perspectiveDescriptor = perspectiveRegistry.findPerspectiveWithId(DynamicPerspective.ID);
		final String perspectiveId = nodeId + "." + perspective.getId();
		IPerspectiveDescriptor newPerspective = perspectiveRegistry.findPerspectiveWithId(perspectiveId);
		if (newPerspective == null) {
			newPerspective = perspectiveRegistry.clonePerspective(
					perspectiveId,
					String.valueOf(perspective.getLabel()),
					perspectiveDescriptor);
			final IViewContainerContext context = registerViews(perspectiveId, perspective.getLayoutContainer());
			viewContainerContextMap.put(perspectiveId, context);
		}
		final IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		activePage.setPerspective(newPerspective);
		// initialize views to update view titles from view model
		final IViewReference[] viewReferences = activePage.getViewReferences();
		for (final IViewReference viewReference : viewReferences) {
			viewReference.getView(true);
		}
	}

	private IViewContainerContext registerViews(final String perspectiveId, final ILayoutContainer viewContainer) {
		if (viewContainer instanceof ISplitLayout) {
			final ISplitLayout splitViewContainer = (ISplitLayout) viewContainer;
			return new SplitViewContainerContext(
				splitViewContainer,
				registerViews(perspectiveId, splitViewContainer.getFirstContainer()),
				registerViews(perspectiveId, splitViewContainer.getSecondContainer()));
		}
		if (viewContainer instanceof IFolderLayout) {
			final IFolderLayout viewListContainer = (IFolderLayout) viewContainer;
			final TabViewContainerContext context = new TabViewContainerContext(perspectiveId + "." + viewListContainer.getId());
			for (final IViewLayout singleViewContainer : viewListContainer.getViews()) {
				context.add(registerView(perspectiveId, viewListContainer, singleViewContainer));
			}
			return context;
		}
		throw new IllegalArgumentException("unknown view container type");
	}

	private SingleViewContainerContext registerView(
		final String perspectiveId,
		final IFolderLayout folderLayout,
		final IViewLayout view) {
		final String viewId;
		if (view instanceof RcpView) {
			viewId = view.getId();
		}
		else {
			viewId = perspectiveId + "." + view.getId();
			viewMap.put(viewId, view);
		}
		return new SingleViewContainerContext(
			viewId,
			folderLayout.getViewsCloseable(),
			view.isDetachable(),
			view instanceof RcpView);
	}
}
