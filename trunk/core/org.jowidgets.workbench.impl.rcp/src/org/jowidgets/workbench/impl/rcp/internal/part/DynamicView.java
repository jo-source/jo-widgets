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

import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jowidgets.api.toolkit.IWidgetWrapperFactory;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.IToolBar;
import org.jowidgets.common.types.Position;
import org.jowidgets.workbench.api.IView;
import org.jowidgets.workbench.impl.rcp.internal.ImageHelper;
import org.jowidgets.workbench.impl.rcp.internal.ViewContext;

public final class DynamicView extends ViewPart implements IPartListener2 {

	public static final String ID = "org.jowidgets.workbench.impl.rcp.internal.part.dynamicView"; //$NON-NLS-1$

	private IView view;

	@Override
	public void createPartControl(final Composite parent) {
		final String viewId = getViewSite().getSecondaryId();
		view = PartRegistry.getInstance().getView(viewId);
		setPartName(view.getLabel());
		setTitleImage(ImageHelper.getImage(view.getIcon(), null));
		setTitleToolTip(view.getTooltip());

		final IComposite composite = Toolkit.getWidgetWrapperFactory().createComposite(parent);
		final ViewContext viewContext = new ViewContext(composite);
		if (view.hasMenu() || view.hasToolBar()) {
			final ToolBar toolBarControl = ((ToolBarManager) getViewSite().getActionBars().getToolBarManager()).getControl();
			final IWidgetWrapperFactory wrapperFactory = Toolkit.getWidgetWrapperFactory();
			if (view.hasMenu()) {
				final IPopupMenu menu = wrapperFactory.createComposite(toolBarControl).createPopupMenu();
				final ToolItem menuToolItem = new ToolItem(toolBarControl, SWT.DROP_DOWN | SWT.TRAIL);
				menuToolItem.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(final SelectionEvent e) {
						menu.show(new Position(e.x, e.y));
					}
				});
				viewContext.setMenu(menu);
				viewContext.setMenuToolItem(menuToolItem);
			}
			if (view.hasToolBar()) {
				// toolbar won't be visible
				final IToolBar toolBar = wrapperFactory.createComposite(toolBarControl.getParent()).add(
						Toolkit.getBluePrintFactory().toolBar(),
						"");
				viewContext.setToolBar(toolBar);
			}
		}

		// lazy initialization of view content
		getViewSite().getPage().addPartListener(new IPartListener2() {
			@Override
			public void partVisible(final IWorkbenchPartReference partRef) {
				final IWorkbenchPart part = partRef.getPart(false);
				if (part == DynamicView.this) {
					init();
				}
			}

			@Override
			public void partOpened(final IWorkbenchPartReference partRef) {
				final IWorkbenchPart part = partRef.getPart(false);
				// needed for lazy initializing detached views, since partVisible() is not fired for them apparently
				if (part == DynamicView.this
					&& part.getSite().getShell() != PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()) {
					init();
				}
			}

			private void init() {
				view.initialize(viewContext);
				final IWorkbenchPage page = getViewSite().getPage();
				page.removePartListener(this);
				view.onVisibleStateChanged(true);
				page.addPartListener(DynamicView.this);
			}

			@Override
			public void partInputChanged(final IWorkbenchPartReference partRef) {}

			@Override
			public void partHidden(final IWorkbenchPartReference partRef) {}

			@Override
			public void partDeactivated(final IWorkbenchPartReference partRef) {}

			@Override
			public void partClosed(final IWorkbenchPartReference partRef) {}

			@Override
			public void partBroughtToTop(final IWorkbenchPartReference partRef) {}

			@Override
			public void partActivated(final IWorkbenchPartReference partRef) {}
		});
	}

	@Override
	public void partActivated(final IWorkbenchPartReference partRef) {
		final IWorkbenchPart part = partRef.getPart(false);
		if (part == this) {
			view.onActiveStateChanged(true);
		}
	}

	@Override
	public void partDeactivated(final IWorkbenchPartReference partRef) {
		final IWorkbenchPart part = partRef.getPart(false);
		if (part == this) {
			view.onActiveStateChanged(false);
		}
	}

	@Override
	public void partVisible(final IWorkbenchPartReference partRef) {
		final IWorkbenchPart part = partRef.getPart(false);
		if (part == this) {
			view.onVisibleStateChanged(true);
		}
	}

	@Override
	public void partHidden(final IWorkbenchPartReference partRef) {
		final IWorkbenchPart part = partRef.getPart(false);
		if (part == this) {
			view.onVisibleStateChanged(false);
		}
	}

	@Override
	public void partClosed(final IWorkbenchPartReference partRef) {
		final IWorkbenchPart part = partRef.getPart(false);
		if (part == this) {
			view.onClose();
		}
	}

	@Override
	public void partBroughtToTop(final IWorkbenchPartReference partRef) {}

	@Override
	public void partOpened(final IWorkbenchPartReference partRef) {}

	@Override
	public void partInputChanged(final IWorkbenchPartReference partRef) {}

	@Override
	public void setFocus() {}

}
