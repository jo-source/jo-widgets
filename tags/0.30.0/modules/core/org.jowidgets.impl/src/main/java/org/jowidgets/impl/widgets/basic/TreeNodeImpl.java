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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.jowidgets.api.controller.IDisposeListener;
import org.jowidgets.api.controller.ITreeSelectionEvent;
import org.jowidgets.api.controller.ITreeSelectionListener;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.types.CheckedState;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.ITree;
import org.jowidgets.api.widgets.ITreeContainer;
import org.jowidgets.api.widgets.ITreeNode;
import org.jowidgets.api.widgets.descriptor.ITreeNodeDescriptor;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controller.IFocusListener;
import org.jowidgets.common.widgets.controller.IKeyEvent;
import org.jowidgets.common.widgets.controller.IKeyListener;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.common.widgets.controller.ITreeNodeListener;
import org.jowidgets.impl.base.delegate.DisposableDelegate;
import org.jowidgets.impl.base.delegate.PopupMenuCreationDelegate;
import org.jowidgets.impl.base.delegate.PopupMenuCreationDelegate.IPopupFactory;
import org.jowidgets.impl.base.delegate.TreeContainerDelegate;
import org.jowidgets.impl.event.TreePopupEvent;
import org.jowidgets.impl.widgets.common.wrapper.AbstractTreeNodeSpiWrapper;
import org.jowidgets.spi.widgets.ITreeNodeSpi;
import org.jowidgets.tools.controller.KeyObservable;
import org.jowidgets.tools.controller.KeyObservable.IKeyObservableCallback;
import org.jowidgets.tools.controller.PopupDetectionObservable;
import org.jowidgets.tools.controller.TreeNodeAdapter;
import org.jowidgets.tools.controller.TreeNodeObservable;
import org.jowidgets.util.Assert;
import org.jowidgets.util.EmptyCheck;

public class TreeNodeImpl extends AbstractTreeNodeSpiWrapper implements ITreeNode {

	private final TreeImpl parentTree;
	private final TreeNodeImpl parentNode;
	private final TreeContainerDelegate treeContainerDelegate;
	private final PopupMenuCreationDelegate popupMenuCreationDelegate;
	private final IPopupDetectionListener popupListener;
	private final DisposableDelegate disposableDelegate;
	private final TreeNodeObservable treeNodeObservable;
	private final PopupDetectionObservable popupDetectionObservable;
	private final KeyObservable keyObservable;
	private final IKeyListener keyListener;

	private final IPopupDetectionListener spiPopupDetectionListener;
	private final ITreeNodeListener spiTreeNodeListener;

	private final ITreeNodeListener autoCheckListener;

	private final boolean autoCheckMode;

	private IMenuModel popupMenuModel;
	private IPopupMenu popupMenu;
	private boolean onRemoveByDispose;

	public TreeNodeImpl(final TreeImpl parentTree, final TreeNodeImpl parentNode, final ITreeNodeSpi widget) {
		this(parentTree, parentNode, widget, Toolkit.getBluePrintFactory().treeNode());
	}

	public TreeNodeImpl(
		final TreeImpl parentTree,
		final TreeNodeImpl parentNode,
		final ITreeNodeSpi widget,
		final ITreeNodeDescriptor descriptor) {
		super(widget);

		this.autoCheckMode = parentTree.getAutoCheckMode();

		this.treeNodeObservable = new TreeNodeObservable();
		this.popupDetectionObservable = new PopupDetectionObservable();

		this.autoCheckListener = new TreeNodeAdapter() {
			@Override
			public void checkedChanged(final boolean checked) {
				setAutoCheckChildState(checked);
				final TreeNodeImpl parent = (TreeNodeImpl) getParent();
				if (parent != null) {
					parent.setAutoCheckParentState();
				}
			}
		};
		if (autoCheckMode) {
			widget.addTreeNodeListener(autoCheckListener);
		}

		this.spiPopupDetectionListener = new IPopupDetectionListener() {

			@Override
			public void popupDetected(final Position position) {
				popupDetectionObservable.firePopupDetected(position);
			}
		};
		widget.addPopupDetectionListener(spiPopupDetectionListener);

		this.spiTreeNodeListener = new ITreeNodeListener() {

			@Override
			public void selectionChanged(final boolean selected) {
				treeNodeObservable.fireSelectionChanged(selected);
			}

			@Override
			public void expandedChanged(final boolean expanded) {
				treeNodeObservable.fireExpandedChanged(expanded);
			}

			@Override
			public void checkedChanged(final boolean checked) {
				treeNodeObservable.fireCheckedChanged(checked);
			}

		};
		widget.addTreeNodeListener(spiTreeNodeListener);

		this.popupListener = new IPopupDetectionListener() {
			@Override
			public void popupDetected(final Position position) {
				if (popupMenuModel != null) {
					if (popupMenu == null) {
						popupMenu = popupMenuCreationDelegate.createPopupMenu();
					}
					if (popupMenu.getModel() != popupMenuModel) {
						popupMenu.setModel(popupMenuModel);
					}
					popupMenu.show(position);
				}
			}
		};

		this.parentTree = parentTree;
		this.parentNode = parentNode;

		this.popupMenuCreationDelegate = new PopupMenuCreationDelegate(new IPopupFactory() {
			@Override
			public IPopupMenu create() {
				return new PopupMenuImpl(getWidget().createPopupMenu(), parentTree);
			}
		});

		this.disposableDelegate = new DisposableDelegate();

		this.onRemoveByDispose = false;

		if (descriptor.getForegroundColor() != null) {
			setForegroundColor(descriptor.getForegroundColor());
		}
		if (descriptor.getBackgroundColor() != null) {
			setBackgroundColor(descriptor.getBackgroundColor());
		}

		setExpanded(descriptor.isExpanded());
		setSelected(descriptor.isSelected());
		setText(descriptor.getText());
		setToolTipText(descriptor.getToolTipText());
		setIcon(descriptor.getIcon());
		setMarkup(descriptor.getMarkup());

		this.keyListener = new IKeyListener() {

			@Override
			public void keyReleased(final IKeyEvent event) {
				if (parentTree.hasFocus() && isFirstSelected()) {
					keyObservable.fireKeyReleased(event);
				}
			}

			@Override
			public void keyPressed(final IKeyEvent event) {
				if (parentTree.hasFocus() && isFirstSelected()) {
					keyObservable.fireKeyPressed(event);
				}
			}
		};

		this.keyObservable = new KeyObservable(new IKeyObservableCallback() {
			@Override
			public void onLastUnregistered() {
				updateKeyListeners();
			}

			@Override
			public void onFirstRegistered() {
				updateKeyListeners();
			}
		});

		addTreeNodeListener(new TreeNodeAdapter() {
			@Override
			public void expandedChanged(final boolean expanded) {
				if (expanded) {
					parentTree.getTreeObservable().fireNodeExpanded(TreeNodeImpl.this);
				}
				else {
					parentTree.getTreeObservable().fireNodeCollapsed(TreeNodeImpl.this);
				}
			}

			@Override
			public void checkedChanged(final boolean checked) {
				if (checked) {
					parentTree.getTreeObservable().fireNodeChecked(TreeNodeImpl.this);
				}
				else {
					parentTree.getTreeObservable().fireNodeUnchecked(TreeNodeImpl.this);
				}
			}

		});

		addPopupDetectionListener(new IPopupDetectionListener() {
			@Override
			public void popupDetected(final Position position) {
				parentTree.getTreePopupDetectionObservable().firePopupDetected(new TreePopupEvent(position, TreeNodeImpl.this));
			}
		});

		final IFocusListener focusListener = new IFocusListener() {
			@Override
			public void focusLost() {
				updateKeyListeners();
			}

			@Override
			public void focusGained() {
				updateKeyListeners();
			}
		};
		parentTree.addFocusListener(focusListener);

		final ITreeSelectionListener treeSelectionListener = new ITreeSelectionListener() {
			@Override
			public void selectionChanged(final ITreeSelectionEvent event) {
				updateKeyListeners();
			}
		};
		parentTree.addTreeSelectionListener(treeSelectionListener);

		addDisposeListener(new IDisposeListener() {
			@Override
			public void onDispose() {
				parentTree.removeFocusListener(focusListener);
				parentTree.removeTreeSelectionListener(treeSelectionListener);
				parentTree.removeKeyListener(keyListener);
			}
		});

		this.treeContainerDelegate = new TreeContainerDelegate(parentTree, parentNode, this, widget);

		checkIcon();
	}

	private void updateKeyListeners() {
		if (keyObservable.size() > 0 && parentTree.hasFocus() && isFirstSelected()) {
			parentTree.addKeyListener(keyListener);
		}
		else {
			parentTree.removeKeyListener(keyListener);
		}
	}

	private void setAutoCheckChildState(final boolean checked) {
		if (!autoCheckMode) {
			return;
		}
		else {
			getWidget().removeTreeNodeListener(autoCheckListener);
			if (isGreyed() || isChecked() != checked) {
				setChecked(checked);
			}
			for (final ITreeNode childNode : getChildren()) {
				((TreeNodeImpl) childNode).setAutoCheckChildState(checked);
			}
			getWidget().addTreeNodeListener(autoCheckListener);
		}
	}

	private void setAutoCheckParentState() {
		if (!autoCheckMode) {
			return;
		}
		else {
			getWidget().removeTreeNodeListener(autoCheckListener);

			int checkedCount = 0;
			int greyedCount = 0;
			for (final ITreeNode childNode : getChildren()) {
				if (childNode.isChecked()) {
					checkedCount++;
				}
				else if (childNode.isGreyed()) {
					greyedCount++;
				}
			}

			if (checkedCount == getChildren().size()) {
				setChecked(true);
			}
			else if (checkedCount > 0 || greyedCount > 0) {
				setGreyed();
			}
			else {
				setChecked(false);
			}
			final TreeNodeImpl parent = (TreeNodeImpl) getParent();
			if (parent != null) {
				parent.setAutoCheckParentState();
			}

			getWidget().addTreeNodeListener(autoCheckListener);
		}
	}

	@Override
	public void setGreyed() {
		if (!isGreyed()) {
			super.setGreyed(true);
			treeNodeObservable.fireCheckedChanged(false);
		}
	}

	@Override
	public CheckedState getCheckedState() {
		if (isChecked()) {
			return CheckedState.CHECKED;
		}
		else if (isGreyed()) {
			return CheckedState.GREYED;
		}
		else {
			return CheckedState.UNCHECKED;
		}
	}

	@Override
	public void setCheckedState(final CheckedState state) {
		Assert.paramNotNull(state, "state");
		if (CheckedState.CHECKED.equals(state)) {
			setChecked(true);
		}
		else if (CheckedState.GREYED.equals(state)) {
			setGreyed(true);
		}
		else if (CheckedState.UNCHECKED.equals(state)) {
			setChecked(false);
		}
		else {
			throw new IllegalArgumentException("The state '" + state + "' is not known");
		}
	}

	@Override
	public boolean isUnchecked() {
		return !isGreyed() && !isChecked();
	}

	@Override
	public void addTreeNodeListener(final ITreeNodeListener listener) {
		treeNodeObservable.addTreeNodeListener(listener);
	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		popupDetectionObservable.addPopupDetectionListener(popupListener);
	}

	@Override
	public void removeTreeNodeListener(final ITreeNodeListener listener) {
		treeNodeObservable.removeTreeNodeListener(listener);
	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		popupDetectionObservable.removePopupDetectionListener(listener);
	}

	@Override
	public void addKeyListener(final IKeyListener listener) {
		keyObservable.addKeyListener(listener);
	}

	@Override
	public void removeKeyListener(final IKeyListener listener) {
		keyObservable.removeKeyListener(listener);
	}

	@Override
	public ITreeNode getParent() {
		return parentNode;
	}

	@Override
	public ITree getTree() {
		return parentTree;
	}

	@Override
	public List<ITreeNode> getPath() {
		final LinkedList<ITreeNode> result = new LinkedList<ITreeNode>();
		ITreeNode node = this;
		while (node != null) {
			result.push(node);
			node = node.getParent();
		}
		return result;
	}

	@Override
	public void dispose() {
		if (!isDisposed()) {
			if (parentNode != null && parentNode.getChildren().contains(this) && !onRemoveByDispose) {
				onRemoveByDispose = true;
				parentNode.removeNode(this); //this will invoke dispose by the parent node
				onRemoveByDispose = false;
			}
			else {
				popupMenuCreationDelegate.dispose();
				treeContainerDelegate.dispose();
				disposableDelegate.dispose();
				treeNodeObservable.dispose();
				popupDetectionObservable.dispose();

				getWidget().removePopupDetectionListener(spiPopupDetectionListener);
				getWidget().removeTreeNodeListener(spiTreeNodeListener);
			}
		}
	}

	@Override
	public boolean isDisposed() {
		return disposableDelegate.isDisposed();
	}

	@Override
	public void addDisposeListener(final IDisposeListener listener) {
		disposableDelegate.addDisposeListener(listener);
	}

	@Override
	public void removeDisposeListener(final IDisposeListener listener) {
		disposableDelegate.removeDisposeListener(listener);
	}

	@Override
	public IPopupMenu createPopupMenu() {
		return popupMenuCreationDelegate.createPopupMenu();
	}

	@Override
	public ITreeContainer getParentContainer() {
		return treeContainerDelegate.getParentContainer();
	}

	@Override
	public ITreeNode addNode() {
		final ITreeNode result = treeContainerDelegate.addNode();
		checkIcon();
		return result;
	}

	@Override
	public ITreeNode addNode(final int index) {
		final ITreeNode result = treeContainerDelegate.addNode(index);
		checkIcon();
		return result;
	}

	@Override
	public ITreeNode addNode(final ITreeNodeDescriptor descriptor) {
		final ITreeNode result = treeContainerDelegate.addNode(descriptor);
		checkIcon();
		return result;
	}

	@Override
	public ITreeNode addNode(final int index, final ITreeNodeDescriptor descriptor) {
		final ITreeNode result = treeContainerDelegate.addNode(index, descriptor);
		checkIcon();
		return result;
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
		setExpanded(expanded);
		treeContainerDelegate.setAllChildrenExpanded(expanded);
	}

	@Override
	public void setAllChildrenChecked(final boolean checked) {
		treeContainerDelegate.setAllChildrenChecked(checked);
	}

	@Override
	public int getLevel() {
		return treeContainerDelegate.getLevel();
	}

	@Override
	public boolean isLeaf() {
		return getChildren().size() == 0;
	}

	@Override
	public boolean isTopLevel() {
		return getParent() == null;
	}

	@Override
	public void setPopupMenu(final IMenuModel popupMenuModel) {
		if (popupMenuModel == null && this.popupMenuModel != null) {
			removePopupDetectionListener(popupListener);
		}
		else if (popupMenuModel != null && this.popupMenuModel == null) {
			addPopupDetectionListener(popupListener);
		}
		this.popupMenuModel = popupMenuModel;
	}

	private boolean isFirstSelected() {
		final Collection<ITreeNode> selection = parentTree.getSelection();
		if (selection.size() > 0) {
			return selection.iterator().next() == this;
		}
		else {
			return false;
		}
	}

	private void checkIcon() {
		if (getIcon() == null) {
			if (isLeaf()) {
				getWidget().setIcon(parentTree.getDefaultLeafIcon());
			}
			else {
				getWidget().setIcon(parentTree.getDefaultInnerIcon());
			}
		}
	}

	@Override
	public String toString() {
		final String text = getText();
		if (!EmptyCheck.isEmpty(text)) {
			return text;
		}
		else {
			return super.toString();
		}
	}

}
