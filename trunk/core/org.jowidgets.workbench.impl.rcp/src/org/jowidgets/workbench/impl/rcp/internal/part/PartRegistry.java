/*
 * Copyright (c) 2011, M. Grossmann, M. Woelker, H. Westphal
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
import org.jowidgets.workbench.api.IPerspective;
import org.jowidgets.workbench.api.ISingleViewContainer;
import org.jowidgets.workbench.api.ISplitViewContainer;
import org.jowidgets.workbench.api.ITabViewContainer;
import org.jowidgets.workbench.api.IView;
import org.jowidgets.workbench.api.IViewContainer;
import org.jowidgets.workbench.impl.rcp.RcpView;

public final class PartRegistry {

	private static final PartRegistry INSTANCE = new PartRegistry();

	private final Map<String, IViewContainerContext> viewContainerContextMap = new HashMap<String, IViewContainerContext>();
	private final Map<String, IView> viewMap = new HashMap<String, IView>();

	private PartRegistry() {}

	public static PartRegistry getInstance() {
		return INSTANCE;
	}

	public IViewContainerContext getViewContainerContext(final String perspectiveId) {
		return viewContainerContextMap.get(perspectiveId);
	}

	public IView getView(final String viewId) {
		return viewMap.get(viewId);
	}

	public void showEmptyPerspective() {
		final IPerspectiveRegistry perspectiveRegistry = PlatformUI.getWorkbench().getPerspectiveRegistry();
		final IPerspectiveDescriptor perspectiveDescriptor = perspectiveRegistry.findPerspectiveWithId(DynamicPerspective.ID);
		final IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		activePage.setPerspective(perspectiveDescriptor);
		perspectiveRegistry.setDefaultPerspective(DynamicPerspective.ID);
	}

	public void showPerspective(final String nodeId, final IPerspective perspective) {
		final IPerspectiveRegistry perspectiveRegistry = PlatformUI.getWorkbench().getPerspectiveRegistry();
		final IPerspectiveDescriptor perspectiveDescriptor = perspectiveRegistry.findPerspectiveWithId(DynamicPerspective.ID);
		final String perspectiveId = nodeId + "." + perspective.getId();
		IPerspectiveDescriptor newPerspective = perspectiveRegistry.findPerspectiveWithId(perspectiveId);
		if (newPerspective == null) {
			newPerspective = perspectiveRegistry.clonePerspective(
					perspectiveId,
					String.valueOf(perspective.getLabel()),
					perspectiveDescriptor);
			final IViewContainerContext context = registerViews(perspectiveId, perspective.getViewContainer());
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

	private IViewContainerContext registerViews(final String perspectiveId, final IViewContainer viewContainer) {
		if (viewContainer instanceof ISplitViewContainer) {
			final ISplitViewContainer splitViewContainer = (ISplitViewContainer) viewContainer;
			return new SplitViewContainerContext(splitViewContainer, registerViews(
					perspectiveId,
					splitViewContainer.createFirstContainer()), registerViews(
					perspectiveId,
					splitViewContainer.createSecondContainer()));
		}
		if (viewContainer instanceof ITabViewContainer) {
			final ITabViewContainer viewListContainer = (ITabViewContainer) viewContainer;
			final TabViewContainerContext context = new TabViewContainerContext(perspectiveId + "." + viewListContainer.getId());
			for (final ISingleViewContainer singleViewContainer : viewListContainer.createViews()) {
				context.add(registerView(perspectiveId, singleViewContainer));
			}
			return context;
		}
		if (viewContainer instanceof ISingleViewContainer) {
			final ISingleViewContainer singleViewContainer = (ISingleViewContainer) viewContainer;
			return registerView(perspectiveId, singleViewContainer);
		}
		throw new IllegalArgumentException("unknown view container type");
	}

	private SingleViewContainerContext registerView(final String perspectiveId, final ISingleViewContainer viewContainer) {
		final IView view = viewContainer.createView();
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
			viewContainer.isCloseable(),
			viewContainer.isDetachable(),
			view instanceof RcpView);
	}
}
