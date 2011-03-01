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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jowidgets.api.model.IListModelListener;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.common.types.Position;
import org.jowidgets.tools.types.VetoHolder;
import org.jowidgets.workbench.api.IView;
import org.jowidgets.workbench.api.IViewLayout;
import org.jowidgets.workbench.impl.rcp.internal.ComponentContext;
import org.jowidgets.workbench.impl.rcp.internal.ViewContext;
import org.jowidgets.workbench.impl.rcp.internal.util.ImageHelper;

public final class DynamicView extends ViewPart implements IPartListener2 {

	public static final String ID = "org.jowidgets.workbench.impl.rcp.internal.part.dynamicView"; //$NON-NLS-1$

	private static final IAction EMPTY_ACTION = new Action() {};

	private IView view;

	@Override
	public void createPartControl(final Composite parent) {
		final String viewId = getViewSite().getSecondaryId();
		final ViewLayoutContext viewLayoutContext = PartSupport.getInstance().getView(viewId);

		if (viewLayoutContext == null) {
			// view layout is not registered -> close it immediately
			Display.getCurrent().asyncExec(new Runnable() {
				@Override
				public void run() {
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().hideView(DynamicView.this);
				}
			});
			return;
		}

		final IViewLayout viewLayout = viewLayoutContext.getViewLayout();
		final ComponentContext componentContext = viewLayoutContext.getComponentContext();

		setPartName(viewLayout.getLabel());
		setTitleImage(ImageHelper.getImage(viewLayout.getIcon(), null));
		setTitleToolTip(viewLayout.getTooltip());

		final IMenuModel menuModel = viewLayoutContext.getFolderContext().getPopupMenu();
		final IPopupMenu popupMenu = Toolkit.getWidgetWrapperFactory().createComposite(parent).createPopupMenu();
		popupMenu.setModel(menuModel);
		final IMenuManager menuManager = getViewSite().getActionBars().getMenuManager();
		menuManager.setRemoveAllWhenShown(true);
		if (!menuModel.getChildren().isEmpty()) {
			menuManager.add(EMPTY_ACTION);
		}
		menuManager.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(final IMenuManager manager) {
				final Point position = parent.toControl(parent.getDisplay().getCursorLocation());
				popupMenu.show(new Position(position.x - 10, -3));
				parent.getDisplay().asyncExec(new Runnable() {
					@Override
					public void run() {
						menuManager.add(EMPTY_ACTION);
						menuManager.update(true);
					}
				});
			}
		});
		menuModel.addListModelListener(new IListModelListener() {
			@Override
			public void childAdded(final int index) {
				if (menuModel.getChildren().size() == 1) {
					menuManager.add(EMPTY_ACTION);
					menuManager.update(true);
				}
			}

			@Override
			public void childRemoved(final int index) {
				if (menuModel.getChildren().size() == 0) {
					menuManager.removeAll();
					menuManager.update(true);
				}
			}
		});

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
				final ViewContext viewContext = new ViewContext(parent, componentContext);
				view = componentContext.getComponent().createView(viewLayout.getId(), viewContext);
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
		if (part == this && view != null) {
			view.onActiveStateChanged(true);
		}
	}

	@Override
	public void partDeactivated(final IWorkbenchPartReference partRef) {
		final IWorkbenchPart part = partRef.getPart(false);
		if (part == this && view != null) {
			view.onActiveStateChanged(false);
		}
	}

	@Override
	public void partVisible(final IWorkbenchPartReference partRef) {
		final IWorkbenchPart part = partRef.getPart(false);
		if (part == this && view != null) {
			view.onVisibleStateChanged(true);
		}
	}

	@Override
	public void partHidden(final IWorkbenchPartReference partRef) {
		final IWorkbenchPart part = partRef.getPart(false);
		if (part == this && view != null) {
			view.onVisibleStateChanged(false);
		}
	}

	@Override
	public void partClosed(final IWorkbenchPartReference partRef) {
		final IWorkbenchPart part = partRef.getPart(false);
		if (part == this && view != null && !PlatformUI.getWorkbench().isClosing()) {
			final VetoHolder vetoHolder = new VetoHolder();
			view.onClose(vetoHolder);
			if (vetoHolder.hasVeto()) {
				final IViewReference viewRef = (IViewReference) partRef;
				try {
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(
							viewRef.getId(),
							viewRef.getSecondaryId(),
							IWorkbenchPage.VIEW_ACTIVATE);
				}
				catch (final PartInitException e) {
					throw new RuntimeException(e);
				}
			}
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

	@Override
	public void dispose() {
		// TODO HRW implement dispose?
		super.dispose();
	}

}
