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

package org.jowidgets.spi.impl.swt.widgets.internal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Markup;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.types.SelectionPolicy;
import org.jowidgets.common.widgets.controler.ITreeNodeListener;
import org.jowidgets.spi.impl.controler.TreeObservableSpi;
import org.jowidgets.spi.impl.swt.widgets.SwtControl;
import org.jowidgets.spi.widgets.ITreeNodeSpi;
import org.jowidgets.spi.widgets.ITreeSpi;
import org.jowidgets.spi.widgets.controler.ITreeListenerSpi;
import org.jowidgets.spi.widgets.setup.ITreeSetupSpi;

public class TreeImpl extends SwtControl implements ITreeSpi, ITreeNodeSpi {

	private final boolean multiSelection;
	private final Map<TreeItem, TreeNodeImpl> items;
	private final TreeObservableSpi treeObservable;

	private List<TreeItem> lastSelection;

	public TreeImpl(final Object parentUiReference, final ITreeSetupSpi setup) {
		super(new Tree((Composite) parentUiReference, getStyle(setup)));

		this.lastSelection = new LinkedList<TreeItem>();
		this.treeObservable = new TreeObservableSpi();
		this.items = new HashMap<TreeItem, TreeNodeImpl>();

		this.multiSelection = setup.getSelectionPolicy() == SelectionPolicy.MULTI_SELECTION;

		setMenuDetectListener(new MenuDetectListener() {
			@Override
			public void menuDetected(final MenuDetectEvent e) {
				final Point position = getUiReference().toControl(e.x, e.y);
				final TreeItem item = getUiReference().getItem(position);
				if (item == null) {
					getPopupDetectionObservable().firePopupDetected(new Position(position.x, position.y));
				}
				else {
					final TreeNodeImpl itemImpl = items.get(item);
					itemImpl.firePopupDetected(new Position(position.x, position.y));
				}
			}
		});

		getUiReference().addTreeListener(new TreeListener() {

			@Override
			public void treeExpanded(final TreeEvent e) {
				fireExpandedChanged(e, true);
			}

			@Override
			public void treeCollapsed(final TreeEvent e) {
				fireExpandedChanged(e, false);
			}

			private void fireExpandedChanged(final TreeEvent event, final boolean expanded) {
				final TreeNodeImpl itemImpl = items.get(event.item);
				if (itemImpl != null) {
					itemImpl.fireExpandedChanged(expanded);
					treeObservable.fireExpansionChanged();
				}
				else {
					throw new IllegalStateException("No item impl registered for item '"
						+ event.item
						+ "'. This seems to be a bug");
				}
			}

		});

		getUiReference().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				fireSelectionChange(getUiReference().getSelection());
			}
		});

	}

	@Override
	public Tree getUiReference() {
		return (Tree) super.getUiReference();
	}

	@Override
	public ITreeNodeSpi getRootNode() {
		return this;
	}

	@Override
	public List<ITreeNodeSpi> getSelectedNodes() {
		final List<ITreeNodeSpi> result = new LinkedList<ITreeNodeSpi>();
		for (final TreeItem item : getUiReference().getSelection()) {
			result.add(items.get(item));
		}
		return result;
	}

	@Override
	public ITreeNodeSpi addNode(final Integer index) {
		final TreeNodeImpl result = new TreeNodeImpl(this, null, index);
		registerItem(result.getUiReference(), result);
		return result;
	}

	@Override
	public void removeNode(final int index) {
		final TreeItem child = getUiReference().getItem(index);
		if (child != null) {
			unRegisterItem(child);
			child.dispose();
		}
	}

	@Override
	public void addTreeListener(final ITreeListenerSpi listener) {
		treeObservable.addTreeListener(listener);
	}

	@Override
	public void removeTreeListener(final ITreeListenerSpi listener) {
		treeObservable.removeTreeListener(listener);
	}

	@Override
	public void setMarkup(final Markup markup) {
		throw new UnsupportedOperationException("setMarkup is not possible on the root node");
	}

	@Override
	public void setExpanded(final boolean expanded) {
		throw new UnsupportedOperationException("setExpanded is not possible on the root node");
	}

	@Override
	public boolean isExpanded() {
		throw new UnsupportedOperationException("isExpanded is not possible on the root node");
	}

	@Override
	public void setSelected(final boolean selected) {
		throw new UnsupportedOperationException("setSelected is not possible on the root node");
	}

	@Override
	public boolean isSelected() {
		throw new UnsupportedOperationException("isSelected is not possible on the root node");
	}

	@Override
	public void setText(final String text) {
		throw new UnsupportedOperationException("setText is not possible on the root node");
	}

	@Override
	public void setToolTipText(final String text) {
		throw new UnsupportedOperationException("setToolTipText is not possible on the root node");
	}

	@Override
	public void setIcon(final IImageConstant icon) {
		throw new UnsupportedOperationException("setIcon is not possible on the root node");
	}

	@Override
	public void addTreeNodeListener(final ITreeNodeListener listener) {
		throw new UnsupportedOperationException("addTreeNodeListener is not possible on the root node");
	}

	@Override
	public void removeTreeNodeListener(final ITreeNodeListener listener) {
		throw new UnsupportedOperationException("removeTreeNodeListener is not possible on the root node");
	}

	protected void setSelected(final TreeNodeImpl treeNode, final boolean selected) {
		if (selected != treeNode.isSelected()) {
			final TreeItem[] newSelection;
			if (multiSelection) {
				if (selected) {//add item to selection
					TreeItem[] oldSelection = getUiReference().getSelection();
					if (oldSelection == null) {
						oldSelection = new TreeItem[0];
					}
					newSelection = new TreeItem[oldSelection.length + 1];
					newSelection[0] = treeNode.getUiReference();
					for (int i = 0; i < oldSelection.length; i++) {
						newSelection[i + 1] = oldSelection[i];
					}
				}
				else {//not selected, so remove item from selection
					final TreeItem[] oldSelection = getUiReference().getSelection();
					if (oldSelection == null || oldSelection.length == 0) {
						//nothing to remove from, so return
						return;
					}
					newSelection = new TreeItem[oldSelection.length - 1];
					int offset = 0;
					for (int i = 0; i < newSelection.length; i++) {
						if (oldSelection[i] == treeNode.getUiReference()) {
							offset = 1;
						}
						else {
							newSelection[i] = oldSelection[i + offset];
						}
					}
				}
			}
			else {//single selection
				if (selected) {
					newSelection = new TreeItem[1];
					newSelection[0] = treeNode.getUiReference();
				}
				else {
					newSelection = new TreeItem[0];
				}
			}
			getUiReference().setSelection(newSelection);
			fireSelectionChange(newSelection);
		}
	}

	protected void registerItem(final TreeItem item, final TreeNodeImpl treeNodeImpl) {
		items.put(item, treeNodeImpl);
	}

	protected void unRegisterItem(final TreeItem item) {
		items.remove(item);
	}

	private void fireSelectionChange(final TreeItem[] newSelection) {
		final List<TreeItem> newSelectionList = Arrays.asList(newSelection);

		for (final TreeItem wasSelected : lastSelection) {
			if (!newSelectionList.contains(wasSelected)) {
				items.get(wasSelected).fireSelectionChanged(false);
			}
		}

		for (final TreeItem isSelected : newSelectionList) {
			if (!lastSelection.contains(isSelected)) {
				items.get(isSelected).fireSelectionChanged(true);
			}
		}

		lastSelection = newSelectionList;
		treeObservable.fireSelectionChanged();
	}

	private static int getStyle(final ITreeSetupSpi setup) {
		int result = SWT.NONE;

		if (setup.isContentScrolled()) {
			result = result | SWT.V_SCROLL | SWT.H_SCROLL;
		}

		if (SelectionPolicy.MULTI_SELECTION == setup.getSelectionPolicy()) {
			result = result | SWT.MULTI;
		}
		else if (SelectionPolicy.SINGLE_SELECTION != setup.getSelectionPolicy()) {
			throw new IllegalArgumentException("SelectionPolicy '" + setup.getSelectionPolicy() + "' is not known");
		}

		return result;
	}
}
