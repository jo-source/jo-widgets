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
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.jowidgets.workbench.api.IFolderContext;
import org.jowidgets.workbench.api.IFolderLayout;
import org.jowidgets.workbench.api.ILayout;
import org.jowidgets.workbench.api.ILayoutContainer;
import org.jowidgets.workbench.api.ISplitLayout;
import org.jowidgets.workbench.api.IView;
import org.jowidgets.workbench.api.IViewLayout;
import org.jowidgets.workbench.impl.rcp.RcpView;
import org.jowidgets.workbench.impl.rcp.internal.ComponentContext;
import org.jowidgets.workbench.impl.rcp.internal.FolderContext;
import org.jowidgets.workbench.impl.rcp.internal.ViewContext;

public final class PartSupport {

	private static final PartSupport INSTANCE = new PartSupport();

	private final Map<String, IViewContainerContext> viewContainerContextMap = new HashMap<String, IViewContainerContext>();
	private final Map<String, ViewLayoutContext> viewLayoutContextMap = new HashMap<String, ViewLayoutContext>();
	private final Map<String, ViewContext> viewContextMap = new HashMap<String, ViewContext>();
	private final Map<String, IView> viewMap = new HashMap<String, IView>();
	private final Set<String> closingViewSet = new HashSet<String>();
	private final Set<String> hidingViewSet = new HashSet<String>();

	private PartSupport() {}

	public static PartSupport getInstance() {
		return INSTANCE;
	}

	public IViewContainerContext getViewContainerContext(final String perspectiveId) {
		return viewContainerContextMap.get(perspectiveId);
	}

	public ViewLayoutContext getViewLayoutContext(final String viewId) {
		return viewLayoutContextMap.get(viewId);
	}

	public IView getView(final String viewId) {
		return viewMap.get(viewId);
	}

	public void setViewAndContext(final String viewId, final IView view, final ViewContext viewContext) {
		viewMap.put(viewId, view);
		viewContextMap.put(viewId, viewContext);
	}

	public ViewContext getViewContext(final String viewId) {
		return viewContextMap.get(viewId);
	}

	public void removeViewAndContext(final String viewId) {
		viewMap.remove(viewId);
		viewContextMap.remove(viewId);
		closingViewSet.remove(viewId);
	}

	public void showEmptyPerspective() {
		final IPerspectiveRegistry perspectiveRegistry = PlatformUI.getWorkbench().getPerspectiveRegistry();
		final IPerspectiveDescriptor perspectiveDescriptor = perspectiveRegistry.findPerspectiveWithId(DynamicPerspective.ID);
		final IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		activePage.setPerspective(perspectiveDescriptor);
		perspectiveRegistry.setDefaultPerspective(DynamicPerspective.ID);
	}

	public void showPerspective(final String nodeId, final ComponentContext componentContext) {
		final IPerspectiveRegistry perspectiveRegistry = PlatformUI.getWorkbench().getPerspectiveRegistry();
		final IPerspectiveDescriptor perspectiveDescriptor = perspectiveRegistry.findPerspectiveWithId(DynamicPerspective.ID);
		if (componentContext == null) {
			showEmptyPerspective();
			return;
		}

		final ILayout perspective = componentContext.getPerspective();
		if (perspective == null) {
			showEmptyPerspective();
			return;
		}

		final String perspectiveId = nodeId + "." + perspective.getId();
		IPerspectiveDescriptor newPerspective = perspectiveRegistry.findPerspectiveWithId(perspectiveId);
		if (newPerspective == null) {
			newPerspective = perspectiveRegistry.clonePerspective(
					perspectiveId,
					String.valueOf(perspective.getLabel()),
					perspectiveDescriptor);
			final IViewContainerContext context = registerViews(perspectiveId, perspective.getLayoutContainer(), componentContext);
			viewContainerContextMap.put(perspectiveId, context);
		}
		final IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		activePage.setPerspective(newPerspective);

		if (componentContext.getResetLayout()) {
			activePage.resetPerspective();
		}

		// initialize views to update view titles from view model
		final IViewReference[] viewReferences = activePage.getViewReferences();
		for (final IViewReference viewReference : viewReferences) {
			viewReference.getView(true);
		}
	}

	private IViewContainerContext registerViews(
		final String perspectiveId,
		final ILayoutContainer viewContainer,
		final ComponentContext componentContext) {
		if (viewContainer instanceof ISplitLayout) {
			final ISplitLayout splitViewContainer = (ISplitLayout) viewContainer;
			return new SplitViewContainerContext(splitViewContainer, registerViews(
					perspectiveId,
					splitViewContainer.getFirstContainer(),
					componentContext), registerViews(perspectiveId, splitViewContainer.getSecondContainer(), componentContext));
		}
		if (viewContainer instanceof IFolderLayout) {
			final IFolderLayout viewListContainer = (IFolderLayout) viewContainer;
			final TabViewContainerContext context = new TabViewContainerContext(perspectiveId + "." + viewListContainer.getId());
			final IFolderContext folderContext = new FolderContext(viewListContainer.getId(), componentContext);
			componentContext.getComponent().onFolderCreated(folderContext);
			for (final IViewLayout singleViewContainer : viewListContainer.getViews()) {
				context.add(registerView(perspectiveId, viewListContainer, singleViewContainer, componentContext, folderContext));
			}
			return context;
		}
		throw new IllegalArgumentException("unknown view container type");
	}

	private SingleViewContainerContext registerView(
		final String perspectiveId,
		final IFolderLayout folderLayout,
		final IViewLayout view,
		final ComponentContext componentContext,
		final IFolderContext folderContext) {
		final String viewId;
		if (view instanceof RcpView) {
			viewId = view.getId();
		}
		else {
			viewId = perspectiveId + "." + view.getId();
			viewLayoutContextMap.put(viewId, new ViewLayoutContext(view, componentContext, folderContext));
		}
		return new SingleViewContainerContext(
			viewId,
			folderLayout.getViewsCloseable(),
			view.isDetachable(),
			view instanceof RcpView);
	}

	public void showView(final IViewLayout viewLayout, final ComponentContext componentContext, final IFolderContext folderContext) {
		final String viewId = viewLayout.getId();

		try {
			if (viewLayout instanceof RcpView) {
				showRcpView(viewId);
				return;
			}

			final String primaryViewId = DynamicView.ID + "." + folderContext.getFolderId();
			viewLayoutContextMap.put(viewId, new ViewLayoutContext(viewLayout, componentContext, folderContext));
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(
					primaryViewId,
					viewId,
					IWorkbenchPage.VIEW_CREATE);
		}
		catch (final PartInitException e) {
			throw new RuntimeException(e);
		}
	}

	private void showRcpView(final String viewId) throws PartInitException {
		final String[] ids = viewId.split(":", 2);
		String secondaryId = null;
		if (ids.length == 2) {
			secondaryId = ids[1];
		}
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(
				ids[0],
				secondaryId,
				IWorkbenchPage.VIEW_CREATE);
	}

	public void closeView(final IView view) {
		for (final Entry<String, IView> viewEntry : viewMap.entrySet()) {
			// find viewId
			if (viewEntry.getValue() == view) {
				final String viewId = viewEntry.getKey();
				final String primaryViewId = viewContextMap.get(viewId).getPrimaryViewId();
				for (final IWorkbenchPage page : PlatformUI.getWorkbench().getActiveWorkbenchWindow().getPages()) {
					final IViewReference viewRef = page.findViewReference(primaryViewId, viewId);
					if (viewRef != null) {
						closingViewSet.add(viewId);
						page.hideView(viewRef);
						break;
					}
				}
				break;
			}
		}
	}

	public boolean isViewClosing(final String viewId) {
		return closingViewSet.contains(viewId);
	}

	public void activateView(final ViewContext viewContext) {
		for (final Entry<String, ViewContext> viewContextEntry : viewContextMap.entrySet()) {
			// find viewId
			if (viewContextEntry.getValue() == viewContext) {
				final String viewId = viewContextEntry.getKey();
				final String primaryViewId = viewContext.getPrimaryViewId();
				for (final IWorkbenchPage page : PlatformUI.getWorkbench().getActiveWorkbenchWindow().getPages()) {
					final IViewReference viewRef = page.findViewReference(primaryViewId, viewId);
					if (viewRef != null) {
						try {
							page.showView(primaryViewId, viewId, IWorkbenchPage.VIEW_ACTIVATE);
						}
						catch (final PartInitException e) {
							throw new RuntimeException(e);
						}
						break;
					}
				}
				break;
			}
		}
	}

	public void hideView(final ViewContext viewContext) {
		for (final Entry<String, ViewContext> viewContextEntry : viewContextMap.entrySet()) {
			// find viewId
			if (viewContextEntry.getValue() == viewContext) {
				final String viewId = viewContextEntry.getKey();
				final String primaryViewId = viewContext.getPrimaryViewId();
				for (final IWorkbenchPage page : PlatformUI.getWorkbench().getActiveWorkbenchWindow().getPages()) {
					final IViewReference viewRef = page.findViewReference(primaryViewId, viewId);
					if (viewRef != null) {
						hidingViewSet.add(viewId);
						page.hideView(viewRef);
						break;
					}
				}
				break;
			}
		}
	}

	public boolean isViewHiding(final String viewId) {
		return hidingViewSet.contains(viewId);
	}

	public void unhideView(final ViewContext viewContext) {
		for (final Entry<String, ViewContext> viewContextEntry : viewContextMap.entrySet()) {
			// find viewId
			if (viewContextEntry.getValue() == viewContext) {
				final String viewId = viewContextEntry.getKey();
				final String primaryViewId = viewContext.getPrimaryViewId();
				try {
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(
							primaryViewId,
							viewId,
							IWorkbenchPage.VIEW_ACTIVATE);
					hidingViewSet.remove(viewId);
				}
				catch (final PartInitException e) {
					throw new RuntimeException(e);
				}
				break;
			}
		}
	}

}
