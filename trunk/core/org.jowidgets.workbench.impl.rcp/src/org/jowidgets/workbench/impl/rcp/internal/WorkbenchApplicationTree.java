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
package org.jowidgets.workbench.impl.rcp.internal;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.jowidgets.api.model.IListModelListener;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.model.item.IToolBarModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.IToolBar;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.types.Position;
import org.jowidgets.tools.model.item.MenuModel;
import org.jowidgets.tools.model.item.ToolBarModel;
import org.jowidgets.workbench.api.IComponentTreeNodeContext;
import org.jowidgets.workbench.api.ILayout;
import org.jowidgets.workbench.api.IWorkbenchApplication;
import org.jowidgets.workbench.api.IWorkbenchPart;
import org.jowidgets.workbench.impl.rcp.internal.part.PartSupport;
import org.jowidgets.workbench.impl.rcp.internal.util.ImageHelper;

public final class WorkbenchApplicationTree extends Composite {

	private final IWorkbenchApplication application;
	private final Composite folderComposite;
	private TreeViewer treeViewer;
	private IMenuModel popupMenuModel;
	private IPopupMenu popupMenu;
	private final IToolBarModel internalToolBarModel;
	private final IToolBarModel toolBarModel;
	private IMenuModel toolBarMenuModel;
	private String nodeId;
	private AtomicReference<ILayout> perspectiveReference;

	public WorkbenchApplicationTree(final Composite parent, final IWorkbenchApplication application) {
		super(parent, SWT.NONE);
		setLayout(new FillLayout());

		this.application = application;
		folderComposite = new Composite(parent, SWT.NONE);
		folderComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		final IComposite joComposite = Toolkit.getWidgetWrapperFactory().createComposite(folderComposite);
		final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();

		final IToolBar toolBar = joComposite.add(bpf.toolBar(), null);
		internalToolBarModel = new ToolBarModel();
		toolBar.setModel(internalToolBarModel);

		toolBarModel = new ToolBarModel();
		toolBarModel.addListModelListener(new IListModelListener() {
			@Override
			public void childRemoved(final int index) {
				internalToolBarModel.removeItem(index);
			}

			@Override
			public void childAdded(final int index) {
				internalToolBarModel.addItem(index, toolBarModel.getItems().get(index));
			}
		});

		addTreeViewer();
	}

	private static class ViewLabelProvider extends ColumnLabelProvider {
		@Override
		public String getToolTipText(final Object element) {
			if (element instanceof IWorkbenchPart) {
				final IWorkbenchPart part = (IWorkbenchPart) element;
				return part.getTooltip();
			}
			return super.getToolTipText(element);
		}

		@Override
		public String getText(final Object element) {
			if (element instanceof IWorkbenchPart) {
				final IWorkbenchPart part = (IWorkbenchPart) element;
				return part.getLabel();
			}
			return super.getText(element);
		}

		@Override
		public Image getImage(final Object element) {
			if (element instanceof IWorkbenchPart) {
				final IWorkbenchPart part = (IWorkbenchPart) element;
				return ImageHelper.getImage(
						part.getIcon(),
						PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER));
			}
			return super.getImage(element);
		}
	}

	public void addTreeViewer() {
		treeViewer = new TreeViewer(this, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
		ColumnViewerToolTipSupport.enableFor(treeViewer);
		treeViewer.setContentProvider(new ITreeContentProvider() {
			@Override
			public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {}

			@Override
			public void dispose() {}

			@Override
			public Object[] getElements(final Object inputElement) {
				return getChildren(inputElement);
			}

			@Override
			public boolean hasChildren(final Object element) {
				return getChildren(element).length > 0;
			}

			@Override
			public Object getParent(final Object element) {
				return null;
			}

			@Override
			public Object[] getChildren(final Object parentElement) {
				if (parentElement instanceof WorkbenchApplicationContext) {
					return ((WorkbenchApplicationContext) parentElement).getComponentTreeNodeContexts();
				}
				if (parentElement instanceof IComponentTreeNodeContext) {
					return ((ComponentTreeNodeContext) parentElement).getComponentTreeNodeContexts();
				}
				return new Object[0];
			}
		});
		treeViewer.setLabelProvider(new ViewLabelProvider());

		final Tree tree = treeViewer.getTree();
		tree.addMenuDetectListener(new MenuDetectListener() {
			@Override
			public void menuDetected(final MenuDetectEvent e) {
				final Point position = WorkbenchApplicationTree.this.toControl(e.x, e.y);
				final TreeItem item = tree.getItem(position);
				if (item != null) {
					final ComponentTreeNodeContext context = (ComponentTreeNodeContext) item.getData();
					final IPopupMenu nodeMenu = context.getJoPopupMenu();
					if (nodeMenu != null) {
						nodeMenu.show(new Position(position.x, position.y));
					}
				}
				else if (popupMenu != null) {
					popupMenu.show(new Position(position.x, position.y));
				}
			}
		});

		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(final SelectionChangedEvent event) {
				if (!event.getSelection().isEmpty()) {
					final ComponentTreeNodeContext context = (ComponentTreeNodeContext) ((ITreeSelection) treeViewer.getSelection()).getFirstElement();
					ILayout perspective = null;
					final ComponentContext componentContext = context.getComponentContext();
					if (componentContext != null) {
						perspective = componentContext.getPerspective();
					}
					nodeId = context.getQualifiedId();
					perspectiveReference = new AtomicReference<ILayout>(perspective);
					showSelectedPerspective();
				}
				else {
					perspectiveReference = null;
					PartSupport.getInstance().showEmptyPerspective();
				}
			}
		});
	}

	public void setInput(final WorkbenchApplicationContext applicationContext) {
		treeViewer.setInput(applicationContext);
	}

	public void showSelectedPerspective() {
		if (perspectiveReference != null) {
			final ILayout perspective = perspectiveReference.get();
			if (perspective == null) {
				PartSupport.getInstance().showEmptyPerspective();
			}
			else {
				PartSupport.getInstance().showPerspective(nodeId, perspective);
			}
		}
	}

	public boolean isPerspectiveSelected() {
		return perspectiveReference != null;
	}

	public void refresh(final Object object) {
		treeViewer.refresh(object);
	}

	public Composite getFolderComposite() {
		return folderComposite;
	}

	public List<String> getSelectedNode() {
		final List<String> result = new LinkedList<String>();
		final TreeItem[] items = treeViewer.getTree().getSelection();
		if (items.length == 1) {
			TreeItem item = items[0];
			while (item != null) {
				final ComponentTreeNodeContext context = (ComponentTreeNodeContext) item.getData();
				result.add(0, context.getId());
				item = item.getParentItem();
			}
		}
		result.add(0, application.getId());
		return result;
	}

	public void setSelectedNode(final List<String> nodes) {
		setSelectedNode(nodes.subList(1, nodes.size()), null);
	}

	private void setSelectedNode(final List<String> nodes, final TreeItem parent) {
		if (nodes.isEmpty()) {
			if (parent != null) {
				final ComponentTreeNodeContext context = (ComponentTreeNodeContext) parent.getData();
				treeViewer.setSelection(new StructuredSelection(context));
			}
			return;
		}

		final TreeItem[] items;
		if (parent == null) {
			items = treeViewer.getTree().getItems();
		}
		else {
			items = parent.getItems();
		}
		final String id = nodes.remove(0);
		for (final TreeItem item : items) {
			final ComponentTreeNodeContext context = (ComponentTreeNodeContext) item.getData();
			if (context != null && context.getId().equals(id)) {
				treeViewer.expandToLevel(context, 1);
				setSelectedNode(nodes, item);
				break;
			}
		}
		nodes.add(0, id);
	}

	public void clearSelection() {
		treeViewer.setSelection(null);
	}

	public IToolBarModel getToolBar() {
		return toolBarModel;
	}

	public IMenuModel getToolBarMenu() {
		if (toolBarMenuModel == null) {
			toolBarMenuModel = new MenuModel();
			internalToolBarModel.addItem(internalToolBarModel.getItems().size(), toolBarMenuModel);
		}
		return toolBarMenuModel;
	}

	public IMenuModel getPopupMenu() {
		if (popupMenuModel == null) {
			popupMenu = Toolkit.getWidgetWrapperFactory().createComposite(treeViewer.getTree()).createPopupMenu();
			popupMenuModel = Toolkit.getModelFactoryProvider().getItemModelFactory().menu();
			popupMenu.setModel(popupMenuModel);
		}
		return popupMenuModel;
	}

}
