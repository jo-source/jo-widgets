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

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.jowidgets.common.types.Orientation;
import org.jowidgets.workbench.api.ISplitViewContainer;

public final class DynamicPerspective implements IPerspectiveFactory {

	public static final String ID = "org.jowidgets.workbench.impl.rcp.internal.part.dynamicPerspective"; //$NON-NLS-1$

	@Override
	public void createInitialLayout(final IPageLayout layout) {
		final String perspectiveId = layout.getDescriptor().getId();
		if (perspectiveId.equals(ID)) {
			return;
		}
		final String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		layout.setFixed(false);
		final IViewContainerContext viewContainerContext = PartRegistry.getInstance().getViewContainerContext(perspectiveId);
		createLayout(layout, viewContainerContext, editorArea);
	}

	/*
	 * Layouting is a bit tricky here, since there is layouting model impedance mismatch.
	 * Given a vertical SplitViewContainer containing two horizontal SplitViewContainers the creation order should be as follows:
	 * +-----+-----+
	 * |..1..|..2..|
	 * |.....|.....|
	 * +-----+.....|
	 * |..3..+-----+
	 * |.....|..4..|
	 * |.....|.....|
	 * +-----+-----+
	 * 
	 * Thus layoutFirst is called recursively to create the first component for each subContainer, then layoutRest to add the
	 * remaing views
	 */
	private void createLayout(final IPageLayout layout, final IViewContainerContext viewContainerContext, final String relativeTo) {
		final String firstId = layoutFirst(layout, viewContainerContext, IPageLayout.LEFT, 1.0f, relativeTo);
		layoutRest(layout, viewContainerContext, firstId);
	}

	private String layoutFirst(
		final IPageLayout layout,
		final IViewContainerContext viewContainerContext,
		final int relation,
		final float ratio,
		final String relativeTo) {
		if (viewContainerContext instanceof SplitViewContainerContext) {
			final SplitViewContainerContext splitViewContainerContext = (SplitViewContainerContext) viewContainerContext;
			final String firstViewId = layoutFirst(layout, splitViewContainerContext.getFirst(), relation, ratio, relativeTo);
			return firstViewId;
		}
		if (viewContainerContext instanceof SingleViewContainerContext) {
			final SingleViewContainerContext singleViewContainerContext = (SingleViewContainerContext) viewContainerContext;
			return layoutSingle(layout, singleViewContainerContext, relation, ratio, relativeTo);
		}
		if (viewContainerContext instanceof TabViewContainerContext) {
			final TabViewContainerContext tabViewContainerContext = (TabViewContainerContext) viewContainerContext;
			return layoutViewList(layout, tabViewContainerContext, relation, ratio, relativeTo);
		}
		throw new IllegalArgumentException("view container context type");
	}

	private String layoutViewList(
		final IPageLayout layout,
		final TabViewContainerContext tabViewContainerContext,
		final int relation,
		final float ratio,
		final String relativeTo) {
		final String folderId = tabViewContainerContext.getFolderId();
		final IFolderLayout folder = layout.createFolder(folderId, relation, ratio, relativeTo);
		for (final SingleViewContainerContext viewContainerContext : tabViewContainerContext) {
			final String viewId = DynamicView.ID + ":" + viewContainerContext.getViewId();
			folder.addView(viewId);
			layout.getViewLayout(viewId).setCloseable(viewContainerContext.isCloseable());
			layout.getViewLayout(viewId).setMoveable(viewContainerContext.isDetachable());
		}
		return folderId;
	}

	private void layoutRest(final IPageLayout layout, final IViewContainerContext viewContainerContext, final String relativeTo) {
		if (viewContainerContext instanceof SplitViewContainerContext) {
			final SplitViewContainerContext splitViewContainerContext = (SplitViewContainerContext) viewContainerContext;
			final ISplitViewContainer splitViewContainer = splitViewContainerContext.getViewContainer();
			final float innerRatio = (float) splitViewContainer.getWeight();
			final int innerRelation = splitViewContainer.getOrientation().equals(Orientation.HORIZONTAL)
					? IPageLayout.BOTTOM : IPageLayout.RIGHT;
			final String secondViewId = layoutFirst(
					layout,
					splitViewContainerContext.getSecond(),
					innerRelation,
					innerRatio,
					relativeTo);
			layoutRest(layout, splitViewContainerContext.getFirst(), relativeTo);
			layoutRest(layout, splitViewContainerContext.getSecond(), secondViewId);
		}
	}

	private String layoutSingle(
		final IPageLayout layout,
		final SingleViewContainerContext viewContainerContext,
		final int relation,
		final float ratio,
		final String relativeTo) {
		final String viewId = DynamicView.ID + ":" + viewContainerContext.getViewId();
		layout.addView(viewId, relation, ratio, relativeTo);
		layout.getViewLayout(viewId).setCloseable(viewContainerContext.isCloseable());
		layout.getViewLayout(viewId).setMoveable(viewContainerContext.isDetachable());
		return viewId;
	}

}
