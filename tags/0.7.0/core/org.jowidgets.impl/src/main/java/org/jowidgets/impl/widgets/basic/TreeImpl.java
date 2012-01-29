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

package org.jowidgets.impl.widgets.basic;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jowidgets.api.controller.IDisposeListener;
import org.jowidgets.api.controller.ITreeListener;
import org.jowidgets.api.controller.ITreePopupDetectionListener;
import org.jowidgets.api.controller.ITreeSelectionListener;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.ITree;
import org.jowidgets.api.widgets.ITreeContainer;
import org.jowidgets.api.widgets.ITreeNode;
import org.jowidgets.api.widgets.descriptor.ITreeDescriptor;
import org.jowidgets.api.widgets.descriptor.ITreeNodeDescriptor;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.impl.base.delegate.ControlDelegate;
import org.jowidgets.impl.base.delegate.TreeContainerDelegate;
import org.jowidgets.impl.event.TreePopupEvent;
import org.jowidgets.impl.event.TreeSelectionEvent;
import org.jowidgets.impl.widgets.basic.factory.internal.util.ColorSettingsInvoker;
import org.jowidgets.impl.widgets.basic.factory.internal.util.VisibiliySettingsInvoker;
import org.jowidgets.impl.widgets.common.wrapper.AbstractControlSpiWrapper;
import org.jowidgets.spi.widgets.ITreeNodeSpi;
import org.jowidgets.spi.widgets.ITreeSpi;
import org.jowidgets.spi.widgets.controller.ITreeSelectionListenerSpi;
import org.jowidgets.tools.controller.TreeObservable;
import org.jowidgets.tools.controller.TreePopupDetectionObservable;
import org.jowidgets.tools.controller.TreeSelectionObservable;

public class TreeImpl extends AbstractControlSpiWrapper implements ITree {

	private final ControlDelegate controlDelegate;
	private final TreeSelectionObservable treeSelectionObservable;
	private final TreeObservable treeObservable;
	private final TreePopupDetectionObservable treePopupDetectionObservable;
	private final TreeContainerDelegate treeContainerDelegate;
	private final Map<ITreeNodeSpi, ITreeNode> nodes;

	private final IImageConstant defaultInnerIcon;
	private final IImageConstant defaultLeafIcon;

	private List<ITreeNodeSpi> lastSelection;

	public TreeImpl(final ITreeSpi widgetSpi, final ITreeDescriptor descriptor) {
		super(widgetSpi);

		this.defaultInnerIcon = descriptor.getDefaultInnerIcon();
		this.defaultLeafIcon = descriptor.getDefaultLeafIcon();

		this.controlDelegate = new ControlDelegate(widgetSpi, this);

		this.treeSelectionObservable = new TreeSelectionObservable();
		this.treeObservable = new TreeObservable();
		this.treePopupDetectionObservable = new TreePopupDetectionObservable();

		this.nodes = new HashMap<ITreeNodeSpi, ITreeNode>();
		this.lastSelection = new LinkedList<ITreeNodeSpi>();

		VisibiliySettingsInvoker.setVisibility(descriptor, this);
		ColorSettingsInvoker.setColors(descriptor, this);

		getWidget().addTreeSelectionListener(new ITreeSelectionListenerSpi() {

			@Override
			public void selectionChanged() {
				final List<ITreeNode> selected = new LinkedList<ITreeNode>();
				final List<ITreeNode> unselected = new LinkedList<ITreeNode>();

				final List<ITreeNodeSpi> newSelection = getWidget().getSelectedNodes();

				for (final ITreeNodeSpi wasSelected : lastSelection) {
					if (!newSelection.contains(wasSelected)) {
						final ITreeNode unselectedNode = nodes.get(wasSelected);
						if (unselectedNode != null) {
							unselected.add(unselectedNode);
						}
					}
				}

				for (final ITreeNodeSpi isSelected : newSelection) {
					if (!lastSelection.contains(isSelected)) {
						final ITreeNode selectedNode = nodes.get(isSelected);
						if (selectedNode != null) {
							selected.add(selectedNode);
						}
					}
				}

				treeSelectionObservable.fireSelectionChanged(new TreeSelectionEvent(selected, unselected));

				lastSelection = newSelection;
			}

		});

		addPopupDetectionListener(new IPopupDetectionListener() {
			@Override
			public void popupDetected(final Position position) {
				treePopupDetectionObservable.firePopupDetected(new TreePopupEvent(position, null));
			}
		});

		this.treeContainerDelegate = new TreeContainerDelegate(this, null, null, widgetSpi.getRootNode());
	}

	@Override
	public ITreeSpi getWidget() {
		return (ITreeSpi) super.getWidget();
	}

	@Override
	public IContainer getParent() {
		return controlDelegate.getParent();
	}

	@Override
	public void setParent(final IContainer parent) {
		controlDelegate.setParent(parent);
	}

	@Override
	public boolean isReparentable() {
		return controlDelegate.isReparentable();
	}

	@Override
	public void addDisposeListener(final IDisposeListener listener) {
		controlDelegate.addDisposeListener(listener);
	}

	@Override
	public void removeDisposeListener(final IDisposeListener listener) {
		controlDelegate.removeDisposeListener(listener);
	}

	@Override
	public boolean isDisposed() {
		return controlDelegate.isDisposed();
	}

	@Override
	public void dispose() {
		if (!isDisposed()) {
			treeContainerDelegate.dispose();
			controlDelegate.dispose();
		}
	}

	@Override
	public IPopupMenu createPopupMenu() {
		return controlDelegate.createPopupMenu();
	}

	@Override
	public void addTreePopupDetectionListener(final ITreePopupDetectionListener listener) {
		treePopupDetectionObservable.addTreePopupDetectionListener(listener);
	}

	@Override
	public void removeTreePopupDetectionListener(final ITreePopupDetectionListener listener) {
		treePopupDetectionObservable.removeTreePopupDetectionListener(listener);
	}

	@Override
	public void addTreeSelectionListener(final ITreeSelectionListener listener) {
		treeSelectionObservable.addTreeSelectionListener(listener);
	}

	@Override
	public void removeTreeSelectionListener(final ITreeSelectionListener listener) {
		treeSelectionObservable.removeTreeSelectionListener(listener);
	}

	@Override
	public void addTreeListener(final ITreeListener listener) {
		treeObservable.addTreeListener(listener);
	}

	@Override
	public void removeTreeListener(final ITreeListener listener) {
		treeObservable.removeTreeListener(listener);
	}

	@Override
	public ITreeContainer getParentContainer() {
		return treeContainerDelegate.getParentContainer();
	}

	@Override
	public ITreeNode addNode() {
		return treeContainerDelegate.addNode();
	}

	@Override
	public ITreeNode addNode(final int index) {
		return treeContainerDelegate.addNode(index);
	}

	@Override
	public ITreeNode addNode(final ITreeNodeDescriptor descriptor) {
		return treeContainerDelegate.addNode(descriptor);
	}

	@Override
	public ITreeNode addNode(final int index, final ITreeNodeDescriptor descriptor) {
		return treeContainerDelegate.addNode(index, descriptor);
	}

	@Override
	public void removeNode(final ITreeNode node) {
		treeContainerDelegate.removeNode(node);
	}

	@Override
	public void removeNode(final int index) {
		treeContainerDelegate.removeNode(index);
	}

	@Override
	public void removeAllNodes() {
		treeContainerDelegate.removeAllNodes();
	}

	@Override
	public List<ITreeNode> getChildren() {
		return treeContainerDelegate.getChildren();
	}

	@Override
	public void setAllChildrenExpanded(final boolean expanded) {
		treeContainerDelegate.setAllChildrenExpanded(expanded);
	}

	public void registerNode(final TreeNodeImpl node) {
		nodes.put(node.getWidget(), node);
	}

	public void unRegisterNode(final TreeNodeImpl node) {
		nodes.remove(node.getWidget());
	}

	public IImageConstant getDefaultInnerIcon() {
		return defaultInnerIcon;
	}

	public IImageConstant getDefaultLeafIcon() {
		return defaultLeafIcon;
	}

	protected TreeObservable getTreeObservable() {
		return treeObservable;
	}

	protected TreePopupDetectionObservable getTreePopupDetectionObservable() {
		return treePopupDetectionObservable;
	}

}
